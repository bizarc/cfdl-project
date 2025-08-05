# Temporal grid generation for cash flow allocation

using Dates

"""
    TimePeriod

Represents a single time period in the temporal grid.
"""
struct TimePeriod
    period_start::Date
    period_end::Date
    period_number::Int
    frequency::String
    business_days::Int
    calendar_days::Int
    year_fraction::Float64
end

"""
    TemporalGrid

Complete temporal structure for cash flow calculations.
"""
struct TemporalGrid
    periods::Vector{TimePeriod}
    frequency::String
    start_date::Date
    end_date::Date
    business_day_convention::String
    day_count_convention::String
    holiday_calendar::String
    total_periods::Int
    metadata::Dict{String, Any}
end

"""
    generate_temporal_grid(ir_data::IRData, sampled_variables::Dict{String, Any}) -> TemporalGrid

Generate temporal grid from IR data and deal configuration.
"""
function generate_temporal_grid(ir_data::IRData, sampled_variables::Dict{String, Any})::TemporalGrid
    # Extract calendar configuration
    calendar = ir_data.calendar
    frequency = get(calendar, "frequency", "monthly")
    business_day_convention = get(calendar, "businessDayConvention", "following")
    day_count_convention = get(calendar, "dayCount", "actual/365")
    holiday_calendar = get(calendar, "holidayCalendar", "US")
    
    # Extract deal dates
    deal = ir_data.deal
    start_date = parse_date(get(deal, "analysisStart", get(deal, "entryDate", "2024-01-01")))
    end_date = parse_date(get(deal, "exitDate", "2029-01-01"))
    
    # Generate periods based on frequency
    periods = generate_periods(start_date, end_date, frequency, business_day_convention, day_count_convention, holiday_calendar)
    
    # Create metadata
    metadata = Dict{String, Any}(
        "generated_at" => now(),
        "sampled_variables_count" => length(sampled_variables),
        "deal_id" => get(deal, "id", "unknown")
    )
    
    return TemporalGrid(
        periods,
        frequency,
        start_date,
        end_date,
        business_day_convention,
        day_count_convention,
        holiday_calendar,
        length(periods),
        metadata
    )
end

"""
    generate_periods(start_date::Date, end_date::Date, frequency::String, 
                    business_day_convention::String, day_count_convention::String, 
                    holiday_calendar::String) -> Vector{TimePeriod}

Generate time periods based on frequency and conventions.
"""
function generate_periods(start_date::Date, end_date::Date, frequency::String,
                         business_day_convention::String, day_count_convention::String,
                         holiday_calendar::String)::Vector{TimePeriod}
    
    periods = TimePeriod[]
    current_date = start_date
    period_number = 1
    
    while current_date < end_date
        # Calculate period end based on frequency
        period_end = calculate_period_end(current_date, frequency)
        
        # Don't go past the final end date
        if period_end > end_date
            period_end = end_date
        end
        
        # Adjust for business day convention
        adjusted_start = adjust_for_business_day(current_date, business_day_convention, holiday_calendar)
        adjusted_end = adjust_for_business_day(period_end, business_day_convention, holiday_calendar)
        
        # Calculate period metrics
        business_days = count_business_days(adjusted_start, adjusted_end, holiday_calendar)
        calendar_days = Dates.value(adjusted_end - adjusted_start) + 1
        year_fraction = calculate_year_fraction(adjusted_start, adjusted_end, day_count_convention)
        
        # Create period
        period = TimePeriod(
            adjusted_start,
            adjusted_end,
            period_number,
            frequency,
            business_days,
            calendar_days,
            year_fraction
        )
        
        push!(periods, period)
        
        # Move to next period
        current_date = period_end + Day(1)
        period_number += 1
        
        # Safety check to prevent infinite loops
        if period_number > 10000
            @warn "Period generation exceeded 10,000 periods, stopping"
            break
        end
    end
    
    return periods
end

"""
    calculate_period_end(start_date::Date, frequency::String) -> Date

Calculate the end date for a period based on frequency.
"""
function calculate_period_end(start_date::Date, frequency::String)::Date
    frequency_lower = lowercase(frequency)
    
    if frequency_lower == "daily"
        return start_date
    elseif frequency_lower == "weekly"
        return start_date + Week(1) - Day(1)
    elseif frequency_lower == "monthly"
        return start_date + Month(1) - Day(1)
    elseif frequency_lower == "quarterly"
        return start_date + Month(3) - Day(1)
    elseif frequency_lower == "annual" || frequency_lower == "yearly"
        return start_date + Year(1) - Day(1)
    else
        @warn "Unknown frequency '$frequency', defaulting to monthly"
        return start_date + Month(1) - Day(1)
    end
end

