/*
 * Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.mos.config;

import com.xuanwu.mos.domain.entity.ConfigRecord;
import com.xuanwu.mos.domain.enums.GsmsSyncVersionType;
import com.xuanwu.mos.exception.BusinessException;
import com.xuanwu.mos.file.FileType;
import com.xuanwu.mos.rest.service.ConfigService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;


/**
 * @Description: 全局配置文件
 * @Author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2012-9-20
 * @Version 1.0.0
 */
@Component
public class Config implements SyncTask {

	private static final Logger logger = LoggerFactory.getLogger(Config.class);

	public static final String DB_PREFIX = "datasource";

	private static final long ONE_MINUTE = 60 * 1000;
	//前台配置数据
	private Map<String, Object> configMap = new HashMap<>();
	//后台配置数据
	private Map<String, Object> backConfigMap = new HashMap<>();

	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	private int prevVersion = -1;

	@Autowired
	private ServerConfig serverConfig;

	@Autowired
	private ConfigService configService;

	@Autowired
	private PlatformMode mode;
	@Autowired
	private FtpConfig ftpConfig;

	/** 最大允许用户执行任务并发数 */
	private int maxPermitUsers = 15;
	/** 每个用户允许最大执行任务数 */
	private int maxPermitPerUserTasks = 3;
	/** 单个导出文本文件最大记录数 */
	private int maxTextPerFileRecords = 100 * 10000;
	/** 单个导出Excel文件最大记录数 */
	private int maxExcelPerFileRecords = 65000;
	/** 单次导出文件记录数 */
	private int maxPerExportRecords = 10000;
	/** 单次导入文件记录数 */
	private int maxPerImportRecords = 20000;
	/** 可导出的最大数据量（条） */
	private int maxExportQuantity = 500000;

	/**在mos中，如果用户是采用的手机号码+验证码进行登录的话，那么需要设置一个remoteIp的值，如果不设置则网关会报用户无效的错误
	 * 因此，解决办法是首先取数据库中的validateIp，如果该值不存在，则使用192.188.188.188进行设置
	 */
	private String defaultValidateIp = "192.188.188.188";

	private static String contextPath = "mos";

	private Date shardTableStartTime = null;

	public static String mmsSupportType = "\"!jpg!: !jpg!,!jpeg!: !jpeg!,!gif!: !gif!,!bmp!: !bmp!,!amr!: !amr!,!mid!: !mid!\"";

	private String phonePattern = "^(86|\\+86)?((0((10[0-9]{7,8})|([2-9]{1}[0-9]{8,10})))|(0?1[3-8]{1}[0-9]{9}))$";

	@PostConstruct
	public void init() {
		sync();
	}

	@Override
	public void run() {
		sync();
	}

	public void sync() {
		try {
			// 初始化分表时间配置
			setShardTableStartTime();
			int ver = configService.findGsmsSyncVersion(GsmsSyncVersionType.SYS_CONFIG);
			logger.info("Begin to sync system configuration, current version[{}], new version[{}]...", prevVersion,
					ver);
			if (prevVersion < ver) {
				syncConfigs(ver);
				logger.info("Sync system configuration to version [{}]", ver);
			}
			logger.info("End to sync system configuration.");
		} catch (Exception e) {
			logger.error("Sync system configuration fail, ", e);
		}
	}

	private void syncConfigs(int version) throws ParseException {
		List<ConfigRecord> configs = configService.findAllConfigs(getPlatformId());
		for (ConfigRecord config : configs) {
			putConfig(config,configMap);
		}
		List<ConfigRecord> backConfigs = configService.findAllConfigs(Platform.BACKEND.getIndex());
		for (ConfigRecord config : backConfigs) {
			putConfig(config,backConfigMap);
		}

		prevVersion = version;
	}

	public int getPlatformId() {
		return getPlatform().getIndex();
	}

	public Platform getPlatform() {
		return mode.getPlatform();
	}

	public String getPlatformName() {
		return mode.getName();
	}

	public String getVersion() {
		return mode.getVersion();
	}

	/**
	 * 0-MOS 1-UMP
	 * 
	 * @return
	 */
	public int getRunMode() {
		return 0; // only mos model
	}

	public long getMaxUploadFileSize() {
		return (Integer) backConfigMap.get("MaxUploadFileSize") * 1024 * 1024;
	}

	public String getPhonePattern() {
		return (String) configMap.get("PhonePattern")==null?phonePattern:(String) configMap.get("PhonePattern");
	}



	public int getPerFileRecords(FileType fileType) {
		switch (fileType) {
		case Text:
		case Csv:
			return (Integer) configMap.get("MaxTextPerFileRecords") == null ? maxTextPerFileRecords
					: (Integer) configMap.get("MaxTextPerFileRecords");
		case Bwf:
			return (Integer) configMap.get("MaxTextPerFileRecords") == null ? maxTextPerFileRecords
					: (Integer) configMap.get("MaxTextPerFileRecords");
		case Excel:
		case ExcelX:
			return (Integer) configMap.get("MaxExcelPerFileRecords") == null ? maxExcelPerFileRecords
					: (Integer) configMap.get("MaxExcelPerFileRecords");
		default:
			throw new BusinessException("Invalid fileType:" + fileType);
		}
	}

