function [mu] = UMtest(Ytest,S,Rtest,theta)

alpha = theta(1:1)'; gamma = sum(alpha); beta = theta(end);
n = size(Ytest,1)
L = diag(sum(S)) - S;
Q = beta*L + gamma*eye(n);
mu = Q\(Rtest*alpha); error = Ytest - mu;
MSE_test_UMGCRF = error'*error / n; %#ok<*NASGU>