DROP TABLE IF EXISTS machine_info;

CREATE TABLE machine_info
(
    `id`         bigint   NOT NULL AUTO_INCREMENT comment 'id',
    `ip`         varchar(1024) comment '机器信息',
    `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `comments`   text COMMENT '注释',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='获取机器唯一标识';

DROP TABLE IF EXISTS experiment;

CREATE TABLE experiment
(
    `experiment_id` bigint       NOT NULL COMMENT '实验id',
    `name`          varchar(128) NOT NULL comment '实验名',
    `gmt_create`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `config`        JSON         NOT NULL COMMENT '实验配置',
    PRIMARY KEY (`experiment_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='实验信息';

DROP TABLE IF EXISTS edge;

CREATE TABLE edge
(
    `edge_id`       bigint   NOT NULL COMMENT '边id',
    `gmt_create`    datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `experiment_id` bigint   NOT NULL COMMENT '实验id',
    `src_node_id`   bigint COMMENT '源组件id',
    `src_node_port` smallint COMMENT '源组件端口',
    `dst_node_id`   bigint COMMENT '目标组件id',
    `dst_node_port` smallint COMMENT '目标组件端口',
    PRIMARY KEY (`edge_id`),
    INDEX `experiment_id_index` (`experiment_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='实验边信息';

DROP TABLE IF EXISTS node;

CREATE TABLE node
(
    `node_id`       bigint       NOT NULL COMMENT '节点id',
    `gmt_create`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `experiment_id` bigint       NOT NULL COMMENT '实验id',
    `type`          smallint     NOT NULL comment '节点类型',
    `name`          varchar(128) NOT NULL comment '节点显示名字',
    `position_x`    double       NOT NULL comment '节点位置x坐标',
    `position_y`    double       NOT NULL comment '节点位置y坐标',
    `class_name`    varchar(128) NOT NULL comment '节点算法名字',
    PRIMARY KEY (`node_id`),
    INDEX `experiment_id_index` (`experiment_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='实验节点信息';

DROP TABLE IF EXISTS node_param;

CREATE TABLE node_param
(
    `node_param_id` bigint       NOT NULL COMMENT '节点参数信息id',
    `gmt_create`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `experiment_id` bigint       NOT NULL COMMENT '实验id',
    `node_id`       bigint       NOT NULL COMMENT '节点id',
    `key`           varchar(128) NOT NULL comment '参数key',
    `value`         varchar(128) comment '参数值',
    PRIMARY KEY (`node_param_id`),
    INDEX `experiment_id_index` (`experiment_id`),
    INDEX `node_id_index` (`node_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='节点参数信息';

