function layers = fprop(layers, data)

%% Forward propagation in a neural network.
%% The output of each layer is stored in layers(i).output

nLayers = length(layers);
nSamples = rows(data);

output = data;
for i = 1:nLayers
	W = layers(i).W;
	B = layers(i).B;
	func = layers(i).func;

	% This is uglier but faster than an feval.
	totalInput = output*W + repmat(B, nSamples, 1);
	switch func
	case 'none'
		output = totalInput;
	case 'sigm'
		output = sigm(totalInput);
	case 'tanh'
		output = tanh(totalInput);
	case 'nic'
		output = nic(totalInput);
	case 'softplus'
		output = softplus(totalInput);
	otherwise
		error('Unknown transfer function');
	end
	layers(i).output = output;
end

% Adding the weight decay errors
for i = 1:nLayers
	wdValue = layers(i).wdValue;
	% Are we due for a weight decay computation now?
	if wdValue > 0
		W = layers(i).W;
		switch layers(i).wdType
		case 0
			layers(i).wdCost = 0;
		case 1
			layers(i).wdCost = wdValue * sum(abs(W(:)));
		case 2
			layers(i).wdCost = .5 * wdValue * sum(W(:).^2);
		end
	else
		layers(i).wdCost = 0;
	end
end
