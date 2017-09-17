%% Prepare the fields for the neural network training.

% Size of the input.
nValues = cols(train(1).data);

% Size of the output.
nLabels = cols(train(1).labels);

% If we are doing regression, we need only two figures. If we are doing classification, we need 3.
% If there is no validation set, we need one fewer.
switch params.task
case 'regr'
	params.nFig = 2;
case 'class'
	params.nFig = 3;
end

if length(validation) == 0
	params.nFig = params.nFig - 1;
end

% sfigure(1);
% clf;

index = 0;
nIter = params.nIter;

% Retrieve the number of training and validation batches.
nBatchesTrain = length(train);
nBatchesValidation = length(validation);

% Retrieve the number of layers in the network.
nLayers = length(layers);

% Initialize the variables storing the errors and the computation times.
timeSpent = zeros(1, nIter);
errors.train = zeros(1, (nIter-1)*nBatchesTrain);
errors.validation.class = zeros(1, (nIter-1)*nBatchesTrain);
errors.validation.cost = zeros(1, (nIter-1)*nBatchesTrain);
stepValidation = zeros(1, (nIter-1)*nBatchesTrain);

startCPU = cputime;
overhead = 0;

% If we use early stopping, initialize the counter for the number of error increases.
if params.early.use == 1, params.early.count = 0; end
