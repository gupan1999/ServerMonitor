import request from '@/utils/request'

export function fetchAlertList(query) {
  return request({
    url: '/alert/get',
    method: 'get',
    params: query
  })
}
