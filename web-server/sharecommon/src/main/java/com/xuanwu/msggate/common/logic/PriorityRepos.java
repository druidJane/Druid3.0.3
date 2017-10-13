/**
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved     
 */
package com.xuanwu.msggate.common.logic;

import com.xuanwu.msggate.common.cache.entity.PriorityBox;

/**
 * <p><pre>
 * Description:
 * </pre></p>
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2010-11-24
 * @Version 1.0.0
 * <p><pre>
 * 修改日期 修改人 修改内容
 * </pre></p>
 */
public interface PriorityRepos {
	
	public PriorityBox getPriority(String identity);
	
	public PriorityBox getNextPriority(String identity, PriorityBox priorityBox);
	
	public void setPollEndFlag(String identity);
	
}
