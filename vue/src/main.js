import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/global.css';
import request from "@/utils/request";

Vue.config.productionTip = false

Vue.use(ElementUI,{size: "mini"});

Vue.prototype.request = request

// Vue.prototype.$server = '20.222.201.190';
Vue.prototype.$server = 'thailink.japaneast.cloudapp.azure.com';
// Vue.prototype.$server = 'localhost';
// Vue.prototype.$server = '10.0.0.5';


new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
