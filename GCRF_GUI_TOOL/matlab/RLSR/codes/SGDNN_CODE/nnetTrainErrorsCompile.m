% Script to extract and display relevant information from the training errors.

costBatches(index) = mean(train(j).errors);

% Compute the time spent to compute this so that it is
% not taken into account in the global computation time.
startOverhead = cputime;

if i > 1
	
	% To compute the average error over one epoch, we have to start nBatchesTrain ago.
	startIndex = index - nBatchesTrain;
	
	if index == nBatchesTrain + 1
		
		% errors.train(k) contains the average error over the last epoch with k-1 the last minibatch.
		errors.train(1) = mean(costBatches(1:nBatchesTrain));
		errorDiff = (costBatches(index) - costBatches(startIndex))/nBatchesTrain;
		errors.train(2) = errors.train(1) + errorDiff;
		
	elseif index > nBatchesTrain + 1
		
		errorDiff = (costBatches(index) - costBatches(startIndex))/nBatchesTrain;
		errors.train(startIndex + 1) = errors.train(startIndex) + errorDiff;
		
	else
		% Do nothing.
	end
	
	% Don't plot too often
% 	if ~mod(index, 50)
% 		sfigure(1);
% 		subplot(1,params.nFig,1);
% 		plot(1 + (2:(startIndex + 2))/nBatchesTrain, errors.train(1:(startIndex + 1)));
% 		eval(['title(''Training ', params.cost, ''');']);
% 		drawnow;
% 	end
end

% Update the total overhead time.
overhead = overhead + cputime - startOverhead;