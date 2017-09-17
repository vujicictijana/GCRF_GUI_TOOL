function [ likelihood ] = calcObjective_ml( hh, yy, yh, Lambda, Theta, lambda)
%CALCOBJECTIVE Summary of this function goes here
%   Detailed explanation goes here
    p = size(hh, 1);
    R = chol(Lambda);
    likelihood = -2*sum(log(diag(R))) + trace(Lambda*yy + 2*yh*Theta + Lambda\(Theta'*hh*Theta)) + lambda*(sum(abs(Lambda(eye(p)<1))) + sum(abs(Theta(:))));
end

