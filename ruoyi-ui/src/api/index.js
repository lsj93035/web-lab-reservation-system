import request from "@/utils/request";

// 获取首页统计数据
export function getStatData() {
  return request({
    url: '/system/index/stat',
    method: 'get'
  })
}
