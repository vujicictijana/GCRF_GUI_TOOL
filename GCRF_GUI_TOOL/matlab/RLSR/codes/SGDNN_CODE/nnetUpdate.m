function layers = nnetUpdate(layers, gradient)

%% nnetUpdate update the parameters of the neural network defined by layers.
%% gradient is a structure the same size as layers containing all gradients.

nLayers = length(layers);
	
for i = nLayers:-1:1
	layers(i).updates = layers(i).updates + 1;
	eps = layers(i).startLR/(1 + layers(i).decr*layers(i).updates);
		
	% It seems like creating extra variables W and B is faster. Aaaah, the mysteries of MATLAB.
	gradW = gradient(i).W;
	gradB = gradient(i).B;
		
	W = layers(i).W;
	B = layers(i).B;
		
	W = W - eps*gradW;
	B = B - eps*gradB;
	layers(i).W = W;
	layers(i).B = B;
	layers(i).eps = eps;
end
