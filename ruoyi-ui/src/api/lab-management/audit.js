import request from '@/utils/request'

// 查询待审核的预约列表
export function listPendingReservation(query) {
  return request({
    url: '/reservation/pendingList',
    method: 'get',
    params: query
  })
}

// 审批预约
export function auditReservation(data) {
  return request({
    url: '/reservation/audit',
    method: 'post',
    data: data
  })
}
