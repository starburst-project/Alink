import React, { useEffect } from "react";
import { Form, Input } from "antd";
import { useObservableState } from "@/common/hooks/useObservableState";
import { useExperimentGraph } from "@/pages/rx-models/experiment-graph";
import { useRequest } from "umi";
import { getExperimentUsingGET, updateExperimentUsingPOST } from "@/services/alink-web/experimentController";

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
      updateExperimentUsingPOST({
        config: JSON.stringify({
          parallelism: changes["parallelism"] || "2",
        })
      })
    }
  };

  const {data} = useRequest(getExperimentUsingGET);

  useEffect(() => {
    const config = JSON.parse(data?.experiment?.config ?? "{}");

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
