package com.linkedkeeper.apns.application;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;

public class OracleDao {
	private static Connection conn;
	private static CallableStatement cstmt;
	private static ResultSet rs;

	// call Procedure
	public void callProcedure() throws Exception {
		conn = OrclConnect.getConnection();
		// 第一参数:in 编号 7902
		// 第二参数:out 姓名
		// 第三参数:out 工作
		// 第四参数:out 薪水
		String sql = "call findEmpNameAndSalAndJob(?,?,?,?)";
		// 创建专用于调用过程或函数的对象
		cstmt = conn.prepareCall(sql);
		// 为?占位符设置in、out值
		// hibernate从0开始，jdbc从1开始
		cstmt.setInt(1, 7902);// in值
		cstmt.registerOutParameter(2, Types.VARCHAR);// out值
		cstmt.registerOutParameter(3, Types.VARCHAR);// out值
		cstmt.registerOutParameter(4, Types.INTEGER);// out值
		cstmt.execute();// 抛行调用存储过程
		// 依次接收3个返回值
		String ename = cstmt.getString(2);
		String job = cstmt.getString(3);
		Integer sal = cstmt.getInt(4);
		// 显示
		System.out.println(ename + "的工作是：" + job + "，它是薪水是" + sal);
		// 关闭连接对象
		OrclConnect.close(cstmt);
		OrclConnect.close(conn);
	}

	// call Function
	public void callFunction() throws Exception {
		Connection conn = OrclConnect.getConnection();
		// 参数一：in 编号 数值型
		// 参数二：out 姓名 字符串型
		// 返回值：out 薪水 数值型
		String sql = "{? = call findEmpNameAndSal(?,?)}";
		CallableStatement cstmt = conn.prepareCall(sql);
		cstmt.setInt(2, 7788);// in
		cstmt.registerOutParameter(3, Types.VARCHAR);// out
		cstmt.registerOutParameter(1, Types.INTEGER);// 返回值
		cstmt.execute();// 执行调用存储函数
		String ename = cstmt.getString(3);
		Integer sal = cstmt.getInt(1);
		System.out.println(ename + "的薪水是" + sal);
		// 关闭连接对象
		OrclConnect.close(cstmt);
		OrclConnect.close(conn);
	}

	// call HERMES_ISTHEREEXCEPTIONS Procedure
	public List<Map<String, Object>> getIsThereExceptionsP() {
		List<Map<String, Object>> listExceptions = new ArrayList<Map<String, Object>>();
		conn = OrclConnect.getConnection();
		String sql = "call HERMES_ISTHEREEXCEPTIONS(?)";
		// 创建专用于调用过程或函数的对象
		try {
			cstmt = conn.prepareCall(sql);
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);// out值
			cstmt.execute();
			rs = (ResultSet) cstmt.getObject(1);

			while (rs.next()) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("EFLOOR", rs.getString(1));
				resultMap.put("GETDATATIME", rs.getTimestamp(2));
				listExceptions.add(resultMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			OrclConnect.close(conn);
			OrclConnect.close(cstmt);
			OrclConnect.close(rs);
		}
		return listExceptions;
	}

	// call HERMES_EXS_GETUSERBYFLOOR Procedure
	public List<Map<String, Object>> getUsersByFloorP(String floor) {
		List<Map<String, Object>> listUsers = new ArrayList<Map<String, Object>>();
		conn = OrclConnect.getConnection();
		String sql = "call HERMES_EXS_GETUSERBYFLOOR(?,?)";
		// 创建专用于调用过程或函数的对象
		try {
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, floor);
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);// out值
			cstmt.execute();
			rs = (ResultSet) cstmt.getObject(2);

			while (rs.next()) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("USERNAME", rs.getString(1));
				resultMap.put("FLOORID", rs.getString(2));
				listUsers.add(resultMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			OrclConnect.close(conn);
			OrclConnect.close(cstmt);
			OrclConnect.close(rs);
		}
		return listUsers;
	}

	// call HERMES_DEVICETOKEN_GETBYUSER Procedure
	public List<Map<String, Object>> getUserExceptionsP(String username, Date captureTime) {
		List<Map<String, Object>> UserExceptions = new ArrayList<Map<String, Object>>();
		conn = OrclConnect.getConnection();
		String sql = "call HERMES_EXCEPTIONS_BYUSER(?,?,?)";
		// 创建专用于调用过程或函数的对象
		try {
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, username);
			System.out.println("PcaptureTime:" + captureTime);
			java.sql.Timestamp captureDate = new java.sql.Timestamp(captureTime.getTime());
			System.out.println("Ptimestamp:" + captureDate);
			cstmt.setTimestamp(2, captureDate);
			cstmt.registerOutParameter(3, OracleTypes.CURSOR);// out值
			cstmt.execute();
			rs = (ResultSet) cstmt.getObject(3);

			while (rs.next()) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("EFLOOR", rs.getString(1));
				resultMap.put("DECIVEDETAIL", rs.getString(2));
				resultMap.put("EDETAIL", rs.getString(3));
				resultMap.put("CAPTURETIME", rs.getTimestamp(4));
				UserExceptions.add(resultMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			OrclConnect.close(conn);
			OrclConnect.close(cstmt);
			OrclConnect.close(rs);
		}
		System.out.println("UserExceptions00:" + UserExceptions.size());
		return UserExceptions;
	}

	public List<Map<String, Object>> getUserDeviceTokenP(String username) {
		List<Map<String, Object>> UserDeviceToken = new ArrayList<Map<String, Object>>();
		conn = OrclConnect.getConnection();
		String sql = "call HERMES_DEVICETOKEN_GETBYUSER(?,?)";
		// 创建专用于调用过程或函数的对象
		try {
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, username);
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);// out值
			cstmt.execute();
			rs = (ResultSet) cstmt.getObject(2);

			while (rs.next()) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("DEVICETOKENNAME", rs.getString(1));
				UserDeviceToken.add(resultMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			OrclConnect.close(conn);
			OrclConnect.close(cstmt);
			OrclConnect.close(rs);
		}
		return UserDeviceToken;
	}
}
