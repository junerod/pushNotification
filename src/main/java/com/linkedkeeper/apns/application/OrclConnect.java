package com.linkedkeeper.apns.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class OrclConnect {
	private static String driver = "oracle.jdbc.driver.OracleDriver";
	// 1521是主端口，也可能是其它端口去连接oracle数据库
	//private static String url = "jdbc:oracle:thin:@10.207.248.24:1521:ZZHermas";
	private static String url = "jdbc:oracle:thin:@10.207.248.24:1521:ZZHermas";
	private static String username = "F3102565";
	private static String password = "F3102565";
	private static Connection conn;
	private static Statement stmt;
	private static ResultSet rs;
	// 注册数据库驱动
	static {
		try {
			Class.forName(driver);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("oracle驱动注册失败");
		}
	}

	// 获取数据库连接
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("oracle connection lost.");
		}
		return conn;
	}

	// 关闭连接对象
	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Connection Close Exception!");
			}
		}
	}

	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Statement Close Exception!");
			}
		}
	}

	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("ResultSet Close Exception!");
			}
		}
	}
}
