function [Theta, MSE, meu] = UMtrain(Y,S,R)
%


[n,a] = size(R);


S = (S/sum(sum(S))) * n;    %Normalize S
L = diag(sum(S)) - S;       %Process L
[V,D] = eig(L); D = diag(D);
C = V' * R; P = -0.5 * (V' * Y) .* (V' * Y);
%initialize
alpha = ones(1,a); beta = 0; Theta = [alpha,beta];
Theta = Theta / sqrt(Theta * Theta');

dn = max(D); d0 = min(D);
A = [ones(1,a) , dn; ...
     ones(1,a) , d0];
b = [0; 0];
A = -A;
    %A Theta < b    =>   -A Theta > b

options = optimset('Algorithm','interior-point','MaxIter',50,'GradObj','off','TolX',1e-6,'TolCon',1e-6);
Theta = fmincon(@(Theta)UMobjective(Theta,Y,V,D,L,R,C,P),Theta,A,b,[],[],[],[],[],options);
Theta = Theta / sqrt(Theta * Theta');

alpha = Theta(1:a);
beta = Theta(a+1);
n = length(Y);
gamma = sum(alpha);

M_inv = zeros(n,1);
for i = 1:n   
    M_inv(i) = 1/(2*(gamma + beta*D(i)) ); 
end
meu = 2 * (V * (M_inv .* (C * alpha') ) );
e = Y - meu;
MSE = e' * e / n;


