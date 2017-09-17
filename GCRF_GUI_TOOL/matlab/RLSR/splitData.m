function [trainValidX, trainValidY, testX, testY] = splitData(x, y, trainSize, validSize, testSize)
    p = size(y,1);
    m = size(y,2);
    l = size(x,2); % l = m*r, where r is the number of attributes per node
    r = l/m;
    n = r*p; % n is the number of attributes per graph instance
    
    yNew = y'; % Now y is in m x p;
    xNew = zeros(m, n);
    for i = 1:m
        indStart = (i-1)*r+1;
        indEnd = i*r;
        xSub = x(:,indStart:indEnd)';
        xNew(i,:) = xSub(:)';
    end
    
    trainValidX = xNew(1:trainSize+validSize,:);
    trainValidY = yNew(1:trainSize+validSize,:);
    testX = xNew(end-testSize+1:end,:);
    testY = yNew(end-testSize+1:end,:);
end