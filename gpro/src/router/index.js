import Vue from 'vue'
import VueRouter from 'vue-router'


Vue.use(VueRouter)
/*
export default new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld
    }
  ]
})
*/
/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const routes = [
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/demochart',
    name: 'demochart',
    component: () => import('../views/demochart.vue')
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path === '/servermap') {
    document.documentElement.style.backgroundColor = '#AAD3DF'
  }
  next()
})

export default router
