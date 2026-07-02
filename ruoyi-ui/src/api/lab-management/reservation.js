import request from '@/utils/request'

// 查询可用实验室列表
export function listLabSelect(query) {
  return request({
    url: '/reservation/labSelect',
    method: 'get',
    params: query
  })
}

// 查询某实验室某天的已预约时间段
export function timeQueryByDate(labId, date) {
  return request({
    url: '/reservation/timeQueryByDate',
    method: 'get',
    params: { labId, date }
  })
}

// 提交预约
export function addReservation(data) {
  return request({
    url: '/reservation',
    method: 'post',
    data: data
  })
}

// 查询我的预约列表
export function listMyReservation(query) {
  return request({
    url: '/reservation/myList',
    method: 'get',
    params: query
  })
}

// 查询预约详细
export function getReservation(reserveId) {
  return request({
    url: '/reservation/' + reserveId,
    method: 'get'
  })
}

// 修改预约
export function updateReservation(data) {
  return request({
    url: '/reservation',
    method: 'put',
    data: data
  })
}

// 撤销预约
export function cancelReservation(reserveId) {
  return request({
    url: '/reservation/' + reserveId,
    method: 'delete'
  })
}
