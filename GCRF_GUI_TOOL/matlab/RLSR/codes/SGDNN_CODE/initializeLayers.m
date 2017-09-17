function [layers, gradient] = initializeLayers(params, sizeInput, sizeOutput)

%% [layers, gradient] = initializeLayers(params, sizeInput, sizeOutput) sets
%% all the fields in the neural networks (represented by the structure array
%% 'layers') and the gradient network using the sets of parameters stored in
%% params. The two arguments sizeInput and sizeOutput are here to determine the
%% sizes of the first and last layers.

% The list of sizes of each layer.
Nh = params.Nh;

nLayers = length(Nh) + 1;
layers = repmat(struct('W', zeros(0)), 1, nLayers);

% If there are no hidden layers, we have a linear classifier
if ~length(Nh)
	layers(1).size = [sizeInput, sizeOutput];
else
	layers(1).size = [sizeInput, Nh(1)];
	for i = 2:nLayers-1	
		layers(i).size = [Nh(i-1), Nh(i)];
	end	
	layers(nLayers).size = [Nh(end), sizeOutput];
end

% For each layer, initialize the parameters. If the number of parameters is less than the number of layers, all the subsequent layers have the last value of the parameters.
for i = 1:nLayers
	% For the last layer, if we use a 'nll' or 'ce' cost, the last transfer function must be linear.
	if i == nLayers && (strcmp(params.cost, 'nll') || strcmp(params.cost, 'ce'))
		layers(i).func = 'none';
	else
		layers(i).func = params.func{min(i, length(params.func))};
	end
	layers(i).startLR = params.startLR(min(i, length(params.startLR)));
	layers(i).decr = params.decr(min(i, length(params.decr)));
	layers(i).momentum = params.momentum(min(i, length(params.momentum)));
	layers(i).updates = 0;
	layers(i).wdType = params.wdType(min(i, length(params.wdType)));
	layers(i).wdValue = params.wdValue(min(i, length(params.wdValue)));
	layers(i).wdCost = 0;
	layers(i).W = randn(layers(i).size)/sqrt(layers(i).size(1));
	layers(i).B = zeros(1, layers(i).size(2));
	layers(i).eps = 0;
	gradient(i).W = zeros(layers(i).size);
	gradient(i).B = zeros(1, layers(i).size(2));
	
end
