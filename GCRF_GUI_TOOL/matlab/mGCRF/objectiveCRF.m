function [f g mu Qcelo] = objectiveCRF(u,Data)

nt = Data.N*Data.Ttr;

% split vector u to find ualpha and ubeta
ualpha = u(1:Data.nalpha); 
ubeta = u(Data.nalpha+1:end);

% calculate precision matrix
[Q1 Q2 b] = calcPrecision(ualpha, ubeta, Data,'training');
Qcelo = 2*(Q1 + Q2);
label=Data.label(1:nt);
% calculate precision matrix for labeled and hidden data in the training set
Qll=Qcelo(label,label);
oznacenih=size(Qll);
Qlh=Qcelo(label,~label);
Qhl=Qcelo(~label,label);
Qhh=Qcelo(~label,~label);

neoznacenih=size(Qhh);
%disp(strcat('unlabeled =',int2str(size(Qhh)),'; labeled =',int2str(size(Qll)))) ;

%Qsl=[Qll Qlh; Qhl Qhh];
bl = b(label);
ylabel = Data.y(label);
% bh = b(~label);   %yhidden = Data.y(~label);

% calculate likelihood for training data
%RR_l= chol(Qll);
% mu_l = Qll\bl;
% mu_h=Qhh\bh;
% mu=[mu_l' mu_h']';


%RR_h= chol(Qhh);
Qhhinv=Qhh\eye(size(Qhh)); %inv(Qhh);%Qhh\eye(size(Qhh));  %sigma =Q\eye(N);
invQL=Qll-Qlh*Qhhinv*Qhl; % inv(A)*b je A\b; b/A for b*inv(A)
%invQL=Qll-(Qlh/Qhh)*Qhl; % inv(A)*b je A\b; b/A for b*inv(A)
%QL=inv(Qll-Qlh*invQhh*Qhl); % Q je ustvari QL

%f = calcLikelihood(RR,QL,ylabel,mu);invQL               % f = calcLikelihood(RR,Qll,ylabel,mu_l);
RR=chol(invQL);
mu = Qll\bl; % mu je ustavari muL
f = calcLikelihood(RR,invQL,ylabel,mu)+Data.regularAlpha*(1/2)*exp(ualpha)^2+Data.regularBeta*(1/2)*exp(ubeta)^2;   %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%PROVERI DA LI JE INVERSE ILI NE
%%%%%%%%%%%inverse je

% calculate first derivatives with respect to parameters
dualpha = derivativeAlpha(Data,invQL,Qhhinv,Qll,Qlh,Qhl,Qhh,ylabel,mu,ualpha)-Data.regularAlpha*exp(ualpha);
dubeta = derivativeBeta(Data,invQL,Qhhinv,Qll,Qlh,Qhl,Qhh,ylabel,mu,ubeta)-Data.regularBeta*exp(ubeta);

g =[-dualpha -dubeta];
%disp(strcat('g =[',num2str(-dualpha),',',num2str(-dubeta), ']')) ;

% f;
% g;