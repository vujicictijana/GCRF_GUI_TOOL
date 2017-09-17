function predictionCRF = nezavisniTestCRF(Data,ualpha,ubeta)
%NEZAVISNITESTCRF Summary of this function goes here
%   Detailed explanation goes here

[Q1 Q2 b] = calcPrecision(ualpha, ubeta, Data, 'test');
Q = 2*(Q1 + Q2);

munew = Q\b;
% take parts of precision matrix for conditional prediction
% Q1 = Q(~Data.label,~Data.label);
% size(Q1)
% Qul = Q(~Data.label,Data.label);
% size(Qul)

predictionCRF = munew(~Data.label); %+ Q1\(Qul*(Data.y(Data.label) - munew(Data.label)));
%predictionCRF(predictionCRF<0)=0;
predictionCRF = predictionCRF(end-Data.N*Data.Ttest+1:end);


% function [predictionNN predictionCRF yTrue alpha beta munew]= testCRF(y,R,lagP,ualpha,ubeta, hospitals, Years,data_matrix)
% 
% N = size(R,2);
% beta = exp(ubeta);
% alpha = exp(ualpha);
% 
% [Qnew  bnew]= precMatrix(y,R,alpha,beta,lagP,hospitals, Years,data_matrix);
% munew = Qnew\bnew;
% 
% predictionCRF = munew(end-N+1:end)';
% % predictionCRF(predictionCRF<-10) = -13.61; 
% yTrue = y(end,:);
% predictionNN = R(end,:);

end

