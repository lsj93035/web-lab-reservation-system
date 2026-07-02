import request from '@/utils/request'

// 查询与我有关的预约
export function listAboutMe(query) {
  return request({
    url: '/aboutMe/list',
    method: 'get',
    params: query
  })
}

// 查询预约详细
export function getAboutMe(reserveId) {
  return request({
    url: '/aboutMe/' + reserveId,
    method: 'get'
  })
}
