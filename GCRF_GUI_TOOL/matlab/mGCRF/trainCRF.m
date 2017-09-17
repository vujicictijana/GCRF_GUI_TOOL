function [ualpha ubeta pred Q]= trainCRF(Data,maxiter)

ualpha =  zeros(1,Data.nalpha);
ubeta = zeros(1,Data.nbeta);
x0 = [ualpha ubeta];%x0=x0+0.5;
% needNewWorkers = (matlabpool('size') == 0);
% if needNewWorkers
%     % Open a new MATLAB pool with 4 workers.
%     matlabpool open 4
% end

options = optimset('Display','Iter','GradObj','on','MaxIter',maxiter); 
options = optimset(options,'UseParallel','always');
u = fminunc(@(x)objectiveCRF(x,Data),x0,options);  
[~,~,pred,Q]= objectiveCRF(u,Data);

ualpha = u(1:Data.nalpha) ;
ubeta = u(Data.nalpha+1:end);

