# Main test runner for CFDL Engine

using Test

# Run all test files
@testset "CFDL Engine Tests" begin
    include("test_monte_carlo.jl")
    include("test_temporal_grid.jl")
    include("test_stream_allocator_fixed.jl")
    
    # Test modular cash flow pipeline
    include("test_cash_flow_assembly.jl")
    include("test_operating_statement.jl")
    include("test_financing_adjustments.jl")
    include("test_tax_processing.jl")
    include("test_available_cash.jl")
    include("test_statement_views.jl")
    
    # Test main aggregator (uses modular pipeline)
    include("test_cash_flow_aggregator.jl")
end