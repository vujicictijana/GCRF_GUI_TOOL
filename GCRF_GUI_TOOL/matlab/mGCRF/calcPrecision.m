function [Q1 Q2 b] = calcPrecision(ualpha, ubeta, Data, str)
% % ualpha;
% % ubeta;
if (strcmp(str,'training')==1)
    t = Data.Ttr;
end;

if (strcmp(str,'test')==1)
    t = Data.Ttr + Data.Ttest;
end;
nt = Data.N*t;
Q1 = sparse(nt,nt);
b = zeros(nt,1);
for i=1:Data.nalpha 
    bb =  2*exp(ualpha(i))*Data.R{i}(:,1:t);
    b = b + bb(:);
    Q1 = Q1 + exp(ualpha(i))*Data.alphaMatrix{i}(1:nt,1:nt);
end;
Q2 = sparse(nt,nt);
for i=1:Data.nbeta
    Q2 = Q2 - exp(ubeta(i))*Data.betaMatrix{i}(1:nt,1:nt);
end;
aux = full(sum(Q2,2));
Q2diag = spconvert([(1:length(aux))', (1:length(aux))', aux]);
Q2 = Q2 - Q2diag;

% full(Q1(1:5,1:5));
% full(Q2(1:5,1:5));
% b(1:5);

    
    