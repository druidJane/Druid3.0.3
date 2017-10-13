package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.DateUtil.DateTimeType;
import com.xuanwu.mos.utils.StringUtil;

import java.util.Arrays;
import java.util.Date;

/**
 * @Description 联系人
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2012-9-28
 * @Version 1.0.0
 */
public class Contact extends AbstractEntity {

	private int id;

	/** 组ID */
	private int groupId;

	/** 所属组对象 */
	private ContactGroup group;

	/** 名称 */
	private String name;

	/** 手机号码 */
	private String phone;

	/** 性别 */
	private int sex;

	/** 性别值 String 用于获取导入值 */
	private String sexStr;

	public String getSexStr() {
		return sexStr;
	}

	public void setSexStr(String sexStr) {
		this.sexStr = sexStr;
	}

	public String getVipStr() {
		return vipStr;
	}

	public void setVipStr(String vipStr) {
		this.vipStr = vipStr;
	}

	/** 编号 */
	private String identifier;

	/** 生日 */
	private Date birthday;

	public String getBirthdayStr() {
		return birthdayStr;
	}

	public void setBirthdayStr(String birthdayStr) {
		this.birthdayStr = birthdayStr;
	}

	/** 生日 String 用于获取导入值 */
	private String birthdayStr;

	/** 是否为VIP */
	private boolean vip;

	/** VIP值 */
	private int vipVal;
	/** VIP值 String 用于获取导入值 */
	private String  vipStr;

	/** 备注 */
	private String remark;

	/** 扩展字段 */
	private String ext1;

	/** 扩展字段 */
	private String ext2;

	/** 扩展字段 */
	private String ext3;

	/**
	 * 群组名
	 */
	private String groupName;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	private String[] array;

	private int parseFlag;// 0:success,1:sex,2:vip


	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public ContactGroup getGroup() {
		if (group == null) {
			group = new ContactGroup();
		}
		return group;
	}

	public void setGroup(ContactGroup group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public boolean isVip() {
		return vip;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}

	public int getVipVal() {
		return vipVal;
	}

	public void setVipVal(int vipVal) {
		this.vipVal = vipVal;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public void setArray(String[] array) {
		this.array = array;
	}

	public String[] getArray() {
		return array;
	}

	public int getParseFlag() {
		return parseFlag;
	}

	public void setParseFlag(int parseFlag) {
		this.parseFlag = parseFlag;
	}

	public String toGridJSON() {
		return null;
	}


	public String toJSON() {
		StringBuffer sb = new StringBuffer();
		sb.append("{\"id\":" + id);
		sb.append(",\"groupId\":" + groupId);
		sb.append(",\"group\":" + getGroup().toJSON());
		sb.append(",\"name\":\"" + name + "\"");
		sb.append(",\"phone\":\"" + phone + "\"");
		sb.append(",\"sex\":" + sex);
		sb.append(",\"identifier\":\"" + identifier
				+ "\"");
		sb.append(",\"birthday\":\""
				+ DateUtil.format(birthday, DateTimeType.Date) + "\"");
		sb.append(",\"vip\":" + vip);
		sb.append(",\"remark\":\"" + StringUtil.fixJsonStr(remark) + "\"}");
		return sb.toString();
	}
	
	public String toJSONHtml() {
		StringBuffer sb = new StringBuffer();
		sb.append("{\"id\":" + id);
		sb.append(",\"groupId\":" + groupId);
		sb.append(",\"group\":" + getGroup().toJSON());
		sb.append(",\"name\":\"" + StringUtil.replaceHtml(StringUtil.fixJsonStr(name)) + "\"");
		sb.append(",\"phone\":\"" + StringUtil.fixJsonStr(phone) + "\"");
		sb.append(",\"sex\":" + sex);
		sb.append(",\"identifier\":\"" + StringUtil.fixJsonStr(identifier)
				+ "\"");
		sb.append(",\"birthday\":\""
				+ DateUtil.format(birthday, DateTimeType.Date) + "\"");
		sb.append(",\"vip\":" + vip);
		sb.append(",\"remark\":\"" + StringUtil.fixJsonStr(remark) + "\"}");
		return sb.toString();
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", groupId=" + groupId + ", group="
				+ group + ", name=" + name + ", phone=" + phone + ", sex="
				+ sex + ", identifier=" + identifier + ", birthday=" + birthday
				+ ", vip=" + vip + ", remark=" + remark + ", ext1=" + ext1
				+ ", ext2=" + ext2 + ", ext3=" + ext3 + ", array="
				+ Arrays.toString(array) + "]";
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Contact cloneContact = new Contact();

		cloneContact.setGroupId(groupId);
		cloneContact.setName(name);
		cloneContact.setPhone(phone);
		cloneContact.setSex(sex);
		cloneContact.setIdentifier(identifier);
		cloneContact.setBirthday(birthday);
		cloneContact.setVip(vip);
		cloneContact.setVipVal(vipVal);
		cloneContact.setRemark(remark);
		cloneContact.setExt1(ext1);
		cloneContact.setExt2(ext2);
		cloneContact.setExt3(ext3);
		cloneContact.setArray(array);
		cloneContact.setParseFlag(parseFlag);
		return cloneContact;
	}

	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return id;
	}

}
