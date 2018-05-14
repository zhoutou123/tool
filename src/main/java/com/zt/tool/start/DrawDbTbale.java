package com.zt.tool.start;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.zt.tool.utils.DbUtils;

/**
 * 绘制数据库表结构
 * 
 * @author miya17071101
 *
 */
public class DrawDbTbale {

	private static final String SECHMA_NAME = "haihang-db";

	private static FileWriter writer;

	public static void main(String[] args) throws Exception {
		writer = new FileWriter("C:\\Users\\miya17071101\\Desktop\\" + SECHMA_NAME + ".html", false);
		writeBegin();
		List<Map<String, String>> list = DbUtils.getTableNames(SECHMA_NAME);
		writeTableSunmmarize(list);
		for (Map<String, String> map : list) {
			writeTableInfo(map.get("a1"), map.get("a2"));
		}
		writeln("</div>");
		writeln("</body>");
		writeln("</html>");
		writer.flush();
		writer.close();
		DbUtils.close();
		System.out.println("绘制完毕");

	}

	private static void writeln(String html) throws IOException {
		writer.write(html);
		writer.write("\n");
	}

	private static void writeBegin() throws IOException {
		writeln("<html>");
		writeln("<head>");
		writeln("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
		writeln("<style type='text/css'>");
		writeln("body,td {font-family:verdana; font-size:12px; line-height:150%;}");
		writeln("table {width:100%; background-color:#ccc; margin:5px 0;}");
		writeln("td {background-color:#fff;padding:3px; padding-left:10px;}");
		writeln("thead td {text-align:center; font-weight:bold; background-color:#eee;}");
		writeln("a:link, a:visited, a:active { color: #015FB6;text-decoration: none; }a:hover { color: #E33E06; }");
		writeln("</style>");
		writeln("</head>");
		writeln("<body style=\"text-align:center;\">");
		writeln("<div style=\"width:800px; margin:20px auto; text-align:left;\">");
		writeln("<a name=\"index\">");
		writer.write("<h2 style=\"text-align:center; line-height:50px;\">");
		writer.write("数据库设计文档");
		writeln("</h2>");
		writeln("<div>");
		writer.write("<b>");
		writer.write("数据库名：");
		writer.write(SECHMA_NAME);
		writeln("</b>");
		writeln("</div>");
	}

	public static void writeTableSunmmarize(List<Map<String, String>> list) throws Exception {
		writeln("<table cellspacing=\"1\" cellpadding=\"0\">");
		writeln("<thead>");
		writeln("<tr>");
		writer.write("<td style=\"width:40px; \">");
		writer.write("序号");
		writeln("</td>");
		writer.write("<td>");
		writer.write("表名");
		writeln("</td>");
		writer.write("<td>");
		writer.write("表名");
		writeln("</td>");
		writeln("</tr>");
		writeln("</thead>");
		writeln("<tbody>");
		int i = 1;
		for (Map<String, String> map : list) {
			writeln("<tr>");
			writer.write("<td style=\"text-align:center;\">");
			writer.write(String.valueOf(i++));
			writeln("</td>");
			writeSummarizeTd(map);
			writeln("</tr>");
		}
		writeln("</tbody>");
		writeln("</table>");
		writeln("</a>");
	}

	private static void writeSummarizeTd(Map<String, String> map) throws IOException {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if ("a1".equals(entry.getKey())) {
				writeln("<td>");
				writer.write("<a href=\"#");
				writer.write(entry.getValue());
				writeln("\">");
				writer.write(entry.getValue());
				writeln("</a>");
				writeln("</td>");
			} else {
				writer.write("<td>");
				writer.write(entry.getValue());
				writeln("</td>");
			}
		}
	}

	private static void writeTableInfo(String tableName, String tableComment) throws IOException {
		List<Map<String, String>> list = DbUtils.getTableInfo(tableName, SECHMA_NAME);
		writer.write("<a name=\"");
		writer.write(tableName);
		writeln("\"></a>");
		writeln("<div style=\"margin-top:30px;\">");
		writer.write("<a name=\"");
		writer.write(tableName);
		writeln("\"></a>");
		writer.write("<a href=\"#index\" style=\"float:right; margin-top:6px;\">");
		writer.write("返回目录");
		writeln("</a>");
		writer.write("<b>");
		writer.write("表名：");
		writer.write(tableName);
		writeln("</b>");
		writeln("</div>");
		writer.write("<div>");
		writer.write("说明：");
		writer.write(tableComment);
		writeln("</div>");
		writer.write("<div>");
		writer.write("数据列：");
		writeln("</div>");
		writeln("<table cellspacing=\"1\" cellpadding=\"0\">");
		writeln("<thead>");
		writeln("<tr>");
		writer.write("<td style=\"width:40px; \">");
		writer.write("序号");
		writeln("</td>");
		writer.write("<td>");
		writer.write("名称");
		writeln("</td>");
		writer.write("<td>");
		writer.write("数据类型");
		writeln("</td>");
		writer.write("<td>");
		writer.write("长度");
		writeln("</td>");
		writer.write("<td>");
		writer.write("小数位");
		writeln("</td>");
		writer.write("<td>");
		writer.write("允许空值");
		writeln("</td>");
		writer.write("<td>");
		writer.write("主键");
		writeln("</td>");
		writer.write("<td>");
		writer.write("默认值");
		writeln("</td>");
		writer.write("<td>");
		writer.write("说明");
		writeln("</td>");
		writeln("</tr>");
		writeln("</thead>");
		writeln("<tbody>");
		int i = 1;
		for (Map<String, String> map : list) {
			writeln("<tr>");
			writer.write("<td style=\"text-align:center;\">");
			writer.write(String.valueOf(i++));
			writeln("</td>");
			writeTbaleInfoTd(map);
			writeln("</tr>");
		}
		writeln("</tbody>");
		writeln("</table>");
	}

	private static void writeTbaleInfoTd(Map<String, String> map) throws IOException {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if ("a1".equals(entry.getKey())) {
				writer.write("<td>");
				writer.write(entry.getValue());
				writeln("</td>");
			} else {
				writer.write("<td align=\"center\">");
				writer.write(entry.getValue());
				writeln("</td>");
			}
		}
	}
}
