// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** Add node in the graph. POST /api/v1/node/add */
export async function addNodeUsingPOST(body: API.AddNodeRequest, options?: { [key: string]: any }) {
  return request<API.AddNodeResponse>('/api/v1/node/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** Delete node in the graph. GET /api/v1/node/del */
export async function deleteNodeUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteNodeUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BasicResponse>('/api/v1/node/del', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** Get the node information in the graph. GET /api/v1/node/get */
export async function getNodeUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getNodeUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.GetNodeResponse>('/api/v1/node/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** Update the node in the graph. GET /api/v1/node/update */
export async function updateNodeUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateNodeUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BasicResponse>('/api/v1/node/update', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
