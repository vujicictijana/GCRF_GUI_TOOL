#include <vector>
#include <Eigen/Dense>

using Eigen::MatrixXd;
using std::vector;

struct GCRFParams {
GCRFParams()
: quiet(false),
    max_iters(10),
    ls_max_iters(20),
    alpha(1),
    beta(0.5),
    sigma(0.001),
    tol(0.0001),
    cd_tol(0.05) {}
    
  // Whether to print info messages
  bool quiet;

  // Maximum iterations 
  int max_iters;
  int ls_max_iters;

  // Line search parameters
  double alpha;
  double beta;
  double sigma;

  // Tolerance parameters 
  double tol;
  double cd_tol;
};

struct GCRFStats {
  vector<double> objval;
  vector<double> time;
};

void OptimizeGCRF(
  const MatrixXd& S_matrix, 
  double lambda,
  const GCRFParams& params,
  MatrixXd& Lambda,
  MatrixXd& Theta,
  GCRFStats* stats);
