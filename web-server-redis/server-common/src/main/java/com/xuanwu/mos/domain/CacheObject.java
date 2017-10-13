package com.xuanwu.mos.domain;

import java.util.concurrent.TimeUnit;

/**
 * @Description 缓存对象
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2013-1-30
 * @Version 1.0.0
 */
public class CacheObject<T> {

	private T t;
	
	private int version;
	
	private Object attach;
	
	private long aliveTime;
	
	private long deadline;
	
	public CacheObject(T t){
		this.t = t;
	}
	
	public CacheObject(T t, long aliveTime, TimeUnit timeUnit){
		this.t = t;
		setAliveTime(aliveTime, timeUnit);
	}
	
	public T getObject(){
		return t;
	}
	
	public void setAttachment(Object obj){
		this.attach = obj;
	}
	
	public Object getAttachment() {
		return attach;
	}
	
	public int getVersion() {
		return version;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
	
	public void setAliveTime(long aliveTime, TimeUnit timeUnit) {
		this.aliveTime = timeUnit.toMillis(aliveTime);
		this.deadline = System.currentTimeMillis() + this.aliveTime;
	}
	
	public boolean isExpired() {
		long now = System.currentTimeMillis();
		if(deadline - now > aliveTime){
			return true;
		}
		return deadline < now;
	}

	public long getDeadline() {
		return deadline;
	}
}
