function Data = precmatCRF(R,ytr, ytest,similarity, Data, nalpha, nbeta)
Data.Ttr = size(ytr,2);% function Data = precmatCRF(R,ytr, ytest,locStations, Data, nalpha, nbeta, spatialScale)
Data.Ttest = size(ytest,2);
[N T] = size([ytr ytest])
Data.y = [ytr ytest];
Data.y = Data.y(:);
Data.N = N;
Data.R = R;
Data.nalpha = nalpha;
Data.nbeta = nbeta; 
Data.label = [~isnan(ytr) zeros(size(ytest))];
Data.label = logical(Data.label(:));
alphaMatrix{1} = speye(N*T,N*T);
M0 = sparse(diag(ones(1,T)))
betaMatrix{1} = kron(M0,similarity);
Data.betaMatrix = betaMatrix;
Data.alphaMatrix = alphaMatrix;