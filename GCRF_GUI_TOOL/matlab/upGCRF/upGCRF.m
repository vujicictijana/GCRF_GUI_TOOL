function [Data,muNoisyGCRF] = upGCRF(lag,trainTs,predictTs,maxiter,select_features,N, X, y, similarities)


addpath('Plotting');
addpath('Predictors');
addpath('Similarity metrics');
addpath('Structure');
addpath('Synthetic data');
addpath('adaptiveGCRF');
addpath('GCRF');
addpath('fastExactGCRF');
addpath(genpath('GPdyn-new'));
addpath('Util');

%% Setting parameters

use_all_data = false;
% lag = how monay previous time step values are used as inputs 
% trainTs = number of time steps used for training the model
% predictTs = number of time steps used for predicting ahead
% [] means we are not using any of the input variables available
%select_features = []; %[1,2,3,4,5,6];
xunstr = 1:lag+numel(select_features); %12 / 18
xsim = 1:lag+numel(select_features); %[7,8];

% Previous parameters stored for conveniece purposes
monthsTr = lag+1:lag+trainTs;
monthsTest = lag+trainTs+1:lag+trainTs+predictTs;
T = [lag+1 lag+trainTs];
steps_to_predict = predictTs;

training_window = trainTs;
no_of_unstructured_predictors = 1;

useWorkers = false; % use MATLAB paralelization toolbox
%maxiter = 50; % maximum number of iterations for optimization function
%% Get rain data
%load data/rain_data_northwest.mat

%clearvars -except use_all_data lag trainTs predictTs monthsTr ...
   % monthsTest T steps_to_predict numTimeSteps training_window ...
   % select_features no_of_unstructured_predictors xunstr xsim ...
   % useWorkers maxiter N X y similarities

%% Prepare the data
[xtrain, lagtrain, utrain, ytrain, xvalid, lagvalid, uvalid, yvalid] = ...
    prepareDataNoisyInputs(...
    X, y, N, select_features, lag, trainTs, predictTs);

%% Benchmarks
N = size(ytrain, 1);
[benchmark_predictors, benchmark_variances] = ...
    benchmarkUncertaintyPropagationIterative(...
    xtrain, lagtrain, utrain, ytrain, xvalid, lagvalid, uvalid, yvalid, N,...
    lag, trainTs, predictTs, select_features, training_window);

%% Prepare unstructured predictor (Linear model)
xx = []; yy = [];
for nts = 1:trainTs
    xx = [xx; squeeze(xtrain(:,nts,:))];
    yy = [yy; ytrain(:,nts)];
end

mdl = LinearModel.fit(xx, yy);
theta_init = mdl.Coefficients.Estimate';

predictors{1} = NaN(size(ytrain,1),size(ytrain,2)+1);
for nts = 1:trainTs
    predictors{1}(:,nts) = predict(mdl, squeeze(xtrain(:,nts,:)));
end

% Predict one step ahead
predictors{1}(:,trainTs+1) = predict(mdl, squeeze(xvalid(:,1,:)));

for nts = 1:predictTs
    predictors{1}(:,trainTs+nts) = predict(mdl, squeeze(xvalid(:,nts,:)));
end

%% Prepare similarity metric (Initialize Gaussian kernel parameters by 
% Gaussian Processes regression optimization)
%shrink the data to speed-up training of gaussian processes
input = xx(1:round(lag+trainTs+predictTs/3),:);
target = yy(1:round(lag+trainTs+predictTs/3));

cov = @covSEard;
% Define covariance function: Gaussinan likelihood function.
lik = @likGauss;
% Define mean function: Zero mean function.
meanFunc = @meanZero;
% meanFunc = {'meanSum',{@meanZero,@meanConst}};  hypsu = [round(mean(ytrain))]; % sum
% Define inference method: Exact Inference
inf= @infExact;
% Setting initial hyperparameters
D = size(input,2); % Input space dimension
hyp.cov  = -ones(D+1,1);
% Define likelihood hyperparameter. In our case this parameter is noise
% parameter.
hyp.lik = log(0.1);
hyp.mean = [];

% Training
% Identification of the GP model
[hyp, flogtheta, i] =...
    trainGParx(hyp, inf, meanFunc, cov, lik, input, target);

psi_init = hyp.cov';

% Calculate similarity matrix for first timestep based on Gaussian Kernel
if exist('similarties','var')
    [xx, yy] = meshgrid(1:N,1:N);
    X_sim = squeeze(xtrain(:,1,:));
    X_sim_dist_sq = (X_sim(xx,:)-X_sim(yy,:)).^2;
    temp_sum = bsxfun(@rdivide, X_sim_dist_sq, 2*(psi_init(2:end).^2));
    similarities{1} = psi_init(1)*reshape(exp(-sum(temp_sum, 2)),N,N);
end

clearvars xx yy input target mdl GPData cov lik hyp meanFunc inf D ...
    X_sim X_sim_dist_sq temp_sum flogtheta i nts

%% Learn original GCRF (Radosavljevic et al. 2010)
CRFData = createCRFstruct(...
    N, T, steps_to_predict, maxiter, y, X, similarities, predictors);
CRFData.lambdaAlpha = 0*pi;
CRFData.lambdaBeta = 0*pi;

tic
[ualpha, ubeta]= trainCRFFast(CRFData, useWorkers);
elapsedTimeLinGCRF = toc
alpha_init = ualpha;
beta_init = ubeta;
CRFData.ualpha = ualpha;
CRFData.ubeta = ubeta;

% Testing GCRF models
%[predictionCRF, Sigma, Variance,...
  %  predictionCRF_all, Sigma_all, Variance_all] = testCRFFast(CRFData);

%% Learn new adaptive GCRF

% Regularization parameters
lambdaAlpha = 0;
lambdaBeta = 0;
lambdaR = 0;
lambdaS = 0;

% Initialization of parameters
thetaAlpha = alpha_init;
thetaBeta = beta_init;
thetaR = theta_init;
thetaS = psi_init;


nalpha = length(predictors);
nbeta = length(similarities);
ntheta_r = length(xunstr) + 1; % add one for the bias term
ntheta_s = length(xsim) + 1; % add one for the outer parameter

Data = struct;
xinput = cat(3, ipermute(xtrain,[1,3,2]), ipermute(xvalid, [1,3,2]));

tic
% Create structure to carry information during training process
Data = createXCRFstruct( xinput, ytrain, yvalid, predictors, similarities, Data, ...
    xunstr, xsim, nalpha, nbeta, ntheta_r, ntheta_s, lambdaAlpha, lambdaBeta,...
    lambdaR, lambdaS, thetaAlpha, thetaBeta, thetaR, thetaS, lag, maxiter, useWorkers);
% Train model
Data.u = trainCRFX(Data);
elapsedTimeAdaptGCRF = toc;

clearvars lambdaAlpha lambdaBeta lambdaR lambdaS thetaAlpha  ...
    thetaBeta  thetaR  thetaS nalpha nbeta ...
    ntheta_r ntheta_s

%% Taylor simulation of the GCRF prediction
tic
% Apply multiple-steps-ahead prediction with Taylor simulation of first
% two moments of the distribution
[muNoisyGCRF, sigmaNoisyGCRF, SigmaX] = simulGCRFTaylor(Data);