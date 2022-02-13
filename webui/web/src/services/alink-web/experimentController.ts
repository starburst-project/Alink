// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** Export the graph to the pyalink script. GET /api/v1/experiment/export_pyalink_script */
export async function exportPyAlinkScriptUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.exportPyAlinkScriptUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.ExportPyAlinkScriptResponse>('/api/v1/experiment/export_pyalink_script', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** Get the experiment. GET /api/v1/experiment/get */
export async function getExperimentUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getExperimentUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.GetExperimentResponse>('/api/v1/experiment/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** Get the entire graph. GET /api/v1/experiment/get_graph */
export async function getExperimentContentUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getExperimentContentUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.GetExperimentGraphResponse>('/api/v1/experiment/get_graph', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** Run the experiment. GET /api/v1/experiment/run */
export async function runExperimentUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.runExperimentUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BasicResponse>('/api/v1/experiment/run', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** Update the experiment. POST /api/v1/experiment/update */
export async function updateExperimentUsingPOST(
  body: API.UpdateExperimentRequest,
  options?: { [key: string]: any },
) {
  return request<API.BasicResponse>('/api/v1/experiment/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
