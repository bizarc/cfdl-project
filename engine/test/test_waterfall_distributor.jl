# Tests for waterfall distribution functionality
# Covers major waterfall patterns across asset classes: CRE, PE, Infrastructure, Energy, etc.

using Test
using Dates
using CFDLEngine

# Test data for various asset class waterfall patterns
include("../src/waterfall_distributor.jl")

@testset "Waterfall Distributor Tests" begin
    
    @testset "Core Data Structures" begin
        @testset "RecipientDistribution Construction" begin
            recipient = RecipientDistribution(
                "equity_investor_1", 
                "party_uri", 
                100000.0, 
                0.6, 
                Dict{String, Any}("source" => "explicit")
            )
            
            @test recipient.recipient_id == "equity_investor_1"
            @test recipient.recipient_type == "party_uri"
            @test recipient.amount == 100000.0
            @test recipient.percentage_of_tier == 0.6
            @test recipient.metadata["source"] == "explicit"
        end
        
        @testset "TierDistribution Construction" begin
            recipients = [RecipientDistribution("test", "party_uri", 50000.0, 1.0, Dict{String, Any}())]
            tier = TierDistribution(
                "tier_1", "First tier", "condition", "remainingCapital > 0", 
                true, 100000.0, 50000.0, recipients, Dict{String, Any}()
            )
            
            @test tier.tier_id == "tier_1"
            @test tier.condition_met == true
            @test tier.cash_allocated == 50000.0
            @test length(tier.recipient_distributions) == 1
        end
        
        @testset "WaterfallDistribution Construction" begin
            tiers = [TierDistribution("t1", "", "always", true, true, 0.0, 0.0, [], Dict{String, Any}())]
            distribution = WaterfallDistribution(
                "entity_1", "waterfall_1", Date(2024, 1, 1), Date(2024, 1, 31),
                100000.0, tiers, 50000.0, 50000.0, Dict{String, Any}(), Dict{String, Any}()
            )
            
            @test distribution.entity_id == "entity_1"
            @test distribution.total_available == 100000.0
            @test distribution.total_distributed == 50000.0
            @test distribution.remaining_cash == 50000.0
        end
    end
    
    @testset "Expression Evaluation Engine" begin
        @testset "Simple Comparisons" begin
            context = Dict{String, Any}("remainingCapital" => 100000.0, "currentIRR" => 0.12)
            
            @test evaluate_expression("remainingCapital > 0", context) == true
            @test evaluate_expression("remainingCapital > 200000", context) == false
            @test evaluate_expression("currentIRR >= 0.10", context) == true
            @test evaluate_expression("currentIRR < 0.10", context) == false
            @test evaluate_expression("remainingCapital == 100000.0", context) == true
            @test evaluate_expression("remainingCapital <= 100000.0", context) == true
        end
        
        @testset "Boolean Values" begin
            context = Dict{String, Any}("flag" => true)
            @test evaluate_expression("true", context) == true
            @test evaluate_expression("false", context) == false
        end
        
        @testset "Numeric Values" begin
            context = Dict{String, Any}()
            @test evaluate_expression("1", context) == true  # Non-zero is true
            @test evaluate_expression("0", context) == false # Zero is false
        end
        
        @testset "Variable Substitution" begin
            context = Dict{String, Any}("totalDistributed" => 50000.0, "target" => 75000.0)
            @test evaluate_expression("totalDistributed < target", context) == true
        end
        
        @testset "Error Handling" begin
            context = Dict{String, Any}()
            @test evaluate_expression("invalid_expression", context) == false
            @test evaluate_expression("", context) == false
        end
    end
    
    @testset "Tier Condition Evaluation" begin
        @testset "Condition Field" begin
            tier = Dict{String, Any}("condition" => "remainingCapital > 50000")
            context = Dict{String, Any}("remainingCapital" => 100000.0)
            @test evaluate_tier_condition(tier, context) == true
            
            context = Dict{String, Any}("remainingCapital" => 25000.0)
            @test evaluate_tier_condition(tier, context) == false
        end
        
        @testset "Until Field" begin
            tier = Dict{String, Any}("until" => "currentIRR >= 0.08")
            context = Dict{String, Any}("currentIRR" => 0.10)
            @test evaluate_tier_condition(tier, context) == true
            
            context = Dict{String, Any}("currentIRR" => 0.06)
            @test evaluate_tier_condition(tier, context) == false
        end
        
        @testset "PrefRate Field" begin
            tier = Dict{String, Any}("prefRate" => 0.08)
            context = Dict{String, Any}("current_irr" => 0.10)
            @test evaluate_tier_condition(tier, context) == true
            
            context = Dict{String, Any}("current_irr" => 0.06)
            @test evaluate_tier_condition(tier, context) == false
        end
        
        @testset "No Condition (Always Active)" begin
            tier = Dict{String, Any}("id" => "always_active")
            context = Dict{String, Any}()
            @test evaluate_tier_condition(tier, context) == true
        end
    end
    
    @testset "Recipient Resolution" begin
        @testset "Explicit Recipients" begin
            distribute_config = [
                Dict{String, Any}("recipient" => "equity_investor", "percentage" => 0.7),
                Dict{String, Any}("recipient" => "debt_investor", "percentage" => 0.3)
            ]
            capital_stack = Dict{String, Any}("participants" => [])
            
            recipients = resolve_recipients(distribute_config, capital_stack)
            @test length(recipients) == 2
            @test recipients[1]["recipient"] == "equity_investor"
            @test recipients[1]["percentage"] == 0.7
            @test recipients[1]["source"] == "explicit"
            @test recipients[2]["percentage"] == 0.3
        end
        
        @testset "Capital Stack Inheritance" begin
            distribute_config = [
                Dict{String, Any}("fromCapitalStack" => true, "layerName" => "equity")
            ]
            capital_stack = Dict{String, Any}(
                "participants" => [
                    Dict{String, Any}("partyId" => "investor_a", "amount" => 600000.0),
                    Dict{String, Any}("partyId" => "investor_b", "amount" => 400000.0)
                ]
            )
            
            recipients = resolve_recipients(distribute_config, capital_stack)
            @test length(recipients) == 2
            @test recipients[1]["recipient"] == "investor_a"
            @test recipients[1]["percentage"] == 0.6  # 600k / 1M
            @test recipients[1]["source"] == "capital_stack"
            @test recipients[2]["percentage"] == 0.4  # 400k / 1M
        end
        
        @testset "Mixed Explicit and Capital Stack" begin
            distribute_config = [
                Dict{String, Any}("recipient" => "management_fee", "percentage" => 0.1),
                Dict{String, Any}("fromCapitalStack" => true, "layerName" => "equity")
            ]
            capital_stack = Dict{String, Any}(
                "participants" => [
                    Dict{String, Any}("partyId" => "investor_a", "amount" => 500000.0),
                    Dict{String, Any}("partyId" => "investor_b", "amount" => 500000.0)
                ]
            )
            
            recipients = resolve_recipients(distribute_config, capital_stack)
            @test length(recipients) == 3
            @test recipients[1]["recipient"] == "management_fee"
            @test recipients[1]["percentage"] == 0.1
            @test recipients[2]["percentage"] == 0.5  # Pro-rata from capital stack
            @test recipients[3]["percentage"] == 0.5
        end
    end
    
    @testset "Cash Distribution to Recipients" begin
        @testset "Simple Distribution" begin
            recipients = [
                Dict{String, Any}("recipient" => "investor_a", "percentage" => 0.6),
                Dict{String, Any}("recipient" => "investor_b", "percentage" => 0.4)
            ]
            
            distributions = distribute_to_recipients(100000.0, recipients)
            @test length(distributions) == 2
            @test distributions[1].amount == 60000.0
            @test distributions[2].amount == 40000.0
            @test distributions[1].percentage_of_tier == 0.6
            @test distributions[2].percentage_of_tier == 0.4
        end
        
        @testset "Percentage Normalization" begin
            # Test with percentages that don't sum to 1.0
            recipients = [
                Dict{String, Any}("recipient" => "investor_a", "percentage" => 0.3),
                Dict{String, Any}("recipient" => "investor_b", "percentage" => 0.2)  # Sum = 0.5
            ]
            
            distributions = distribute_to_recipients(100000.0, recipients)
            @test length(distributions) == 2
            # After normalization: 0.3/0.5 = 0.6, 0.2/0.5 = 0.4
            @test distributions[1].amount ≈ 60000.0
            @test distributions[2].amount ≈ 40000.0
        end
        
        @testset "Recipient Type Detection" begin
            recipients = [
                Dict{String, Any}("recipient" => "https://example.com/party/123", "percentage" => 0.5),
                Dict{String, Any}("recipient" => "equity_group", "percentage" => 0.3),
                Dict{String, Any}("recipient" => 0, "percentage" => 0.2)  # Index
            ]
            
            distributions = distribute_to_recipients(100000.0, recipients)
            @test distributions[1].recipient_type == "party_uri"
            @test distributions[2].recipient_type == "named_group"
            @test distributions[3].recipient_type == "participant_index"
        end
    end
    
    @testset "Commercial Real Estate Waterfall Patterns" begin
        @testset "Simple Pref + Promote Structure" begin
            # 8% preferred return, then 80/20 split
            available_cash = [AvailableCashCalculation(
                "cre_deal", Date(2024, 1, 1), Date(2024, 1, 31),
                120000.0, 0.0, 0.0, 0.0, 120000.0, Dict{String, Any}()
            )]
            
            waterfall_config = Dict{String, Any}(
                "id" => "cre_pref_promote",
                "tiers" => [
                    Dict{String, Any}(
                        "id" => "preferred_return",
                        "description" => "8% preferred return to LP",
                        "prefRate" => 0.08,
                        "distribute" => [
                            Dict{String, Any}("recipient" => "limited_partner", "percentage" => 1.0)
                        ]
                    ),
                    Dict{String, Any}(
                        "id" => "promote_split",
                        "description" => "Remaining cash 80/20 split",
                        "condition" => "remainingCapital > 0",
                        "distribute" => [
                            Dict{String, Any}("recipient" => "limited_partner", "percentage" => 0.8),
                            Dict{String, Any}("recipient" => "general_partner", "percentage" => 0.2)
                        ]
                    )
                ]
            )
            
            capital_stack = Dict{String, Any}("participants" => [])
            ir_data = IRData(
                Dict{String, Any}(), [], [], [], Dict{String, Any}(), Dict{String, Any}(), [], waterfall_config, Dict{String, Any}()
            )
            
            distributions = execute_waterfall_distribution(available_cash, ir_data)
            @test length(distributions) == 1
            
            dist = distributions[1]
            @test length(dist.tier_distributions) == 2
            
            # All cash should be distributed (assuming IRR condition is met)
            @test dist.total_distributed > 0
            @test dist.remaining_cash >= 0
        end
        
        @testset "Multiple Hurdle Rates" begin
            # 6% pref, then 70/30 to 12% IRR, then 50/50
            available_cash = [AvailableCashCalculation(
                "cre_multi_hurdle", Date(2024, 1, 1), Date(2024, 1, 31),
                200000.0, 0.0, 0.0, 0.0, 200000.0, Dict{String, Any}()
            )]
            
            waterfall_config = Dict{String, Any}(
                "id" => "cre_multi_hurdle",
                "tiers" => [
                    Dict{String, Any}(
                        "id" => "tier_1_pref",
                        "prefRate" => 0.06,
                        "distribute" => [Dict{String, Any}("recipient" => "lp", "percentage" => 1.0)]
                    ),
                    Dict{String, Any}(
                        "id" => "tier_2_promote",
                        "until" => "currentIRR < 0.12",
                        "distribute" => [
                            Dict{String, Any}("recipient" => "lp", "percentage" => 0.7),
                            Dict{String, Any}("recipient" => "gp", "percentage" => 0.3)
                        ]
                    ),
                    Dict{String, Any}(
                        "id" => "tier_3_promote",
                        "condition" => "remainingCapital > 0",
                        "distribute" => [
                            Dict{String, Any}("recipient" => "lp", "percentage" => 0.5),
                            Dict{String, Any}("recipient" => "gp", "percentage" => 0.5)
                        ]
                    )
                ]
            )
            
            ir_data = IRData(
                Dict{String, Any}(), [], [], [], Dict{String, Any}(), Dict{String, Any}(), [], waterfall_config, Dict{String, Any}()
            )
            
            distributions = execute_waterfall_distribution(available_cash, ir_data)
            @test length(distributions) == 1
            @test length(distributions[1].tier_distributions) == 3
        end
    end
    
    @testset "Private Equity Waterfall Patterns" begin
        @testset "European Style Waterfall" begin
            # Real PE waterfall: Return capital, then 8% pref, then GP catch-up, then promote
            available_cash = [AvailableCashCalculation(
                "pe_deal", Date(2024, 1, 1), Date(2024, 1, 31),
                2500000.0, 0.0, 0.0, 0.0, 2500000.0, Dict{String, Any}()
            )]
            
            waterfall_config = Dict{String, Any}(
                "id" => "pe_european_style",
                "tiers" => [
                    Dict{String, Any}(
                        "id" => "return_of_capital",
                        "description" => "Return of invested capital to LPs (100% to LPs until 1M returned)",
                        "condition" => "totalDistributed < 1000000",  # Return 1M invested capital
                        "distribute" => [Dict{String, Any}("recipient" => "limited_partners", "percentage" => 1.0)]
                    ),
                    Dict{String, Any}(
                        "id" => "preferred_return", 
                        "description" => "8% cumulative preferred return to LPs (100% to LPs until 1.4M total)",
                        "condition" => "totalDistributed < 1400000",  # 1M + 400K (8% on 1M for 5 years)
                        "distribute" => [Dict{String, Any}("recipient" => "limited_partners", "percentage" => 1.0)]
                    ),
                    Dict{String, Any}(
                        "id" => "gp_catch_up",
                        "description" => "GP catch-up (100% to GP until GP has 20% of total distributions)",
                        "condition" => "totalDistributed < 1750000",  # Until GP catches up to 20% (350K of 1.75M)
                        "distribute" => [Dict{String, Any}("recipient" => "general_partner", "percentage" => 1.0)]
                    ),
                    Dict{String, Any}(
                        "id" => "carried_interest",
                        "description" => "Carried interest split (80% LP, 20% GP on remaining)",
                        "condition" => "remainingCapital > 0",
                        "distribute" => [
                            Dict{String, Any}("recipient" => "limited_partners", "percentage" => 0.8),
                            Dict{String, Any}("recipient" => "general_partner", "percentage" => 0.2)
                        ]
                    )
                ]
            )
            
            ir_data = IRData(
                Dict{String, Any}(), [], [], [], Dict{String, Any}(), Dict{String, Any}(), [], waterfall_config, Dict{String, Any}()
            )
            
            distributions = execute_waterfall_distribution(available_cash, ir_data)
            @test length(distributions) == 1
            @test length(distributions[1].tier_distributions) == 4
            
            # Verify realistic PE waterfall behavior
            dist = distributions[1]
            @test dist.total_distributed == 2500000.0  # All cash should be distributed
            
            # Tier 1: LPs get first $1M (return of capital)
            tier1 = dist.tier_distributions[1]
            @test tier1.condition_met == true
            @test tier1.cash_allocated == 1000000.0
            
            # Tier 2: LPs get next $400K (preferred return) 
            tier2 = dist.tier_distributions[2]
            @test tier2.condition_met == true
            @test tier2.cash_allocated == 400000.0
            
            # Tier 3: GP gets next $350K (catch-up)
            tier3 = dist.tier_distributions[3]
            @test tier3.condition_met == true
            @test tier3.cash_allocated == 350000.0
            
            # Tier 4: Remaining $750K split 80/20
            tier4 = dist.tier_distributions[4]
            @test tier4.condition_met == true
            @test tier4.cash_allocated == 750000.0
        end
        
        @testset "American Style Waterfall (Deal-by-Deal)" begin
            # American style: No catch-up, immediate carry on each deal after hurdle
            available_cash = [AvailableCashCalculation(
                "pe_american", Date(2024, 1, 1), Date(2024, 1, 31),
                1800000.0, 0.0, 0.0, 0.0, 1800000.0, Dict{String, Any}()
            )]
            
            waterfall_config = Dict{String, Any}(
                "id" => "pe_american_style", 
                "tiers" => [
                    Dict{String, Any}(
                        "id" => "return_of_capital",
                        "description" => "Return of invested capital to LPs",
                        "condition" => "totalDistributed < 800000",  # Return 800K invested capital
                        "distribute" => [Dict{String, Any}("recipient" => "lp", "percentage" => 1.0)]
                    ),
                    Dict{String, Any}(
                        "id" => "preferred_return",
                        "description" => "8% preferred return to LPs",
                        "condition" => "totalDistributed < 1200000",  # 800K + 400K (8% hurdle)
                        "distribute" => [Dict{String, Any}("recipient" => "lp", "percentage" => 1.0)]
                    ),
                    Dict{String, Any}(
                        "id" => "carried_interest", 
                        "description" => "20% carried interest to GP, 80% to LPs",
                        "condition" => "remainingCapital > 0",
                        "distribute" => [
                            Dict{String, Any}("recipient" => "lp", "percentage" => 0.8),
                            Dict{String, Any}("recipient" => "gp", "percentage" => 0.2)
                        ]
                    )
                ]
            )
            
            ir_data = IRData(
                Dict{String, Any}(), [], [], [], Dict{String, Any}(), Dict{String, Any}(), [], waterfall_config, Dict{String, Any}()
            )
            
            distributions = execute_waterfall_distribution(available_cash, ir_data)
            @test length(distributions) == 1
            @test length(distributions[1].tier_distributions) == 3
            
            # Verify American style waterfall (no catch-up)
            dist = distributions[1]
            @test dist.total_distributed == 1800000.0
            
            # Tier 1: Return of capital ($800K to LPs)
            @test dist.tier_distributions[1].cash_allocated == 800000.0
            
            # Tier 2: Preferred return ($400K to LPs) 
            @test dist.tier_distributions[2].cash_allocated == 400000.0
            
            # Tier 3: Carried interest on remaining $600K (80/20 split)
            @test dist.tier_distributions[3].cash_allocated == 600000.0
        end
    end
    
    @testset "Infrastructure & Energy Waterfall Patterns" begin
        @testset "Project Finance Waterfall" begin
            # Infrastructure project: Operating expenses, debt service, then equity
            available_cash = [AvailableCashCalculation(
                "infra_project", Date(2024, 1, 1), Date(2024, 1, 31),
                3200000.0, 0.0, 0.0, 0.0, 3200000.0, Dict{String, Any}()
            )]
            
            waterfall_config = Dict{String, Any}(
                "id" => "project_finance",
                "tiers" => [

                    Dict{String, Any}(
                        "id" => "senior_debt_service",
                        "description" => "Senior debt principal and interest",
                        "condition" => "totalDistributed < 1200000",  # 1M for senior debt service
                        "distribute" => [Dict{String, Any}("recipient" => "senior_lender", "percentage" => 1.0)]
                    ),
                    Dict{String, Any}(
                        "id" => "mezzanine_debt_service", 
                        "description" => "Mezzanine debt service",
                        "condition" => "totalDistributed < 1700000",  # 500K for mezzanine debt
                        "distribute" => [Dict{String, Any}("recipient" => "mezzanine_lender", "percentage" => 1.0)]
                    ),
                    Dict{String, Any}(
                        "id" => "equity_distributions",
                        "description" => "Equity distributions to sponsors pro-rata",
                        "condition" => "remainingCapital > 0",
                        "distribute" => [
                            Dict{String, Any}("fromCapitalStack" => true, "layerName" => "equity")
                        ]
                    )
                ]
            )
            
            capital_stack = Dict{String, Any}(
                "participants" => [
                    Dict{String, Any}("partyId" => "sponsor_a", "amount" => 400000.0),  # 40% equity
                    Dict{String, Any}("partyId" => "sponsor_b", "amount" => 600000.0)   # 60% equity
                ]
            )
            
            ir_data = IRData(
                Dict{String, Any}("capitalStack" => capital_stack), [], [], [], 
                Dict{String, Any}(), Dict{String, Any}(), [], waterfall_config, Dict{String, Any}()
            )
            
            distributions = execute_waterfall_distribution(available_cash, ir_data)
            @test length(distributions) == 1
            @test length(distributions[1].tier_distributions) == 3
            
            dist = distributions[1]
            @test dist.total_distributed == 3200000.0
            
            # Verify project finance waterfall structure (no operating expenses - those are already deducted)
            @test dist.tier_distributions[1].cash_allocated == 1200000.0  # Senior debt (up to 1.2M total distributed)
            @test dist.tier_distributions[2].cash_allocated == 500000.0   # Mezz debt (1.2M to 1.7M total distributed)
            @test dist.tier_distributions[3].cash_allocated == 1500000.0  # Equity (remaining)
            
            # Check pro-rata equity distribution
            equity_tier = dist.tier_distributions[3]
            @test length(equity_tier.recipient_distributions) == 2
            @test equity_tier.recipient_distributions[1].percentage_of_tier == 0.4  # 40%
            @test equity_tier.recipient_distributions[2].percentage_of_tier == 0.6  # 60%
        end
        
        @testset "Renewable Energy Tax Equity Structure" begin
            # Complex structure with tax equity, developer, and investor tiers
            available_cash = [AvailableCashCalculation(
                "solar_project", Date(2024, 1, 1), Date(2024, 1, 31),
                200000.0, 0.0, 0.0, 0.0, 200000.0, Dict{String, Any}()  # Increased to $200K
            )]
            
            waterfall_config = Dict{String, Any}(
                "id" => "tax_equity_structure",
                "tiers" => [
                    Dict{String, Any}(
                        "id" => "operating_expenses",
                        "description" => "Operating and maintenance costs",
                        "condition" => "totalDistributed < 40000",  # Operating expenses limit
                        "distribute" => [Dict{String, Any}("recipient" => "operator", "percentage" => 1.0)]
                    ),
                    Dict{String, Any}(
                        "id" => "tax_equity_preferred",
                        "description" => "Tax equity investor preferred return",
                        "prefRate" => 0.06,
                        "distribute" => [Dict{String, Any}("recipient" => "tax_equity_investor", "percentage" => 1.0)]
                    ),
                    Dict{String, Any}(
                        "id" => "developer_fee",
                        "description" => "Developer management fee",
                        "condition" => "totalDistributed < 100000",  # Developer fee limit
                        "distribute" => [Dict{String, Any}("recipient" => "developer", "percentage" => 1.0)]
                    ),
                    Dict{String, Any}(
                        "id" => "remaining_cash_split",
                        "description" => "Remaining cash split",
                        "condition" => "remainingCapital > 0",
                        "distribute" => [
                            Dict{String, Any}("recipient" => "tax_equity_investor", "percentage" => 0.5),
                            Dict{String, Any}("recipient" => "developer", "percentage" => 0.5)
                        ]
                    )
                ]
            )
            
            ir_data = IRData(
                Dict{String, Any}(), [], [], [], Dict{String, Any}(), Dict{String, Any}(), [], waterfall_config, Dict{String, Any}()
            )
            
            distributions = execute_waterfall_distribution(available_cash, ir_data)
            @test length(distributions) == 1
            @test length(distributions[1].tier_distributions) == 4
        end
    end
    
    @testset "Alternative Asset Class Patterns" begin
        @testset "Hedge Fund Fee Structure" begin
            # Management fee + performance fee structure
            available_cash = [AvailableCashCalculation(
                "hedge_fund", Date(2024, 1, 1), Date(2024, 1, 31),
                1000000.0, 0.0, 0.0, 0.0, 1000000.0, Dict{String, Any}()
            )]
            
            waterfall_config = Dict{String, Any}(
                "id" => "hedge_fund_fees",
                "tiers" => [
                    Dict{String, Any}(
                        "id" => "management_fee",
                        "description" => "2% annual management fee",
                        "condition" => "totalDistributed < 50000",  # Management fee limit
                        "distribute" => [Dict{String, Any}("recipient" => "fund_manager", "percentage" => 1.0)]
                    ),
                    Dict{String, Any}(
                        "id" => "high_water_mark",
                        "description" => "Return to high water mark",
                        "condition" => "totalDistributed < 400000",  # High water mark limit
                        "distribute" => [Dict{String, Any}("recipient" => "investors", "percentage" => 1.0)]
                    ),
                    Dict{String, Any}(
                        "id" => "performance_fee",
                        "description" => "20% performance fee above high water mark",
                        "condition" => "remainingCapital > 0",
                        "distribute" => [
                            Dict{String, Any}("recipient" => "investors", "percentage" => 0.8),
                            Dict{String, Any}("recipient" => "fund_manager", "percentage" => 0.2)
                        ]
                    )
                ]
            )
            
            ir_data = IRData(
                Dict{String, Any}(), [], [], [], Dict{String, Any}(), Dict{String, Any}(), [], waterfall_config, Dict{String, Any}()
            )
            
            distributions = execute_waterfall_distribution(available_cash, ir_data)
            @test length(distributions) == 1
            @test length(distributions[1].tier_distributions) == 3
        end
        
        @testset "REIT Distribution Waterfall" begin
            # REIT-specific distribution requirements
            available_cash = [AvailableCashCalculation(
                "reit_portfolio", Date(2024, 1, 1), Date(2024, 1, 31),
                2000000.0, 0.0, 0.0, 0.0, 2000000.0, Dict{String, Any}()
            )]
            
            waterfall_config = Dict{String, Any}(
                "id" => "reit_distribution",
                "tiers" => [
                    Dict{String, Any}(
                        "id" => "required_distribution",
                        "description" => "90% of taxable income to shareholders",
                        "condition" => "remainingCapital > 0",
                        "distribute" => [
                            Dict{String, Any}("fromCapitalStack" => true, "layerName" => "shareholders")
                        ]
                    ),
                    Dict{String, Any}(
                        "id" => "discretionary_distribution",
                        "description" => "Discretionary additional distributions",
                        "condition" => "remainingCapital > 0",
                        "distribute" => [
                            Dict{String, Any}("fromCapitalStack" => true, "layerName" => "shareholders")
                        ]
                    )
                ]
            )
            
            capital_stack = Dict{String, Any}(
                "participants" => [
                    Dict{String, Any}("partyId" => "public_shareholders", "amount" => 15000000.0),
                    Dict{String, Any}("partyId" => "institutional_investors", "amount" => 5000000.0)
                ]
            )
            
            ir_data = IRData(
                Dict{String, Any}("capitalStack" => capital_stack), [], [], [], 
                Dict{String, Any}(), Dict{String, Any}(), [], waterfall_config, Dict{String, Any}()
            )
            
            distributions = execute_waterfall_distribution(available_cash, ir_data)
            @test length(distributions) == 1
            
            # Verify pro-rata distribution based on shareholding
            for tier in distributions[1].tier_distributions
                if !isempty(tier.recipient_distributions)
                    @test tier.recipient_distributions[1].percentage_of_tier == 0.75  # 15M / 20M
                    @test tier.recipient_distributions[2].percentage_of_tier == 0.25  # 5M / 20M
                end
            end
        end
    end
    
    @testset "Complex Multi-Tier Scenarios" begin
        @testset "Seven-Tier Waterfall" begin
            # Complex institutional-grade waterfall with multiple hurdles
            available_cash = [AvailableCashCalculation(
                "complex_deal", Date(2024, 1, 1), Date(2024, 1, 31),
                5000000.0, 0.0, 0.0, 0.0, 5000000.0, Dict{String, Any}()
            )]
            
            waterfall_config = Dict{String, Any}(
                "id" => "seven_tier_waterfall",
                "tiers" => [
                    Dict{String, Any}("id" => "tier_1", "condition" => "totalDistributed < 500000", 
                         "distribute" => [Dict{String, Any}("recipient" => "lp", "percentage" => 1.0)]),
                    Dict{String, Any}("id" => "tier_2", "condition" => "totalDistributed < 1000000",
                         "distribute" => [Dict{String, Any}("recipient" => "lp", "percentage" => 1.0)]),
                    Dict{String, Any}("id" => "tier_3", "condition" => "totalDistributed < 1500000",
                         "distribute" => [Dict{String, Any}("recipient" => "lp", "percentage" => 0.8), 
                                        Dict{String, Any}("recipient" => "gp", "percentage" => 0.2)]),
                    Dict{String, Any}("id" => "tier_4", "condition" => "totalDistributed < 2000000",
                         "distribute" => [Dict{String, Any}("recipient" => "lp", "percentage" => 0.7), 
                                        Dict{String, Any}("recipient" => "gp", "percentage" => 0.3)]),
                    Dict{String, Any}("id" => "tier_5", "condition" => "totalDistributed < 2500000",
                         "distribute" => [Dict{String, Any}("recipient" => "lp", "percentage" => 0.6), 
                                        Dict{String, Any}("recipient" => "gp", "percentage" => 0.4)]),
                    Dict{String, Any}("id" => "tier_6", "condition" => "totalDistributed < 3000000",
                         "distribute" => [Dict{String, Any}("recipient" => "lp", "percentage" => 0.5), 
                                        Dict{String, Any}("recipient" => "gp", "percentage" => 0.5)]),
                    Dict{String, Any}("id" => "tier_7", "condition" => "remainingCapital > 0",
                         "distribute" => [Dict{String, Any}("recipient" => "lp", "percentage" => 0.4), 
                                        Dict{String, Any}("recipient" => "gp", "percentage" => 0.6)])
                ]
            )
            
            ir_data = IRData(
                Dict{String, Any}(), [], [], [], Dict{String, Any}(), Dict{String, Any}(), [], waterfall_config, Dict{String, Any}()
            )
            
            distributions = execute_waterfall_distribution(available_cash, ir_data)
            @test length(distributions) == 1
            @test length(distributions[1].tier_distributions) == 7
            
            # Verify all tiers are processed
            dist = distributions[1]
            @test all(tier -> !isempty(tier.recipient_distributions), dist.tier_distributions)
        end
    end
    
    @testset "Edge Cases and Error Handling" begin
        @testset "Zero Available Cash" begin
            available_cash = [AvailableCashCalculation(
                "zero_cash", Date(2024, 1, 1), Date(2024, 1, 31),
                0.0, 0.0, 0.0, 0.0, 0.0, Dict{String, Any}()
            )]
            
            waterfall_config = Dict{String, Any}(
                "id" => "test_waterfall",
                "tiers" => [
                    Dict{String, Any}("id" => "tier_1", "condition" => "remainingCapital > 0",
                         "distribute" => [Dict{String, Any}("recipient" => "investor", "percentage" => 1.0)])
                ]
            )
            
            ir_data = IRData(
                Dict{String, Any}(), [], [], [], Dict{String, Any}(), Dict{String, Any}(), [], waterfall_config, Dict{String, Any}()
            )
            
            distributions = execute_waterfall_distribution(available_cash, ir_data)
            @test length(distributions) == 1
            @test distributions[1].total_distributed == 0.0
            @test distributions[1].remaining_cash == 0.0
        end
        
        @testset "Negative Available Cash" begin
            available_cash = [AvailableCashCalculation(
                "negative_cash", Date(2024, 1, 1), Date(2024, 1, 31),
                -50000.0, 0.0, 0.0, 0.0, -50000.0, Dict{String, Any}()
            )]
            
            waterfall_config = Dict{String, Any}(
                "id" => "test_waterfall",
                "tiers" => [
                    Dict{String, Any}("id" => "tier_1", "condition" => "remainingCapital > 0",
                         "distribute" => [Dict{String, Any}("recipient" => "investor", "percentage" => 1.0)])
                ]
            )
            
            ir_data = IRData(
                Dict{String, Any}(), [], [], [], Dict{String, Any}(), Dict{String, Any}(), [], waterfall_config, Dict{String, Any}()
            )
            
            distributions = execute_waterfall_distribution(available_cash, ir_data)
            @test length(distributions) == 1
            @test distributions[1].total_distributed == 0.0
        end
        
        @testset "No Waterfall Configuration" begin
            available_cash = [AvailableCashCalculation(
                "no_waterfall", Date(2024, 1, 1), Date(2024, 1, 31),
                100000.0, 0.0, 0.0, 0.0, 100000.0, Dict{String, Any}()
            )]
            
            ir_data = IRData(
                Dict{String, Any}(), [], [], [], Dict{String, Any}(), Dict{String, Any}(), [], nothing, Dict{String, Any}()
            )
            
            distributions = execute_waterfall_distribution(available_cash, ir_data)
            @test length(distributions) == 0
        end
        
        @testset "Empty Tiers" begin
            available_cash = [AvailableCashCalculation(
                "empty_tiers", Date(2024, 1, 1), Date(2024, 1, 31),
                100000.0, 0.0, 0.0, 0.0, 100000.0, Dict{String, Any}()
            )]
            
            waterfall_config = Dict{String, Any}("id" => "empty", "tiers" => [])
            ir_data = IRData(
                Dict{String, Any}(), [], [], [], Dict{String, Any}(), Dict{String, Any}(), [], waterfall_config, Dict{String, Any}()
            )
            
            distributions = execute_waterfall_distribution(available_cash, ir_data)
            @test length(distributions) == 1
            @test distributions[1].total_distributed == 0.0
            @test distributions[1].remaining_cash == 100000.0
        end
    end
    
    @testset "Validation Functions" begin
        @testset "Valid Distribution" begin
            tier = TierDistribution(
                "test_tier", "", "always", "true", true, 100000.0, 50000.0,
                [RecipientDistribution("investor", "party_uri", 50000.0, 1.0, Dict{String, Any}())],
                Dict{String, Any}()
            )
            
            distribution = WaterfallDistribution(
                "entity", "waterfall", Date(2024, 1, 1), Date(2024, 1, 31),
                100000.0, [tier], 50000.0, 50000.0, Dict{String, Any}(), Dict{String, Any}()
            )
            
            errors = validate_waterfall_distribution(distribution)
            @test length(errors) == 0
        end
        
        @testset "Cash Conservation Error" begin
            tier = TierDistribution(
                "test_tier", "", "always", "true", true, 100000.0, 60000.0,
                [RecipientDistribution("investor", "party_uri", 50000.0, 1.0, Dict{String, Any}())],
                Dict{String, Any}()
            )
            
            distribution = WaterfallDistribution(
                "entity", "waterfall", Date(2024, 1, 1), Date(2024, 1, 31),
                100000.0, [tier], 50000.0, 50000.0, Dict{String, Any}(), Dict{String, Any}()
            )
            
            errors = validate_waterfall_distribution(distribution)
            @test length(errors) > 0
            @test any(error -> occursin("does not match sum of tier allocations", error), errors)
        end
        
        @testset "Percentage Sum Error" begin
            recipients = [
                RecipientDistribution("investor_a", "party_uri", 30000.0, 0.6, Dict{String, Any}()),
                RecipientDistribution("investor_b", "party_uri", 20000.0, 0.5, Dict{String, Any}())  # Sum = 1.1
            ]
            
            tier = TierDistribution(
                "test_tier", "", "always", "true", true, 50000.0, 50000.0, recipients, Dict{String, Any}()
            )
            
            distribution = WaterfallDistribution(
                "entity", "waterfall", Date(2024, 1, 1), Date(2024, 1, 31),
                50000.0, [tier], 50000.0, 0.0, Dict{String, Any}(), Dict{String, Any}()
            )
            
            errors = validate_waterfall_distribution(distribution)
            @test length(errors) > 0
            @test any(error -> occursin("percentages sum to", error), errors)
        end
    end
    
    @testset "Summary Statistics" begin
        @testset "Multi-Period Summary" begin
            distributions = [
                WaterfallDistribution("e1", "w1", Date(2024, 1, 1), Date(2024, 1, 31), 
                                    100000.0, [], 80000.0, 20000.0, Dict{String, Any}(), Dict{String, Any}()),
                WaterfallDistribution("e1", "w1", Date(2024, 2, 1), Date(2024, 2, 28), 
                                    150000.0, [], 120000.0, 30000.0, Dict{String, Any}(), Dict{String, Any}()),
                WaterfallDistribution("e1", "w1", Date(2024, 3, 1), Date(2024, 3, 31), 
                                    200000.0, [], 180000.0, 20000.0, Dict{String, Any}(), Dict{String, Any}())
            ]
            
            summary = summarize_waterfall_distribution(distributions)
            @test summary["total_periods"] == 3
            @test summary["total_available"] == 450000.0
            @test summary["total_distributed"] == 380000.0
            @test summary["total_remaining"] == 70000.0
            @test summary["distribution_efficiency"] ≈ 380000.0 / 450000.0
            @test summary["average_per_period"] ≈ 380000.0 / 3
            @test summary["periods_with_distributions"] == 3
        end
        
        @testset "Empty Distributions Summary" begin
            summary = summarize_waterfall_distribution(Vector{WaterfallDistribution}())
            @test summary["total_periods"] == 0
            @test summary["total_available"] == 0.0
            @test summary["total_distributed"] == 0.0
            @test summary["distribution_efficiency"] == 0.0
        end
    end
    
    @testset "Capital Stack Extraction" begin
        @testset "Deal Level Capital Stack" begin
            capital_stack = Dict{String, Any}(
                "id" => "deal_capital_stack",
                "participants" => [
                    Dict{String, Any}("partyId" => "investor_1", "amount" => 500000.0)
                ]
            )
            
            ir_data = IRData(
                Dict{String, Any}("capitalStack" => capital_stack), [], [], [], 
                Dict{String, Any}(), Dict{String, Any}(), [], Dict{String, Any}(), Dict{String, Any}()
            )
            
            extracted = extract_capital_stack(ir_data)
            @test extracted["id"] == "deal_capital_stack"
            @test length(extracted["participants"]) == 1
        end
        
        @testset "Asset Level Capital Stack" begin
            asset_capital_stack = Dict{String, Any}(
                "id" => "asset_capital_stack",
                "participants" => [
                    Dict{String, Any}("partyId" => "asset_investor", "amount" => 300000.0)
                ]
            )
            
            ir_data = IRData(
                Dict{String, Any}(), [Dict{String, Any}("capitalStack" => asset_capital_stack)], [], [], 
                Dict{String, Any}(), Dict{String, Any}(), [], Dict{String, Any}(), Dict{String, Any}()
            )
            
            extracted = extract_capital_stack(ir_data)
            @test extracted["id"] == "asset_capital_stack"
        end
        
        @testset "Default Capital Stack" begin
            ir_data = IRData(
                Dict{String, Any}(), [], [], [], Dict{String, Any}(), Dict{String, Any}(), [], Dict{String, Any}(), Dict{String, Any}()
            )
            
            extracted = extract_capital_stack(ir_data)
            @test extracted["id"] == "default_capital_stack"
            @test length(extracted["participants"]) == 0
        end
    end
    
end  # End of main testset