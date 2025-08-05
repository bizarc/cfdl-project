# Verification script to mathematically validate metrics correctness
using Pkg
Pkg.activate(".")
using CFDLEngine
using Printf

println("🔬 Mathematical Verification of Metrics Correctness")
println("=" ^ 55)

# Test 1: NPV Verification
println("\n1️⃣  NPV Verification")
println("-" ^ 25)

cash_flows = [100.0, 110.0, 121.0]
discount_rate = 0.10
initial_investment = 300.0

# Calculate using engine
engine_npv = CFDLEngine.calculate_npv(cash_flows, discount_rate, initial_investment)

# Manual calculation
manual_pv = 100.0/(1.10)^1 + 110.0/(1.10)^2 + 121.0/(1.10)^3
manual_npv = manual_pv - initial_investment

println("Engine NPV: $(@sprintf "%.6f" engine_npv)")
println("Manual NPV: $(@sprintf "%.6f" manual_npv)")
println("Difference: $(@sprintf "%.10f" abs(engine_npv - manual_npv))")
println("✅ Match: $(abs(engine_npv - manual_npv) < 1e-10)")

# Test 2: IRR Verification  
println("\n2️⃣  IRR Verification")
println("-" ^ 25)

# Calculate IRR using engine
engine_irr = CFDLEngine.calculate_irr(cash_flows, initial_investment)

# Verify by calculating NPV at the IRR (should be ≈ 0)
npv_at_irr = CFDLEngine.calculate_npv(cash_flows, engine_irr, initial_investment)

println("Engine IRR: $(@sprintf "%.6f" engine_irr) ($(@sprintf "%.2f%%" (engine_irr * 100)))")
println("NPV at IRR: $(@sprintf "%.10f" npv_at_irr)")
println("✅ IRR is correct: $(abs(npv_at_irr) < 1e-6)")

# Test 3: DSCR Verification
println("\n3️⃣  DSCR Verification")
println("-" ^ 25)

operating_cfs = [120.0, 130.0, 140.0]
debt_service = [100.0, 110.0, 120.0]

engine_dscr = CFDLEngine.calculate_dscr(operating_cfs, debt_service)
manual_dscr = [120.0/100.0, 130.0/110.0, 140.0/120.0]

println("Engine DSCR: $([(@sprintf "%.3f" x) for x in engine_dscr])")
println("Manual DSCR: $([(@sprintf "%.3f" x) for x in manual_dscr])")
println("✅ Match: $(all(abs(engine_dscr[i] - manual_dscr[i]) < 1e-10 for i in 1:3))")

# Test 4: MOIC Verification
println("\n4️⃣  MOIC Verification")
println("-" ^ 25)

total_distributions = 250.0
initial_inv = 100.0

engine_moic = CFDLEngine.calculate_moic(total_distributions, initial_inv)
manual_moic = total_distributions / initial_inv

println("Engine MOIC: $(@sprintf "%.6f" engine_moic)")
println("Manual MOIC: $(@sprintf "%.6f" manual_moic)")
println("✅ Match: $(abs(engine_moic - manual_moic) < 1e-10)")

# Test 5: Payback Period Verification
println("\n5️⃣  Payback Period Verification")
println("-" ^ 35)

payback_cfs = [40.0, 60.0, 50.0]
payback_initial = 100.0

engine_payback = CFDLEngine.calculate_payback_period(payback_cfs, payback_initial)

# Manual calculation: 40 + 60 = 100 recovered exactly at end of period 2
manual_payback = 2.0

println("Engine Payback: $(@sprintf "%.6f" engine_payback) years")
println("Manual Payback: $(@sprintf "%.6f" manual_payback) years")
println("✅ Match: $(abs(engine_payback - manual_payback) < 1e-10)")

# Test 6: Complex Real-World Scenario
println("\n6️⃣  Complex Real-World Scenario")
println("-" ^ 35)

# 5-year commercial real estate investment
complex_cfs = [75000.0, 80000.0, 85000.0, 90000.0, 95000.0 + 1200000.0]  # NOI + sale
complex_rate = 0.09
complex_initial = 1000000.0

