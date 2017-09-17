function [layers, errors, params, timeSpent] = nnet(data, labels, params, model, layers)

%% [layers, errors, params, timeSpent] = nnet(data, labels, params, layers) trains a neural network using the set of parameters params.
%%
%% Inputs:
%% - data is the matrix containing the datapoints, one per row;
%% - labels is the matrix containing the labels, one per row;
%% - params is a structure containing the set of parameters for the network. Information on these parameters may be found in nnetDefaultParams.m .
%% - layers (optional) is a trained network. Use it to continue the optimization.
%%
%% Outputs:
%% - layers is a structure array containing all the parameters of the neural network.
%%			In particular layers(i).W and layers(i).B are the weight matrix and the bias between the i-th and the i-th + 1 layer;
%% - errors is the list of errors at the last iteration;
%% - params is the original structure with the missing entries filled;
%% - timeSpent is a vector containing the computation time for every iteration.
%%
%% Copyright Nicolas Le Roux, 2012. Released under the WTFPL.

% Extract the dimensions of the data.
[nSamples nValues] = size(data);

params.nSamples = nSamples;

% Fill the missing parameter fields, asking for user input if needed.
params = nnetDefaultParams(params);

% If we are using negative log-likelihood with only one label, create a one-hot encoding.
if strcmp(params.cost, 'nll') & cols(labels) == 1
	labels = oneHot(labels);
end

% If the cost is the cross-entropy, make sure the labels are 0 and 1.
if strcmp(params.cost, 'ce') && cols(labels) == 1
	if unique(labels) == [-1; 1], labels = (labels+1)/2 ;
	elseif unique(labels) == [0; 1],
	else error('Wrong setting of the labels.');
	end
end

% If the nTrain parameter was set to a value lower than 1, use it as a proportion.
if params.nTrain <= 1
	params.nTrain = floor(params.nTrain * nSamples / model.p) * model.p;
else
	params.nTrain = floor(params.nTrain);
end

% If the nValidation parameter was set to a value lower than 1, use it as a proportion.
if params.nValidation <= 1
	params.nValidation = ceil(params.nValidation * nSamples / model.p) * model.p;
else
	params.nValidation = ceil(params.nValidation);
end

batchSize = params.batchSize;

% Making sure there are enough datapoints in the matrix provided.
if params.nTrain + params.nValidation > nSamples
	error('Too few samples'); end

% Create batches.
% Batches for the validation set are not required, it is just to avoid memory issues.
train = createBatches(data(1:params.nTrain,:), labels(1:params.nTrain, :), batchSize*model.p);
validation = createBatches(data(params.nTrain + (1:params.nValidation),:), labels(params.nTrain + (1:params.nValidation), :), 1000*model.p);

nLabels =  cols(labels);
if nargin < 5
	% Initialize the layers of the neural network.
	[layers, gradient] = initializeLayers(params, nValues, nLabels);
else
	% Only initialize the gradient.
	[~, gradient] = initializeLayers(params, nValues, nLabels);
end

% Train the network.
[layers, errors, timeSpent] = nnetTrain(layers, gradient, params, model, train, validation);
