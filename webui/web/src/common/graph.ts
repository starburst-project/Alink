import { getAlgoConfig } from "@/common/algo";
import { range } from "lodash-es";

const inPortPrefix = "in";
const outPortPrefix = "out";

export const convertNode = (node: any) => {
  const config = getAlgoConfig(node.className);
  node.inPorts = range(config.numInPorts).map((_, index) => ({
    sequence: index + 1,
    id: `${inPortPrefix}-${node.id}-${index}`,
  }));
  node.outPorts = range(config.numOutPorts).map((_, index) => ({
    sequence: index + 1,
    id: `${outPortPrefix}-${node.id}-${index}`,
  }));
  node.nodeInstanceId = node.id;
  return node;
};

export const convertEdge = (edge: any) => {
  edge.source = edge.srcNodeId;
  edge.outputPortId = `${outPortPrefix}-${edge.source}-${edge.srcNodePort}`;
  edge.target = edge.dstNodeId;
  edge.inputPortId = `${inPortPrefix}-${edge.target}-${edge.dstNodePort}`;
  return edge;
};

export const convertGraph = (graph: any) => {
  graph.nodes = graph.nodes.map((d: any) => convertNode(d));
  graph.links = graph.edges.map((d: any) => convertEdge(d));
  delete graph.edges;
  return graph;
};
