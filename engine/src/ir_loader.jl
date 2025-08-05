# IR JSON file loading and parsing

"""
    load_ir(file_path::String) -> IRData

Load and parse IR JSON file into IRData structure.
"""
function load_ir(file_path::String)::IRData
    if !isfile(file_path)
        throw(ArgumentError("IR file not found: $file_path"))
    end
    
    try
        # Load JSON data
        json_data = JSON3.read(read(file_path, String))
        
        # Extract main components and convert to Dict{String, Any}
        deal = convert_to_dict(get(json_data, :deal, Dict()))
        assets = [convert_to_dict(a) for a in get(json_data, :assets, [])]
        components = [convert_to_dict(c) for c in get(json_data, :components, [])]
        streams = [convert_to_dict(s) for s in get(json_data, :streams, [])]
        calendar = convert_to_dict(get(json_data, :calendar, Dict()))
        assumptions = convert_to_dict(get(json_data, :assumptions, Dict()))
        logic_blocks = [convert_to_dict(lb) for lb in get(json_data, :logic_blocks, [])]
        waterfall = isnothing(get(json_data, :waterfall, nothing)) ? nothing : convert_to_dict(json_data.waterfall)
        
        # Parse stochastic parameters
        stochastic_params = parse_stochastic_parameters(json_data)
        
        return IRData(
            deal,
            assets,
            components, 
            streams,
            calendar,
            assumptions,
            logic_blocks,
            waterfall,
            stochastic_params
        )
        
    catch e
        throw(ArgumentError("Failed to parse IR file '$file_path': $(e)"))
    end
end

"""
    parse_stochastic_parameters(json_data) -> Dict{String, StochasticParameter}

Parse stochastic parameter definitions from IR JSON.
"""
function parse_stochastic_parameters(json_data)::Dict{String, StochasticParameter}
    stochastic_params = Dict{String, StochasticParameter}()
    
    # Look for stochastic parameters in assumptions section
    if haskey(json_data, :assumptions)
        assumptions = json_data.assumptions
        for (param_name, param_def) in assumptions
            param = parse_parameter_definition(param_def)
            if param !== nothing
                stochastic_params[String(param_name)] = param
            end
        end
    end
    
    # Look for stochastic parameters in streams (growth rates, amounts, etc.)
    if haskey(json_data, :streams)
        for stream in json_data.streams
            if haskey(stream, :stochastic_params)
                for (param_name, param_def) in stream.stochastic_params
                    param = parse_parameter_definition(param_def)
                    if param !== nothing
                        stochastic_params["$(stream.id).$(param_name)"] = param
                    end
                end
            end
        end
    end
    
    return stochastic_params
end

"""
    parse_parameter_definition(param_def) -> Union{StochasticParameter, Nothing}

Parse a single parameter definition into appropriate StochasticParameter type.
"""
function parse_parameter_definition(param_def)::Union{StochasticParameter, Nothing}
    # Handle different parameter formats from IR
    
    if isa(param_def, Number)
        # Fixed numeric value
        return FixedParameter(param_def)
    
    elseif isa(param_def, String)
        # Fixed string value
        return FixedParameter(param_def)
    
    elseif isa(param_def, Dict) || hasmethod(keys, (typeof(param_def),))
        # Convert to Dict if needed
        param_dict = convert_to_dict(param_def)
        
        if haskey(param_dict, "type")
            param_type = param_dict["type"]
            
            if param_type == "Normal"
                return DistributionParameter("Normal", Dict(
                    "mean" => get(param_dict, "mean", 0.0),
                    "std" => get(param_dict, "std", 1.0)
                ))
            
            elseif param_type == "Uniform"
                return DistributionParameter("Uniform", Dict(
                    "min" => get(param_dict, "min", 0.0),
                    "max" => get(param_dict, "max", 1.0)
                ))
            
            elseif param_type == "LogNormal"
                return DistributionParameter("LogNormal", Dict(
                    "meanlog" => get(param_dict, "meanlog", 0.0),
                    "stdlog" => get(param_dict, "stdlog", 1.0)
                ))
            
            elseif param_type == "RandomWalk"
                return RandomWalkParameter(
                    get(param_dict, "initial_value", 0.0),
                    get(param_dict, "drift", 0.0),
                    get(param_dict, "volatility", 0.01)
                )
            
            else
                @warn "Unknown parameter type: $param_type"
                return nothing
            end
        else
            # Dict without type field - treat as fixed
            return FixedParameter(param_dict)
        end
    
    else
        # Other types - treat as fixed
        return FixedParameter(param_def)
    end
end

"""
    validate_ir_data(ir_data::IRData) -> Bool

Validate loaded IR data for completeness and consistency.
"""
function validate_ir_data(ir_data::IRData)::Bool
    # Check required fields
    if isempty(ir_data.deal)
        @error "IR data missing deal definition"
        return false
    end
    
    if isempty(ir_data.calendar)
        @error "IR data missing calendar definition"
        return false
    end
    
    # Validate stochastic parameters
    for (name, param) in ir_data.stochastic_params
        if !validate_parameter(param)
            @error "Invalid stochastic parameter: $name"
            return false
        end
    end
    
    return true
end

"""
    convert_to_dict(obj) -> Dict{String, Any}

Convert JSON3 objects to Dict{String, Any} with String keys.
"""
function convert_to_dict(obj)::Dict{String, Any}
    if isa(obj, Dict)
        return obj
    elseif hasmethod(keys, (typeof(obj),)) && hasmethod(getindex, (typeof(obj), String))
        # Handle JSON3.Object
        result = Dict{String, Any}()
        for key in keys(obj)
            str_key = string(key)
            result[str_key] = obj[key]
        end
        return result
    else
        # Fallback - try to convert directly
        return Dict{String, Any}(string(k) => v for (k, v) in pairs(obj))
    end
end

"""
    validate_parameter(param::StochasticParameter) -> Bool

Validate a single stochastic parameter definition.
"""
function validate_parameter(param::StochasticParameter)::Bool
    if isa(param, DistributionParameter)
        # Validate distribution parameters
        if param.distribution_type == "Normal"
            return haskey(param.parameters, "mean") && haskey(param.parameters, "std") &&
                   param.parameters["std"] > 0
        elseif param.distribution_type == "Uniform"
            return haskey(param.parameters, "min") && haskey(param.parameters, "max") &&
                   param.parameters["min"] < param.parameters["max"]
        elseif param.distribution_type == "LogNormal"
            return haskey(param.parameters, "meanlog") && haskey(param.parameters, "stdlog") &&
                   param.parameters["stdlog"] > 0
        else
            return false
        end
    elseif isa(param, RandomWalkParameter)
        return param.volatility >= 0
    else
        return true  # FixedParameter is always valid
    end
end