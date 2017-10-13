alter table gsms_announcement add column update_time datetime;


ALTER TABLE `gsms_phrase`
ADD COLUMN `phrase_type`  int(11) NULL COMMENT '用于区分“普通模板（0）”与“变量模板”（1）' AFTER `templatetype`;


ALTER TABLE `gsms_user_task`
ADD COLUMN `data_type`  int(1) NULL COMMENT '数据类型:用户,联系人,通讯录（前台导入导出任务类型识别）' AFTER `is_read`;

ALTER TABLE `gsms_user_task`
ADD COLUMN `task_type`  int(1) NULL DEFAULT 0 COMMENT '用于区分该任务被data-server（0），或前台处理（1），默认0' AFTER `data_type`;

#修改任务表添加一个是否读取状态
alter table gsms_user_task add column `is_read` BIT(1) NOT NULL DEFAULT 0 COMMENT '完成任务读取状态0-未读 1-已读';

#修改gsms_permission表增加一个临时过渡的字段,用于区分新旧mos前台权限
alter table gsms_permission add column `share_flag` int(11) not null default 0 comment '新旧平台权限标识0:新旧系统共用 1:旧系统独有 2:新系统独有';

ALTER TABLE `gsms_user_task`
MODIFY COLUMN `file_size`  bigint(11) NULL DEFAULT NULL COMMENT '文件大小' AFTER `file_address`;

#修改gsms_user_task 表file_name 字段长度由原来的128位改成256位

ALTER TABLE `gsms_user_task`
MODIFY COLUMN  `file_name` varchar(256) NULL DEFAULT NULL COMMENT '处理产生文件名' AFTER `upload_file_address`;