DROP TABLE IF EXISTS gsms_message_reminder;
CREATE TABLE `gsms_message_reminder` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `enterprise_id` INT(11) NOT NULL  COMMENT '消息所属的企业',
  `message_title` VARCHAR(200) NOT NULL COMMENT '消息提醒显示标题',
  `object_id` VARCHAR(50) NOT NULL DEFAULT '0' COMMENT '消息类型关联对应的ID',
  `create_user` INT(11) NOT NULL DEFAULT '0' COMMENT '该条消息的创建的用户ID',
  `read_user` INT(11)  DEFAULT '0' COMMENT '读取该条消息的用户ID 当scope=1时需要填读取该消息的用户id',
  `message_type` INT(11) NOT NULL COMMENT '消息类型：1，短信发送通知; 2，彩信发送通知; 3，短信审核通知;4，彩信审核通知; 5，系统公告',
  `push_time` DATETIME NOT NULL COMMENT '推送时间，不同类型消息的新增或修改时间',
  `create_time` DATETIME NOT NULL COMMENT '该消息记录创建时间',
  `read_permission` INT(11)  COMMENT '读取权限 当scope为2时必填 1-短信发送一级审核 2-彩信发送一级审核',
  `scope` INT(11) NOT NULL COMMENT '权限域,0-不需要权限,系统公告,针对该企业全部用户 1-仅个人 2-具有某个操作权限的数据',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='消息提醒';

DROP TABLE IF EXISTS gsms_user_message_reminder;
CREATE TABLE `gsms_user_message_reminder` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` INT(11) NOT NULL COMMENT '用户id',
  `message_id` INT(11) NOT NULL COMMENT '消息id',
  `state` INT(11) NOT NULL DEFAULT 0 COMMENT '消息读取状态0-未读 1-已读',
  `is_remove` INT(11) NOT NULL DEFAULT 0 COMMENT '消息逻辑删除状态0-未删除 1-已删除,可恢复',
  PRIMARY KEY (`id`),
  KEY `ix_gsms_user_message_reminder_user_id` (`user_id`) USING BTREE,
  KEY `ix_gsms_user_message_reminder_message_id` (`message_id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='用户消息提醒';

DROP TABLE IF EXISTS gsms_contact_group_share_map;
CREATE TABLE `gsms_contact_group_share_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL COMMENT '共享组ID',
  `show_contact_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否允许查看联系人 0:不允许;1:允许',
  `share_child_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否共享子组 0: 否,1: 是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

