#include <time.h>
#if defined(LANG_M) || defined(MATLAB_MEX_FILE)
#include <mex.h>
#define PRINTF mexPrintf
#else
#include <stdio.h>
#define PRINTF printf
#endif
#include <iostream>
#include <vector>
#include <Eigen/Dense>
#include <Eigen/Cholesky>
#include "gcrf_newton.h"

#include <math.h>
#include <algorithm>

using Eigen::Block;
using Eigen::LLT;
using Eigen::MatrixXd;
using Eigen::Success;

using std::vector;
using std::max;

struct Coordinate {
  Coordinate(int _i, int _j) : i(_i), j(_j) {}
  int i, j;
};

struct Covariance {
  Covariance(const MatrixXd& S, int p, int n)
  : yy(S.block(0, 0, p, p)),
    yx(S.block(0, p, p, n)),
    xy(S.block(p, 0, n, p)),
    xx(S.block(p, p, n, n)) {}
  
  Block<const MatrixXd> yy;
  Block<const MatrixXd> yx;
  Block<const MatrixXd> xy;
  Block<const MatrixXd> xx;
};

// Per iteration state, caches expensive matrix products.
// caches the result?
struct IterationState {
  IterationState(
    const Covariance& S,
    const MatrixXd& Lambda,
    const MatrixXd& Theta,
    const MatrixXd& Sigma)
    : A(Theta*Sigma),        
      B(S.xx*A),               // S.xx*Theta*Sigma
      C(Theta.transpose()*B),  // Theta.transpose()*S.xx*Theta*Sigma
      D(Sigma*C) {}            // Sigma*Theta.transpose()*S.xx*Theta*Sigma
  MatrixXd A, B, C, D;
};

double SoftThreshold(double a, double kappa) {
  return max(double(0), double(a-kappa)) - max(double(0), double(-a-kappa));
}

double L1Norm(const MatrixXd& A) {
  return A.lpNorm<1>();
}

double L1NormOffDiag(const MatrixXd& A) {
  return A.lpNorm<1>() - A.diagonal().lpNorm<1>();
}

double L1SubGrad(double x, double g, double lambda) {
  if (x > 0)
    return lambda;
  else if (x < 0)
    return -lambda;
  else
    return fabs(g) - lambda;
}

double Objective(
  const Covariance& S, 
  double lambda, 
  const MatrixXd& Lambda, 
  const MatrixXd& Theta, 
  const MatrixXd& L, 
  const MatrixXd& Sigma) {
  int n = Theta.rows();
  int p = Theta.cols();
  return -2*L.diagonal().array().log().sum() + 
    (Lambda*S.yy).trace() + 
    2*(Theta*S.yx).trace() + 
    (Theta*Sigma*Theta.transpose()*S.xx).trace() + 
    lambda*(L1Norm(Theta) + L1NormOffDiag(Lambda));
}

double CalculateSubgradient(
  const Covariance& S,
  const MatrixXd& Lambda,
  const MatrixXd& Theta,
  const MatrixXd& Sigma,
  double lambda,
  const IterationState& state,
  vector<Coordinate>* active_lambda,
  vector<Coordinate>* active_theta) {
  int n = Theta.rows();
  int p = Theta.cols();
  double subgrad = 0.0;
  // Lambda
  MatrixXd G = S.yy - Sigma - state.D;
  for (int j = 0; j < p; j++) {
    for (int i = 0; i < j; i++) {
      if (Lambda(i,j) != 0 || fabs(G(i,j)) > lambda) {
        active_lambda->push_back(Coordinate(i, j));
        subgrad += 2*fabs(G(i,j) + L1SubGrad(Lambda(i,j), G(i,j), lambda));
      }
      subgrad += fabs(G(j,j));
    }
  }

  // Theta
  G = 2*S.xy + 2*state.B;
  for (int j = 0; j < p; j++) {
    for (int i = 0; i < n; i++) {
      if (Theta(i,j) != 0 || fabs(G(i,j)) > lambda) {
        active_theta->push_back(Coordinate(i, j));
        subgrad += fabs(G(i,j) + L1SubGrad(Theta(i,j), G(i,j), lambda));
      }
    }
  }

  return subgrad;
}

