#修改部分数据的is_menu字段
update gsms_permission set is_menu = 1 where id in (13100,13200,13400,18400);


#修改部分数据的parent_id字段
update gsms_permission set parent_id = 18400 where id in (18430,18440,18450);

#新增[审核短信]3条权限
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('11400', '审核短信', '短信管理', 'SmsMgr', 'SendPending', 'Index', '1', '11000', '', NULL, '审核短信', '11400', '\0', '', NULL, '0', '2', '2', '\0', '~/SmsMgr/SendTracking/SendPending', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('11410', '查询', '审核短信', 'SmsMgr', 'SendPending', 'LoadWaitBatchs', '1', '11400', '\0', NULL, '查询', '11410', '\0', '', NULL, '0', '1,2,3', '3', '\0', '~/SmsMgr/SendTracking/LoadWaitBatchs', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('11420', '审批', '审核短信', 'SmsMgr', 'SendPending', 'CheckBatch', '1', '11400', '\0', NULL, '审批', '11420', '\0', '', NULL, '0', '2,3', '3', '\0', '~/SmsMgr/SendTracking/CheckBatch', '', '2', NULL, '2');

#[审核短信]对应原mos的[发送记录->待处理,现在把[待处理]的3条权限(待处理,查询,审批)的share_flag改为1
update gsms_permission set share_flag = 1 where id = 11310;
update gsms_permission set share_flag = 1 where id = 11311;
update gsms_permission set share_flag = 1 where id = 11312;


#将企业通讯录归入旧mos功能
update gsms_permission set share_flag=1 where id=12200;
update gsms_permission set share_flag=1 where id=12210;
update gsms_permission set share_flag=1 where id=12211;
update gsms_permission set share_flag=1 where id=12212;
update gsms_permission set share_flag=1 where id=12213;
update gsms_permission set share_flag=1 where id=12214;
update gsms_permission set share_flag=1 where id=12215;
update gsms_permission set share_flag=1 where id=12216;
update gsms_permission set share_flag=1 where id=12217;
update gsms_permission set share_flag=1 where id=12218;
update gsms_permission set share_flag=1 where id=12219;

UPDATE gsms_permission SET share_flag=1 WHERE id = 13100; -- 部门总量
UPDATE gsms_permission SET share_flag=1 WHERE id = 13110; -- 部门总量统计
UPDATE gsms_permission SET share_flag=1 WHERE id = 13111; -- 部门总量统计查询
UPDATE gsms_permission SET share_flag=1 WHERE id = 13112; -- 部门总量统计导出
UPDATE gsms_permission SET share_flag=1 WHERE id = 13120; -- 用户总量统计
UPDATE gsms_permission SET share_flag=1 WHERE id = 13121; -- 查询
UPDATE gsms_permission SET share_flag=1 WHERE id = 13122; -- 导出
UPDATE gsms_permission SET share_flag=1 WHERE id = 13130; -- 通道总量统计
UPDATE gsms_permission SET share_flag=1 WHERE id = 13131; -- 查询
UPDATE gsms_permission SET share_flag=1 WHERE id = 13132; -- 导出
UPDATE gsms_permission SET share_flag=1 WHERE id = 13140; -- 业务类型总量统计
UPDATE gsms_permission SET share_flag=1 WHERE id = 13141; -- 查询
UPDATE gsms_permission SET share_flag=1 WHERE id = 13142; -- 导出
UPDATE gsms_permission SET share_flag=1 WHERE id = 13200; -- 趋势统计
UPDATE gsms_permission SET share_flag=1 WHERE id = 13210; -- 部门趋势统计
UPDATE gsms_permission SET share_flag=1 WHERE id = 13211; -- 查询
UPDATE gsms_permission SET share_flag=1 WHERE id = 13212; -- 导出
UPDATE gsms_permission SET share_flag=1 WHERE id = 13220; -- 用户趋势统计
UPDATE gsms_permission SET share_flag=1 WHERE id = 13221; -- 查询
UPDATE gsms_permission SET share_flag=1 WHERE id = 13222; -- 导出
UPDATE gsms_permission SET share_flag=1 WHERE id = 13230; -- 通道趋势统计
UPDATE gsms_permission SET share_flag=1 WHERE id = 13231; -- 查询
UPDATE gsms_permission SET share_flag=1 WHERE id = 13232; -- 导出
UPDATE gsms_permission SET share_flag=1 WHERE id = 13240; -- 业务类型趋势统计
UPDATE gsms_permission SET share_flag=1 WHERE id = 13241; -- 查询
UPDATE gsms_permission SET share_flag=1 WHERE id = 13242; -- 导出

UPDATE gsms_permission SET share_flag = 1 WHERE id = 18200;

#通道分配归入旧mos功能
UPDATE gsms_permission SET share_flag=1 WHERE id=14000;

#新增共享通讯录为新mos功能
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
 VALUES ('12300', '共享通讯录', '共享通讯录', 'ContactMgr', 'EContact', 'Index', '1', '12000', '', NULL, '共享通讯录', '12300', '', '\0', NULL, '0', '3', '3', '\0', '~/ContactMgr/EContact/Index', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
 VALUES ('12310', '共享通讯录', '共享通讯录', 'ContactMgr', 'EContact', 'Index', '1', '12300', '\0', '~/Content/Images/toolB/contacts.png', '共享通讯录', '12310', '', '', '', '0', '3', '2', '\0', '~/ContactMgr/EContact/Index', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
 VALUES ('12311', '查询共享联系人', '共享通讯录', 'ContactMgr', 'EContact', 'Index', '1', '12310', '\0', NULL, '查询共享联系人', '12311', '', '', NULL, '0', '3', '3', '\0', '~/ContactMgr/EContact/Index', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
 VALUES ('12312', '添加共享通讯录', '共享通讯录', 'ContactMgr', 'EContact', 'AddContactGroup', '1', '12310', '\0', NULL, '添加共享通讯录', '12312', '', '', NULL, '0', '3', '3', '\0', '~/ContactMgr/EContact/AddContactGroup', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
 VALUES ('12313', '修改共享通讯录', '共享通讯录', 'ContactMgr', 'EContact', 'UpdateContactGroup', '1', '12310', '\0', NULL, '修改共享通讯录', '12313', '', '', NULL, '0', '3', '3', '\0', '~/ContactMgr/EContact/UpdateContactGroup', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
 VALUES ('12314', '删除共享通讯录', '共享通讯录', 'ContactMgr', 'EContact', 'DeleteContactGroup', '1', '12310', '\0', NULL, '删除共享通讯录', '12314', '', '', NULL, '0', '3', '3', '\0', '~/ContactMgr/EContact/DeleteContactGroup', '', '2', NULL, '2');
 
 
 #--------- 增加新MOS统计报表部门统计、 用户统计、业务类型统计相关权限点
insert into `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) values('13101','部门统计','部门统计','Statistics','SumStatistics','SumDepartment','1','13000','','','部门统计','13101','','','','0','2,3','2','','/Statistics/SumStatistics/SumDepartment','','2',NULL,'2');
insert into `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) values('13102','用户统计','用户统计','Statistics','SumStatistics','SumAccount','1','13000','','','用户统计','13102','','','','0','1,2,3','2','','/Statistics/SumStatistics/SumAccount','','2',NULL,'2');
insert into `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) values('13103','业务类型统计','业务类型统计','Statistics','SumStatistics','SumBusinessType','1','13000','','','业务类型统计','13103','','','','0','3','2','','/Statistics/SumStatistics/SumBusinessType','','2',NULL,'2');
#----------- 部门统计查询、导出
insert into `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) values('13105','查询','查询','Statistics','SumStatistics','GetSumDepartment','1','13101','','','查询','13105','','','','0','3','3','','/Statistics/SumStatistics/GetSumDepartment','','2',NULL,'2');
insert into `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) values('13106','导出','导出','Statistics','SumStatistics','ExportDepartment','1','13101','','','导出','13106','','','','0','3','3','','/Statistics/SumStatistics/ExportDepartment','','2',NULL,'2');
#------------ 用户统计查询、导出、统计详情
insert into `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) values('13107','查询','查询','Statistics','SumStatistics','GetSumAccount','1','13102','','','查询','13107','','','','0','3','3','','/Statistics/SumStatistics/GetSumAccount','','2',NULL,'2');
insert into `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) values('13108','导出','导出','Statistics','SumStatistics','ExportAccount','1','13102','','','导出','13108','','','','0','3','3','','/Statistics/SumStatistics/ExportAccount','','2',NULL,'2');

insert into `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) values('13114','统计详情','统计详情','Statistics','SumStatistics','singleUserList','1','13102','','','统计详情','13114','','','','0','3','3','','/Statistics/SumStatistics/singleUserList','','2',NULL,'2');
#----------- 业务类型查询、导出
insert into `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) values('13109','查询','查询','Statistics','SumStatistics','GetSumBusinessType','1','13103','','','查询','13109','','','','0','3','3','','/Statistics/SumStatistics/GetSumBusinessType','','2',NULL,'2');
insert into `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) values('13113','导出','导出','Statistics','SumStatistics','ExportBusinessType','1','13103','','','导出','13113','','','','0','3','3','','/Statistics/SumStatistics/ExportBusinessType','','2',NULL,'2');
#------------- 计费帐户统计计费详情
insert into `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) values('13413','计费详情','计费详情','Statistics','BillingAccountStatistics','detail','1','13410','','','计费详情','13413','','','','0','3','3','','/Statistics/BillingAccountStatistics/detail','','2',NULL,'2');

#-------------- 部门统计依赖权限
INSERT INTO `gsms_permission_depends`(`pri_id`,`sub_id`,`islevel`) VALUES(13106,13105,0);
INSERT INTO `gsms_permission_depends`(`pri_id`,`sub_id`,`islevel`) VALUES(13108,13107,0);
INSERT INTO `gsms_permission_depends`(`pri_id`,`sub_id`,`islevel`) VALUES(13114,13107,0);
INSERT INTO `gsms_permission_depends`(`pri_id`,`sub_id`,`islevel`) VALUES(13113,13109,0);

#--------- 增加新MOS首页以及个人信息栏、报表统计栏权限点
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES('10000','平台首页','平台首页','Home','Index','Index','1','0','','','平台首页','10000','','','','0','3','1','','/Home/Index/Index','','2',NULL,'2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`)
VALUES('10100','个人信息栏','个人信息栏','Home','Index','PersonInfo','1','10000','','','首页','10100','','','','0','3','2','','/Home/Index/PersonInfo','','2',NULL,'2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`)
VALUES('10200','报表统计栏','报表统计栏','Home','Index','Statistics','1','10000','','','首页','10200','','','','0','3','2','','/Home/Index/Statistics','','2',NULL,'2');


# 插入彩信管理-审核彩信-权限三条(仅用于新mos(java))
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('17600', '审核彩信', '彩信管理', 'SmsMgr', 'SendPendingMms', 'Index', '1', '17000', b'1', NULL, '审核彩信', '11420', b'1', b'1', NULL, '0', '2', '2', b'0', '~/SmsMgr/SendPendingMms/Index', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('17610', '查询', '审核彩信', 'SmsMgr', 'SendPendingMms', 'LoadWaitBatchs', '1', '17600', b'0', NULL, '查询', '11421', b'1', b'1', NULL, '0', '1,2,3', '3', b'0', '~/SmsMgr/SendPendingMms/LoadWaitBatchs', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('17620', '审批', '审核彩信', 'SmsMgr', 'SendPendingMms', 'CheckBatch', '1', '17600', b'0', NULL, '审批', '11421', b'1', b'1', NULL, '0', '2,3', '3', b'0', '~/SmsMgr/SendPendingMms/CheckBatch', '', '2', NULL, '2');

# 更新三条彩信管理的审核彩信相关（仅用于旧mos(C#)）
update gsms_permission set share_flag = 1 where id in(17310,17311,17312);

# 更新三条彩信管理的素材管理相关（仅用于旧mos(C#)）
update gsms_permission set share_flag = 1 where id in(16000,16100,16110,16111,16112,16113,16200,16210,16211,16212,16213,16300,16310,16311,16312,16313,16400,16410,16411,16412,16413,16500,16510,16511);

# 新增彩信审核记录查询权限到role表中
INSERT into gsms_role_permission VALUES (224,17610,9);
INSERT into gsms_role_permission VALUES (65,17610,3);
INSERT into gsms_role_permission VALUES (167,17610,3);
# 新增彩信审核记录审核到role表中
INSERT into gsms_role_permission VALUES (224,17620,9);
INSERT into gsms_role_permission VALUES (65,17620,3);
INSERT into gsms_role_permission VALUES (167,17620,3);
# 权限依赖表
INSERT into gsms_permission_depends VALUES(17620,17610,0);

# 新增短信审核记录查询权限到role_permission表中
INSERT into gsms_role_permission VALUES (61,11420,1);
INSERT into gsms_role_permission VALUES (223,11420,3);
INSERT into gsms_role_permission VALUES (167,11420,3);
# 新增短信审核记录审核权限到role_permission表中
INSERT into gsms_role_permission VALUES (223,11410,3);
INSERT into gsms_role_permission VALUES (167,11410,3);
# 权限依赖表-短信审核
INSERT into gsms_permission_depends VALUES(11420,11410,0);



# 新增短信审核记录查询权限到role_permission表中
INSERT into gsms_role_permission VALUES (61,11420,1);
INSERT into gsms_role_permission VALUES (223,11420,3);
INSERT into gsms_role_permission VALUES (167,11420,3);



# 新增共享通讯录权限到role表
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('101', '12300', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('101', '12310', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('101', '12311', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('101', '12312', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('101', '12313', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('101', '12314', '3');

# 新增默认角色绑定的权限
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('102', '10100', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('102', '10000', '9');

# 更新接收彩信权限
UPDATE gsms_permission SET share_flag = 1 WHERE id IN(17200,17210,17211,17212,17213,17214);

# 更新企业白名单权限
UPDATE gsms_permission SET display_name = '企业白名单', menu_display_name = '企业白名单' where id = 26953;
UPDATE gsms_permission SET display_name = '企业白名单', menu_display_name = '企业白名单' where id = 26954;
UPDATE gsms_permission SET share_flag = 1 where id = 26957;

# 更新黑名单权限
UPDATE gsms_permission SET share_flag = 1 where id = 15113;

# 更新计费账户
UPDATE gsms_permission SET display_name = '计费账户', operation_obj = '计费账户', menu_display_name = '计费账户', data_scopes = '3' WHERE id = 18910;
UPDATE gsms_permission SET display_name = '查询', operation_obj = '计费账户', menu_display_name = '查询', data_scopes = '3' WHERE id = 18911;
UPDATE gsms_permission SET display_name = '查看', operation_obj = '计费账户', menu_display_name = '查看', data_scopes = '3' WHERE id = 18912;
UPDATE gsms_permission SET display_name = '新增', operation_obj = '计费账户', menu_display_name = '新增', data_scopes = '3' WHERE id = 18913;
UPDATE gsms_permission SET display_name = '修改', operation_obj = '计费账户', menu_display_name = '修改', data_scopes = '3' WHERE id = 18914;
UPDATE gsms_permission SET display_name = '删除', operation_obj = '计费账户', menu_display_name = '删除', data_scopes = '3' WHERE id = 18915;
UPDATE gsms_permission SET display_name = '充值', operation_obj = '计费账户', menu_display_name = '充值', data_scopes = '3' WHERE id = 18916;
UPDATE gsms_permission SET operation_obj = '计费账户', data_scopes = '3' WHERE id = 18917;
UPDATE gsms_permission SET share_flag = 1 WHERE id = 18918;
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('18919', '导入充值', '计费账户', 'SystemMgr', 'ChargingAccountMgr', 'ImportingCharging', '1', '18910', '\0', NULL, '导入充值', '18919', '\0', '', NULL, '1', '3', '3', '\0', '~/SystemMgr/ChargingAccountMgr/ImportingCharging', '', '2', NULL, '2');

# 更新充值记录
UPDATE gsms_permission SET display_name = '充值记录', operation_obj = '计费账户', menu_display_name = '充值记录', data_scopes = '3' WHERE id = 18920;
UPDATE gsms_permission SET display_name = '查询', operation_obj = '计费账户', menu_display_name = '查询', data_scopes = '3' WHERE id = 18921;
UPDATE gsms_permission SET display_name = '导出', operation_obj = '计费账户', menu_display_name = '导出', data_scopes = '3' WHERE id = 26930;
UPDATE gsms_permission SET display_name = '自动充值', operation_obj = '计费账户', menu_display_name = '自动充值', data_scopes = '3' WHERE id = 18923;
UPDATE gsms_permission SET share_flag = 1 WHERE id IN(18922);

# 更新用户管理
UPDATE gsms_permission SET display_name = '新增', operation_obj = '用户管理', menu_display_name = '新增', data_scopes = '3' WHERE id = 18111;

# 更新部门管理
UPDATE gsms_permission SET display_name = '新增' WHERE id = 18211;
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('18214', '查询', '部门管理', 'SystemMgr', 'AccountMgr', 'DeptList', '1', '18210', '\0', NULL, '查询', '18214', '', '', NULL, '0', '3', '3', '\0', '~/SystemMgr/AccountMgr/DeptList', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('18215', '查看', '部门管理', 'SystemMgr', 'AccountMgr', 'DeptDetail', '1', '18210', '\0', NULL, '查看', '18215', '', '', NULL, '0', '3', '3', '\0', '~/SystemMgr/AccountMgr/DeptDetail', '', '2', NULL, '2');

# 更新角色管理
UPDATE gsms_permission SET display_name = '新增', menu_display_name = '新增', data_scopes = '3' WHERE id = 18311;

# 系统配置
UPDATE gsms_permission SET display_name = '企业信息', operation_obj = '系统配置', menu_display_name = '企业信息', data_scopes = '3' WHERE id = 18410;
UPDATE gsms_permission SET display_name = '参数配置', operation_obj = '系统配置', menu_display_name = '参数配置', data_scopes = '3' WHERE id = 18430;
UPDATE gsms_permission SET share_flag = 1 WHERE id IN(18411,18412,18440,18450);

# 日志记录
UPDATE gsms_permission SET display_name = '日志记录', operation_obj = '日志记录', menu_display_name = '日志记录', data_scopes = '3' WHERE id = 18500;
UPDATE gsms_permission SET display_name = '日志记录', operation_obj = '日志记录', menu_display_name = '日志记录', data_scopes = '3' WHERE id = 18510;
UPDATE gsms_permission SET operation_obj = '日志记录', data_scopes = '3' WHERE id = 18511;

# 新增计费账户角色权限表
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('53', '18919', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '18919', '3');

# 更新充值记录角色权限表
UPDATE `gsms_role_permission` SET `data_scope`='3' WHERE role_id = 53 and permission_id in(18920, 18921, 18923, 26930);

# 新增部门管理角色权限表
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('53', '18214', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('53', '18215', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '18211', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '18212', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '18213', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '18214', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '18215', '3');

# 新增用户管理角色权限表
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('53', '18111', '3');

# 新增系统配置角色权限表
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '18410', '3');

# 更新角色管理角色权限表
UPDATE `gsms_role_permission` SET `data_scope`='3' WHERE role_id = 167 and permission_id in(18310, 18311, 18312, 18313, 18314);

# 更新日志记录角色权限表
UPDATE `gsms_role_permission` SET `data_scope`='3' WHERE role_id = 167 and permission_id in(18510, 18511);

# 更新公告管理角色权限表
UPDATE `gsms_role_permission` SET `data_scope`='3' WHERE role_id = 167 and permission_id in(18710, 18711, 18712, 18713, 18714);

# 新增公告管理角色权限表
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('227', '18711', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('227', '18712', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('227', '18713', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('227', '18714', '3');

# 新增计费账户权限依赖数据
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('18919', '18910', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('18912', '18911', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('18913', '18911', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('18914', '18911', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('18915', '18911', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('18916', '18911', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('18917', '18911', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('18919', '18911', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('18923', '18920', '0');

# 新增部门管理权限依赖数据
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('18214', '18210', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('18215', '18210', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('18211', '18214', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('18212', '18214', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('18213', '18214', '0');

# 更新个人管理权限
UPDATE gsms_permission SET share_flag = 1 WHERE id IN(18600, 18610, 18611);

# 更新任务管理权限
UPDATE gsms_permission SET share_flag = 1 WHERE id IN(19100, 19110, 19111, 19112, 19113, 19114, 19120, 19121, 19122, 19130, 19131, 19132, 27200, 27210, 27220);
# 将国际短信归入旧mos特有功能
UPDATE gsms_permission set share_flag=1 where id in (11120,11121,11122);
# 新增短信管理，接受记录权限
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('11500', '接受记录', '短信管理', 'SmsMgr', 'Inbox', 'Index', '1', '11000', '', NULL, '接收短信', '11500', '', '\0', NULL, '0', '3', '0', '\0', '~/SmsMgr/Inbox/Index', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('11510', '接收短信', '接收短信', 'SmsMgr', 'Inbox', 'Index', '1', '11500', '\0', '~/Content/Images/toolB/box.png', '接收短信', '11210', '', '', '', '0', '1,2,3', '2', '', '~/SmsMgr/Inbox/Index', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('11511', '查询', '接收短信', 'SmsMgr', 'Inbox', 'GetSMSlist', '1', '11510', '\0', '', '查询', '11511', '', '', NULL, '0', '9', '3', '\0', '~/SmsMgr/Inbox/GetSMSlist', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('11512', '导出', '接收短信', 'SmsMgr', 'Inbox', 'ExportInbox', '1', '11510', '\0', NULL, '导出', '11512', '', '', NULL, '0', '9', '3', '\0', '~/SmsMgr/Inbox/ExportInbox', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('11513', '切换获取状态', '接收短信', 'SmsMgr', 'Inbox', 'UpdateInbox', '1', '11510', '\0', NULL, '切换获取状态', '11513', '', '\0', NULL, '0', '9', '3', '\0', '~/SmsMgr/Inbox/UpdateInbox', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('11514', '回复', '接收短信', 'SmsMgr', 'Inbox', 'ReplySMS', '1', '11510', '\0', NULL, '回复', '11514', '', '', NULL, '0', '9', '3', '\0', '~/SmsMgr/Inbox/ReplySMS', '', '2', NULL, '2');
UPDATE gsms_permission set is_menu=0 where id=11500;

# 将接受短信归入旧mos特有功能
UPDATE gsms_permission set share_flag=1 where id in (11200,11210,11211,11212,11213,11214);


# 新增接受记录到角色权限关联表
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('101', '11500', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('101', '11510', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('101', '11511', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('101', '11512', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('101', '11513', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('101', '11514', '3');

#----- 给代理商赋报表统计和首页的权限
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(53,10000,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(53,10100,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(53,10200,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(53,13101,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(53,13102,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(53,13103,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(53,13105,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(53,13106,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(53,13107,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(53,13108,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(53,13109,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(53,13113,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(53,13114,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(53,13413,3);
#------- 给超级管理员赋报表统计和首页的权限
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(101,10000,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(101,10100,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(101,10200,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(101,13101,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(101,13102,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(101,13103,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(101,13105,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(101,13106,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(101,13107,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(101,13108,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(101,13109,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(101,13113,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(101,13114,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(101,13413,3);

#------- 给默认角色赋首页的权限
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(102,10000,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(102,10100,3);

#------- 给接收短信赋首页的权限
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(163,10000,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(163,10100,3);
#------- 给部门报表统计角色赋统计和首页的权限
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(164,10000,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(164,10100,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(164,10200,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(164,13101,2);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(164,13102,2);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(164,13105,2);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(164,13106,2);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(164,13107,2);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(164,13108,2);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(164,13114,2);
#------- 给系统管理员赋统计和首页的权限
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(167,10000,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(167,10100,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(167,10200,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(167,13101,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(167,13102,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(167,13103,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(167,13105,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(167,13106,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(167,13107,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(167,13108,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(167,13109,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(167,13113,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(167,13114,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(167,13413,3);
#------- 给财务人员赋统计和首页的权限
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(168,10000,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(168,10100,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(168,10200,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(168,13101,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(168,13102,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(168,13103,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(168,13105,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(168,13106,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(168,13107,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(168,13108,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(168,13109,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(168,13113,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(168,13114,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(168,13413,3);
#------- 给接收短信个人角色赋首页的权限
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(170,10000,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(170,10100,3);
#------- 给接收短信全局角色赋首页的权限
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(171,10000,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(171,10100,3);
#------- 给短信审核角色赋首页的权限
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(223,10000,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(223,10100,3);
#------- 给彩信审核角色赋首页的权限
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(224,10000,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(224,10100,3);

#------- 给维护人员角色赋首页的权限
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(227,10000,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(227,10100,3);

#------- 给审批人员角色赋首页的权限
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(230,10000,3);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(230,10100,3);
#------- 给发送短信(免审)角色赋首页的权限
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(228,10000,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(228,10100,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(228,10200,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(228,13102,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(228,13107,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(228,13108,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(228,13114,1);
#------- 给发送短信(非免审)角色赋首页的权限
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(229,10000,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(229,10100,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(229,10200,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(229,13102,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(229,13107,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(229,13108,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(229,13114,1);
#------- 给发送彩信(免审)角色赋首页的权限
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(225,10000,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(225,10100,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(225,10200,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(225,13102,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(225,13107,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(225,13108,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(225,13114,1);
#------- 给发送彩信(非免审)角色赋首页的权限
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(226,10000,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(226,10100,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(226,10200,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(226,13102,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(226,13107,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(226,13108,1);
INSERT INTO gsms_role_permission(role_id,permission_id,data_scope) VALUE(226,13114,1);

# 修改初始化角色类型
UPDATE gsms_role SET role_type = 1 WHERE platform_id = 2 AND id IN(102, 163, 164, 167, 168, 223, 224, 225, 226, 227, 228, 229, 230, 216, 217, 221);

# 修改审核短信排序
UPDATE gsms_permission set menu_display_order=11200 where id=11400;

# 新增系统配置权限
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('27300', '系统配置', '系统管理', 'SystemMgr', 'SysConfig', 'Index', '1', '18000', '', '', '系统管理', 
'27300', '', '\0', '', '0', '3', '2', '\0', '~/SystemMgr/SysConfig/Index', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('27310', '企业信息', '系统配置', 'SystemMgr', 'SysConfig', 'Enterprise', '1', '27300', '\0', '', '企业信息', 
'27310', '', '', '', '0', '3', '2', '\0', '~/SystemMgr/SysConfig/Enterprise', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('27320', '参数设置', '系统配置', 'SystemMgr', 'SysConfig', 'ParameterConfig', '1', '27300', '\0', '', '参数配置', 
'27320', '', '', '', '0', '3', '2', '\0', '~/SystemMgr/SysConfig/ParameterConfig', '', '2', NULL, '2');

# 更新系统配置为旧mos使用
UPDATE gsms_permission SET share_flag = 1 WHERE id IN(18400,18410,18430);

INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('27310', '27300', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('27320', '27300', '0');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '27300', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '27310', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '27320', '3');

# 系统管理员默认的角色权限添加[补]
insert into gsms_role_permission(role_id,permission_id,data_scope) VALUES(167,11500,3);
insert into gsms_role_permission(role_id,permission_id,data_scope) VALUES(167,11510,3);
insert into gsms_role_permission(role_id,permission_id,data_scope) VALUES(167,11511,3);
insert into gsms_role_permission(role_id,permission_id,data_scope) VALUES(167,11512,3);
insert into gsms_role_permission(role_id,permission_id,data_scope) VALUES(167,11513,3);
insert into gsms_role_permission(role_id,permission_id,data_scope) VALUES(167,11514,3);
insert into gsms_role_permission(role_id,permission_id,data_scope) VALUES(167,12300,3);
insert into gsms_role_permission(role_id,permission_id,data_scope) VALUES(167,12310,3);
insert into gsms_role_permission(role_id,permission_id,data_scope) VALUES(167,12311,3);
insert into gsms_role_permission(role_id,permission_id,data_scope) VALUES(167,12312,3);
insert into gsms_role_permission(role_id,permission_id,data_scope) VALUES(167,12313,3);
insert into gsms_role_permission(role_id,permission_id,data_scope) VALUES(167,12314,3);

--  =========================================短信管理========================================================= --

# 将发送短信权限设置为旧mos权限
UPDATE gsms_permission SET share_flag = 1 WHERE id IN(11110, 11111);

# 将短信发送导出权限设置为旧mos权限
UPDATE gsms_permission SET share_flag = 1 WHERE id IN(26966, 26967);

# 将短信模板管理权限设置为旧mos权限
UPDATE gsms_permission SET share_flag = 1 WHERE id IN(26970, 26971, 26972, 26973, 26974, 26975);


# 新增发送普通短信与发送变量短信相关权限
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11130', '发送普通短信', '短信发送', 'SmsMgr', 'SendSms', 'CommonSms', '1', '11100', '\0', '', '发送普通短信', '11130', 
'', '', NULL, '0', '9', '2', '', '~/SmsMgr/SendSms/CommonSms', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11131', '发送', '发送普通短信', 'SmsMgr', 'SendSms', 'CommonSmsSend', '1', '11130', '\0', '', '发送', '11131', 
'', '', '', '0', '9', '3', '\0', '~/SmsMgr/SendSms/CommonSmsSend', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11132', '发送免审', '发送普通短信', 'SmsMgr', 'SendSms', 'CommonSmsSendFreeAudit', '1', '11130', '\0', '', '发送免审', '11132', 
'', '', '', '0', '9', '3', '\0', '~/SmsMgr/SendSms/CommonSmsSendFreeAudit', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11140', '发送变量短信', '短信发送', 'SmsMgr', 'SendSms', 'VariantSms', '1', '11100', '\0', '', '发送变量短信', '11140', 
'', '', NULL, '0', '9', '2', '', '~/SmsMgr/SendSms/VariantSms', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11141', '发送', '发送变量短信', 'SmsMgr', 'SendSms', 'VariantSmsSend', '1', '11140', '\0', '', '发送', '11141', 
'', '', '', '0', '9', '3', '\0', '~/SmsMgr/SendSms/VariantSmsSend', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11142', '发送免审', '发送变量短信', 'SmsMgr', 'SendSms', 'VariantSmsSendFreeAudit', '1', '11140', '\0', '', '发送免审', '11142', 
'', '', '', '0', '9', '3', '\0', '~/SmsMgr/SendSms/VariantSmsSendFreeAudit', '', '2', NULL, '2');	

# 发送普通短信与发送变量短信初始化角色权限绑定
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11100', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11130', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11132', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11140', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11142', '3');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11100', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11130', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11131', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11140', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11141', '3');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11100', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11130', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11131', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11140', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11141', '3');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11100', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11130', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11131', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11132', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11140', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11141', '3');

# 审核短信初始化角色权限绑定
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11400', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11410', '1');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11400', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11410', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11420', '3');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11400', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11410', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11420', '3');


# 短信发送记录中新增失败重发权限
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11324', '失败重发', '批次历史', 'SmsMgr', 'SendTracking', 'reSend', '1', '11320', '\0', NULL, '失败重发', '11324', 
'', '', NULL, '0', '9', '3', '\0', '~/SmsMgr/SendTracking/reSend', '', '2', NULL, '2');

UPDATE gsms_permission SET platform_id = 2, share_flag = 2 WHERE id = 11323;

# 短信失败重发初始化角色权限绑定
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11324', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11324', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11324', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11324', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11324', '3');

# 短信发送记录中新增失败重发,导出权限的依赖关系
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('11324', '11321', '0');

# 短信记录中新增接收记录权限
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11340', '接收记录', '短信记录', 'SmsMgr', 'ReceiveRecording', 'Index', '1', '11300', '\0', NULL, '接收记录', '11340', 
'', '', NULL, '0', '1,2,3', '2', '\0', '~/SmsMgr/ReceiveRecording/Index', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11341', '查询', '接收记录', 'SmsMgr', 'ReceiveRecording', 'RecordList', '1', '11340', '\0', NULL, '查询', '11341', 
'', '', NULL, '0', '9', '3', '\0', '~/SmsMgr/ReceiveRecording/RecordList', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11342', '回复', '接收记录', 'SmsMgr', 'ReceiveRecording', 'Reply', '1', '11340', '\0', NULL, '回复', '11342', 
'', '', NULL, '0', '9', '3', '\0', '~/SmsMgr/ReceiveRecording/Reply', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11343', '导出', '接收记录', 'SmsMgr', 'ReceiveRecording', 'ExportRecord', '1', '11340', '\0', NULL, '导出', '11343', 
'', '', NULL, '0', '9', '3', '\0', '~/SmsMgr/ReceiveRecording/ExportRecord', '', '2', NULL, '2');

# 短信记录中新增接收记录权限的依赖关系
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('11342', '11341', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('11343', '11341', '0');

# 短信接收记录初始化角色权限绑定
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11340', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11341', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11342', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11343', '3');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('170', '11340', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('170', '11341', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('170', '11342', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('170', '11343', '1');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('163', '11340', '2');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('163', '11341', '2');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('163', '11342', '2');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('163', '11343', '2');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('171', '11340', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('171', '11341', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('171', '11342', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('171', '11343', '3');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11340', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11341', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11342', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11343', '3');

# 新增短信模板权限
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11600', '短信模板', '短信管理', 'SmsMgr', 'SmsTemplate', 'Index', '1', '11000', '', NULL, '短信模板', '11600', 
'\0', '', NULL, '0', '9', '2', '\0', '~/SmsMgr/SmsTemplate/Index', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11610', '普通模板', '短信模板', 'SmsMgr', 'SmsTemplate', 'CommonTemplate', '1', '11600', '\0', NULL, '普通模板', '11610', 
'', '', NULL, '0', '9', '2', '\0', '~/SmsMgr/SmsTemplate/CommonTemplate', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11611', '查询', '普通模板', 'SmsMgr', 'SmsTemplate', 'CommonTemplateList', '1', '11610', '\0', NULL, '查询', '11611', 
'', '', NULL, '0', '9', '3', '\0', '~/SmsMgr/SmsTemplate/CommonTemplateList', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11612', '新增', '普通模板', 'SmsMgr', 'SmsTemplate', 'CommonTemplateAdd', '1', '11610', '\0', NULL, '新增', '11612', 
'', '', NULL, '0', '9', '3', '\0', '~/SmsMgr/SmsTemplate/CommonTemplateAdd', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11613', '修改', '普通模板', 'SmsMgr', 'SmsTemplate', 'CommonTemplateUpdate', '1', '11610', '\0', NULL, '修改', '11613', 
'', '', NULL, '0', '9', '3', '\0', '~/SmsMgr/SmsTemplate/CommonTemplateUpdate', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11614', '删除', '普通模板', 'SmsMgr', 'SmsTemplate', 'CommonTemplateDel', '1', '11610', '\0', NULL, '删除', '11614', 
'', '', NULL, '0', '9', '3', '\0', '~/SmsMgr/SmsTemplate/CommonTemplateDel', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11620', '变量模板', '短信模板', 'SmsMgr', 'SmsTemplate', 'VariantTemplate', '1', '11600', '\0', NULL, '变量模板', '11620', 
'', '', NULL, '0', '9', '2', '\0', '~/SmsMgr/SmsTemplate/VariantTemplate', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11621', '查询', '变量模板', 'SmsMgr', 'SmsTemplate', 'VariantTemplateList', '1', '11620', '\0', NULL, '查询', '11621', 
'', '', NULL, '0', '9', '3', '\0', '~/SmsMgr/SmsTemplate/VariantTemplateList', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11622', '新增', '变量模板', 'SmsMgr', 'SmsTemplate', 'VariantTemplateAdd', '1', '11620', '\0', NULL, '新增', '11622', 
'', '', NULL, '0', '9', '3', '\0', '~/SmsMgr/SmsTemplate/VariantTemplateAdd', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11623', '修改', '变量模板', 'SmsMgr', 'SmsTemplate', 'VariantTemplateUpdate', '1', '11620', '\0', NULL, '修改', '11623', 
'', '', NULL, '0', '9', '3', '\0', '~/SmsMgr/SmsTemplate/VariantTemplateUpdate', '', '2', NULL, '2');

INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('11624', '删除', '变量模板', 'SmsMgr', 'SmsTemplate', 'VariantTemplateDel', '1', '11620', '\0', NULL, '删除', '11624', 
'', '', NULL, '0', '9', '3', '\0', '~/SmsMgr/SmsTemplate/VariantTemplateDel', '', '2', NULL, '2');

# 短信模板权限的依赖关系
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('11612', '11611', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('11613', '11611', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('11614', '11611', '0');

INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('11622', '11621', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('11623', '11621', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('11624', '11621', '0');

# 短信模板与初始化角色权限绑定关系
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11600', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11610', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11611', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11612', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11613', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11614', '9');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11620', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11621', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11622', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11623', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11624', '9');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11600', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11610', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11611', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11612', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11613', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11614', '9');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11620', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11621', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11622', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11623', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11624', '9');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11600', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11610', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11611', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11612', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11613', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11614', '9');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11620', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11621', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11622', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11623', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('223', '11624', '9');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11600', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11610', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11611', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11612', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11613', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11614', '9');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11620', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11621', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11622', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11623', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11624', '9');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11600', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11610', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11611', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11612', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11613', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11614', '9');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11620', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11621', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11622', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11623', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11624', '9');

--  =========================================彩信管理========================================================= --
# 发送彩信中新增发送免审权限
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, 
`parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, 
`type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) 
VALUES ('17112', '发送免审', '批次彩信', 'SmsMgr', 'SendMms', 'FreeAuditSend', '1', '17110', '\0', '', '发送免审', '17112', 
'', '', '', '0', '3', '3', '\0', '~/SmsMgr/SendMms/FreeAuditSend', '', '2', NULL, '2');

# 彩信发送免审与初始化角色权限绑定关系
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('225', '17112', '9');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '17112', '9');

# 修改彩信发送记录中导出权限
UPDATE gsms_permission SET platform_id = 2, share_flag = 2 WHERE id = 17323;

# 将彩信发送导出权限设置为旧mos独有权限
UPDATE gsms_permission SET share_flag = 1 WHERE id IN(26968, 26969);

# 新增彩信模板管理权限依赖关系
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('17412', '17411', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('17413', '17411', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('17414', '17411', '0');

# 修改个人通讯录权限排序
UPDATE gsms_permission SET menu_display_order = 12115 WHERE id = 12111;

# 新增彩信发送记录导出权限与初始化角色绑定关系
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '17323', '3');

update gsms_permission set share_flag=1 where id in (26970,26971,26972,26973,26974,26975);





-----2017/6/27 网关写死发送权限，新mos对应修改相关权限start-------

update gsms_permission set share_flag=1 where id in (11100,11110,11111,11120,11121,11122,11130,11131,11132,11140,11141,11142);

# 新增发送普通短信与发送变量短信相关权限
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('11700', '发送短信', '短信管理', 'SmsMgr', 'SendSms', 'DoSendSms', '1', '11000', '', '', '发送短信', '11100', '', '\0', NULL, '0', '3', '0', '\0', '~/SmsMgr/SendSms/Index', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('11730', '发送普通短信', '短信发送', 'SmsMgr', 'SendSms', 'CommonSms', '1', '11700', '\0', '', '发送普通短信', '11130', '', '', NULL, '0', '9', '2', '', '~/SmsMgr/SendSms/CommonSms', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('11731', '发送', '发送普通短信', 'SmsMgr', 'SendSms', 'CommonSmsSend', '1', '11730', '\0', '', '发送', '11131', '', '', '', '0', '9', '3', '\0', '~/SmsMgr/SendSms/CommonSmsSend', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('11732', '发送免审', '发送普通短信', 'SmsMgr', 'SendSms', 'DoSendMms', '1', '11730', '\0', '', '发送免审', '11132', '', '', '', '1', '9', '3', '\0', '~/SmsMgr/SendSms/CommonSmsSendFreeAudit', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('11740', '发送变量短信', '短信发送', 'SmsMgr', 'SendSms', 'VariantSms', '1', '11700', '\0', '', '发送变量短信', '11140', '', '', NULL, '0', '9', '2', '', '~/SmsMgr/SendSms/VariantSms', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('11741', '发送', '发送变量短信', 'SmsMgr', 'SendSms', 'VariantSmsSend', '1', '11740', '\0', '', '发送', '11141', '', '', '', '0', '9', '3', '\0', '~/SmsMgr/SendSms/VariantSmsSend', '', '2', NULL, '2');
INSERT INTO `gsms_permission` (`id`, `display_name`, `operation_obj`, `area_name`, `controller_name`, `action_name`, `form_method`, `parent_id`, `is_menu`, `menu_image_path`, `menu_display_name`, `menu_display_order`, `menu_has_hyperlink`, `is_display`, `remark`, `type`, `data_scopes`, `level`, `is_quick_link`, `url`, `operationstr`, `platform_id`, `industry_type`, `share_flag`) VALUES ('11742', '发送免审', '发送变量短信', 'SmsMgr', 'SendSms', 'DoSendMms', '1', '11740', '\0', '', '发送免审', '11142', '', '', '', '1', '9', '3', '\0', '~/SmsMgr/SendSms/VariantSmsSendFreeAudit', '', '2', NULL, '2');

# 发送普通短信与发送变量短信初始化角色权限绑定
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11700', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11730', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11732', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11740', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11742', '3');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11700', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11730', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11731', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11740', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11741', '3');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11700', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11730', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11731', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11740', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '11741', '3');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11700', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11730', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11731', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11732', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11740', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11741', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11742', '3');

# 删除无用的发送普通短信与发送变量短信相关权限
delete from gsms_permission where id in (11130,11131,11132,11140,11141,11142);

delete from gsms_role_permission where permission_id in (11130,11131,11132,11140,11141,11142);


-----2017/6/27 end-------

-----2017/6/28 start-------

# 修改彩信模块权限
UPDATE gsms_permission SET display_name = '新增', menu_display_name = '新增'  WHERE id = 17412;
UPDATE gsms_permission SET display_name = '修改', menu_display_name = '修改'  WHERE id = 17413;
UPDATE gsms_permission SET menu_display_order = 17411 WHERE id = 17411;
UPDATE gsms_permission SET menu_display_order = 17412 WHERE id = 17412;
UPDATE gsms_permission SET menu_display_order = 17413 WHERE id = 17413;
UPDATE gsms_permission SET menu_display_order = 17414 WHERE id = 17414;

# 修改通讯录管理权限
UPDATE gsms_permission SET display_name = '新增群组', menu_display_name = '新增群组' WHERE id = 12112;
UPDATE gsms_permission SET display_name = '修改群组', menu_display_name = '修改群组' WHERE id = 12113;
UPDATE gsms_permission SET display_name = '删除群组', menu_display_name = '删除群组' WHERE id = 12114;
UPDATE gsms_permission SET display_name = '查询联系人', menu_display_name = '查询联系人' WHERE id = 12111;
UPDATE gsms_permission SET display_name = '新增联系人', menu_display_name = '新增联系人' WHERE id = 12115;
UPDATE gsms_permission SET display_name = '修改联系人', menu_display_name = '修改联系人' WHERE id = 12116;
UPDATE gsms_permission SET display_name = '删除联系人', menu_display_name = '删除联系人' WHERE id = 12117;
UPDATE gsms_permission SET display_name = '导入联系人', menu_display_name = '导入联系人' WHERE id = 12118;
UPDATE gsms_permission SET display_name = '导出联系人', menu_display_name = '导出联系人' WHERE id = 12119;

UPDATE gsms_permission SET display_name = '查询联系人', menu_display_name = '查询联系人' WHERE id = 12311;
UPDATE gsms_permission SET display_name = '新增群组', menu_display_name = '新增群组' WHERE id = 12312;
UPDATE gsms_permission SET display_name = '删除群组', menu_display_name = '删除群组' WHERE id = 12314;
UPDATE gsms_permission SET menu_display_order = 12315 WHERE id = 12311;
DELETE FROM gsms_permission WHERE id = 12313;

# 黑名单相关权限修改
UPDATE gsms_permission SET display_name = '新增', menu_display_name = '新增' WHERE id = 15111;
UPDATE gsms_permission SET menu_display_order = 15111 WHERE id = 15114;
UPDATE gsms_permission SET menu_display_order = 15112 WHERE id = 15111;
UPDATE gsms_permission SET menu_display_order = 15113 WHERE id = 15112;

# 非法关键字相关权限修改
UPDATE gsms_permission SET display_name = '新增', menu_display_name = '新增' WHERE id = 15211;
UPDATE gsms_permission SET menu_display_order = 15211 WHERE id = 15214;
UPDATE gsms_permission SET menu_display_order = 15212 WHERE id = 15211;
UPDATE gsms_permission SET menu_display_order = 15214 WHERE id = 15212;

# 业务类型相关权限修改
UPDATE gsms_permission SET display_name = '新增', menu_display_name = '新增' WHERE id = 15311;
UPDATE gsms_permission SET menu_display_order = 15311 WHERE id = 15314;
UPDATE gsms_permission SET menu_display_order = 15312 WHERE id = 15311;
UPDATE gsms_permission SET menu_display_order = 15313 WHERE id = 15313;
UPDATE gsms_permission SET menu_display_order = 15314 WHERE id = 15312;

# 运营商号段相关权限修改
UPDATE gsms_permission SET display_name = '新增', menu_display_name = '新增' WHERE id = 15411;
UPDATE gsms_permission SET menu_display_order = 15411 WHERE id = 15414;
UPDATE gsms_permission SET menu_display_order = 15412 WHERE id = 15411;
UPDATE gsms_permission SET menu_display_order = 15413 WHERE id = 15412;
UPDATE gsms_permission SET menu_display_order = 15414 WHERE id = 15413;

# 企业白名单相关权限修改
UPDATE gsms_permission SET display_name = '新增', menu_display_name = '新增' WHERE id = 26956;


-----2017/6/28 end-------

-----2017/6/29 start-------

UPDATE gsms_permission SET action_name = 'ReSend', url = '~/SmsMgr/SendTracking/ReSend' WHERE id = 11324;

-----2017/6/29 end-------

-----2017/7/1 start-------

UPDATE gsms_permission SET action_name = 'DoSendSms' WHERE id in (11732,11742);

-----2017/7/1 end-------


-----2017/7/3 start-------

ALTER TABLE `gsms_contact_group_share_map`
ADD COLUMN `enterprise_id`  int(11) NOT NULL AFTER `id`;


-----2017/7/3 end-------

-----2017/7/4 start-------

UPDATE gsms_permission SET display_name = '彩信模板', menu_display_name = '彩信模板' WHERE id IN(17400, 17410);

# 修改用户管理权限顺序
UPDATE gsms_permission SET menu_display_order = 18111 WHERE id = 18114;
UPDATE gsms_permission SET menu_display_order = 18112 WHERE id = 18111;
UPDATE gsms_permission SET menu_display_order = 18113 WHERE id = 18112;
UPDATE gsms_permission SET menu_display_order = 18114 WHERE id = 18113;
UPDATE gsms_permission SET menu_display_order = 18115 WHERE id = 18116;
UPDATE gsms_permission SET menu_display_order = 18116 WHERE id = 18115;

# 修改部门管理权限顺序
UPDATE gsms_permission SET menu_display_order = 18211 WHERE id = 18214;
UPDATE gsms_permission SET menu_display_order = 18212 WHERE id = 18215;
UPDATE gsms_permission SET menu_display_order = 18213 WHERE id = 18211;
UPDATE gsms_permission SET menu_display_order = 18214 WHERE id = 18212;
UPDATE gsms_permission SET menu_display_order = 18215 WHERE id = 18213;

# 修改角色管理权限顺序
UPDATE gsms_permission SET menu_display_order = 18311 WHERE id = 18313;
UPDATE gsms_permission SET menu_display_order = 18312 WHERE id = 18311;
UPDATE gsms_permission SET menu_display_order = 18313 WHERE id = 18314;
UPDATE gsms_permission SET menu_display_order = 18314 WHERE id = 18312;

# 修改公告管理权限顺序
UPDATE gsms_permission SET menu_display_order = 18711 WHERE id = 18714;
UPDATE gsms_permission SET menu_display_order = 18712 WHERE id = 18711;
UPDATE gsms_permission SET menu_display_order = 18713 WHERE id = 18713;
UPDATE gsms_permission SET menu_display_order = 18714 WHERE id = 18712;

-----2017/7/4 end-------

-----2017/7/6 start-------

ALTER TABLE gsms_user_task MODIFY result_message VARCHAR(1024) COMMENT '处理结果报告';

# 短信发送权限依赖配置
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('11732', '11731', '0');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('11742', '11741', '0');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11731', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11741', '3');

-----2017/7/6 end-------

-----2017/7/7 start-------

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('228', '11323', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '11323', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('167', '11323', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('226', '17600', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('226', '17610', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('224', '17600', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '17600', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '17610', '3');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('230', '17620', '3');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('225', '12300', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('225', '12311', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('225', '12312', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('225', '12314', '1');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('226', '12300', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('226', '12311', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('226', '12312', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('226', '12314', '1');

INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '12300', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '12311', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '12312', '1');
INSERT INTO `gsms_role_permission` (`role_id`, `permission_id`, `data_scope`) VALUES ('229', '12314', '1');

UPDATE gsms_role_permission SET data_scope = 3 WHERE role_id = 167 AND permission_id in(17600,17610,17620,11400);
UPDATE gsms_role_permission SET data_scope = 1 WHERE role_id = 167 AND permission_id in(12111,12112,12113,12114,12115,12116,12117,12118,12119);
UPDATE gsms_role_permission SET data_scope = 1 WHERE role_id IN(167,228) AND permission_id IN(12300,12311,12312,12314);
-----2017/7/7 end-------

----- 2017/7/12 start -----
# 更新彩信发送免审权限的URL
UPDATE `gsms_permission` SET `id`='17112', `display_name`='发送免审', `operation_obj`='批次彩信', `area_name`='SmsMgr', `controller_name`='SendMms', `action_name`='DoSendMms', `form_method`='1', `parent_id`='17110', `is_menu`=b'0', `menu_image_path`='', `menu_display_name`='发送免审', `menu_display_order`='17112', `menu_has_hyperlink`=b'1', `is_display`=b'1', `remark`='', `type`='2', `data_scopes`='3', `level`='3', `is_quick_link`=b'0', `url`='~/SmsMgr/SendMms/DoSendMms', `operationstr`='', `platform_id`='2', `industry_type`=NULL, `share_flag`='2' WHERE (`id`='17112');
INSERT INTO `gsms_permission_depends` (`pri_id`, `sub_id`, `islevel`) VALUES ('17112', '17111', '0');
----- 2017/7/12  end  -----