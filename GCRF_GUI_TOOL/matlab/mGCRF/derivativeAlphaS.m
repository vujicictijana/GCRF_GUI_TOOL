function dualpha = derivativeAlpha(Data, invQL,Qhhinv,Qll,Qlh,Qhl,Qhh,ylabel,mu,ualpha)
nt = Data.N*Data.Ttr;             
dualpha = nan(1,Data.nalpha);

label=Data.label(1:nt);
%Q = 2*(Q1+Q2);
%Qll = Q(label,label);
for i=1:Data.nalpha
    
    dQ = 2*Data.alphaMatrix{i};
    dQll = dQ(label,label);
    dQlh=dQ(label,~label);
    dQhl=dQ(~label,label);
    dQhh=dQ(~label,~label);
    
    %dQLalpha=dQll- dQlh/Qhh*Qhl+ Qlh/Qhh*dQhh/Qhh*Qhl-Qlh/Qhh*dQhl;
    %Qhhinv=inv(Qhh);
    
    %invQL=Qll-Qlh*Qhh\Qhl; % inv(A)*b je A\b; b/A for b*inv(A)
    dinvQlalpha=dQll- dQlh*Qhhinv*Qhl+ Qlh*Qhhinv*dQhh*Qhhinv*Qhl-Qlh*Qhhinv*dQhl;
    %dinvQlalpha=dQll- (dQlh/Qhh)*Qhl+ ((Qlh/Qhh)*dQhh)/Qhh*Qhl-(Qlh/Qhh)*dQhl;
    %dinvQlalpha=dQll- dQlh*Qhh\Qhl+ Qlh*Qhh\dQhh*Qhh\Qhl-Qlh*Qhh\dQhl;
    %dQLalpha=QL*dQalpha*QL;

    
    db = 2*Data.R{i}(:,1:Data.Ttr);
    db = db(:);
    dbl = db(label);
 % dualpha(i)= (-1/2*(ylabel-mu)'*dinvQlalpha*(ylabel-mu) + (dbl' - mu'*dinvQlalpha)*(ylabel-mu) + 1/2*computeTraceParallel(invQL,dinvQlalpha))*exp(ualpha(i));%-2*exp(ualpha(i));
  
   dualpha(i)= (-1/2*(ylabel-mu)'*dinvQlalpha*(ylabel-mu) + (dbl' - mu'*dinvQlalpha)*(ylabel-mu) + 1/2*trace(invQL\dinvQlalpha))*exp(ualpha(i));%-2*exp(ualpha(i));
  
   
end;