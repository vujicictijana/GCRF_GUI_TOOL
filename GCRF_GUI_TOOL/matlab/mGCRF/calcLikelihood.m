function likelihood = calcLikelihood(RR,Q,ylabel,mu)
likelihood = - sum(log(diag(RR))) + 0.5*(ylabel-mu)'*Q*(ylabel-mu);
