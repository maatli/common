package com.nice.db.database;

import java.util.Map;

import soft.xiniu.common.utils.StringUtils;


public class SqlBuilder {
	public static final String TMP_FIELD_NAME = "tmp_field";
	public static final String FIELD_AUTO_ID = "auto_id";
	public static final int DEFAULT_TMP_FILED_COUNT = 10;
	
	/**
	 * 创建建表SQL
	 * @param tableName	表名
	 * @param fields	字段名和属性
	 * @param primaryKey	主键
	 * @param tmpFieldsCount	临时字段的数量
	 * @param autoId	是否需要自增id
	 * @param index	创建索引的字段
	 * @return
	 */
	public static String getCreatTableSQL(String tableName, Map<String, String> fields, String primaryKey, int tmpFieldsCount, boolean autoId,String[] index) {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("CREATE TABLE IF NOT EXISTS ");
		strSQL.append(tableName);
		strSQL.append(" ( ");			
		
		// 是否有自增ID
		if(true == autoId) {
			strSQL.append(FIELD_AUTO_ID);
			if ((false == StringUtils.isEmpty(primaryKey)) && primaryKey.equalsIgnoreCase(FIELD_AUTO_ID)) {
				strSQL.append(" " + "INTEGER primary key autoincrement");
			} else {
				strSQL.append(" " + "INTEGER autoincrement");
			}
			
			strSQL.append(", ");
		}
		
		// 判断主键
		if ((false == StringUtils.isEmpty(primaryKey)) && fields.containsKey(primaryKey)) {
			strSQL.append(primaryKey);
			strSQL.append(" " + fields.get(primaryKey));
			strSQL.append(" primary key");
			strSQL.append(", ");
			
			// 移除，防止重复
			fields.remove(primaryKey);
		}
		
		// 依次添加所有的字段
		for(String key:fields.keySet()) {
			strSQL.append(key);
			strSQL.append(" " + fields.get(key));
			strSQL.append(", ");
		}
		
		// 添加临时字段
		if(tmpFieldsCount > 0) {
			addTmpField(strSQL, tmpFieldsCount);
		} else {
			addTmpField(strSQL, DEFAULT_TMP_FILED_COUNT);	// 默认添加10个字段
		}
		
		return strSQL.toString();
	}
	
	/**
	 * 添加指定数量的临时字段，字段类型为NUMERIC
	 * @param sql
	 * @param filedCount
	 * @return
	 */
	private static void addTmpField(StringBuffer sql, int filedCount) {
		sql.append(", ");
		for (int i = 1; i < filedCount; i++) {
			String field = TMP_FIELD_NAME + i + " NUMERIC";
			if (i < filedCount - 1) {
				field += ", ";
			}

			sql.append(field);
		}

		sql.append(")");
	}
}
