function [predicted, errors] = nnetTest(data, labels, cost, layers)

%% [predicted, errors] = nnetTest(data, labels, params, layers) tests the network on new datapoints.
%%
%% Inputs:
%% - data is the matrix containing the datapoints, one per row;
%% - labels is the matrix containing the labels, one per row;
%% - cost is a string containing the error measure we are interested in.
%%   Possible values are 'mse', 'ce', 'nll' and 'class'.
%% - layers is the structure containing the trained network.
%%
%% Outputs:
%% - predicted is the predicted output for each datapoint;
%% - errors is the list of errors for each datapoint.

% Forward propagation.
layers = fprop(layers, data);
predicted = layers(end).output;

errors = 0;