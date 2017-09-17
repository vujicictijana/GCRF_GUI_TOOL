close all
clear all

addpath(genpath('../codes'));

% neural network parameters
parameters;
nn_params = params;
nn_params.Nh = 20; 
nn_params.nIter = 200; %1000
nn_params.save = 0; 
nn_params.func = {'sigm', 'none'};
nn_params.early.use = 1;

% Structure learning parameters
sse_params = struct;
sse_params.quiet = 1;
sse_params.max_iters = 1000;
sse_params.ls_max_iters = 1000;
sse_params.tol = 1e-6;
sse_params.col_tol = 1e-6;
sse_params.alpha = 1;

% Training parameters
maxIter = 10; %30
trainSize = 1000;
validSize = 300;
testSize = 300;
LFSize = 5; %20

% Loading and spliting the data
x = csvread('../data/X.csv');
y = csvread('../data/Y.csv');

[trainValidX, trainValidY, testX, testY] = splitData(x, y, trainSize, validSize, testSize);

% Training and validating
lambda_set = 0.01; % lambda_set = [0.0001,0.001,0.01,0.01,0.1,1];
[best_layer, best_Lambda, best_Theta, best_lambda, time_spent] = ...
    RLSR_train_valid(trainValidX, trainValidY, trainSize, validSize, lambda_set, maxIter, LFSize, nn_params, sse_params);

% Testing
[test_mse] = RLSR_test(testX, testY, best_layer, best_Lambda, best_Theta);
