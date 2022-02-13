declare namespace API {
  type AddEdgeRequest = {
    dstNodeId: number;
    dstNodePort: number;
    experimentId?: number;
    srcNodeId: number;
    srcNodePort: number;
  };

  type AddEdgeResponse = {
    data?: AddEdgeResponseDataT;
    message?: string;
    status: string;
  };

  type AddEdgeResponseDataT = {
    id?: number;
  };

  type AddNodeRequest = {
    className: string;
    experimentId?: number;
    nodeName: string;
    nodeType: 'FUNCTION' | 'SINK' | 'SOURCE';
    positionX: number;
    positionY: number;
  };

  type AddNodeResponse = {
    data?: AddNodeResponseDataT;
    message?: string;
    status: string;
  };

  type AddNodeResponseDataT = {
    id?: number;
  };

  type BasicResponse = {
    message?: string;
    status: string;
  };

  type Edge = {
    dstNodeId?: number;
    dstNodePort?: number;
    experimentId?: number;
    gmtCreate?: Timestamp;
    gmtModified?: Timestamp;
    id?: number;
    srcNodeId?: number;
    srcNodePort?: number;
  };

  type Experiment = {
    config?: string;
    gmtCreate?: Timestamp;
    gmtModified?: Timestamp;
    id?: number;
    name?: string;
  };

  type ExportPyAlinkScriptResponse = {
    data?: ExportPyAlinkScriptResponseDataT;
    message?: string;
    status: string;
  };

  type ExportPyAlinkScriptResponseDataT = {
    lines?: string[];
  };

  type GetExperimentGraphResponse = {
    data?: GetExperimentGraphResponseDataT;
    message?: string;
    status: string;
  };

  type GetExperimentGraphResponseDataT = {
    edges?: Edge[];
    nodes?: Node[];
  };

  type GetExperimentResponse = {
    data?: GetExperimentResponseDataT;
    message?: string;
    status: string;
  };

  type GetExperimentResponseDataT = {
    experiment?: Experiment;
  };

  type GetNodeParamResponse = {
    data?: GetNodeParamResponseDataT;
    message?: string;
    status: string;
  };

  type GetNodeParamResponseDataT = {
    parameters?: NodeParam[];
  };

  type GetNodeResponse = {
    data?: GetNodeResponseDataT;
    message?: string;
    status: string;
  };

  type GetNodeResponseDataT = {
    node?: Node;
  };

  type Node = {
    className?: string;
    experimentId?: number;
    gmtCreate?: Timestamp;
    gmtModified?: Timestamp;
    id?: number;
    name?: string;
    positionX?: number;
    positionY?: number;
    type?: 'FUNCTION' | 'SINK' | 'SOURCE';
  };

  type NodeParam = {
    experimentId?: number;
    gmtCreate?: Timestamp;
    gmtModified?: Timestamp;
    id?: number;
    key?: string;
    nodeId?: number;
    value?: string;
  };

  type Timestamp = {
    date?: number;
    day?: number;
    hours?: number;
    minutes?: number;
    month?: number;
    nanos?: number;
    seconds?: number;
    time?: number;
    timezoneOffset?: number;
    year?: number;
  };

  type UpdateExperimentRequest = {
    config?: string;
    id?: number;
    name?: string;
  };

  type UpdateParamRequest = {
    experimentId?: number;
    nodeId: number;
    paramsToDel: string[];
    paramsToUpdate: Record<string, any>;
  };

  type deleteEdgeUsingGETParams = {
    /** experiment_id */
    experiment_id?: number;
    /** edge_id */
    edge_id: number;
  };

  type exportPyAlinkScriptUsingGETParams = {
    /** experiment_id */
    experiment_id?: number;
  };

  type getExperimentUsingGETParams = {
    /** experiment_id */
    experiment_id?: number;
  };

  type getExperimentContentUsingGETParams = {
    /** experiment_id */
    experiment_id?: number;
  };

  type runExperimentUsingGETParams = {
    /** experiment_id */
    experiment_id?: number;
  };

  type deleteNodeUsingGETParams = {
    /** experiment_id */
    experiment_id?: number;
    /** node_id */
    node_id: number;
  };

  type getNodeUsingGETParams = {
    /** experiment_id */
    experiment_id?: number;
    /** node_id */
    node_id: number;
  };

  type updateNodeUsingGETParams = {
    /** experiment_id */
    experiment_id?: number;
    /** node_id */
    node_id: number;
    /** name */
    name?: string;
    /** position_x */
    position_x?: number;
    /** position_y */
    position_y?: number;
  };

  type getNodeParamUsingGETParams = {
    /** experiment_id */
    experiment_id?: number;
    /** node_id */
    node_id: number;
  };
}
