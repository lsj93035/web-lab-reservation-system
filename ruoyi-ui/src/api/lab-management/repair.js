import request from '@/utils/request'

// 提交报修
export function addRepair(data) {
  return request({
    url: '/repair',
    method: 'post',
    data: data
  })
}

// 查询我的报修列表
export function listMyRepair(query) {
  return request({
    url: '/repair/myList',
    method: 'get',
    params: query
  })
}

// 查询报修详细
export function getRepair(repairId) {
  return request({
    url: '/repair/' + repairId,
    method: 'get'
  })
}

// 导出我的报修
export function exportRepair(query) {
  return request({
    url: '/repair/export',
    method: 'post',
    params: query
  })
}
