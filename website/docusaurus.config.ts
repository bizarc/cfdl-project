import {themes as prismThemes} from 'prism-react-renderer';
import type {Config} from '@docusaurus/types';
import type * as Preset from '@docusaurus/preset-classic';

const config: Config = {
  title: 'CFDL - Cash Flow Domain Language',
  tagline: 'A domain-specific language for financial modeling and cash flow analysis',
  favicon: 'img/favicon.ico',

  // Set the production url of your site here
  url: 'https://bizarc.github.io',
  // Set the /<baseUrl>/ pathname under which your site is served
  // For GitHub pages deployment, it is often '/<projectName>/'
  baseUrl: '/cfdl-project/',

  // GitHub pages deployment config.
  // If you aren't using GitHub pages, you don't need these.
  organizationName: 'bizarc', // Usually your GitHub org/user name.
  projectName: 'cfdl-project', // Usually your repo name.

  onBrokenLinks: 'warn',
  onBrokenMarkdownLinks: 'warn',

  // Even if you don't use internationalization, you can use this field to set
  // useful metadata like html lang. For example, if your site is Chinese, you
  // may want to replace "en" with "zh-Hans".
  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      {
        docs: {
          sidebarPath: './sidebars.ts',
          // Please change this to your repo.
          // Remove this to remove the "edit this page" links.
          editUrl:
            'https://github.com/bizarc/cfdl-project/tree/main/website/',
        },
        blog: {
          showReadingTime: true,
          // Please change this to your repo.
          // Remove this to remove the "edit this page" links.
          editUrl:
            'https://github.com/bizarc/cfdl-project/tree/main/website/',
        },
        theme: {
          customCss: './src/css/custom.css',
        },
      } satisfies Preset.Options,
    ],
  ],

  themeConfig: {
    // Replace with your project's social card
    image: 'img/docusaurus-social-card.jpg',
    navbar: {
      title: 'CFDL',
      logo: {
        alt: 'CFDL Logo',
        src: 'img/logo.svg',
      },
      items: [
        {
          type: 'docSidebar',
          sidebarId: 'docs',
          position: 'left',
          label: 'Documentation',
        },
        {to: '/blog', label: 'Blog', position: 'left'},
        {
          href: 'https://github.com/bizarc/cfdl-project',
          label: 'GitHub',
          position: 'right',
        },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        {
          title: 'Documentation',
          items: [
            {
              label: 'Getting Started',
              to: '/docs/overview/getting-started',
            },
            {
              label: 'CFDL Specification',
              to: '/docs/specification/cfdl-v1-spec',
            },
            {
              label: 'Authoring Guide',
              to: '/docs/authoring/best-practices',
            },
          ],
        },
        {
          title: 'Technical',
          items: [
            {
              label: 'Schema Reference',
              to: '/docs/specification/ontology-reference',
            },
            {
              label: 'What is CFDL?',
              to: '/docs/overview/what-is-cfdl',
            },
          ],
        },
        {
          title: 'More',
          items: [
            {
              label: 'Blog',
              to: '/blog',
            },
            {
              label: 'GitHub',
              href: 'https://github.com/bizarc/cfdl-project',
            },
          ],
        },
      ],
      copyright: `Copyright © ${new Date().getFullYear()} CFDL Project. Built with Docusaurus.`,
    },
    prism: {
      theme: prismThemes.github,
      darkTheme: prismThemes.dracula,
      additionalLanguages: ['yaml', 'json', 'java'],
    },
    // algolia: {
    //   // The application ID provided by Algolia
    //   appId: 'YOUR_APP_ID',
    //   // Public API key: it is safe to commit it
    //   apiKey: 'YOUR_SEARCH_API_KEY',
    //   indexName: 'cfdl',
    //   // Optional: see doc section below
    //   contextualSearch: true,
    //   // Optional: path for search page that enabled by default (`false` to disable it)
    //   searchPagePath: 'search',
    // },
  } satisfies Preset.ThemeConfig,
};

export default config;