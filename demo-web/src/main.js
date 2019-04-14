import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import iView from 'iview'
import 'iview/dist/styles/iview.css'

Vue.config.productionTip = false

// Axios属性
Vue.prototype.$http = axios

// 引入iView
Vue.use(iView)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
