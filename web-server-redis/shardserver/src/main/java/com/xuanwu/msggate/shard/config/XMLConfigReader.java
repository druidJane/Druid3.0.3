package com.xuanwu.msggate.shard.config;

import com.xuanwu.msggate.common.util.StringUtil;
import com.xuanwu.msggate.shard.entity.Column;
import com.xuanwu.msggate.shard.entity.ColumnType;
import com.xuanwu.msggate.shard.entity.ItemType;
import com.xuanwu.msggate.shard.entity.ParamInfo;
import com.xuanwu.msggate.shard.entity.ParamItem;
import com.xuanwu.msggate.shard.entity.ParamType;
import com.xuanwu.msggate.shard.entity.ShardMapper;
import com.xuanwu.msggate.shard.entity.ShardRule;
import com.xuanwu.msggate.shard.entity.SqlInfo;
import com.xuanwu.msggate.shard.entity.Table;
import com.xuanwu.msggate.shard.util.Delimiters;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2013-09-05
 * @Version 1.0.0
 */
public class XMLConfigReader {
	
	@SuppressWarnings("unchecked")
	public static List<ShardMapper> loadMapperConfig() throws Exception {
		SAXReader reader = new SAXReader();
		ignoreDTDValidate(reader);
		Document document = reader.read(new ClassPathResource("shard-mapper-config.xml").getInputStream());
		Element root = document.getRootElement();
		List<Element> mapperEles = (List<Element>)root.element("mappers").elements("mapper");
		
		List<ShardMapper> mappers = new ArrayList<ShardMapper>();
		for(Element mapperEle : mapperEles){
			String resource = mapperEle.attributeValue("resource");
			mappers.add(loadSqlMapper(resource));
		}
		return mappers;
	}
	
	@SuppressWarnings("unchecked")
	private static ShardMapper loadSqlMapper(String resource) throws Exception{
		SAXReader reader = new SAXReader();
		ignoreDTDValidate(reader);
		Document document = reader.read(new ClassPathResource(resource).getInputStream());
		Element root = document.getRootElement();
		List<Element> sqlEles = (List<Element>)root.elements("sql");

		ShardMapper mapperInfo = new ShardMapper();
		mapperInfo.setNamespace(root.attributeValue("namespace"));
		for(Element sqlEle : sqlEles){
			SqlInfo sqlInfo = new SqlInfo();
			String id = sqlEle.attributeValue("id");
			sqlInfo.setId(id);
			String cacheStr = sqlEle.attributeValue("cache");
			if(!StringUtil.isBlank(cacheStr)){
				sqlInfo.setCache(Boolean.valueOf(cacheStr));
			}
			
			String[] refTables = sqlEle.attributeValue("refTables").split(Delimiters.COMMA);
			for(String table : refTables){
				sqlInfo.addRefTable(table.trim());
			}
			
			Element paramEle = sqlEle.element("param");
			String paramName = paramEle.attributeValue("name");
			String paramType = paramEle.attributeValue("type");
			
			ParamInfo paramInfo = new ParamInfo();
			paramInfo.setName(paramName);
			paramInfo.setType(ParamType.forCode(paramType));
			 
			List<Element> attrEles = (List<Element>)paramEle.elements("item");
			for(Element attrEle : attrEles){
				ParamItem item = new ParamItem();
				String itemName = attrEle.attributeValue("name");
				String itemType = attrEle.attributeValue("type");
				String columnName = attrEle.attributeValue("column");
				String enumIndex = attrEle.attributeValue("enumIndex");
				String replace = attrEle.attributeValue("replace");
				
				item.setName(itemName);
				item.setColumnName(columnName);
				item.setType(ItemType.forCode(itemType));
				item.setEnumIndex(enumIndex);
				item.setReplace(replace);
				paramInfo.putItem(item);
			}
			 
			sqlInfo.setParam(paramInfo);
			mapperInfo.addSqlInfo(sqlInfo);
		}
		return mapperInfo;
	}
	
	@SuppressWarnings("unchecked")
	public static ShardRule loadShardConfig() throws Exception {
		SAXReader reader = new SAXReader();
		ignoreDTDValidate(reader);
		Document document = reader.read(new ClassPathResource("shard-config.xml").getInputStream());
		Element root = document.getRootElement();
		List<Element> tabEles = (List<Element>)root.elements("table");
		ShardRule shardRule = new ShardRule();
		for(Element tabEle : tabEles){
			String name = tabEle.attributeValue("name");
	
			Table table = new Table();
			table.setName(name);
			table.setColumns(parseColumn(tabEle));
			shardRule.putTable(table);
		}
		
		return shardRule;
	}
	
	@SuppressWarnings("unchecked")
	private static List<Column> parseColumn(Element tabEle){
		List<Column> columns = new ArrayList<Column>();
		List<Element> colEles = (List<Element>)tabEle.elements("column");
		for(Element colEle : colEles){
			Column column = new Column();
			String columnName = colEle.attributeValue("name");
			ColumnType type = ColumnType.forCode(colEle.attributeValue("type"));
			String spilt = colEle.attributeValue("spilt");
			spilt = (StringUtil.isBlank(spilt)) ? "" : spilt;
			String format = "";
			if(type == ColumnType.DATE)
				format = colEle.attributeValue("format");
			
			List<Element> valEles = (List<Element>)colEle.elements("value");
			for(Element valEle : valEles){
				String alias = valEle.attributeValue("alias");
				String valueStr = valEle.getTextTrim();
				String[] valueArr = valueStr.split(Delimiters.COMMA);
				for(String val : valueArr){
					if(StringUtil.isBlank(val))
						continue;
					column.putAlias(val, alias);
				}
			}
			
			column.setName(columnName);
			column.setType(type);
			column.setSpilt(spilt);
			column.setFormat(format);
			columns.add(column);
		}
		return columns;
	}
	
	private static void ignoreDTDValidate(SAXReader reader) throws SAXException{
		reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
	}
}
