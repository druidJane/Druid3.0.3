package com.xuanwu.msggate.common.cache.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xuanwu.msggate.common.cache.BizHandleRepos.ListType;
import com.xuanwu.msggate.common.sbi.entity.CachePhone;

public interface CacheMapper {
	
	public int existsPhone(@Param("phone")Long phone, @Param("listType")ListType listType);
	
	public List<CachePhone> getPhones(@Param("phone")Long Phone, @Param("listType")ListType listType, @Param("target")Integer target);

	public long getPhoneNextKey();
	
	public void insertPhone(CachePhone phone);
	
	public void insertKeyword(@Param("id")Long ID, @Param("keyword")String keyword, @Param("user")String user);
	
	public void updateKeyword(@Param("keyword")String keyword, @Param("removed")Boolean removed);
	
	public int existsKeyword(@Param("keyword")String keyword);
	
	public long getKeywordNextKey();
//	[B.RD20-65][Leason][2010/12/23][start]
	public List<String> validateExistKeywords(@Param("keywords") String[] keywords);
//	[B.RD20-65][Leason][2010/12/23][end]
	
//	[mos2.0-115][Leason][2012/04/12][start]
	public void insertSyncEffectTime(@Param("type")int type, @Param("minId")long minId, @Param("maxId")long maxId);
	
	public void updateKeywordEffectTime(@Param("maxId")long maxId);
	
	public void clearSyncEffectTime();
//	[mos2.0-115][Leason][2012/04/12][end]
}
