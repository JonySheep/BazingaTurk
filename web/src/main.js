// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'

//引入主题样式
import ElementUI from 'element-ui';
import './assets/lockIcon/iconfont.css'
import './assets/filterIcon/iconfont.css'
import 'element-ui/lib/theme-chalk/index.css';
import './style/themeStyle.scss'

//引入axios,并绑定到全局
import axios from 'axios'
Vue.prototype.$axios = axios;

//引用api文件，并将api方法绑定到全局
import api from './api/index.js'
Vue.prototype.$api = api;

//引用util文件,并绑定到全局
import utils from './utils/index.js'
Vue.prototype.$utils = utils;

Vue.config.productionTip = false
Vue.use(ElementUI);

//用来显示页面标题
router.beforeEach((to, from, next) => {//beforeEach是router的钩子函数，在进入路由前执行
  if (to.meta.title) {//判断是否有标题
    document.title = to.meta.title
  }
  next()//执行进入路由，如果不写就不会进入目标页
})

//引入拖拽排序组件
import VueDND from 'awe-dnd'
Vue.use(VueDND)


//
Vue.prototype.loginPrompt=function () {
  this.$http.get('login_p.json').then(function (r) {
    this.$router.push('/')
  })
}


/* eslint-disable no-new */

new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App }
})

