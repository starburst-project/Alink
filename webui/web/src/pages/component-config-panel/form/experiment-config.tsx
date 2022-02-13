import React, { useEffect } from "react";
import { Form, Input } from "antd";
import { useObservableState } from "@/common/hooks/useObservableState";
import { useExperimentGraph } from "@/pages/rx-models/experiment-graph";
import { getExperimentReq, updateExperimentReq } from "@/requests/graph";
import { useRequest } from "umi";
import { getExperimentUsingGET } from "@/services/alink-web/experimentController";

export interface Props {
  name: string;
  experimentId: string;
}

export const ExperimentForm: React.FC<Props> = ({ experimentId, name }) => {
  const [form] = Form.useForm();

  const expGraph = useExperimentGraph(experimentId);
  const [activeExperiment] = useObservableState(expGraph.experiment$);

  const onValuesChange = (changes: any) => {
    if ("parallelism" in changes) {
      updateExperimentReq(
        JSON.stringify({
          parallelism: changes["parallelism"] || "2",
        })
      );
    }
  };

  const {data} = useRequest(getExperimentUsingGET);

  useEffect(() => {
    const configStr = data?.config;

    const config = configStr ? JSON.parse(configStr) : null;

    if (config && "parallelim" in config) {
      form.setFieldsValue({
        parallelism: config.parallelism
      });
    }
  }, [activeExperiment]);

  return (
    <Form
      form={form}
      layout="vertical"
      onValuesChange={onValuesChange}
      requiredMark={false}
    >
      <Form.Item name="parallelism" label="并发度">
        <Input placeholder="2" />
      </Form.Item>
    </Form>
  );
};
