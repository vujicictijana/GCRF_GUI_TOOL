function batches = createBatches(data, labels, batchSize)

%% Create minibatches of size batchSize.
%% If only two arguments are provided, we assume there are no labels.
%%
%% batches is a structure array where each element contains a field data (with
%% the datapoints of that minibatch) and a field labels (with the labels of that
%% minibatch). If the total number of samples is not a multiple of the batch
%% size, the last minibatch is made bigger.

if nargin == 2, batchSize = labels; end

[nSamples nValues] = size(data);
nLabels = cols(labels);
nBatches = ceil(nSamples/batchSize);

if nargin == 3

	batches = repmat(struct('data', zeros(batchSize, nValues), 'labels', zeros(batchSize, nLabels)), 1, nBatches);

	if nSamples
		index = 0;
		for i = 1:(nBatches-1)
			batches(i).data = data( index + (1:batchSize), :);
			batches(i).labels = labels( index + (1:batchSize), :);
			index = index + batchSize;
		end
		
		% Remaining samples in the last batch.
		batches(end).data = data( (index+1) :end, :);
		batches(end).labels = labels( (index+1) :end, :);
	end
	
elseif nargin == 2
	batches = repmat(struct('data', zeros(batchSize, nValues)), 1, nBatches);

	if nSamples
		index = 0;
		for i = 1:(nBatches-1)
			batches(i).data = data( index + (1:batchSize), :);
			index = index + batchSize;
		end
		
		% Remaining samples in the last batch.
		batches(end).data = data( (index+1) :end, :);
	end
else
	error('Wrong number of arguments');
end
