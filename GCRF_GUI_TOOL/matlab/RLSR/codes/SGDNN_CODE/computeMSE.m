function [mseError, mu] = computeMSE(output, labels, cost, model)
%COMPUTEMSE Summary of this function goes here
%   Detailed explanation goes here
%% outputs - stores the outputs of Neural Network
[nSamples ~] = size(output);
nG = nSamples / model.p;

switch cost
    case 'mse'
        output = reshape(output, model.p, nG).';
        labels = reshape(labels, model.p, nG).';
        mu = output;
        mseError = mse(mu(:) - labels(:));
        mu = reshape(mu', model.p*nG, 1);  
    case 'sngcrf'
        output = reshape(output, model.p, nG).';
        labels = reshape(labels, model.p, nG).';
        mu = -model.Lambda\(model.Theta'*output');
        mu = mu';
        mseError = mse(mu(:) - labels(:));
        mu = reshape(mu', model.p*nG, 1);  
    case 'ground'
        output = reshape(output, model.p, nG).';
        labels = reshape(labels, model.p, nG).';
        mu = -model.Lambda\(model.Theta'*output');
        mu = mu';
        mseError = mse(mu(:) - labels(:));
        mu = reshape(mu', model.p*nG, 1);    	
end