"""
    adjust_for_business_day(date::Date, convention::String, calendar::String) -> Date

Adjust date according to business day convention.
"""
function adjust_for_business_day(date::Date, convention::String, calendar::String)::Date
    convention_lower = lowercase(convention)
    
    if convention_lower == "none" || convention_lower == "unadjusted"
        return date
    elseif convention_lower == "following"
        return adjust_following(date, calendar)
    elseif convention_lower == "preceding" || convention_lower == "previous"
        return adjust_preceding(date, calendar)
    elseif convention_lower == "modified_following" || convention_lower == "modifiedfollowing"
        return adjust_modified_following(date, calendar)
    else
        @warn "Unknown business day convention '$convention', using 'following'"
        return adjust_following(date, calendar)
    end
end

"""
    adjust_following(date::Date, calendar::String) -> Date

Adjust date to following business day.
"""
function adjust_following(date::Date, calendar::String)::Date
    while !is_business_day(date, calendar)
        date += Day(1)
    end
    return date
end

"""
    adjust_preceding(date::Date, calendar::String) -> Date

Adjust date to preceding business day.
"""
function adjust_preceding(date::Date, calendar::String)::Date
    while !is_business_day(date, calendar)
        date -= Day(1)
    end
    return date
end

"""
    adjust_modified_following(date::Date, calendar::String) -> Date

Adjust date using modified following convention.
"""
function adjust_modified_following(date::Date, calendar::String)::Date
    original_month = month(date)
    adjusted = adjust_following(date, calendar)
    
    # If adjustment moved to next month, use preceding instead
    if month(adjusted) != original_month
        return adjust_preceding(date, calendar)
    else
        return adjusted
    end
end

"""
    is_business_day(date::Date, calendar::String) -> Bool

Check if a date is a business day according to the specified calendar.
"""
function is_business_day(date::Date, calendar::String)::Bool
    # Weekend check (Saturday = 6, Sunday = 7)
    if dayofweek(date) in [6, 7]
        return false
    end
    
    # Holiday check based on calendar
    return !is_holiday(date, calendar)
end

"""
    is_holiday(date::Date, calendar::String) -> Bool

Check if a date is a holiday according to the specified calendar.
"""
function is_holiday(date::Date, calendar::String)::Bool
    calendar_lower = lowercase(calendar)
    
    if calendar_lower == "us" || calendar_lower == "united_states"
        return is_us_holiday(date)
    elseif calendar_lower == "uk" || calendar_lower == "united_kingdom"
        return is_uk_holiday(date)
    elseif calendar_lower == "none" || calendar_lower == "target"
        return false  # No holidays
    else
        @warn "Unknown holiday calendar '$calendar', assuming no holidays"
        return false
    end
end

"""
    is_us_holiday(date::Date) -> Bool

Check if date is a US federal holiday.
"""
function is_us_holiday(date::Date)::Bool
    year = Dates.year(date)
    month = Dates.month(date)
    day = Dates.day(date)
    
    # New Year's Day
    if month == 1 && day == 1
        return true
    end
    
    # Independence Day
    if month == 7 && day == 4
        return true
    end
    
    # Christmas Day
    if month == 12 && day == 25
        return true
    end
    
    # Martin Luther King Jr. Day (3rd Monday in January)
    if month == 1 && dayofweek(date) == 1 && day >= 15 && day <= 21
        return true
    end
    
    # Presidents Day (3rd Monday in February)
    if month == 2 && dayofweek(date) == 1 && day >= 15 && day <= 21
        return true
    end
    
    # Memorial Day (last Monday in May)
    if month == 5 && dayofweek(date) == 1 && day >= 25
        return true
    end
    
    # Labor Day (1st Monday in September)
    if month == 9 && dayofweek(date) == 1 && day <= 7
        return true
    end
    
    # Columbus Day (2nd Monday in October)
    if month == 10 && dayofweek(date) == 1 && day >= 8 && day <= 14
        return true
    end
    
    # Thanksgiving (4th Thursday in November)
    if month == 11 && dayofweek(date) == 4 && day >= 22 && day <= 28
        return true
    end
    
    return false
end

"""
    is_uk_holiday(date::Date) -> Bool

Check if date is a UK bank holiday (simplified implementation).
"""
function is_uk_holiday(date::Date)::Bool
    month = Dates.month(date)
    day = Dates.day(date)
    
    # New Year's Day
    if month == 1 && day == 1
        return true
    end
    
    # Christmas Day
    if month == 12 && day == 25
        return true
    end
    
    # Boxing Day
    if month == 12 && day == 26
        return true
    end
    
    # TODO: Add more UK holidays (Easter, Spring Bank Holiday, etc.)
    # These require more complex calculations
    
    return false
end

"""
    count_business_days(start_date::Date, end_date::Date, calendar::String) -> Int

Count business days between two dates.
"""
function count_business_days(start_date::Date, end_date::Date, calendar::String)::Int
    count = 0
    current = start_date
    
    while current <= end_date
        if is_business_day(current, calendar)
            count += 1
        end
        current += Day(1)
    end
    
    return count
end

