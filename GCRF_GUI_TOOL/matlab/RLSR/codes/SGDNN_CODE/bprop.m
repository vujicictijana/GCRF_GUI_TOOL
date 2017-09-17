function gradient = bprop(layers, gradient, gradInput, data)

%% Back-propagation in a neural network.
%% gradInput is the gradient of the cost with respect to the output
%% (I know, it doesn't make much sense but simplifies the code).

nLayers = length(layers);

for i = nLayers:-1:1
	
	W = layers(i).W;
	
	if i == nLayers
		layerOutput = layers(i).output;
	else
		% The output of this layer is the input of the layer above.
		layerOutput = layerInput;
	end
	
	if i == 1
		layerInput = data;
	else
		layerInput = layers(i-1).output;
	end

	% Propagating the gradient through the activation function.
	switch layers(i).func
	case 'none'
		gradFunc = ones(size(gradInput));
	case 'sigm'
		gradFunc = layerOutput.*(1 - layerOutput);
	case 'tanh'
		gradFunc = 1 - layerOutput.^2;
	case 'nic'
		gradFunc = (1 - abs(layerOutput)).^2;
	case 'softplus'
		gradFunc = sigm(layerOutput);
	end
	gradOutput = gradInput.*gradFunc;
	
	% Computing the gradient of the weight decay.
	if layers(i).wdValue > 0
		switch layers(i).wdType
		case 0
			wdGrad = 0;
		case 1
			wdGrad = layers(i).wdValue * sign(W);
		case 2
			wdGrad = layers(i).wdValue * W;
		end
	else
		wdGrad = 0;
	end
	
	% Updating the gradient field (with potential momentum).
	gradW = layerInput'*gradOutput + wdGrad;
	gradB = sum(gradOutput);
	momentum = layers(i).momentum;
	if momentum > 0
		oldGradW = gradient(i).W;
		oldGradB = gradient(i).B;
		gradient(i).W = momentum*oldGradW + (1-momentum)*gradW;
		gradient(i).B = momentum*oldGradB + (1-momentum)*gradB;
	else
		gradient(i).W = gradW;
		gradient(i).B = gradB;
	end

	if i > 1,gradInput = gradOutput*W.'; end
end
