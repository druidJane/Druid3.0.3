package com.xuanwu.mos.file.importbean;

import com.xuanwu.mos.domain.AbstractEntity;

import java.io.Serializable;

/**
 * @author <a href="zhangpengfei@wxchina.com">ZhangPengFei</a>
 * @Discription 计费充值导入
 * @Data 2017-6-5
 * @Version 1.0.0
 */
public class ChargeAccountImport extends AbstractEntity {
	/* 账户名称 */
	private String accountName;
	/* 充值金额 */
	private String chargeMoney;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getChargeMoney() {
		return chargeMoney;
	}

	public void setChargeMoney(String chargeMoney) {
		this.chargeMoney = chargeMoney;
	}

	@Override
	public Serializable getId() {
		return 0;
	}
}
