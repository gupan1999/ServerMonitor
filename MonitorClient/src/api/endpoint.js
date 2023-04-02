import request from '@/utils/request'
export function addEndpoint(data) {
  return request({
    url: '/endpoint/add',
    method: 'post',
    data
  })
}

export function updateEndpoint(data) {
  return request({
    url: '/endpoint/update',
    method: 'post',
    data
  })
}

export function fetchEndpointList(data) {
  return request({
    url: '/endpoint/get',
    method: 'get',
    params: data
  })
}

export function bind(data) {
  return request({
    url: '/endpoint/bind',
    method: 'post',
    data
  })
}

export function unbind(data) {
  return request({
    url: '/endpoint/unbind',
    method: 'post',
    data
  })
}

export function deleteEndpoint(id) {
  return request({
    url: '/endpoint/delete',
    method: 'post',
    params: { id }
  })
}


