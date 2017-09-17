function [layers, errors, timeSpent] = nnetTrain(layers, gradient, params, model, train, validation)

% Prepare the fields for the neural network training.
nnetPrepare;

for i = 1:nIter
	if params.verbose == 1, fprintf('Iter %i	', i); end
	
	for j = 1:nBatchesTrain
		index = index+1;
		
		% Forward propagation.
		layers = fprop(layers, train(j).data);
		
		% Computing the training error and check for NaNs.
		[train(j).errors gradOutput] = computeCost(layers, train(j).labels, params.cost, model);
		if sum(isnan(gradOutput(:)))
			fprintf('NaNs have been found.');
			keyboard;
		end
		
		% Back-propagation.
		gradient = bprop(layers, gradient, gradOutput, train(j).data);
		
		% Update the parameters of the neural network.
		layers = nnetUpdate(layers, gradient);
		
		% Extract and display relevant information from the training errors.
		nnetTrainErrorsCompile;
		
	end
	
	% At the end of each pass through the data, we compute the error on the validation set.
	nnetValidErrorsCompile;

	% Display the time spent so far.
	if params.verbose == 1, fprintf('Time: %g\n', timeSpent(i)); end

	% Save the network.
	if ~mod(i, params.save)
		save(['layers_iter=', num2str(i)], 'layers');
	end

	% If we do validation and early-stopping keep track of the best network.
	if params.nValidation > 1 && params.early.use == 1
		if i == 1
			% At the first iteration, we always store the network.
			best_layers = layers;
			best_error = errors.validation.cost(1);
		else
			% If the error is better, we store the network.
			if errors.validation.cost(i) < best_error
				best_layers = layers;
				best_error = errors.validation.cost(i);
				params.early.count = 0;
			else
				% If the error is worse, we add 1 to the counter.
				params.early.count = params.early.count + 1;
			end
		end
	
		% When we reach the last iteration or when the counter is full, get the best network and stop the optimization.
		if i == nIter || params.early.count == params.early.max
			layers = best_layers;
			break;
		end
	end

end

% Tighten the axes on the plots.
% for nFig = 1:params.nFig
% 	subplot(1,params.nFig,nFig);
% 	axis('tight');
% end
if params.verbose == 1, fprintf('\n'); end
