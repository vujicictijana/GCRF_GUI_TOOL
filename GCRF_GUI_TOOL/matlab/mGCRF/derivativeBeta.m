function dubeta = derivativeBeta(Data,invQL,Qhhinv,Qll,Qlh,Qhl,Qhh,ylabel,mu,ubeta)

nt = Data.N*Data.Ttr;
dubeta = nan(1,Data.nbeta);
% Q = 2*(Q1+Q2);
% Qll = Q(Data.label(1:nt),Data.label(1:nt));
label=Data.label(1:nt);

for i=1:Data.nbeta
    dQ = -2*Data.betaMatrix{i};
    aux = full(sum(dQ,2));
    dQdiag = spconvert([(1:length(aux))', (1:length(aux))', aux]);
    dQ = dQ - dQdiag;
    
    
    dQll = dQ(label,label);
%      a=size(dQll)
    dQlh=dQ(label,~label);
%      b=size(dQlh)
    dQhl=dQ(~label,label);
%      c=size(dQhl)
    dQhh=dQ(~label,~label);
%     d=size(dQhh)
    %Qhhinv=inv(Qhh);
    dinvQlbeta=dQll- dQlh*Qhhinv*Qhl+ Qlh*Qhhinv*dQhh*Qhhinv*Qhl-Qlh*Qhhinv*dQhl;
    % dinvQlbeta=dQll- (dQlh/Qhh)*Qhl+ ((Qlh/Qhh)*dQhh)/Qhh*Qhl-(Qlh/Qhh)*dQhl;
    
   %dinvQlbeta=dQll- dQlh*Qhh\Qhl+ Qlh*Qhh\dQhh*Qhh\Qhl-Qlh*Qhh\dQhl;

%     dQLbeta=QL*dQbeta*QL;
    %dQLbeta=dinvQlbeta;
   % tt = computeTraceParallel(invQL,dinvQlbeta);
    
    %tic;
    %computeTraceParallel(invQL,dinvQlbeta);
    %trace(invQL\dinvQlbeta);
    %fprintf('\fraction of missing = %f Denisty of dinvQlbeta = %f and time = %f ', size(dQhh)/(size(dQhh)+size(dQll))  , nnz(dinvQlbeta)/prod(size(dinvQlbeta)), toc);
        nn= size(dQll,1)/Data.Ttr;

   %  dubeta(i)= (-1/2*(ylabel+mu)'*dinvQlbeta*(ylabel-mu) + 1/2*computeTraceParallel(invQL,dinvQlbeta))*exp(ubeta(i));%-2*exp(ubeta(i))/2;   
    dubeta(i)= (-1/2*(ylabel+mu)'*dinvQlbeta*(ylabel-mu) +1/2 * blokTraceInvMatrix(invQL, dinvQlbeta, nn,Data.Ttr)) *exp(ubeta(i));%-2*exp(ubeta(i))/2;   

end;
