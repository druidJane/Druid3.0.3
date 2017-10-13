package com.xuanwu.mos.domain.handler;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xuanwu.mos.domain.serializer.HasIndexValueEnumDeSerializer;
import com.xuanwu.mos.domain.serializer.HasIndexValueEnumSerializer;

import java.io.Serializable;

/**
 * index是数据库里面存储的值 ,label是数据库对应的说明 有时候可避免在前端 进行If else判断 强制枚举类型 有合法的 说明
 * 
 * @author <a href="kangqinghua@wxchina.com">Qinghua Kang</a>
 * @version 1.0.0
 * @date 2016/9/13 17:21:01
 */
@JsonDeserialize(using = HasIndexValueEnumDeSerializer.class)
@JsonSerialize(using = HasIndexValueEnumSerializer.class)
public interface HasIndexValue extends Serializable {

	public int getIndex();

	public static final int LEVEL_1_VALUE = 10000;
	public static final int LEVEL_2_VALUE = 20000;
	public static final int LEVEL_3_VALUE = 30000;
}
