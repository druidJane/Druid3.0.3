package com.xuanwu.mos.domain.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.xuanwu.mos.domain.handler.HasIndexValue;
import com.xuanwu.mos.utils.EnumUtil;

/**
 * @author <a href="kangqinghua@wxchina.com">Qinghua Kang</a>
 * @version 1.0.0
 * @description: todo
 * @date 2016/9/18 18:07:01
 */

public class HasIndexValueEnumSerializer<E extends Enum<E> & HasIndexValue> extends StdSerializer<E> {

	private static final long serialVersionUID = 1L;

	public HasIndexValueEnumSerializer() {
		this(null);
	}

	public HasIndexValueEnumSerializer(Class<E> t) {
		super(t);
	}

	public void serialize(E havIndexValueEnumObj, JsonGenerator generator, SerializerProvider provider)
			throws IOException {
		generator.writeStartObject();
		generator.writeFieldName("index");
		generator.writeNumber(havIndexValueEnumObj.getIndex());
		generator.writeFieldName("name");
		generator.writeString(havIndexValueEnumObj.name());
		generator.writeFieldName("label");
		generator.writeString(EnumUtil.getEnumLabel(havIndexValueEnumObj));
		generator.writeEndObject();
	}
}