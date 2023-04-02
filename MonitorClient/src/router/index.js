import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard' }
    }]
  },
  {
    path: '/chart',
    component: Layout,
    children: [{
      path: '/chart',
      name: 'Chart',
      component: () => import('@/views/chart/line'),
      meta: { title: '图表', icon: 'chart'}
    }
    ]
  
  }
]
// {
  //   path: '/chart',
  //   component: Layout,
  //   meta: { title: '图表', icon: 'chart' },
  //   children: 
  //   [{
  //     path: 'query',
  //     name: 'Query',
  //     component: () => import('@/views/chart/tmp'),
  //     meta: { title: '指标查询', icon: 'dashboard' }
  //   },
  //   {
  //     path: 'system',
  //     name: 'System',
  //     component: () => import('@/views/chart/line'),
  //     meta: { title: '系统概况', icon: 'documentation' }
  //   }
  
  // ]
  // }
export const asyncRoutes = [
  {
    path: '/alert',
    component: Layout,
    // redirect: '/permission/page',
    alwaysShow: true, // will always show the root menu
    name: 'Alert',
    meta: {
      title: '告警',
      icon: 'el-icon-message-solid',
      roles: ['admin', 'user'] // you can set roles in root nav
    },
    children: [
      {
        path: 'task',
        component: () => import('@/views/alert/tasks'),
        name: 'Task',
        meta: {
          icon: 'el-icon-circle-check',
          title: '检测任务',
          roles: ['admin', 'user']
        }
      },
      {
        path: 'endpoint',
        component: () => import('@/views/alert/endpoints'),
        name: 'EndPoint',
        meta: {
          icon: 'el-icon-message',
          title: '端点',
          roles: ['admin', 'user']
        }
      },
      {
        path: 'message',
        component: () => import('@/views/alert/records'),
        name: 'message',
        meta: {
          icon: 'el-icon-warning',
          title: '异常消息',
          roles: ['admin', 'user']
        }
      }

    ]
  },
  {
    path: '/setting',
    component: Layout,
    redirect: '/setting/metric',
    name: 'Setting',
    alwaysShow: true, // will always show the root menu
    meta: { title: '管理', icon: 'el-icon-s-tools' },

    children: [
      {
        path: 'metric',
        name: 'Metric',
        component: () => import('@/views/setting/server'),
        meta: {
          title: '监控项',
          icon: 'el-icon-s-promotion',
          roles: ['admin', 'user']
        }
      },

      {
        path: 'serverUser',
        name: 'ServerUser',
        component: () => import('@/views/setting/user'),
        meta: {
          title: '服务器可见性',
          icon: 'el-icon-s-platform',
          roles: ['admin']
        }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()
// router.addRoutes(asyncRoutes)

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
