import clsx from 'clsx';
import Heading from '@theme/Heading';
import styles from './styles.module.css';

type FeatureItem = {
  title: string;
  Svg: React.ComponentType<React.ComponentProps<'svg'>>;
  description: JSX.Element;
};

const FeatureList: FeatureItem[] = [
  {
    title: 'Human-Readable Models',
    Svg: require('@site/static/img/undraw_docusaurus_mountain.svg').default,
    description: (
      <>
        CFDL uses clean YAML syntax to define complex financial models. 
        Your models are readable, maintainable, and version-controllable 
        just like code.
      </>
    ),
  },
  {
    title: 'Schema-Driven Validation',
    Svg: require('@site/static/img/undraw_docusaurus_tree.svg').default,
    description: (
      <>
        Comprehensive JSON schema validation ensures your models are correct 
        before execution. Catch errors early with detailed validation reports 
        and helpful error messages.
      </>
    ),
  },
  {
    title: 'Hierarchical Modeling',
    Svg: require('@site/static/img/undraw_docusaurus_react.svg').default,
    description: (
      <>
        Model complex deal structures with deals, assets, components, and streams. 
        Support for commercial real estate, infrastructure, renewable energy, 
        and private equity use cases.
      </>
    ),
  },
];

function Feature({title, Svg, description}: FeatureItem) {
  return (
    <div className={clsx('col col--4')}>
      <div className="text--center">
        <Svg className={styles.featureSvg} role="img" />
      </div>
      <div className="text--center padding-horiz--md">
        <Heading as="h3">{title}</Heading>
        <p>{description}</p>
      </div>
    </div>
  );
}

export default function HomepageFeatures(): JSX.Element {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row">
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
        
        {/* Quick Start Section */}
        <div className="row margin-top--xl">
          <div className="col col--12">
            <div className="text--center">
              <Heading as="h2">Quick Start</Heading>
              <p className="hero__subtitle">
                Create your first CFDL model in minutes
              </p>
            </div>
          </div>
        </div>
        
        <div className="row">
          <div className="col col--6">
            <Heading as="h3">1. Define Your Deal</Heading>
            <pre>
              <code>{`# my-deal.cfdl
deal:
  MyFirstDeal:
    name: "Sample Real Estate Deal"
    dealType: commercial_real_estate
    currency: "USD"`}</code>
            </pre>
          </div>
          <div className="col col--6">
            <Heading as="h3">2. Add Cash Flow Streams</Heading>
            <pre>
              <code>{`streams:
  - stream:
      RentalIncome:
        name: "Monthly Rent"
        category: Revenue
        amount: 10000
        schedule:
          type: recurring
          freq: monthly`}</code>
            </pre>
          </div>
        </div>
        
        <div className="row margin-top--md">
          <div className="col col--12">
            <div className="text--center">
              <Heading as="h3">3. Process Your Model</Heading>
              <pre>
                <code>{`mvn exec:java -Dexec.mainClass="dev.cfdl.Parser" -Dexec.args="my-deal.cfdl"

🎉 COMPLETE CFDL Pipeline Success!
📊 Pipeline Summary:
  • Parsed 2 AST nodes
  • Built 2 IR nodes
  • All schemas validated successfully`}</code>
              </pre>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}