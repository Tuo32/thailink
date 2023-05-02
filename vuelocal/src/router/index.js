import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/Manage.vue'

Vue.use(VueRouter)


let storeMenus = localStorage.getItem("menus");
let menus = JSON.parse(storeMenus);

const itemMenu = [{path:'/home', name:'Home',component:()=> import('../views/Home.vue')},{path:'/person', name:'Person',component:()=> import('../views/Person.vue')}]
if(menus != null) {
  menus.forEach(item => {
    itemMenu.push({path: item.path, name: item.name, component: () => import('../views/' + item.docName + '.vue')})
  });
}

let routes = [
  {
    path: '/mn',
    name: 'Manage',
    component: ()=>import('../views/Manage'),
    children:
    itemMenu
    //     [
    //   {path:'user', name:'User', component:()=>import('../views:/user.vue')},
    //   {path:'home', name:'Home',component:()=> import('../views/Home.vue')},
    //   {path:'person',name:'Person', component:()=>import('../views/Person')},
    //   {path:'customer',name:'Customer', component:()=>import('../views/Customer')},
    //   {path:'file',name:'File',component:()=>import('../views/File')},
    //   {path:'role',name:'Role',component:()=>import('../views/Role')},
    //   {path:'menu',name:'Menu',component:()=>import('../views/Menu')}
    //
    // ]
  },
  {
    path:'/',
    name:'login',
    component:()=>import('../views/Login.vue')
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },

]

console.log("routes"+routes.toString())
localStorage.setItem("routes",JSON.stringify(routes[0]))

// const setRoutes = () => {
//   const storeMenus = localStorage.getItem("menus");
//   if (storeMenus) {
//     const menus = JSON.parse(storeMenus)
//     menus.forEach(item => {
//       let itemMenu = { path: item.path, name: item.name, component: () => import('../views/' + item.path + '.vue')}
//       route.children.push(itemMenu)
//     })
//   }
// }
//

const router = new VueRouter({
  mode: 'hash',
  // base: process.env.BASE_URL,
  routes
})

export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'hash',
    // base: process.env.BASE_URL,
    routes
  })
}

router.beforeEach((to,from,next)=>{
  return next();
})



export default router
