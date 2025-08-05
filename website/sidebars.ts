import type {SidebarsConfig} from '@docusaurus/plugin-content-docs';

const sidebars: SidebarsConfig = {
  // Main documentation sidebar
  docs: [
    // Overview Section
    {
      type: 'category',
      label: 'Overview',
      items: [
        'overview/what-is-cfdl',
        'overview/getting-started',
      ],
    },

    // CFDL Specification
    {
      type: 'category',
      label: 'CFDL Specification',
      items: [
        'specification/cfdl-v1-spec',
        'specification/ontology-reference',
        {
          type: 'category',
          label: 'Schema Reference',
          items: [
            // Common Types
            'specification/ontology/common-types.schema',
            
            // Entity Schemas
            'specification/entity/deal.schema',
            'specification/entity/asset.schema', 
            'specification/entity/component.schema',
            'specification/entity/party.schema',
            'specification/entity/fund.schema',
            'specification/entity/portfolio.schema',
            'specification/entity/contract.schema',
            'specification/entity/template.schema',
            'specification/entity/capital-stack.schema',
            
            // Behavior Schemas
            'specification/behavior/stream.schema',
            'specification/behavior/assumption.schema',
            'specification/behavior/logic-block.schema',
            'specification/behavior/rule_block.schema',
            'specification/behavior/market-data.schema',
            'specification/behavior/calculators.schema',
            
            // Temporal Schemas
            'specification/temporal/schedule.schema',
            'specification/temporal/recurrence_rule.schema',
            'specification/temporal/event_trigger.schema',
            
            // Result Schemas
            'specification/result/cash-flow.schema',
            'specification/result/waterfall.schema',
            'specification/result/tag-definition.schema',
            
            // Metrics Schemas
            'specification/metrics/npv.schema',
            'specification/metrics/irr.schema',
            'specification/metrics/eirr.schema',
            'specification/metrics/enpv.schema',
            'specification/metrics/moic.schema',
            'specification/metrics/dscr.schema',
            'specification/metrics/payback.schema',
          ],
        },
      ],
    },

    // Authoring Guide
    {
      type: 'category',
      label: 'Authoring Guide',
      items: [
        'authoring/best-practices',
      ],
    },
  ],
};

export default sidebars;