void CoordinateDescent(
  const Covariance& S,
  const MatrixXd& Lambda,
  const MatrixXd& Theta,
  const MatrixXd& Sigma,
  const GCRFParams& params,
  const vector<Coordinate>& active_lambda,
  const vector<Coordinate>& active_theta,
  double lambda,
  int max_iters,
  const IterationState& state,
  MatrixXd& U,
  MatrixXd& V) {
  int n = Theta.rows();
  int p = Theta.cols();
    
  U = MatrixXd::Zero(p, p);
  V = MatrixXd::Zero(n, p);
    
  const MatrixXd& A = state.D;
  MatrixXd B = state.B.transpose();
  const MatrixXd& C = state.B;

  MatrixXd USigma = MatrixXd::Zero(p, p);
  MatrixXd VSigma = MatrixXd::Zero(n, p);
  for (int t = 0; t < max_iters; t++) {
    MatrixXd Uold = U;
    MatrixXd Vold = V;
        
    // Minimize over diagonal elements of Lambda
    for (int i = 0; i < p; i++) {
      double a = Sigma(i,i)*Sigma(i,i) + 2*Sigma(i,i)*A(i,i);
      double b = -Sigma(i,i) + S.yy(i,i) - A(i,i) + Sigma.row(i)*USigma.col(i)
        - 2*B.row(i)*VSigma.col(i) + 2*A.row(i)*USigma.col(i);
      double mu = -b/a;
      U(i,i) = U(i,i) + mu;
      USigma.row(i) = USigma.row(i) + mu*Sigma.row(i);
    }

    // Minimize over off-diagonal elements of Lambda
    for (int k = 0; k < active_lambda.size(); k++) {
      int i = active_lambda[k].i; 
      int j = active_lambda[k].j;

      double a = Sigma(i,j)*Sigma(i,j) + Sigma(i,i)*Sigma(j,j) 
        + Sigma(i,i)*A(j,j) + 2*Sigma(i,j)*A(i,j) + Sigma(j,j)*A(i,i);
      double b = -Sigma(i,j) + S.yy(i,j) - A(i,j) + Sigma.row(i)*USigma.col(j) 
        - B.row(i)*VSigma.col(j) - B.row(j)*VSigma.col(i) 
        + A.row(i)*USigma.col(j) + A.row(j)*USigma.col(i);
      double c = Lambda(i,j) + U(i,j);  

      double mu = -c + SoftThreshold(c - b/a, lambda/a);
      U(i,j) = U(i,j) + mu;
      U(j,i) = U(j,i) + mu;
      USigma.row(i) = USigma.row(i) + mu*Sigma.row(j);
      USigma.row(j) = USigma.row(j) + mu*Sigma.row(i);
    }

    // Minimize over each element of Theta
    for (int k = 0; k < active_theta.size(); k++) {
      int i = active_theta[k].i;
      int j = active_theta[k].j;

      double a = 2*Sigma(j,j)*S.xx(i,i);
      double b = 2*S.xy(i,j) + 2*C(i,j) + 2*S.xx.row(i)*VSigma.col(j) - 
        2*C.row(i)*USigma.col(j);
      double c = Theta(i,j) + V(i,j);
    
      double mu = -c + SoftThreshold(c - b/a, lambda/a);
      V(i,j) = V(i,j) + mu;
      VSigma.row(i) = VSigma.row(i) + mu*Sigma.row(j);
    }
    
    double normU = U.norm()/p;
    double normV = V.norm()/sqrt(double(n*p));
    double diffU = (U - Uold).norm()/p;
    double diffV = (V - Vold).norm()/sqrt(double(n*p));

    if (diffU < normU*params.cd_tol && 
        (normV == 0 || diffV < normV*params.cd_tol)) {
      break;
    }
    
    if (!params.quiet) {
      PRINTF("  coordinate descent %d, normU=%f\tdiffU=%f\tnormV=%f\tdiffV=%f\n", 
             t, normU, diffU, normV, diffV);
    }
  }
}

