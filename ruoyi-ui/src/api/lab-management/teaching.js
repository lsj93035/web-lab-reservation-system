import request from '@/utils/request'

// 查询教学占用列表
export function listTeaching(query) {
  return request({
    url: '/teaching/list',
    method: 'get',
    params: query
  })
}

// 查询教学占用详细
export function getTeaching(occupyId) {
  return request({
    url: '/teaching/' + occupyId,
    method: 'get'
  })
}

// 新增教学占用
export function addTeaching(data) {
  return request({
    url: '/teaching',
    method: 'post',
    data: data
  })
}

// 删除教学占用
export function delTeaching(occupyIds) {
  return request({
    url: '/teaching/' + occupyIds,
    method: 'delete'
  })
}
