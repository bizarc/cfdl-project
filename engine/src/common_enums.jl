# Common Enums for CFDL Engine
# Centralized enum definitions to avoid conflicts and ensure consistency

"""
    CashFlowCategory

Industry-standard cash flow categories for financial modeling.
Used across multiple pipeline stages for consistent categorization.
"""
@enum CashFlowCategory begin
    OPERATING
    FINANCING  
    INVESTING
    TAX_RELATED
end

"""
    StatementView

Enumeration of different statement view types for financial reporting.
Each view provides a different perspective on the same underlying cash flows.
"""
@enum StatementView begin
    GAAP
    NON_GAAP
    TAX
    MANAGEMENT
end

"""
    ReportingFrequency

Frequency options for financial statement reporting and aggregation.
"""
@enum ReportingFrequency begin
    MONTHLY
    QUARTERLY
    ANNUAL
    CUMULATIVE
end

"""
    EntityType

Types of entities in the cash flow hierarchy.
"""
@enum EntityType begin
    DEAL
    ASSET
    COMPONENT
    STREAM
end

"""
    ProcessingStage

Stages in the 7-stage cash flow processing pipeline.
"""
@enum ProcessingStage begin
    STREAM_COLLECTION
    CASH_FLOW_ASSEMBLY
    OPERATING_STATEMENT_GENERATION
    FINANCING_ADJUSTMENTS
    TAX_PROCESSING
    AVAILABLE_CASH_CALCULATION
    STATEMENT_VIEWS
end

"""
    ValidationSeverity

Severity levels for validation messages.
"""
@enum ValidationSeverity begin
    INFO
    WARNING
    ERROR
    CRITICAL
end