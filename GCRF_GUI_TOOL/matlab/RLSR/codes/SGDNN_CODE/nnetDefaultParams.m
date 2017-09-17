function params = nnetDefaultParams(params);

%% Prepare the parameters for the training of a neural network.
%% Asks for user input when required.
%%
%% Main fields you may wish to set:
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
%%
%% Check the file for the remaining parameters.

% Number of training examples.
params = ask_params(params, 'nTrain', 'pos', 'Number of training samples (a value < 1 denotes a proportion, default = .9)? ', .9);

% Round the number of training examples.
if params.nTrain > 1, params.nTrain = round(params.nTrain); end

%% Number of validation samples %%
params = ask_params(params, 'nValidation', 'pos', 'Number of validation samples (a value < 1 denotes a proportion, default = .1)? ', .1);

% Round the number of validation examples.
if params.nValidation > 1, params.nValidation = round(params.nValidation); end

% Size of the minibatches.
params = ask_params(params, 'batchSize', 'pos_int', 'Size of the mini-batches (default = 20)? ', 20);

% Save the network every params.save epochs (0 = disable).
params = ask_params(params, 'save', 'pos_int', 'Every how many epochs do we save the network (default = 0 = disable)? ', 20);

%% Task to perform.
% Possible options:
% class: classification.
% regr: regression.
% If the task is 'class', the classification error on the validation set is computed.
if ~isfield(params, 'task')
	params.task = 'regr';
	fprintf('task set to %s\n', params.task);
end

%% Output cost.
% Possible options:
% mse: mean squared error
% nll: negative log-likelihood
% ce: cross entropy
% class: classification error, cannot be used for training.
if ~isfield(params, 'cost')
	switch params.task
	case 'regr'
		params.cost = 'mse';
	case 'class'
		params.cost = 'nll';
	end
	fprintf('cost set to %s\n', params.cost);
end

%% Number of iterations through the entire training set.
if ~isfield(params, 'nIter')
	params.nIter = 20;
	fprintf('nIter set to %i\n', params.nIter);
end

%% Do we use early-stopping?
if ~isfield(params, 'early') || ~isfield(params.early, 'use')
	params.early.use = 1;
	fprintf('Early stopping will be used\n');
end

%% How many iterations of consecutive increase do we allow before stopping?
if params.early.use == 1 && ~isfield(params.early, 'max')
	params.early.max = 10;
	fprintf('Early stopping max counter set to %i\n', params.early.max);
end


%% Number of hidden layers and their size.
% It is a vector containing as many elements as the number of hidden layers.
% The i-th element is the number of hidden units in the i-th hidden layer.
if ~isfield(params, 'Nh')
	params.Nh = [];
	fprintf('No hidden layer set\n');
end

if ~isfield(params, 'verbose')
	params.verbose = 1;
end

%% From now on, all the parameters are set independently for each layer.
%% The fields are vectors (or cells if the parameters are strings).
%% The i-th element is the parameter value for the i-th layer.
%% If there are fewer elements than the number of layers,
%% the last field is used for all subsequent layers.

%% Transfer functions.
% Possible options:
% sigm: sigmoid, f(x) = 1/(1 + e^(-x) )
% tanh: hyperbolic tangent
% nic: x/(1 + |x|)
% softplus: f(x) = log(1 + e^x)
% none
if ~isfield(params, 'func'), params.func = {'sigm'}; end

%% Initial learning rate.
if ~isfield(params, 'startLR')
	params.startLR = .001;
end
	
%% Decrease constant.
if ~isfield(params, 'decr'), params.decr = 0; end

%% Momentum for the gradient.
if ~isfield(params, 'momentum'), params.momentum = 0; end	

%% Type of weight decay.
% 0: no weight decay.
% 1: L1 norm.
% 2: Squared L2 norm.
if ~isfield(params, 'wdType'), params.wdType = 0; end	

%% Value of the weight decay.
if ~isfield(params, 'wdValue'), params.wdValue = 0;	end
