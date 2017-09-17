function [calc_mse] = mse(m)
    calc_mse = (m(:)'*m(:))/length(m(:));

end