package com.xuanwu.mos.domain.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.xuanwu.mos.domain.handler.HasIndexValue;
import com.xuanwu.mos.utils.EnumUtil;

import org.apache.commons.lang3.EnumUtils;

import java.io.IOException;
import java.util.UnknownFormatConversionException;

/**
 * 方便 直接将 post中的json字段 转换成相应的 enum
 * 
 * @author <a href="kangqinghua@wxchina.com">Qinghua Kang</a>
 * @version 1.0.0
 * @date 2016/9/18 18:07:01
 */

public class HasIndexValueEnumDeSerializer<E extends Enum<E> & HasIndexValue> extends StdDeserializer<E>
		implements ContextualDeserializer {

	private static final long serialVersionUID = 1L;
	private Class<E> type;

	public HasIndexValueEnumDeSerializer() {
		this(null);
	}

	public HasIndexValueEnumDeSerializer(Class<E> t) {
		super(t);
		type = t;
	}

	@Override
	public E deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
		JsonNode node = jp.getCodec().readTree(jp);
		E matchedEnum = null;
		if (node.isValueNode()) {
			if (node.isInt()) {
				matchedEnum = deserializeUseIntNode((IntNode) node);
			} else if (node.isTextual()) {
				matchedEnum = deserializeUseTextNode((TextNode) node);
			}
		} else {
			TextNode textNode = (TextNode) node.get("name");
			matchedEnum = deserializeUseTextNode(textNode);

			if (matchedEnum == null) {
				JsonNode shouldBeIntJsonNode = node.get("index");
				if (null == shouldBeIntJsonNode || shouldBeIntJsonNode.isNull()) {// 之前有value
					shouldBeIntJsonNode = node.get("value");
				}
				if (shouldBeIntJsonNode.isInt()) {
					matchedEnum = deserializeUseIntNode((IntNode) shouldBeIntJsonNode);
				} else {
					matchedEnum = deserializeUseTextNode((TextNode) shouldBeIntJsonNode);
				}
			}
		}
		if (null == matchedEnum) {
			throw new UnknownFormatConversionException("unknown " + type.getCanonicalName() + " type");
		} else {
			return matchedEnum;
		}
	}

	private E deserializeUseIntNode(IntNode intNode) {
		try {
			if (intNode == null || intNode.isNull()) {
				return null;
			}
			return EnumUtil.indexMapping(type).get(intNode.intValue());
		} catch (Exception ex) {
			return null;
		}
	}

	// enum textNode can not match,may be its index,parse it to integer,and
	private E deserializeUseTextNode(TextNode textNode) {
		try {
			if (textNode == null || textNode.isNull()) {
				return null;
			}
			E matchedEnum = EnumUtils.getEnumMap(type).get(textNode.textValue());
			if (matchedEnum == null) {
				Boolean parsableIntString = textNode.textValue().matches("\\d+");
				matchedEnum = parsableIntString
						? EnumUtil.indexMapping(type).get(Integer.parseInt(textNode.textValue())) : null;
			}
			return matchedEnum;
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
			throws JsonMappingException {
		// Class<E> clazz = (Class<E>) ctxt.getContextualType().getRawClass();
		this.type = (Class<E>) ctxt.getContextualType().getRawClass();
		return this;
	}

}
