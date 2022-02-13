export default {
  publicPath: "./",
  dva: {
    hmr: true,
  },
  layout: {
    name: 'Alink',
    navTheme: 'light',
    layout: 'top',
    locale: true
  },
  theme: {
    // https://github.com/ant-design/pro-components/issues/4343
    'primary-color-hover': '#fff',
  },
  locale: {
    // default zh-CN
    default: 'zh-CN',
    antd: false,
    // default true, when it is true, will use `navigator.language` overwrite default
    baseNavigator: true,
    title: true
  },
  openAPI: [
    {
      requestLibPath: "import { request } from 'umi'",
      schemaPath: "http://localhost:8080/v3/api-docs",
      mock: false,
    }
  ],
  proxy: {
    '/api/v1/': {
      target: 'http://localhost:8080/',
      changeOrigin: true
    }
  },
  routes: [
    {
      path: '/welcome',
      name: 'welcome',
      component: './Welcome'
    },
    {
      path: '/develop',
      name: 'develop',
      component: './',
    },
    {
      path: '/',
      redirect: '/welcome',
    },
    {
      component: './404',
    },
  ]
};
