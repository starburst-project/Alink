// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** Get the node's params. GET /api/v1/param/get */
export async function getNodeParamUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getNodeParamUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.GetNodeParamResponse>('/api/v1/param/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** Update the node's param. POST /api/v1/param/update */
export async function updateParamUsingPOST(
  body: API.UpdateParamRequest,
  options?: { [key: string]: any },
) {
  return request<API.BasicResponse>('/api/v1/param/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
