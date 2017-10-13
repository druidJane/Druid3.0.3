/**
 * created at 2011-7-28
 */
package com.xuanwu.msggate.common.util;

import java.util.Map;

import com.thoughtworks.xstream.XStream;

/**
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2011-7-28
 * @Version 1.0.0
 */
public class XmlUtil {
	
	private static final XStream xstream = new XStream();

	private static final String MAP_OPEN = "<map>";
	private static final String MAP_CLOSE = "</map>";
	private static final String MAP_EMPTY = "<map/>";
	private static final String ENTRY_OPEN = "<entry>";
	private static final String ENTRY_CLOSE = "</entry>";
	private static final String VALUE_OPEN = "<string>";
	private static final String VALUE_CLOSE = "</string>";
	
	public static Object fromXML(String xml){
		return xstream.fromXML(xml);
	}
	
	@SuppressWarnings("unchecked")
	public static String toXML(Object obj){
		Map<String, Object> map = (Map<String, Object>)obj;
		if(map == null || map.isEmpty()){
			return MAP_EMPTY;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(MAP_OPEN);
		for(Map.Entry<String, Object> entry : map.entrySet()){
			addEntry(sb, entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString());
		}
		sb.append(MAP_CLOSE);
		return sb.toString();
	}
	
	public static String addParam(String key, String value, String srcStr){
		if(srcStr == null || srcStr.startsWith(MAP_EMPTY)){
			StringBuilder sb = new StringBuilder();
			sb.append(MAP_OPEN);
			addEntry(sb, key, value);
			sb.append(MAP_CLOSE);
			return sb.toString();
		}
		
		if(srcStr.startsWith(MAP_OPEN)){
			StringBuilder sb = new StringBuilder();
			sb.append(MAP_OPEN);
			addEntry(sb, key, value);
			sb.append(srcStr.substring(MAP_OPEN.length()));
			return sb.toString();
		}
		
		return srcStr;
	}
	
	private static void addEntry(StringBuilder sb, String key, String value){
		if(key == null || value == null){
			throw new IllegalArgumentException("key or value is null");
		}
		sb.append(ENTRY_OPEN);
		sb.append(VALUE_OPEN);
		escapeElementEntities(sb, key);
		sb.append(VALUE_CLOSE);
		sb.append(VALUE_OPEN);
		escapeElementEntities(sb, value);
		sb.append(VALUE_CLOSE);
		sb.append(ENTRY_CLOSE);
	}
	
	private static void escapeElementEntities(StringBuilder sb, String text){
		for(int i = 0; i < text.length(); i++){
			char c = text.charAt(i);
			switch(c){
			case '<':
                sb.append("&lt;");
                break;
            case '>':
            	sb.append("&gt;");
                break;
            case '&':
            	sb.append("&amp;");
                break;
            default:
            	sb.append(c);
			}
		}
	}
}
