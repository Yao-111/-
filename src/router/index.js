import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '/src/views/HomeView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('/src/components/auth/Login.vue')
  },
  {
    path: '/ebooks',
    name: 'ebooks',
    component: () => import('/src/components/ebooks/EbookList.vue')
  },
  {
    path: '/ebooks/upload',
    name: 'ebook-upload',
    component: () => import('/src/components/ebooks/EbookUpload.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  
    next()
  
})

export default router