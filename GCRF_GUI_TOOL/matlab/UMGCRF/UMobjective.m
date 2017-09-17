function [l, delta_theta, meu] = UMobjective(Theta,Y,V,D,L,R,C,P)
%
%Theta = Theta / sqrt(Theta * Theta');
[n,a] = size(R);
alpha = Theta(1:a);
beta = Theta(a+1);
gamma = sum(alpha);

M_inv = zeros(n,1);
for i = 1:n
    M_inv(i) = 1/(2*(gamma + beta*D(i)) );
end
M = zeros(n,1);
for i = 1:n
    M(i) = (2*(gamma + beta*D(i)) );
end
meu = 2 * (V * (M_inv .* (C * alpha') ) );
zeta = Y - meu; z2 = -(zeta' * zeta);
tr = 0; 
for i = 1:n
    tr = tr + 1/(2*(gamma + beta*D(i)) );
end
delta_alpha = zeros(1,a);
for i = 1:a
    delta_alpha(i) = z2 + 2*(R(:,i) - meu)'*zeta + tr;
end
tr = 0; 
for i = 1:n
    tr = tr + D(i)/(2*(gamma + beta*D(i)) ); 
end
delta_beta = -(transpose(Y + meu) * L * zeta) + tr;

%likelihood
Ca = C * alpha'; Ca2 = -2 * Ca .* Ca;
l = P'*M + 2*(Y'*R*alpha') + ((Ca2 - .5*ones(n,1))' * M_inv);
l = -l;

delta_theta = [-delta_alpha,-delta_beta];
