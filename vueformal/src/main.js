import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/global.css';
import locale from 'element-ui/lib/locale/lang/en'
import request from "@/utils/request";

Vue.config.productionTip = false

// Vue.use(ElementUI,{size: "mini"});
Vue.use(ElementUI, { locale, size:"mini" })


Vue.prototype.request = request

//change the string to be the server address
Vue.prototype.$server = 'thailink.japaneast.cloudapp.azure.com/api';
// Vue.prototype.$server = 'localhost:9090';


new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
