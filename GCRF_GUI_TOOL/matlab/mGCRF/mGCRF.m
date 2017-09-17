function [ualpha, ubeta, Data,predictionCRF] = mGCRF(numTimeSteps,training,maxiter,regAlpha,regBeta,RR,y,similarity)

trainingTS = 1:training;
testTS = numTimeSteps;
Data = struct;
Data.regularAlpha = regAlpha*pi; 
Data.regularBeta = regBeta*pi;
Data = precmatCRF(RR,y(:,trainingTS),y(:,testTS),similarity,Data, 1,1);
tic
disp(strcat('m-GCRF training'));
[ualpha, ubeta, pred, Q]= trainCRF(Data,maxiter);
mGCRF_time=toc
predictionCRF= nezavisniTestCRF(Data,ualpha,ubeta);
