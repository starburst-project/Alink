// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** Add edge of the graph. POST /api/v1/edge/add */
export async function addEdgeUsingPOST(body: API.AddEdgeRequest, options?: { [key: string]: any }) {
  return request<API.AddEdgeResponse>('/api/v1/edge/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** Delete edge of the graph. GET /api/v1/edge/del */
export async function deleteEdgeUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteEdgeUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BasicResponse>('/api/v1/edge/del', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
