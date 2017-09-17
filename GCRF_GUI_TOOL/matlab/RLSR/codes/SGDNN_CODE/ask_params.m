function params = ask_params(params, field, type, question, value)

% params = ask_params(params, field, type, question, value) checks if the field
% 'field' exists. If it doesn't, it will ask the user the question 'question'.
% The type allows for different types of answer (strings, binary, real, ...).
% The default value for the parameter is 'value'.

wrong_input = 0;
switch type
case 'pos'
	while ~isfield(params,field) | ~isnumeric(params.(field)) | params.(field) < 0
	 if wrong_input == 1, fprintf('Improper value.\n'); end
	 params.(field) = input(question);
	 if isempty(params.(field)), params.(field) = value; end
	 wrong_input = 1;
	end

case 'pos_int'
	while ~isfield(params,field) | ~isnumeric(params.(field)) | params.(field) < 0 | round(params.(field)) ~= params.(field)
	 if wrong_input == 1, fprintf('Improper value.\n'); end
	 params.(field) = input(question);
	 if isempty(params.(field)), params.(field) = value; end
	 wrong_input = 1;
	end

case 'binary'
	while ~isfield(params,field) | ~isnumeric(params.(field)) | (params.(field) ~= 0 & params.(field) ~= 1)
	 if wrong_input == 1, fprintf('Improper value.\n'); end
	 params.(field) = input(question);
	 if isempty(params.(field)), params.(field) = value; end
	 wrong_input = 1;
	end
end
