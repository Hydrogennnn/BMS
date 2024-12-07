import { createRouter, createWebHistory } from 'vue-router'
import Layout from "../layout/Layout";

const routes = [
  {
    path: '/',
    name: 'Layout',
    redirect:"user",
    component: Layout,
    children:[
      {
        path:'user',
        name:'user',
        component:() => import("@/views/User")
      },
      {
        path: 'bookManage',
        name: 'bookManage',
        component: () => import("@/views/BookManage")
      },
      {
        path: 'person',
        name: 'Person',
        component: () => import("@/views/Person")
      },
      {
        path: 'password',
        name: 'Password',
        component: () => import("@/views/Password")
      },
      {
        path: 'lendrecord',
        name: 'LendRecord',
        component: () => import("@/views/LendRecord")
      },
      {
        path:'dashboard',
        name:'Dashboard',
        component:() => import("@/views/MyDashboard")
      },
      {
        path: 'bookwithuser',
        name: 'BookWithUser',
        component: () => import("@/views/BookWithUser")
      },
      {
        path: 'bookinstance',
        name: 'BookInstance',
        component: () => import("@/views/BookInstanceManage")
      },
      {
        path: 'test',
        name: 'Test',
        component: () => import("@/views/test")
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import("@/views/Login")
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import("@/views/Register")
  },


]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
