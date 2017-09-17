% Compute the validation error at the end of every epoch.
if params.nValidation > 0
	stepValidation(i) = index/nBatchesTrain;
% 	sfigure(1);
	startOverhead = cputime;
	errors = computeValidation(layers, params, model, validation, errors, i, stepValidation);
	overhead = overhead + cputime - startOverhead;
end
timeSpent(i) = cputime - startCPU - overhead;