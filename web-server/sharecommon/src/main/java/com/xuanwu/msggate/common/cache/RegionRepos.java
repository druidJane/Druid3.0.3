/*   
* Copyright (c) 2012 by XUANWU INFORMATION TECHNOLOGY CO. 
*             All rights reserved                         
*/
package com.xuanwu.msggate.common.cache;

/**
 * @Author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2012-12-3
 * @Version 1.0.0
 */
public interface RegionRepos  {
    /**
     * 通过地区代码获取地区运营商对象
     * 
     * @param phone
     * @return
     */
    public RegionCarrierResult getRegionCarrier(String phone);
}
