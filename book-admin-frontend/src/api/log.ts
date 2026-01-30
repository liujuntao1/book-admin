import request from '@/utils/request'

/**
 * 分页查询日志列表
 * @param params 筛选和分页参数
 * @returns 分页结果
 */
export function fetchLogList(data: any) {
  return request({
    url: '/api/logs/page',
    method: 'post',
    data,
  })
}
