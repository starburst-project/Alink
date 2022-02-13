import React from "react";
import { RouteComponentProps } from "react-router";
import { DndProvider } from "react-dnd";
import { HTML5Backend } from "react-dnd-html5-backend";
import { ComponentTreePanel } from "./component-tree-panel";
import { ComponentConfigPanel } from "./component-config-panel";
import { DAGCanvas } from "./dag-canvas";

import styles from "./index.less";
import { PageContainer } from "@ant-design/pro-layout";

interface Props extends RouteComponentProps<{ experimentId: string }> {
  experimentId: string;
}

const DagDemo: React.FC<Props> = (props) => {
  const { experimentId = "1" } = props;

  return (
    <div className={styles.experiment}>
      <DndProvider backend={HTML5Backend}>
        <ComponentTreePanel className={styles.nodeSourceTree} />
        <div className={styles.editPanel}>
          <DAGCanvas
            experimentId={experimentId}
            className={styles.dagCanvas}
          />
          <ComponentConfigPanel
            experimentId={experimentId}
            className={styles.confPanel}
          />
        </div>
      </DndProvider>
    </div>
  );
};

export default DagDemo;
