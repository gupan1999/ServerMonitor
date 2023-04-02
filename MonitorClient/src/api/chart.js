import request from '@/utils/request'

export function getChart(data) {
  // console.log(process.env.VUE_APP_BASE_API + '/show')
  return request({
    url: '/query',
    method: 'get',
    params: data
  })
}
