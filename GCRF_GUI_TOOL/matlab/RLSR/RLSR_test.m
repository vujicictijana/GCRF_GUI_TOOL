function [test_mse, pred_test] = RLSR_test(x, y, best_layer, best_Lambda, best_Theta)
%RLSR_TEST Summary of this function goes here
%   Detailed explanation goes here
    m = size(x,1);
    n = size(x,2);
    p = size(y,2);
    l = n/p;
    
    best_layer = deserialize(best_layer);
    
    xNN = reshape(x', l, m*p)';
    layers = fprop(best_layer, xNN);
    hNN = layers(end).output;
    h = reshape(hNN, p, m)';
    
    pred_test = -best_Lambda\(best_Theta'*h');
    pred_test = pred_test';
    test_mse = mse(y - pred_test);

end