"""
    calculate_year_fraction(start_date::Date, end_date::Date, convention::String) -> Float64

Calculate year fraction between two dates using specified day count convention.
"""
function calculate_year_fraction(start_date::Date, end_date::Date, convention::String)::Float64
    convention_lower = lowercase(replace(convention, "/" => "_"))
    
    if convention_lower == "actual_365" || convention_lower == "act_365"
        days = Dates.value(end_date - start_date) + 1
        return days / 365.0
    elseif convention_lower == "actual_360" || convention_lower == "act_360"
        days = Dates.value(end_date - start_date) + 1
        return days / 360.0
    elseif convention_lower == "30_360" || convention_lower == "30e_360"
        return calculate_30_360_fraction(start_date, end_date)
    elseif convention_lower == "actual_actual" || convention_lower == "act_act"
        return calculate_actual_actual_fraction(start_date, end_date)
    else
        @warn "Unknown day count convention '$convention', using Actual/365"
        days = Dates.value(end_date - start_date) + 1
        return days / 365.0
    end
end

"""
    calculate_30_360_fraction(start_date::Date, end_date::Date) -> Float64

Calculate year fraction using 30/360 day count convention.
"""
function calculate_30_360_fraction(start_date::Date, end_date::Date)::Float64
    y1, m1, d1 = year(start_date), month(start_date), day(start_date)
    y2, m2, d2 = year(end_date), month(end_date), day(end_date)
    
    # Adjust days according to 30/360 rules
    if d1 == 31
        d1 = 30
    end
    if d2 == 31 && d1 >= 30
        d2 = 30
    end
    
    days = 360 * (y2 - y1) + 30 * (m2 - m1) + (d2 - d1)
    return days / 360.0
end

"""
    calculate_actual_actual_fraction(start_date::Date, end_date::Date) -> Float64

Calculate year fraction using Actual/Actual day count convention.
"""
function calculate_actual_actual_fraction(start_date::Date, end_date::Date)::Float64
    start_year = year(start_date)
    end_year = year(end_date)
    
    if start_year == end_year
        # Same year
        year_days = isleapyear(start_year) ? 366 : 365
        days = Dates.value(end_date - start_date) + 1
        return days / year_days
    else
        # Multiple years - more complex calculation
        total_fraction = 0.0
        current_date = start_date
        
        while year(current_date) <= end_year
            year_start = Date(year(current_date), 1, 1)
            year_end = Date(year(current_date), 12, 31)
            
            period_start = max(current_date, year_start)
            period_end = min(end_date, year_end)
            
            if period_start <= period_end
                year_days = isleapyear(year(current_date)) ? 366 : 365
                days = Dates.value(period_end - period_start) + 1
                total_fraction += days / year_days
            end
            
            current_date = Date(year(current_date) + 1, 1, 1)
        end
        
        return total_fraction
    end
end

"""
    parse_date(date_str::String) -> Date

Parse date string in various formats.
"""
function parse_date(date_str::String)::Date
    # Try different date formats
    formats = [
        "yyyy-mm-dd",
        "mm/dd/yyyy", 
        "dd/mm/yyyy",
        "yyyy/mm/dd"
    ]
    
    for format in formats
        try
            return Date(date_str, format)
        catch
            continue
        end
    end
    
    # If all formats fail, try default parsing
    try
        return Date(date_str)
    catch e
        throw(ArgumentError("Unable to parse date string '$date_str': $e"))
    end
end

"""
    get_period_by_date(grid::TemporalGrid, date::Date) -> Union{TimePeriod, Nothing}

Find the period that contains a specific date.
"""
function get_period_by_date(grid::TemporalGrid, date::Date)::Union{TimePeriod, Nothing}
    for period in grid.periods
        if period.period_start <= date <= period.period_end
            return period
        end
    end
    return nothing
end

"""
    get_periods_in_range(grid::TemporalGrid, start_date::Date, end_date::Date) -> Vector{TimePeriod}

Get all periods that overlap with a date range.
"""
function get_periods_in_range(grid::TemporalGrid, start_date::Date, end_date::Date)::Vector{TimePeriod}
    overlapping_periods = TimePeriod[]
    
    for period in grid.periods
        # Check if periods overlap
        if period.period_start <= end_date && period.period_end >= start_date
            push!(overlapping_periods, period)
        end
    end
    
    return overlapping_periods
end

"""
    summarize_grid(grid::TemporalGrid) -> Dict{String, Any}

Create a summary of the temporal grid for debugging/logging.
"""
function summarize_grid(grid::TemporalGrid)::Dict{String, Any}
    total_days = sum(p.calendar_days for p in grid.periods)
    total_business_days = sum(p.business_days for p in grid.periods)
    total_year_fraction = sum(p.year_fraction for p in grid.periods)
    
    return Dict{String, Any}(
        "frequency" => grid.frequency,
        "total_periods" => grid.total_periods,
        "start_date" => string(grid.start_date),
        "end_date" => string(grid.end_date),
        "total_calendar_days" => total_days,
        "total_business_days" => total_business_days,
        "total_year_fraction" => round(total_year_fraction, digits=4),
        "business_day_convention" => grid.business_day_convention,
        "day_count_convention" => grid.day_count_convention,
        "holiday_calendar" => grid.holiday_calendar
    )
end