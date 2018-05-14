package com.zt.tool.utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

/**
 * @author miya17071101
 *
 */
public class DbUtils {

	public static void main(String[] args) {
		PreparedStatement pstmt = null;
		try {

			pstmt = conn.prepareStatement("select 1");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {

				System.out.println("链接成功");
			}
		} catch (SQLException e) {
			System.out.println("链接失败");
			e.printStackTrace();
		}

	}

	private static Logger logger = Logger.getLogger(DbUtils.class);

	public static Connection conn = null;

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

	public static void batchInsert(List<JSONObject> list) throws Exception {

		PreparedStatement pstmt = conn.prepareStatement(
				"insert into hh_log_data(userId,portVersion,`port`,`from`,action_time,reActivity_time,deviceId,appVersion,os,create_time,action_date) values(?,?,?,?,?,?,?,?,?,SYSDATE(),?)");
		for (int i = 0; i < list.size(); i++) {
			pstmt.setString(1, list.get(i).getString("userId"));
			pstmt.setString(2, list.get(i).getString("portVersion"));
			pstmt.setString(3, list.get(i).getString("port"));
			pstmt.setString(4, list.get(i).getString("from"));
			pstmt.setTimestamp(5, new Timestamp(list.get(i).getLong("timestamp")));
			pstmt.setTimestamp(6, list.get(i).getLong("reActivityTime") == null ? null : new Timestamp(list.get(i).getLong("reActivityTime")));
			pstmt.setString(7, list.get(i).getString("deviceId"));
			pstmt.setString(8, list.get(i).getString("version"));
			pstmt.setString(9, list.get(i).getString("os"));
			pstmt.setDate(10, new Date(list.get(i).getLong("timestamp")));
			pstmt.addBatch(); // 加入批量处理
		}
		pstmt.executeBatch();// 执行批量处理
		conn.commit();
		pstmt.close();
	}

	public static void deleteOldData() throws Exception {
		PreparedStatement pstmt = conn.prepareStatement("DELETE from hh_log_data where create_time<=DATE_SUB(NOW(),INTERVAL 3 MONTH )");
		pstmt.execute();
		conn.commit();
		pstmt.close();
	}

	public static String getPhoneById(String id) throws Exception {
		String phone = null;
		PreparedStatement pstmt = conn.prepareStatement("SELECT phone from hh_user where type=7 and id=?");
		pstmt.setString(1, id);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			phone = rs.getString(1);
		}
		return phone;
	}

	public static JSONObject getGoodByPubId(String pubId) throws Exception {
		JSONObject obj = null;
		PreparedStatement pstmt = conn.prepareStatement("select c.goodid,c.goodname from couponpublish a,ruledescribe b,goods c "
				+ " where a.getruleid =b.ruleid and b.goodid =c.goodid and a.pubid =?");
		pstmt.setString(1, pubId);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			obj = new JSONObject();
			obj.put("goodid", rs.getString(1));
			obj.put("goodname", rs.getString(2));
		}
		if (obj == null) {
			obj = new JSONObject();
			obj.put("goodid", "");
			obj.put("goodname", "");
		}
		return obj;
	}

	public static JSONObject getGoodByPubIdType(String pubId) throws Exception {
		JSONObject obj = null;
		PreparedStatement pstmt = conn.prepareStatement("select GROUP_CONCAT(d.goodid) as goodid,GROUP_CONCAT(d.goodname) as goodname"
				+ " from couponpublish a,ruledescribe b, coupon_goodgroup c,goods d where a.getruleid"
				+ " =b.ruleid and b.goodid =c.groupid and c.goodid =d.goodid and a.pubid =?");
		pstmt.setString(1, pubId);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			obj = new JSONObject();
			obj.put("goodid", rs.getString(1));
			obj.put("goodname", rs.getString(2));
		}
		if (obj == null) {
			obj = new JSONObject();
			obj.put("goodid", "");
			obj.put("goodname", "");
		}
		return obj;
	}

	public static List<Map<String, String>> getTableNames(String schemaName) {
		List<Map<String, String>> list = new ArrayList<>();
		try (PreparedStatement pstmt = conn
				.prepareStatement("select TABLE_NAME,TABLE_COMMENT " + "from information_schema.tables where table_schema='" + schemaName + "'")) {
			ResultSet rs = pstmt.executeQuery();
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
		StringBuffer sql = new StringBuffer();
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
		try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
			ResultSet rs = pstmt.executeQuery();
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
