package com.zt.tool.utils;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author miya17071101
 */
public class DbUtils {

    private static Connection conn = null;
    private static Logger logger = Logger.getLogger(DbUtils.class);

    static {
        try {
            PropertiesUtil a = new PropertiesUtil();
            a.setPropertiesDataSource("/jdbc.properties");
            String dbDriver = a.getString("jdbc.driver");
            String dbUrl = a.getString("jdbc.url");
            String dbUser = a.getString("jdbc.username");
            String dbPass = a.getString("jdbc.password");
            // 指定连接类型
            Class.forName(dbDriver);
            // 获取连接
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            conn.setAutoCommit(false);
        } catch (Exception e) {
            logger.error("数据库连接失败" + e.getMessage());
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try (PreparedStatement pstmt = conn.prepareStatement("select 1");
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                System.out.println("链接成功");
            }
        } catch (SQLException e) {
            System.out.println("链接失败");
            e.printStackTrace();
        }

    }

    public static void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            logger.error("数据库连接关闭失败" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Map<String, String>> getTableNames(String schemaName) {
        List<Map<String, String>> list = new ArrayList<>();
        try (PreparedStatement pstmt = conn
                .prepareStatement(
                        "select TABLE_NAME,TABLE_COMMENT " + "from information_schema.tables where table_schema='"
                                + schemaName + "'");
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, String> map = new HashMap<>(3);
                map.put("a1", rs.getString(1));
                map.put("a2", rs.getString(2));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Map<String, String>> getTableInfo(String tableName, String schemaName) {
        List<Map<String, String>> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT");
        sql.append(" b.COLUMN_NAME,");
        sql.append(" b.COLUMN_TYPE,");
        sql.append(" IFNULL( b.CHARACTER_MAXIMUM_LENGTH, IFNULL(b.NUMERIC_PRECISION, 0)),");
        sql.append(" IFNULL(b.NUMERIC_SCALE, 0),");
        sql.append(" b.IS_NULLABLE,");
        sql.append(" b.COLUMN_KEY,");
        sql.append(" IFNULL(IF(b.COLUMN_DEFAULT='','empty string',b.COLUMN_DEFAULT),''),");
        sql.append(" b.COLUMN_COMMENT ");
        sql.append("FROM");
        sql.append(" information_schema. COLUMNS b ");
        sql.append("WHERE");
        sql.append("  b.table_schema = '");
        sql.append(schemaName);
        sql.append("'");
        sql.append(" AND b.TABLE_NAME = '");
        sql.append(tableName);
        sql.append("'");
        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString());
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, String> map = new HashMap<>(11);
                map.put("a1", rs.getString(1));
                map.put("a2", rs.getString(2));
                map.put("a3", rs.getString(3));
                map.put("a4", rs.getString(4));
                map.put("a5", rs.getString(5));
                map.put("a6", rs.getString(6));
                map.put("a7", rs.getString(7));
                map.put("a8", rs.getString(8));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