# Calculate all metrics
all_metrics = CFDLEngine.calculate_all_metrics(complex_cfs, Dict(
    "discount_rate" => complex_rate,
    "initial_investment" => complex_initial,
    "debt_service" => [45000.0, 45000.0, 45000.0, 45000.0, 45000.0]
))

# Manual NPV verification
manual_complex_pv = sum(complex_cfs[i] / (1.09)^i for i in 1:length(complex_cfs))
manual_complex_npv = manual_complex_pv - complex_initial

println("Complex scenario results:")
println("  NPV (engine): $(@sprintf "%.2f" all_metrics["npv"].value)")
println("  NPV (manual): $(@sprintf "%.2f" manual_complex_npv)")
println("  NPV difference: $(@sprintf "%.10f" abs(all_metrics["npv"].value - manual_complex_npv))")

# Verify IRR by checking NPV = 0
irr_verification_npv = CFDLEngine.calculate_npv(complex_cfs, all_metrics["irr"].value, complex_initial)
println("  IRR: $(@sprintf "%.2f%%" (all_metrics["irr"].value * 100))")
println("  NPV at IRR: $(@sprintf "%.10f" irr_verification_npv)")

println("✅ Complex scenario verification: $(abs(all_metrics["npv"].value - manual_complex_npv) < 1e-6 && abs(irr_verification_npv) < 1e-6)")

# Test 7: Distribution Analysis Verification
println("\n7️⃣  Distribution Analysis Verification")
println("-" ^ 40)

# Create known data set for verification
test_data = [1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0]
dist_stats = CFDLEngine.analyze_distribution(test_data)

# Manual calculations
manual_mean = sum(test_data) / length(test_data)  # Should be 5.5
manual_median = (test_data[5] + test_data[6]) / 2  # Should be 5.5
manual_std = sqrt(sum((x - manual_mean)^2 for x in test_data) / (length(test_data) - 1))  # Sample std

println("Distribution statistics:")
println("  Mean (engine): $(@sprintf "%.6f" dist_stats.mean)")
println("  Mean (manual): $(@sprintf "%.6f" manual_mean)")
println("  Median (engine): $(@sprintf "%.6f" dist_stats.median)")
println("  Median (manual): $(@sprintf "%.6f" manual_median)")
println("  Std Dev (engine): $(@sprintf "%.6f" dist_stats.std_dev)")
println("  Std Dev (manual): $(@sprintf "%.6f" manual_std)")

mean_match = abs(dist_stats.mean - manual_mean) < 1e-10
median_match = abs(dist_stats.median - manual_median) < 1e-10
std_match = abs(dist_stats.std_dev - manual_std) < 1e-10

println("✅ Distribution verification: $(mean_match && median_match && std_match)")

# Summary
println("\n" ^ 2 * "=" ^ 55)
println("📋 VERIFICATION SUMMARY")
println("=" ^ 55)

tests = [
    ("NPV calculation", abs(engine_npv - manual_npv) < 1e-10),
    ("IRR calculation", abs(npv_at_irr) < 1e-6),
    ("DSCR calculation", all(abs(engine_dscr[i] - manual_dscr[i]) < 1e-10 for i in 1:3)),
    ("MOIC calculation", abs(engine_moic - manual_moic) < 1e-10),
    ("Payback calculation", abs(engine_payback - manual_payback) < 1e-10),
    ("Complex scenario", abs(all_metrics["npv"].value - manual_complex_npv) < 1e-6 && abs(irr_verification_npv) < 1e-6),
    ("Distribution analysis", mean_match && median_match && std_match)
]

global all_passed = true
for (test_name, passed) in tests
    status = passed ? "✅ PASS" : "❌ FAIL"
    println("$status  $test_name")
    global all_passed = all_passed && passed
end

println("\n" * "=" ^ 55)
if all_passed
    println("🎉 ALL TESTS PASSED - Metrics engine is mathematically correct!")
    println("\nThe engine demonstrates:")
    println("  • Accurate NPV calculations using present value formulas")
    println("  • Correct IRR calculations (NPV = 0 at computed IRR)")
    println("  • Proper DSCR, MOIC, and payback period computations")
    println("  • Valid statistical analysis of distributions")
    println("  • Robust handling of complex real-world scenarios")
else
    println("❌ Some tests failed - please review calculations")
end