import request from '@/utils/request'

// 查询实验室列表
export function listLab(query) {
  return request({
    url: '/lab/list',
    method: 'get',
    params: query
  })
}

// 查询实验室详细
export function getLab(labId) {
  return request({
    url: '/lab/' + labId,
    method: 'get'
  })
}

// 新增实验室
export function addLab(data) {
  return request({
    url: '/lab',
    method: 'post',
    data: data
  })
}

// 修改实验室
export function updateLab(data) {
  return request({
    url: '/lab',
    method: 'put',
    data: data
  })
}

// 删除实验室
export function delLab(labIds) {
  return request({
    url: '/lab/' + labIds,
    method: 'delete'
  })
}

// 修改实验室状态
export function changeLabStatus(labId, status) {
  const data = {
    labId,
    status
  }
  return request({
    url: '/lab/changeStatus',
    method: 'put',
    data: data
  })
}

// 获取楼宇列表
export function getBuildingList() {
  return request({
    url: '/lab/buildingList',
    method: 'get'
  })
}
