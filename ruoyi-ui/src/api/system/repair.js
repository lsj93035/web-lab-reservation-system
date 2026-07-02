import request from '@/utils/request'

// 查询报修列表
export function listRepair(query) {
  return request({
    url: '/system/repair/list',
    method: 'get',
    params: query
  })
}

// 新增报修申请
export function addRepair(data) {
  return request({
    url: '/system/repair',
    method: 'post',
    data: data
  })
}