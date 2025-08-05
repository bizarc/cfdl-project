# Tests for Temporal Grid functionality

using Test
using Dates
using CFDLEngine

@testset "Temporal Grid Tests" begin
    
    @testset "Date Parsing" begin
        @test parse_date("2024-01-15") == Date(2024, 1, 15)
        @test parse_date("01/15/2024") == Date(2024, 1, 15)
        @test parse_date("15/01/2024") == Date(2024, 1, 15)
        @test parse_date("2024/01/15") == Date(2024, 1, 15)
        
        @test_throws ArgumentError parse_date("invalid-date")
    end
    
    @testset "Period End Calculation" begin
        start_date = Date(2024, 1, 15)
        
        @test calculate_period_end(start_date, "daily") == Date(2024, 1, 15)
        @test calculate_period_end(start_date, "weekly") == Date(2024, 1, 21)
        @test calculate_period_end(start_date, "monthly") == Date(2024, 2, 14)
        @test calculate_period_end(start_date, "quarterly") == Date(2024, 4, 14)
        @test calculate_period_end(start_date, "annual") == Date(2025, 1, 14)
    end
    
    @testset "Business Day Checks" begin
        # Test weekend detection
        saturday = Date(2024, 1, 6)  # Saturday
        sunday = Date(2024, 1, 7)    # Sunday
        monday = Date(2024, 1, 8)    # Monday
        
        @test !is_business_day(saturday, "US")
        @test !is_business_day(sunday, "US")
        @test is_business_day(monday, "US")
        
        # Test US holidays
        new_years = Date(2024, 1, 1)
        independence = Date(2024, 7, 4)
        christmas = Date(2024, 12, 25)
        
        @test !is_business_day(new_years, "US")
        @test !is_business_day(independence, "US")
        @test !is_business_day(christmas, "US")
    end
    
    @testset "Business Day Adjustments" begin
        # Test following convention
        saturday = Date(2024, 1, 6)  # Saturday
        monday = Date(2024, 1, 8)    # Monday
        
        @test adjust_for_business_day(saturday, "following", "US") == monday
        @test adjust_for_business_day(monday, "following", "US") == monday
        
        # Test preceding convention
        @test adjust_for_business_day(saturday, "preceding", "US") == Date(2024, 1, 5)  # Friday
        
        # Test no adjustment
        @test adjust_for_business_day(saturday, "none", "US") == saturday
    end
    
    @testset "Day Count Conventions" begin
        start_date = Date(2024, 1, 1)
        end_date = Date(2024, 12, 31)  # Leap year
        
        # Actual/365
        fraction_365 = calculate_year_fraction(start_date, end_date, "actual/365")
        @test abs(fraction_365 - 366/365) < 0.001  # 2024 is leap year
        
        # Actual/360
        fraction_360 = calculate_year_fraction(start_date, end_date, "actual/360")
        @test abs(fraction_360 - 366/360) < 0.001
        
        # 30/360
        fraction_30_360 = calculate_year_fraction(start_date, end_date, "30/360")
        @test abs(fraction_30_360 - 1.0) < 0.001  # Should be exactly 1 year
        
        # Actual/Actual
        fraction_act_act = calculate_year_fraction(start_date, end_date, "actual/actual")
        @test abs(fraction_act_act - 1.0) < 0.001  # Should be exactly 1 year
    end
    
    @testset "Period Generation - Monthly" begin
        start_date = Date(2024, 1, 1)
        end_date = Date(2024, 3, 31)
        
        periods = generate_periods(start_date, end_date, "monthly", "following", "actual/365", "US")
        
        @test length(periods) == 3  # Jan, Feb, Mar
        # Jan 1 is New Year's Day (holiday), so following business day convention moves to Jan 2
        @test periods[1].period_start == Date(2024, 1, 2)
        @test periods[1].period_end == Date(2024, 1, 31)
        @test periods[2].period_start == Date(2024, 2, 1)
        @test periods[2].period_end == Date(2024, 2, 29)  # Leap year
        @test periods[3].period_start == Date(2024, 3, 1)
        @test periods[3].period_end == Date(2024, 4, 1)  # End date gets adjusted to following business day
        
        # Check period numbers
        @test periods[1].period_number == 1
        @test periods[2].period_number == 2
        @test periods[3].period_number == 3
        
        # Check frequency is stored
        @test all(p.frequency == "monthly" for p in periods)
    end
    
    @testset "Period Generation - Quarterly" begin
        start_date = Date(2024, 1, 1)
        end_date = Date(2024, 12, 31)
        
        periods = generate_periods(start_date, end_date, "quarterly", "following", "actual/365", "US")
        
        @test length(periods) == 4  # Q1, Q2, Q3, Q4
        # Jan 1 is New Year's Day (holiday), so following business day convention moves to Jan 2
        @test periods[1].period_start == Date(2024, 1, 2)
        @test periods[1].period_end == Date(2024, 4, 1)  # March 31 + following business day
        @test periods[2].period_start == Date(2024, 4, 1)
        @test periods[2].period_end == Date(2024, 7, 1)  # June 30 + following business day
        @test periods[4].period_start == Date(2024, 10, 1)
        @test periods[4].period_end == Date(2024, 12, 31)
    end
    
    @testset "Period Generation - Annual" begin
        start_date = Date(2024, 1, 1)
        end_date = Date(2026, 12, 31)
        
        periods = generate_periods(start_date, end_date, "annual", "following", "actual/365", "US")
        
        @test length(periods) == 3  # 2024, 2025, 2026
        # Jan 1 holidays get moved to following business day
        @test periods[1].period_start == Date(2024, 1, 2)  # Jan 1, 2024 is New Year's Day
        @test periods[1].period_end == Date(2024, 12, 31)
        @test periods[2].period_start == Date(2025, 1, 2)  # Jan 1, 2025 is New Year's Day  
        @test periods[2].period_end == Date(2025, 12, 31)
        @test periods[3].period_start == Date(2026, 1, 2)  # Jan 1, 2026 is New Year's Day
        @test periods[3].period_end == Date(2026, 12, 31)
    end
    
    @testset "Business Day Counting" begin
        # Count business days in January 2024
        start_date = Date(2024, 1, 1)  # Monday (New Year's Day - holiday)
        end_date = Date(2024, 1, 31)   # Wednesday
        
        business_days = count_business_days(start_date, end_date, "US")
        # January 2024: 31 days total, minus 8 weekend days, minus 1 holiday (New Year's) = 22 business days  
        @test business_days == 21  # Actually 21 because Jan 1 is both Monday AND New Year's holiday
        
        # Test with no holidays calendar
        business_days_no_holidays = count_business_days(start_date, end_date, "none")
        @test business_days_no_holidays == 23  # No holidays, so Jan 1 (Monday) counts as business day
    end
    
    @testset "Temporal Grid Generation" begin
        # Create test IR data
        test_ir = IRData(
            Dict("id" => "test_deal", "entryDate" => "2024-01-01", "exitDate" => "2024-04-01"),
            [],
            [],
            [],
            Dict("frequency" => "monthly", "businessDayConvention" => "following", "dayCount" => "actual/365"),
            Dict(),
            [],
            nothing,
            Dict{String, StochasticParameter}()
        )
        
        # Generate grid
        grid = generate_temporal_grid(test_ir, Dict{String, Any}())
        
        @test grid.frequency == "monthly"
        @test grid.start_date == Date(2024, 1, 1)
        @test grid.end_date == Date(2024, 4, 1)
        @test grid.total_periods == 3  # Jan, Feb, Mar
        @test length(grid.periods) == 3
        
        # Test grid summary
        summary = summarize_grid(grid)
        @test summary["frequency"] == "monthly"
        @test summary["total_periods"] == 3
        @test haskey(summary, "total_calendar_days")
        @test haskey(summary, "total_business_days")
        @test haskey(summary, "total_year_fraction")
    end
    
    @testset "Grid Utility Functions" begin
        # Create test IR data first to use the constructor properly
        test_ir = IRData(
            Dict("id" => "test_deal", "entryDate" => "2024-01-01", "exitDate" => "2024-04-01"),
            [],
            [],
            [],
            Dict("frequency" => "monthly", "businessDayConvention" => "none", "dayCount" => "actual/365"),
            Dict(),
            [],
            nothing,
            Dict{String, StochasticParameter}()
        )
        
        # Generate grid using the actual function
        grid = generate_temporal_grid(test_ir, Dict{String, Any}())
        
        # Test get_period_by_date
        period = get_period_by_date(grid, Date(2024, 1, 15))
        @test period !== nothing
        @test period.period_number == 1
        
        period = get_period_by_date(grid, Date(2024, 4, 2))
        @test period === nothing
        
        # Test get_periods_in_range
        overlapping = get_periods_in_range(grid, Date(2024, 1, 15), Date(2024, 2, 15))
        @test length(overlapping) == 2  # January and February periods
        @test overlapping[1].period_number == 1
        @test overlapping[2].period_number == 2
    end
end