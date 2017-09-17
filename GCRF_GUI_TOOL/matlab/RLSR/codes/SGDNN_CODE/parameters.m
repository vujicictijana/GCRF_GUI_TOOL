%% Default set of parameters for the nnet.m file

%% - params.nTrain: number of training samples. If less than 1, used as a proportion of the total number of samples.
%% - params.nValidation: number of validation samples. If less than 1, used as a proportion of the total number of samples.
%% - params.batchSize: size of the mini-batches.
%% - params.task: task you wish to perform. 'regr' for regression, 'class' for classification.
%% - params.cost: cost to use on the output. 'mse' for mean squared error, 'nll' for negative log-likelihood.
%% - params.nIter: number of passes through the entire training set.
%% - params.early.use: do we use early stopping (only valid when there is a validation set).
%% - params.early.max: maximum number of iterations during which the validation error may increase before the optimization stops.
%% - params.Nh: vector containing the sizes of the hidden layers. params.Nh = [] trains a linear model, which is the default when the field is not set.
%% - params.func: a cell containing the transfer function for each layer. If the cell contains fewer elements than the number of layers, the last element in the cell is used for all subsequent layers. Possible values for this field are 'sigm' (sigmoid), 'tanh' (hyperbolic tangent), 'nic' (g(x) = x/[1 + |x|]) and 'softplus' (g(x) = log [1 + exp(x)]).
%% - params.startLR: a vector containing the learning rate for each layer. If this is a scalar, the same learning rate will be used for all the layers.
%% - params.wdType: a vector containing the type of weight decay at each layer. 0 = no weight decay. 1 = L1-norm. 2 = squared L2-norm.
%% - params.wdValue: a vector containing the weight decay value at each layer.

% Percentage of samples you wish to use for training.
params.nTrain = 0.7;
if params.nTrain > 1, params.nTrain = round(params.nTrain); end

% Percentage of samples you wish to use for validation.
params.nValidation = 0.3;

% Size of the minibatches.
params.batchSize = 20;

% Save the network every params.save passes through the data.
params.save = 0;

% Perform regression.
params.task = 'regr';

% Use the mean squared error.
params.cost = 'mse';

% Number of passes through the entire training set.
params.nIter = 1000;

% Use early-stopping.
params.early.use = 1;

% Allow 10 iterations of consecutive error increase before stopping?
params.early.max = 20;

% % Use two hidden layers of size 200.
% params.Nh = [200 200];

% Use the tanh as transfer function.
params.func = {'sigm', 'none'};

% Use a different learning rate for each layer.
params.startLR = [.05 .01];
	
% Use an L2 weight decay.
params.wdType = 2;

%% Strength of the weight decay.
params.wdValue = 10^-5;

params.nFig = 2;

params.verbose = 1 ;

params.Nh = 10; % Use only one hidden layer for the demo.

params.cost = 'sngcrf';

params.verbose = 0;
