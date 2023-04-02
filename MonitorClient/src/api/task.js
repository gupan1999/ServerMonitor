import request from '@/utils/request'
export function addTask(data) {
  return request({
    url: '/task/add',
    method: 'post',
    data
  })
}

export function fetchTaskList(data) {
  return request({
    url: '/task/get',
    method: 'get',
    params: data
  })
}

export function fetchTaskIds(data) {
  return request({
    url: '/task/fetch',
    method: 'get',
    params: data
  })
}

export function updateTask(data) {
  return request({
    url: '/task/update',
    method: 'post',
    data
  })
}

export function deleteTask(id) {
  return request({
    url: '/task/delete',
    method: 'post',
    params: { id }
  })
}

export function stopTask(data) {
  return request({
    url: '/task/stop',
    method: 'post',
    data
  })
}

export function resumeTask(data) {
  return request({
    url: '/task/save',
    method: 'post',
    data
  })
}
