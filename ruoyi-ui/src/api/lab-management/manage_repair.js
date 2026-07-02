import request from '@/utils/request'

// 查询报修列表
export function listManageRepair(query) {
  return request({
    url: '/manage-repair/list',
    method: 'get',
    params: query
  })
}

// 修改报修状态
export function updateRepairStatus(data) {
  return request({
    url: '/manage-repair/status',
    method: 'put',
    data: data
  })
}

// 导出报修列表
export function exportManageRepair(query) {
  return request({
    url: '/manage-repair/export',
    method: 'post',
    params: query
  })
}
