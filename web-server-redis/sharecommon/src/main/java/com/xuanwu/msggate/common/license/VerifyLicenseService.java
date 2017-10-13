package com.xuanwu.msggate.common.license;


/**
 * 受权认证
 * @author liudajun
 *
 */
public interface VerifyLicenseService {
	
	public enum VerifyState {
		/** 验证通过 */
		SUCCESS(0), 
		/** 没有license文件 */
		NOT_LICENSE(1),
		/** MAC地址不通过 */
		INVALID_MAC(2), 
		/** 签名不通过 */
		INVALID_SIGN(3),
		/** 使用日期已失效 */
		OUT_OF_DATE(4),
		/** 版本号不一致 */
		INVALID_VERSION(5),
		/** 其它错误 */
		OTHER(99);
		private final int index;
		
		private VerifyState(int index) {
			this.index = index;
		}
		
		public int getIndex() {
			return index;
		}
		
		public static VerifyState getType(int index){
			switch(index){
			case 0: return SUCCESS;
			case 1: return NOT_LICENSE;
			case 2: return INVALID_MAC;
			case 3: return INVALID_SIGN;
			case 4: return OUT_OF_DATE;
			case 5:	return INVALID_VERSION;
			default : return OTHER;
			}
		}
	}
				
	/**
	 * 验证
	 * @return
	 * @throws Exception
	 */
	public VerifyState verify() throws Exception;
	
	/**
	 * 获取授权实体
	 * @return
	 */
	public LicenseBean getLicense();
	
	/**
	 * 返回对应的状态描述
	 * @param state
	 * @return
	 */
	public String getStateMsg(VerifyState state);
	
	/**
	 * License更新
	 * @param data
	 * @return 
	 * @throws Exception
	 */
	public boolean update(byte[] data) throws Exception;
}