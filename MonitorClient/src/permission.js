import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'
// import isObjectValueEqualNew from '@/utils/index'
NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to.meta.title)
  console.log(router)
  // determine whether the user has logged in
  const hasToken = getToken()
  // console.log(store.getters.add_routes)
  // console.log(store.getters.permission_routes)
  // console.log(router.constantRoutes)
  if (hasToken) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done()
    } else {
      const hasRoles = store.getters.roles && store.getters.roles.length > 0
      if (hasRoles) {
        // console.log('hasRoles')
        // console.log(router.const)
        next()
      } else {
        // console.log('获取信息')
        try {
          // get user info
          const { data } = await store.dispatch('user/getInfo')
          const roles = data.info.roles
          const temp = []
          for (const role of roles) {
            temp.push(role.roleName)
          }
          // console.log(temp)
          const accessRoutes = await store.dispatch('permission/generateRoutes', temp)
          // console.log('accessRoutes')
          // console.log(accessRoutes)
          router.addRoutes(accessRoutes)

          // hack method to ensure that addRoutes is complete
          // set the replace: true, so the navigation will not leave a history record
          next({ ...to, replace: true })
          // next()
        } catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /* has no token*/

    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
