package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;
import com.xuanwu.msggate.common.util.DateUtil;

import java.util.Date;

/**
 * 号码记录总数统计
 * @Description 
 * @author <a href="mailto:lizongxian@139130.net">ZongXian.Li</a>
 * @Date 2013-9-10 
 * @Version
 */

/**
 * @Description
 * @author <a href="mailto:lizongxian@139130.net">ZongXian.Li</a>
 * @Date 2013-9-9
 * @Version
 */
public class TicketCount extends AbstractEntity {
	/** 主键 **/
	private int id;
	/** 消息类型，1-短信，2-彩信 **/
	private int msgType;
	/** 用户ID **/
	private int userId;
	/**部门路径**/
	private String deptPath;	
	/** 通道ID **/
	private int channelId;
	/** 提交报告 **/
	private int state;
	/** 数量 **/
	private int stateCount;
	/** 产生统计记录的时间 **/
	private Date genDate;
	/** 企业ID **/
	private int enterpriseId;
	/** 对应分表ticket主键ID **/
	private long curMaxId;

	private Date postTime;

	private long currMillis;

	
	public TicketCount() {

	}
	
	public TicketCount(int msgType, int userId, String deptPath, int channleId, int state,
                       int enterpriseId) {
		this.msgType = msgType;
		this.channelId = channleId;
		this.userId = userId;
		this.deptPath = deptPath;
		this.state = state;
		this.enterpriseId = enterpriseId;
	}
	
	public TicketCount(int msgType, int userId, int channleId, int state,
                       int enterpriseId, Date postTime) {
		this.msgType = msgType;
		this.channelId = channleId;
		this.userId = userId;
		this.state = state;
		this.enterpriseId = enterpriseId;
		setPostTime(postTime);
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public int getChannelId() {
		return channelId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getStateCount() {
		return stateCount;
	}

	public void setStateCount(int stateCount) {
		this.stateCount = stateCount;
	}

	public Date getGenDate() {
		return genDate;
	}

	public void setGenDate(Date genDate) {
		this.genDate = genDate;
	}

	public int getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public long getCurMaxId() {
		return curMaxId;
	}

	public void setCurMaxId(long curMaxId) {
		this.curMaxId = curMaxId;
	}

	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
		currMillis = DateUtil.fixTime(postTime.getTime() / 600000);
	}
	
	public long getCurrMillis() {
		return currMillis;
	}

	public void setCurrMillis(long currMillis) {
		this.currMillis = currMillis;
	}
	
	public void increment(){
		this.stateCount += 1;
	}
	
	public String getDeptPath() {
		return deptPath;
	}
	
	public void setDeptPath(String deptPath) {
		this.deptPath = deptPath;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof TicketCount)) {
			return false;
		}
		TicketCount tc = (TicketCount) obj;
		if (this.msgType == tc.msgType && this.channelId == tc.channelId
				&& this.userId == tc.userId && this.state == tc.state
				&& this.enterpriseId == tc.enterpriseId && this.currMillis == tc.currMillis) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + msgType;
		result = prime * result + channelId;
		result = prime * result + userId;
		result = prime * result + state;
		result = prime * result + enterpriseId;
		result = (int) (prime * result + currMillis);
		return result;
	}
}