	public void putConfig(ConfigRecord config,Map<String, Object> map) throws ParseException {
		if ("int".equalsIgnoreCase(config.getType())) {
			map.put(config.getKey(), Integer.parseInt(config.getValue()));
		} else if ("TimeSpan".equalsIgnoreCase(config.getType())) {
			map.put(config.getKey(), sdf.parse(config.getValue()));
		} else {
			map.put(config.getKey(), config.getValue());
		}
	}


	public String getMixKey() {
		return (String) configMap.get("MixKey");
	}

    public String getSendValidateCodeAccount() {return (String)configMap.get("SendValidateCodeAccount");}

	public String getSendValidateCodePassWord() {return (String)configMap.get("SendValidateCodePassWord");}

	@Override
	public long getPeriod() {
		return getSettingsCheckingInterval() * ONE_MINUTE;
	}

	public int getSettingsCheckingInterval() {
		return (Integer) configMap.get("SettingsCheckingInterval");
	}

	/**
	 * 用户发送同号限制时间间隔（分）
	 * @return
	 */
	public int getUserTimeInterval() {
		return (Integer) backConfigMap.get("userTimeInterval");
	}

	/**
	 * 用户发送限制同号码发送条数
	 * @return
	 */
	public int getUserSendNum() {
		return (Integer) backConfigMap.get("userSendNum");
	}

	/**
	 * 用户发送同号同内容限制时间间隔
	 * @return
	 */
	public int getUserContentInterval() {
		return (Integer) backConfigMap.get("userContentInterval");
	}

	public static void setContextPath(String contextPath) {
		Config.contextPath = contextPath;
	}

	public static String getContextPath() {
		return Config.contextPath;
	}

	public String getGatewayIPAddress() {
		return (String) configMap.get("GatewayIPAddress");
	}

	public int getGatewayPort() {
		return (Integer) configMap.get("GatewayPort");
	}

	public String getDeptNoPrefix(Platform platform) {
		switch (platform) {
			case FRONTKIT:
				return (String) configMap.get("DeptNoPrefix");
			case BACKEND:
				return (String) backConfigMap.get("DeptNoPrefix");
			default: return null;
		}
	}

	public String getMoSmsAccount() {
		return (String)configMap.get("MoSmsAccount");
	}
	
	public int getExpirareminderdays() {
		return (Integer)configMap.get("Expirareminderdays");
	}

	public int getBalanceRemind() {
		return (Integer) configMap.get("BalanceRemind");
	}

	public int getMaxPerExportRecords() {
		return (Integer) configMap.get("MaxPerExportRecords") == null ? maxPerExportRecords
				: (Integer) configMap.get("MaxPerExportRecords");
	}
	public int getMaxPermitUsers() {
		return (Integer) configMap.get("MaxPermitUsers") == null ? maxPermitUsers
				: (Integer) configMap.get("MaxPermitUsers");
	}
	public int getMaxPermitPerUserTasks() {
		return (Integer) configMap.get("MaxPermitPerUserTasks") == null ? maxPermitPerUserTasks
				: (Integer) configMap.get("MaxPermitPerUserTasks");
	}
	public int getMaxExportQuantity(Platform platform) {
		switch (platform){
			case BACKEND:
				return (Integer) backConfigMap.get("MaxExportQuantity") == null ? maxPermitPerUserTasks
						: (Integer) backConfigMap.get("MaxExportQuantity");
			default:
				return (Integer) configMap.get("MaxExportQuantity") == null ? maxPermitPerUserTasks
						: (Integer) configMap.get("MaxExportQuantity");
		}
	}

	public int getMaxPerImportRecords() {
		return (Integer) configMap.get("MaxPerImportRecords") == null ? maxPerImportRecords
				: (Integer) configMap.get("MaxPerImportRecords");
	}
	public int getMaxTreeDeep() {
		return (Integer) configMap.get("MaxTreeDeep");
	}

	public boolean shardingTableOn(Date postTime) {
		// mos 的数据库里面没有ShardingTableOn的配置，因此我们默认为true。
		boolean shardingTableOn = true;
		if (shardingTableOn && postTime != null && postTime.before(shardTableStartTime)) {
			return false;
		}
		return shardingTableOn;
	}
	// 初始化分表的时间
	public void setShardTableStartTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		//Calendar中月份是从零开始的，因此这里对月份赋值9而不是10
		calendar.set(2013,9,29);
		this.shardTableStartTime = calendar.getTime();
	}
	public String getCommonNamePattern() {
		return (String) configMap.get("CommonNamePattern");
	}

	public String getValidateIp() {
		return (String) configMap.get("validateIp");
	}

	public String getDefaultValidateIp() {
		return defaultValidateIp;
	}
}
