% nnetLib or whatever its name (Author: Nicolas Le Roux, nicolas@le-roux.name)
% Version 1.0 Feb 2012
%
% This toolbox makes the training of multilayer neural networks incredibly easy.
% It doesn't display nice class boundaries, lengthy statistics.
% Nope, it simply trains the network, but it does so fast (or so I like to believe).
%
% As of now, the only available training method is first-order stochastic (mini-batch) gradient descent.
% I welcome any addition to the code.
%
% nnetLib is available at http://nicolas.le-roux.name/code.html
%
% Toolbox contents
%
%    Contents.m    - This file
%    demo.m        - Trains a neural network on a small version of MNIST.
%
%  User-callable functions
%
%    nnet.m        - trains the neural network
%    nnetTest.m    - tests the neural network on a new dataset
%    parameters.m  - example of values for the parameters of the neural network.
%
%  All the other functions are not meant to be called by the user.
%
% Questions or problems; email me - nicolas@le-roux.name.