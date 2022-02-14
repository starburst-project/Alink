import React, { useEffect } from "react";
import { Form, Input } from "antd";
import { getAlgoConfig } from "@/common/algo";
import { getNodeParamUsingGET, updateParamUsingPOST } from "@/services/alink-web/nodeParamController";

export interface Props {
  node: { id: number; className: string };
}

export const NodeParamForm: React.FC<Props> = ({ node }: Props) => {
  const [form] = Form.useForm();

  const onValuesChange = (data: any) => {
    updateParamUsingPOST({
      nodeId: node.id,
      paramsToUpdate: data,
      paramsToDel: []
    })
  };

  useEffect(() => {
    if (node?.id) {
      getNodeParamUsingGET({
        node_id: node.id
      }).then((response) => {
        let paramMap: { [name: string]: string | null | undefined } = {};
        for (const { key, value } of response?.data?.parameters ?? []) {
          if (key) {
            paramMap[key] = value;
          }
        }
        form.setFieldsValue(paramMap);
      })
    }
  }, [node]);

  const config = getAlgoConfig(node.className);
  const paramNames: string[] = config.params;

  return (
    <Form form={form} layout="vertical" onValuesChange={onValuesChange}>
      {paramNames.map((name) => (
        <Form.Item key={name} name={name} label={name}>
          <Input placeholder="NOT SET" />
        </Form.Item>
      ))}
    </Form>
  );
};
