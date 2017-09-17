function [best_layer, best_Lambda, best_Theta, best_lambda, time_spent] = ...
    RLSR_train_valid(x, y, trainSize, validSize, lambda_set, maxIter, LFSize, nn_params, sse_params)
%RLSR_TRAIN_VALID Summary of this function goes here
%   Detailed explanation goes here
    % x: data
    % y: label
    % trainSize: length of training data
    % validSize: length of validataion data
    % testSize: length of test data
    % maxIter: maximum number of iteration
    % LFsize: number of look forward steps of stop criteria
    % nn_params: parameters of nn
    % sse_params: parameters of structure learning
    
    %% Output Structures
    layerStructs = {};
    lambdaMatrices = {};
    thetaMatrices = {};
    
    %% Construct the model parameter for nn
    model.m = size(x,1);
    model.n = size(x,2);
    model.p = size(y,2);
    model.l = model.n/model.p;
    model.Lambda = eye(model.p);
    model.Theta = eye(model.p);
    model.c = nn_params.batchSize;
    
    % Construct the s matrix for structure learning
    p = model.p;
    y_train = y(1:trainSize,:);
    y_valid = y(trainSize+1:trainSize+validSize,:);
    yy = 1/trainSize*(y_train'*y_train);
    s(1:p,1:p) = yy;
    
    %% Initialization the parameters
    rng('default')
    % Initialization for iterative learning
    best_valid_mse = Inf;
    %% Warm Start with a regular neural netowrk
    % Constructing data
    xNN = reshape(x', model.l, model.m*model.p)';
    yNN = reshape(y', model.m*model.p, 1);
    
    data_train = xNN(1:trainSize*model.p,:);
    label_train = yNN(1:trainSize*model.p,:);
    
    data_valid = xNN(trainSize*model.p+1:(trainSize+validSize)*model.p,:);
    label_valid = yNN(trainSize*model.p+1:(trainSize+validSize)*model.p,:);
    
    % Training the model
    tic;
    nn_params.cost = 'mse';
    [layers, ~, ~, ~] = nnet(data_train, label_train, nn_params, model);
    layers = fprop(layers, xNN);
    hNN = layers(end).output;
    h = reshape(hNN, model.p, model.m)';
    h = -h;

    h_train = h(1:trainSize,:);
    h_valid = h(trainSize+1:trainSize+validSize,:);
    
    % Log to the console
    fprintf('Out Iteration \t TrainMSE \t ValidMSE \n');
    pred_train = hNN(1:trainSize*model.p,:);
    pred_valid = hNN(trainSize*model.p+1:(trainSize+validSize)*model.p,:);
    
    trainMSE = mse(label_train - pred_train);
    validMSE = mse(label_valid - pred_valid);
    
    yh = 1/trainSize*(y_train'*h_train);
    hh = 1/trainSize*(h_train'*h_train);
    fprintf('NN: %g \t %g \t %g \n', 0, trainMSE, validMSE);
    
    % Log to output
    layerStructs{1} = layers;
    trainMSEs(1) = trainMSE;
    validMSEs(1) = validMSE;
    
    %% NN+ZICO Structure Learning
    best_validMSE = Inf;
%     lambda_set = [0.0001,0.0005,0.001,0.005,0.01,0.05,0.1,0.5,1,5];
%     lambda_set = [0.0001,0.001,0.01,0.01,0.1,1];
    s(1:p,p+1:2*p) = yh;
    s(p+1:2*p,1:p) = yh';
    s(p+1:2*p,p+1:2*p) = hh;
    for i = 1:length(lambda_set)
        lambda = lambda_set(i);
        [Lambda, Theta, ~] = gcrf_newton(s, lambda, model.Lambda, model.Theta, sse_params);     
    
        % Train MSE
        pred_train = -Lambda\(Theta'*h_train');
        pred_train = pred_train';
        trainMSE = mse(y_train - pred_train);
        
        % Valid MSE
        pred_valid = -Lambda\(Theta'*h_valid');
        pred_valid = pred_valid';
        validMSE = mse(y_valid - pred_valid);
        

        
        if(validMSE <= best_validMSE)
            best_trainMSE = trainMSE;
            best_validMSE = validMSE;
            best_Lambda = Lambda;
            best_Theta = Theta;
            best_lambda = lambda;
        end
    end
        
    % Reassign the global variables
    lambda = best_lambda;
    trainMSE = best_trainMSE;
    validMSE = best_validMSE;
    model.Lambda = best_Lambda;
    model.Theta = best_Theta;
   
    fprintf('NN+ZICO: %g \t %g \t %g \n', 0, trainMSE, validMSE);

    % Log to output
    lambdaMatrices{1} = model.Lambda;
    thetaMatrices{1} = model.Theta;
    trainMSEs(2) = trainMSE;
    validMSEs(2) = validMSE;
    
    %% Iteratively learning
    for it = 1:maxIter
        %% Representation Learning
        nn_params.cost = 'sngcrf';
        [layers, ~, nn_params, ~] = nnet(data_train, label_train, nn_params, model);
        layers = fprop(layers, xNN);
        hNN = layers(end).output;
        h = reshape(hNN, model.p, model.m)';

        h_train = h(1:trainSize,:);
        h_valid = h(trainSize+1:trainSize+validSize,:);

        % Log to the console
        pred_train = -model.Lambda\(model.Theta'*h_train');
        pred_train = pred_train';
        trainMSE = mse(y_train - pred_train);
        
        pred_valid = -model.Lambda\(model.Theta'*h_valid');
        pred_valid = pred_valid';
        validMSE = mse(y_valid - pred_valid);      
        
        yh = 1/trainSize*(y_train'*h_train);
        hh = 1/trainSize*(h_train'*h_train);
        fprintf('RL: %g \t %g \t %g \n', it, trainMSE, validMSE);

        % Log to output
        for i = 1:length(layers)
            layers(i).output = [];
        end
        layerStructs{it+1} = layers;
        trainMSEs(2*it+1) = trainMSE;
        validMSEs(2*it+1) = validMSE;
        
        % Stop Criteria
        if(validMSE < best_valid_mse)
            best_valid_mse = validMSE;
            best_mse.best_valid_mse = validMSE;
            best_mse.best_train_mse = trainMSE;
            best_mse.best_it_index = it;
            early_count = 0;
            
            best_layer = layers;
            best_Lambda = model.Lambda;
            best_Theta = model.Theta;
        else
            early_count = early_count+1;
        end
        
        if(early_count == LFSize)
            break;
        end  
        
        %% Structure Learning
        s(1:p,p+1:2*p) = yh;
        s(p+1:2*p,1:p) = yh';
        s(p+1:2*p,p+1:2*p) = hh;
        [model.Lambda, model.Theta, ~] = gcrf_newton(s, lambda, model.Lambda, model.Theta, sse_params);     

        % Log to console
        % Train MSE
        pred_train = -model.Lambda\(model.Theta'*h_train');
        pred_train = pred_train';
        trainMSE = mse(y_train - pred_train);
        
        % Valid MSE
        pred_valid = -model.Lambda\(model.Theta'*h_valid');
        pred_valid = pred_valid';
        validMSE = mse(y_valid - pred_valid);        
        
        fprintf('SSE: %g \t %g \t %g \n', it, trainMSE, validMSE);

        % Log to output
        lambdaMatrices{it+1} = model.Lambda;
        thetaMatrices{it+1} = model.Theta;
        trainMSEs(2*it+2) = trainMSE;
        validMSEs(2*it+2) = validMSE;    
        
        % Stop Criteria
        if(validMSE < best_valid_mse)
            best_valid_mse = validMSE;
            best_mse.best_valid_mse = validMSE;
            best_mse.best_train_mse = trainMSE;
            best_mse.best_it_index = it;
            early_count = 0;
            
            best_layer = layers;
            best_Lambda = model.Lambda;
            best_Theta = model.Theta;
        else
            early_count = early_count+1;
        end
        
        if(early_count == LFSize)
            break;
        end        
    end
    time_spent = toc;
    best_layer = serialize(best_layer);   
end

