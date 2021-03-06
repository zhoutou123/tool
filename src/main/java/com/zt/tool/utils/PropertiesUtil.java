package com.zt.tool.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author miya17071101
 *
 */
public class PropertiesUtil {

	/**
	 * 私有化Properties对象
	 */
	private Properties prop;

	private static PropertiesUtil rpf = new PropertiesUtil();

	public static PropertiesUtil getInstance() {
		return rpf;
	}

	/**
	 * 读取文件的路径
	 * 
	 * @param url
	 */
	public void setPropertiesDataSource(String url) {
		prop = new Properties();
		InputStream in = getClass().getResourceAsStream(url);
		BufferedReader bf = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		try {
			prop.load(bf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过key值获取文件的int类型数据
	 * 
	 * @param key
	 * @return
	 */
	public Integer getInteger(String key) {
		return Integer.parseInt(prop.getProperty(key));
	}

	/**
	 * 通过key值获取文件的String类型数据
	 * 
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return prop.getProperty(key);
	}

	/**
	 * 通过key值获取文件的double类型数据
	 * 
	 * @param key
	 * @return
	 */
	public Double getDouble(String key) {
		return Double.parseDouble(prop.getProperty(key));
	}

	/**
	 * 通过key值获取文件的boolean类型数据
	 * 
	 * @param key
	 * @return
	 */
	public Boolean getBoolean(String key) {
		return Boolean.parseBoolean(prop.getProperty(key));
	}

	public static void main(String[] args) {
		PropertiesUtil a = new PropertiesUtil();
		a.setPropertiesDataSource("/redis.properties");
		System.out.println(a.getString("redis.getSelect"));
	}
}