void OptimizeGCRF(
  const MatrixXd& S_matrix, 
  double lambda,
  const GCRFParams& params,
  MatrixXd& Lambda,
  MatrixXd& Theta,
  GCRFStats* stats) {
  int n = Theta.rows();
  int p = Theta.cols();
  assert(p == Lambda.rows() && p == Lambda.cols());
  assert(n + p == S_matrix.rows() && n + p == S_matrix.cols());

  if (!params.quiet)
    PRINTF("Eigen instruction sets: %s\n", Eigen::SimdInstructionSetsInUse());

  Covariance S(S_matrix, p, n);  
  LLT<MatrixXd> cholesky(Lambda);
  if (cholesky.info() != Success) { 
    if (!params.quiet) PRINTF("Lambda0 not positive definite\n");
    return;
  }
  MatrixXd Sigma = cholesky.solve(MatrixXd::Identity(p, p));

  double f = Objective(S, lambda, Lambda, Theta, cholesky.matrixL(), Sigma);
  double start = clock();

  for (int t = 0; t < params.max_iters; t++) {
    IterationState state(S, Lambda, Theta, Sigma);

    // Calculate the sub gradient and active set
    vector<Coordinate> active_lambda, active_theta;
    double subgrad = CalculateSubgradient(
      S, Lambda, Theta, Sigma, lambda, state, &active_lambda, &active_theta);
    double l1_norm = L1Norm(Theta) + L1NormOffDiag(Lambda);

    if (subgrad < params.tol || subgrad < params.tol*lambda*l1_norm) {
      if (!params.quiet) {
        PRINTF("Converged, subgrad: %f, active set size: %ld, l1 norm: %f\n", 
               subgrad, active_lambda.size() + active_theta.size() + p, 
               l1_norm);
      }
      break;
    }
    
    if (!params.quiet) {
      PRINTF("Newton iteration %d, subgrad: %f, active set size: %ld, l1 norm: %f\n", 
             t, subgrad, active_lambda.size() + active_theta.size() + p, 
             l1_norm);
    }

    // Find the Newton direction
    MatrixXd U, V;
    CoordinateDescent(
      S, Lambda, Theta, Sigma, params, active_lambda, active_theta, lambda, 
      1+t/3, state, U, V);
    
    // Find step size w/ backtracking line search
    double df = (S.yy*U - Sigma*U - state.D*U).trace() + 
      2*(S.yx*V + state.B.transpose()*V).trace();
    double dl1 = L1NormOffDiag(Lambda+U) + L1Norm(Theta+V) - l1_norm;
    
    double alpha = params.alpha;
    double f_alpha;
    MatrixXd Lambda_alpha, Theta_alpha;
    int i;
    for (i = 0; i < params.ls_max_iters; i++) {
      Lambda_alpha = Lambda + alpha*U;
      LLT<MatrixXd> cholesky(Lambda_alpha);
      if (cholesky.info() != Success) { 
        if (!params.quiet) { 
          PRINTF("  line search %d, alpha=%f\tnot PD\n", i, alpha);
        }
        alpha *= params.beta;
        continue;
      }

      Theta_alpha = Theta + alpha*V;
      Sigma = cholesky.solve(MatrixXd::Identity(p, p));
      f_alpha = Objective(S, lambda, Lambda_alpha, Theta_alpha, 
                          cholesky.matrixL(), Sigma);

      if (!params.quiet) 
        PRINTF("  line search %d, alpha=%f\tf=%f\n", i, alpha, f_alpha);        
      if (f_alpha < f + alpha*params.sigma*(df + lambda*dl1)) 
        break;
      alpha *= params.beta;
    }

    if (i == params.ls_max_iters) {
        if (!params.quiet) PRINTF("Failed to improve objective\n");
        break;
    }

    Lambda = Lambda_alpha;
    Theta = Theta_alpha;
    f = f_alpha;
  
    stats->objval.push_back(f);
    stats->time.push_back((clock()-start)/CLOCKS_PER_SEC);

#if defined(LANG_M) || defined(MATLAB_MEX_FILE)
    mexEvalString("drawnow;");  
#endif
  }  
}

