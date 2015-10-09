package soft.xiniu.common.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据库操作工具类
 */
public class DBUtils {

    /**
     * 执行一条sql语句
     * @param db
     * @param sql
     * @param parameters
     */
    public static void executeSQL(SQLiteDatabase db, String sql, Object[] parameters) {
        if(db == null) {
            return;
        }
        checkProcessable(db);

        if(parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                if(parameter != null && parameter instanceof Date) {
                    parameters[i] = toDbTime((Date) parameter);
                } else if(parameter != null && parameter instanceof Boolean) {
                    parameters[i] = ((Boolean)parameter) ? 1 : 0;
                } else if(parameter != null && parameter instanceof Object[]) {
                    parameters[i] = StringUtils.join((Object[]) parameter, ",");
                }
            }
        }

        db.execSQL(sql, parameters);
    }

    /**
     * 执行无参sql语句
     * @param db
     * @param sql
     */
    public static void executeSQL(SQLiteDatabase db, String sql) {
        if(db == null) {
            return;
        }

        checkProcessable(db);
        db.execSQL(sql);
    }

    /**
     * 查询，返回结果集游标
     * @param sql
     * @param params
     * @return
     */
    public static Cursor query(SQLiteDatabase db, String sql, String[] params) {
        if(db == null) {
            return null;
        }

        return db.rawQuery(sql, params);
    }

    /**
     * 验证数据库是否可访问和可写
     */
    public static void checkProcessable(SQLiteDatabase db) {
        checkAccessable(db);

        if(db.isReadOnly()) {
            throw new RuntimeException(new SQLiteException("db is read only"));
        }
    }

    /**
     * 验证数据库是否可访问（未建立连接或连接已关闭）
     */
    public static void checkAccessable(SQLiteDatabase db) {
        if(db == null) {
            throw new RuntimeException(new NullPointerException("db is null"));
        }

        if(!db.isOpen()) {
            throw new RuntimeException(new SQLiteException("db is already closed"));
        }
    }

    /**
     * 根据键、值集合，返回字段列表。并参照某个字段排序。支持分页操作。
     * @param selectField 需要从数据库查询的字段
     * @param fields 字段
     * @param params 参数
     * @param orderField 排序字段
     * @param asc 是否为升序。true表示升序，false为降序
     * @param pageStartIndex 分页字段：开始位置
     * @param offset 分页字段：要获取的记录条数
     * @return
     */
    public static List<String> list(SQLiteDatabase db, String tableName, String selectField, String[] fields, String[] params, String orderField, boolean asc, int pageStartIndex, int offset) {
        List<String> list = new ArrayList<String>();

        if(db == null) {
            return list;
        }

        StringBuilder sql = new StringBuilder("select "+selectField+" from "+ tableName + " "); // + " limit " + pageStartIndex + "," + offset +

        int fieldCount = fields.length;
        if(fieldCount > 0) {
            sql.append("where ");
        }

        for(int i=0; i<fieldCount;) {
            sql.append(fields[i]).append("=? ");
            if(++i < fieldCount) {
                sql.append("and ");
            }
        }

        sql.append(" order by ").append(orderField);
        if(!asc) {
            sql.append(" asc");
        } else {
            sql.append(" desc");
        }

        sql.append(" limit ").append(pageStartIndex).append(",").append(offset);

        Cursor cursor = query(db, sql.toString(), params);

        while(cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }
        cursor.close();

        return list;
    }

    public static String[] arrayFiled(SQLiteDatabase db, String tableName, String field, String[] fileds, String[] params) {
        StringBuilder sql = new StringBuilder("select "+field+" from " + tableName + " ");

        int fieldCount = fileds.length;
        if(fieldCount > 0) {
            sql.append("where ");
        }
        for(int i=0; i<fieldCount;) {
            sql.append(fileds[i]).append("=? ");
            if(++i < fieldCount) {
                sql.append("and ");
            }
        }

        List<String> list = new ArrayList<String>();
        Cursor cursor = query(db, sql.toString(), params);
        while(cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }
        cursor.close();

        String[] arrays = new String[list.size()];

        return list.toArray(arrays);
    }

    public static String minField(SQLiteDatabase db, String tableName, String filed) {
        String sql = "select min("+filed+") from " + tableName;
        Cursor cursor = query(db, sql, new String[]{});
        if(cursor.moveToNext()) {
            return cursor.getString(0);
        }
        return null;
    }

    public static String maxField(SQLiteDatabase db, String tableName, String selectedField, String maxFiled, String[] fields, String[] values) {
        if(db == null) {
            return null;
        }

        StringBuilder sql = new StringBuilder("select "+selectedField+" from " + tableName + " where " + maxFiled + "=" + "(select max("+maxFiled+") from "+ tableName +" ");
        int fieldCount = fields.length;
        if(fieldCount > 0) {
            sql.append("where ");
        }
        for(int i=0; i<fieldCount;) {
            sql.append(fields[i]).append("=? ");
            if(++i < fieldCount) {
                sql.append("and ");
            }
        }

        sql.append(")");

        Cursor cursor = query(db, sql.toString(), values);
        try {
            if(cursor.moveToFirst()) {
                String value = cursor.getString(0);
                return value;
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    public static String getFiled(SQLiteDatabase db, String tableName, String getFiledName, String[] fields, String[] values) {
        if(db == null) {
            return null;
        }

        StringBuilder sql = new StringBuilder("select "+getFiledName+" from "+ tableName + " ");
        int fieldCount = fields.length;
        if(fieldCount > 0) {
            sql.append("where ");
        }
        for(int i=0; i<fieldCount;) {
            sql.append(fields[i]).append("=? ");
            if(++i < fieldCount) {
                sql.append("and ");
            }
        }
        Cursor cursor = query(db, sql.toString(), values);
        String value = null;
        if(cursor.moveToNext()) {
            value =  cursor.getString(0);
        }
        cursor.close();
        return value;
    }

    /**
     * 删除，按指定字段
     * @param field
     * @param value
     */
    public static void delete(SQLiteDatabase db, String tableName, String field, Object value) {
        if(db == null) {
            return;
        }

        String sql = "delete from "+ tableName +" where "+ field +"=?";
        executeSQL(db, sql, new Object[]{value.toString()});
    }

    /**
     * 删除，按指定字段
     * @param fields
     * @param values
     */
    public static void delete(SQLiteDatabase db, String tableName, String[] fields, Object[] values) {
        if(db == null) {
            return;
        }

        StringBuilder sql = new StringBuilder("delete from "+ tableName + " ");
        int fieldCount = fields.length;
        if(fieldCount > 0) {
            sql.append("where ");
        }
        for(int i=0; i<fieldCount;) {
            sql.append(fields[i]).append("=? ");
            if(++i < fieldCount) {
                sql.append("and ");
            }
        }

        executeSQL(db, sql.toString(), values);
    }

    /**
     *  按照条件删除对象
     *  例如：listBySelection("username=? and password=?", String[] {"wjh", "wjh123"})
     *
     * @param where 条件
     * @param params 参数，如果没有，则传入一个空的数组
     */
    public static void deleteBySelection(SQLiteDatabase db, String tableName, String where, Object[] params) {
        if(db == null) {
            return;
        }

        StringBuilder sql = new StringBuilder("delete from "+ tableName + " ");

        sql.append(" where ").append(where);

        executeSQL(db, sql.toString(), params);
    }

    /**
     * 删除与values串里的值相等的字段的所有记录:delete from users where id in (111,222,333);
     *
     * @param field 字段
     * @param values 数组，如：{111,222,333},将自动转换为"111,222,333"样式的字符串
     */
    public static void delelteIn(SQLiteDatabase db, String tableName, String field, Object[] values) {
        if(db == null) {
            return;
        }

        StringBuilder sql = new StringBuilder("delete from "+ tableName + " ");
        sql.append(" where ").append(field)
                .append(" in ").append("(").append(StringUtils.join(values, "'", ",")).append(")");
        executeSQL(db, sql.toString());
    }

    /**
     * 删除所有记录
     */
    public static void deleteAll(SQLiteDatabase db, String tableName) {
        if(db == null) {
            return;
        }

        executeSQL(db, "delete from " + tableName);
    }

    public static int sum(SQLiteDatabase db, String tableName, String field, String[] where, String[] values) {
        if(db == null) {
            return 0;
        }

        StringBuilder sql = new StringBuilder("select sum("+field+") s from "+ tableName + " ");
        int fieldCount = where.length;
        if(fieldCount > 0) {
            sql.append("where ");
        }
        for(int i=0; i<fieldCount;) {
            sql.append(where[i]).append("=? ");
            if(++i < fieldCount) {
                sql.append("and ");
            }
        }
        int count = 0;
        Cursor c = query(db, sql.toString(), values);
        if(c.moveToFirst()) {
            count = c.getInt(0);
        }
        c.close();
        return count;
    }

    public static void updateFields(SQLiteDatabase db, String tableName, Map<String, Object> map, String[] whereFields, Object[] wherevalues) {
        String[] fields = new String[map.size()];
        Object[] values = new Object[map.size()];

        Set<Map.Entry<String,Object>> entrySet = map.entrySet();
        int i=0;
        for (Map.Entry<String, Object> entry : entrySet) {
            fields[i] = entry.getKey();
            values[i] = entry.getValue();
            i++;
        }

        updateField(db, tableName, fields, values, whereFields, wherevalues);
    }

    public static void updateField(SQLiteDatabase db, String tableName, String[] fields, Object[] values, String[] whereFields, Object[] wherevalues) {
        if(db == null) {
            return;
        }

        if(fields.length != values.length) {
            throw new SQLiteException("fields.length != values.length");
        }

        if(whereFields.length != wherevalues.length) {
            throw new SQLiteException("whereFields.length != wherevalues.length");
        }

        StringBuilder sql = new StringBuilder();
        sql.append("update "+ tableName +" set ");

        Object[] params = new Object[fields.length + whereFields.length];

        for(int i=0; i<fields.length;) {
            params[i] = values[i];

            sql.append(fields[i]).append("=? ");
            if(++i < fields.length) {
                sql.append(", ");
            }

        }

        int fieldCount = whereFields.length;
        if(fieldCount > 0) {
            sql.append(" where ");
        }
        for(int i=0; i<fieldCount;) {
            params[fields.length + i] = wherevalues[i];

            sql.append(whereFields[i]).append("=? ");
            if(++i < fieldCount) {
                sql.append("and ");
            }
        }


        executeSQL(db, sql.toString(), params);
    }

    public static void insertFields(SQLiteDatabase db, String tableName, Map<String, Object> map) {
        String[] fields = new String[map.size()];
        Object[] values = new Object[map.size()];

        Set<Map.Entry<String,Object>> entrySet = map.entrySet();
        int i=0;
        for (Map.Entry<String, Object> entry : entrySet) {
            fields[i] = entry.getKey();
            values[i] = entry.getValue();
            i++;
        }

        insert(db, tableName, fields, values);
    }

    public static void insert(SQLiteDatabase db, String tableName, String[] fields, Object[] values) {
        if(fields.length != values.length) {
            throw new SQLiteException("fields.length != values.length");
        }

        StringBuilder sql = new StringBuilder();
        StringBuilder params = new StringBuilder();

        sql.append("insert into "+ tableName +" (");

        for(int i=0; i<fields.length; i++) {
            sql.append(fields[i]);
            params.append("?");

            if(i < fields.length-1) {
                sql.append(", ");
                params.append(",");
            }
        }

        sql.append(") values (");
        sql.append(params);
        sql.append(") ");

        executeSQL(db, sql.toString(), values);
    }

    public static void updateIn(SQLiteDatabase db, String tableName, String field, Object newValue, Object oldValue, String arrayField, Object[] arrayValues) {
        StringBuilder sql = new StringBuilder();
        sql.append("update " + tableName + " set ")
                .append(field + "=? ")
                .append(" where " + field + "=? ")
                .append(" and " + arrayField + " in (" + StringUtils.join(arrayValues, "'", ",") + ")");

        executeSQL(db, sql.toString(), new Object[]{newValue, oldValue});
    }

    public static void updateIn(SQLiteDatabase db, String tableName, String field, Object value, String arrayField, Object[] arrayValues) {
        StringBuilder sql = new StringBuilder();
        sql.append("update " + tableName + " set ")
                .append(field +"=? ")
                .append(" where " + arrayField + " in (" + StringUtils.join(arrayValues, "'", ",") + ")");

        executeSQL(db, sql.toString(), new Object[]{value});
    }

    private static Field getDeclaredField(Class clazz, String name) {
        Field field= null;
        try {
            field = clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
        }

        if(field == null && clazz.getSuperclass() != null) {
            return getDeclaredField(clazz.getSuperclass(), name);
        }
        return field;
    }

    /**
     *  直接删除表
     */
    public static void drop(SQLiteDatabase db, String tableName) {
        if(db == null) {
            return;
        }

        executeSQL(db, "DROP TABLE IF EXISTS " + tableName);
    }

    public static boolean checkExsit(SQLiteDatabase db, String tableName, String filed, String value) {
        if(db == null) {
            return false;
        }

        return count(db, tableName, new String[]{filed}, new String[]{value}) > 0;
    }

    /**
     * 根据筛选条件，获取记录条数
     *
     * @param fields 筛选字段集合
     * @param params 筛选字段对应的值的集合
     *
     * @return 记录条数。没有对应记录返回为0
     */
    public static int count(SQLiteDatabase db, String tableName, String[] fields, String[] params) {
        if(db == null) {
            return 0;
        }

        StringBuilder sql = new StringBuilder("select count(*) c from "+ tableName + " ");
        int fieldCount = fields.length;
        if(fieldCount > 0) {
            sql.append("where ");
        }
        for(int i=0; i<fieldCount;) {
            sql.append(fields[i]).append("=? ");
            if(++i < fieldCount) {
                sql.append("and ");
            }
        }

        int count = 0;
        Cursor c = query(db, sql.toString(), params);
        if(c.moveToFirst()) {
            count = c.getInt(0);
        }
        c.close();
        return count;
    }


    public static int countIn(SQLiteDatabase db, String tableName, String field, String[] params) {
        String sql = "select count(*) c from "+ tableName +" where " + field + " in ("+StringUtils.join(params, "'", ",")+") ;";
        Cursor c = query(db, sql.toString(), new String[]{});
        int count = 0;
        if(c.moveToFirst()) {
            count = c.getInt(0);
        }
        c.close();

        return count;
    }

    public static int countIn(SQLiteDatabase db, String tableName, String field, String[] params, String[] whereFields, String[] whereParams) {
        StringBuilder sql = new StringBuilder("select count(*) c from "+ tableName +" where " + field + " in (" + StringUtils.join(params, "'", ",") + ") ");
        int fieldCount = whereFields.length;
        if(fieldCount > 0) {
            sql.append(" and ");
        }
        for(int i=0; i<fieldCount;) {
            sql.append(whereFields[i]).append("=? ");
            if(++i < fieldCount) {
                sql.append(" and ");
            }
        }

        Cursor c = query(db, sql.toString(), whereParams);
        int count = 0;
        if(c.moveToFirst()) {
            count = c.getInt(0);
        }
        c.close();

        return count;
    }

    /**
     * 日期转换成毫秒数值
     * @param date
     * @return
     */
    public static long toDbTime(Date date) {
        if(date == null) {
            return 0L;
        }

        return date.getTime();
    }

    /**
     * 毫秒数值转换成日期
     * @param time
     * @return
     */
    public static Date toDate(long time) {
        if(time <= 0) {
            return null;
        }

        return new Date(time);
    }

    public static int getInt(Cursor cursor, String field) {
        int c = cursor.getColumnIndex(field);
        if(c >= 0) {
            return cursor.getInt(c);
        }
        return -1;
    }

    public static long getLong(Cursor cursor, String field) {
        int c = cursor.getColumnIndex(field);
        if(c >= 0) {
            return cursor.getLong(c);
        }
        return -1;
    }

    public static String getString(Cursor cursor, String field) {
        int c = cursor.getColumnIndex(field);
        if(c >= 0) {
            return cursor.getString(c);
        }
        return null;
    }

    public static Date getDate(Cursor cursor, String field) {
        return toDate(getLong(cursor, field));
    }

    public static boolean getBoolean(Cursor cursor, String field) {
        return getInt(cursor, field) == 1;
    }

    public static float getFloat(Cursor cursor, String field) {
        int c = cursor.getColumnIndex(field);
        if(c >= 0) {
            return cursor.getFloat(c);
        }
        return -1;
    }

    public static String[] getStringArray(Cursor cursor, String field) {
        String string = getString(cursor, field);

        if(string != null & string.length() > 0) {
            return StringUtils.str2Arr(string, ",");
        } else {
            return null;
        }
    }

    public static String addTmpField() {
        return addTmpField(10);
    }

    /**
     * 创建表格的时候使用，拼接在原来的sql语句中括号中
     * 假设原sql语句为 "CREATE TABLE IF NOT EXISTS " + TABLENAME + "(" + field_id + " VARCHAR primary key)"
     * 调用本函数添加临时字段："CREATE TABLE IF NOT EXISTS " + TABLENAME + "(" + field_id + " VARCHAR primary key" + addTmpField(10) + ")"
     *
     * @param filedCount
     * @return 结果sql语句
     */
    public static String addTmpField(int filedCount) {
        String sql = "";
        for (int i = 1; i < filedCount; i++) {
            String field = ", ";
            field += DBFIELD_TMP + i + " VARCHAR";

            sql += field;
        }

        return sql;
    }

    public final static String DBFIELD_TMP = "field_";
}
