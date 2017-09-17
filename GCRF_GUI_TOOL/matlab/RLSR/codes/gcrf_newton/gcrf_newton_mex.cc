
// Compile on OS X (10.8.2): 
//   brew install eigen
//   mex gcrf_newton.cc gcrf_newton_mex.cc -I/usr/local/include/eigen3/
// 
// Compile on Linux (Ubuntu 12.10): 
//   sudo apt-get install libeigen3-dev
//   mex gcrf_newton.cc gcrf_newton_mex.cc -I/usr/include/eigen3  

#include <vector>
#include <mex.h>
#include <Eigen/Dense>
#include "gcrf_newton.h"

using Eigen::Map;
using Eigen::MatrixXd;

enum InputArguments {
  INPUT_S,
  INPUT_LAMBDA,
  INPUT_LAMBDA0,
  INPUT_THETA0,
  INPUT_PARAMS,
};

enum OutputArguments {
  OUTPUT_LAMBDA,
  OUTPUT_THETA,
  OUTPUT_HISTORY
};

// Constants needed to create the history structure
const char *kHistoryFieldNames[] = {"objval", "time"};
const int kNumHistoryFields = 
  sizeof(kHistoryFieldNames)/sizeof(*kHistoryFieldNames);
const mwSize kHistoryDims[2] = {1, 1};
enum HistoryFields {
  HISTORY_OBJVAL,
  HISTORY_TIME
};

// Functions for marshalling data to Matlab 
mxArray* getMexArray(const std::vector<double>& v) {
  mxArray* a = mxCreateDoubleMatrix(1, v.size(), mxREAL);
  std::copy(v.begin(), v.end(), mxGetPr(a));
  return a;
}

mxArray* getMexArray(const MatrixXd& A) {
  mxArray* B = mxCreateDoubleMatrix(A.rows(), A.cols(), mxREAL);
  memcpy(mxGetPr(B), A.data(), A.rows()*A.cols()*mxGetElementSize(B));
  return B;
}

void mexFunction(int nlhs, mxArray* plhs[], int nrhs, const mxArray* prhs[]) {
  if (nrhs < 4)
    mexErrMsgIdAndTxt("GCRF:arguments", "Not enough input arguments.");    
  if (nrhs > 5)
    mexErrMsgIdAndTxt("GCRF:arguments", "Too many input arguments.");

  int n = mxGetM(prhs[INPUT_THETA0]);
  int p = mxGetN(prhs[INPUT_THETA0]);
  double lambda = mxGetScalar(prhs[INPUT_LAMBDA]);

  if (mxGetM(prhs[INPUT_S]) != n + p || mxGetN(prhs[INPUT_S]) != n + p)
    mexErrMsgIdAndTxt("GCRF:arguments", "S must have dimension (n+p)^2.");
  if (mxGetM(prhs[INPUT_LAMBDA0]) != p || mxGetN(prhs[INPUT_LAMBDA0]) != p) 
    mexErrMsgIdAndTxt("GCRF:arguments", "Lambda0 must have dimension p^2.");

  GCRFParams params;
  if (nrhs > INPUT_PARAMS) {
    for (int i = 0; i < mxGetNumberOfFields(prhs[INPUT_PARAMS]); i++) {
      const char* name = mxGetFieldNameByNumber(prhs[INPUT_PARAMS], i);
      mxArray* value = mxGetFieldByNumber(prhs[INPUT_PARAMS], 0, i);
      if (!strcmp(name, "quiet")) 
        params.quiet = mxGetScalar(value);
      else if (!strcmp(name, "max_iters"))
        params.max_iters = (int)mxGetScalar(value);
      else if (!strcmp(name, "ls_max_iters"))
        params.ls_max_iters = (int)mxGetScalar(value);
      else if (!strcmp(name, "sigma"))
        params.sigma = mxGetScalar(value);
      else if (!strcmp(name, "alpha"))
        params.alpha = mxGetScalar(value);
      else if (!strcmp(name, "beta"))
        params.beta = mxGetScalar(value);
      else if (!strcmp(name, "tol"))
        params.tol = mxGetScalar(value);
      else if (!strcmp(name, "cd_tol"))
        params.cd_tol = mxGetScalar(value);
      // Ignore unknown fields
    }
  }

  MatrixXd S = Map<MatrixXd>(mxGetPr(prhs[INPUT_S]), n + p, n + p);
  MatrixXd Lambda = Map<MatrixXd>(mxGetPr(prhs[INPUT_LAMBDA0]), p, p);
  MatrixXd Theta = Map<MatrixXd>(mxGetPr(prhs[INPUT_THETA0]), n, p);
  GCRFStats stats;
  OptimizeGCRF(S, lambda, params, Lambda, Theta, &stats);
  plhs[OUTPUT_LAMBDA] = getMexArray(Lambda);
  plhs[OUTPUT_THETA] = getMexArray(Theta);

  mxArray* history = mxCreateStructArray(
    2, kHistoryDims, kNumHistoryFields, kHistoryFieldNames);
  mxSetFieldByNumber(history, 0, HISTORY_OBJVAL, getMexArray(stats.objval));
  mxSetFieldByNumber(history, 0, HISTORY_TIME, getMexArray(stats.time));
  plhs[OUTPUT_HISTORY] = history;
}
