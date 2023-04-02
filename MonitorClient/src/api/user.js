import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/user/info',
    method: 'get',
    // params: { token }
    token
  })
}

export function fetchLink(data) {
  return request({
    url: '/userServer/fetch',
    method: 'get',
    // params: { token }
    params: data
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}

export function bind(data) {
  return request({
    url: '/userServer/bind',
    method: 'post',
    params: data
  })
}

export function unbind(data) {
  return request({
    url: '/userServer/unbind',
    method: 'post',
    params: data
  })
}

