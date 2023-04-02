import request from '@/utils/request'
// export function addTask(data) {
//   return request({
//     url: '/task/add',
//     method: 'post',
//     data
//   })
// }

// export function fetchTaskList(data) {
//   return request({
//     url: '/task/get',
//     method: 'get',
//     params: data
//   })
// }

export function fetchServerNames(data) {
  return request({
    url: '/server/fetch',
    method: 'get',
    params: data
  })
}

export function fetchAvailServerNames(data) {
  return request({
    url: '/server/avail',
    method: 'get',
    params: data
  })
}

export function fetchAllServer(data) {
  return request({
    url: '/server/all',
    method: 'get',
    params: data
  })
}

export function fetchAvailServer(data) {
  return request({
    url: '/server/available',
    method: 'get',
    params: data
  })
}

export function stop(data) {
  return request({
    url: '/server/stop',
    method: 'post',
    params: data
  })
}

export function start(data) {
  return request({
    url: '/server/start',
    method: 'post',
    params: data
  })
}

// export function updateTask(data) {
//   return request({
//     url: '/task/update',
//     method: 'post',
//     data
//   })
// }

// export function deleteTask(id) {
//   return request({
//     url: '/task/delete',
//     method: 'post',
//     params: { id }
//   })
// }

// export function stopTask(data) {
//   return request({
//     url: '/task/stop',
//     method: 'post',
//     data
//   })
// }

// export function resumeTask(data) {
//   return request({
//     url: '/task/save',
//     method: 'post',
//     data
//   })
// }
