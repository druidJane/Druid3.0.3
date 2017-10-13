/**
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved     
 */
package com.xuanwu.msggate.common.wappush;

import com.xuanwu.msggate.common.encode.WordFragment;

import java.util.List;
/**
 * 
 * <p><pre>
 * Description: Wappush 消息封装，超长则进行分段处理
 * </pre></p>
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2010-11-26
 * @Version 1.0.0
 * <p><pre>
 * 修改日期 修改人 修改内容
 * </pre></p>
 */
public interface WappushRepos {
	public List<WordFragment> splitWappushContent(String content,
			int mixLength);
}
