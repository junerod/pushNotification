package com.linkedkeeper.apns.application;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class TestConnection {

	public static void main(String[] args) {
		//testUsersExceptions();
		//testIsThereExceptions();
		//testprocedure();
		//testUserDeviceTokenP();
		testTimer1();//定时器
	}
	
	public static void testIsThereExceptions(){
		System.out.println(System.getProperties().get("java.library.path"));
		Connection conn=OrclConnect.getConnection();
		System.out.println(conn==null?"no":"yes");
		OracleDao oracleDao = new OracleDao();
		Date captureTime = new Date();
		SimpleDateFormat formatTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try {
			List<Map<String, Object>> listExceptions = oracleDao.getIsThereExceptionsP();
			for (Map<String, Object> mapException : listExceptions) {
		    System.out.println(mapException.get("GETDATATIME").toString());
			String captureTimeS = formatTime.format(mapException.get("GETDATATIME"));
			System.out.println(captureTimeS);
			captureTime = formatTime.parse(captureTimeS);
			System.out.println("parse:" + captureTime);
			java.sql.Timestamp timestamp = new Timestamp(captureTime.getTime());
			System.out.println("timestamp：" + timestamp);
			System.out.println("timestampFormat：" + formatTime.format(timestamp));
			
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
 	
	public static void testprocedure(){
		//Date captureTime = new Date();
		SimpleDateFormat formatTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		OracleDao oracleDao = new OracleDao();
		List<Map<String, Object>> listExceptions = oracleDao.getIsThereExceptionsP();
		System.out.println(listExceptions.size());
		if (listExceptions.size() > 0) {
			for (Map<String, Object> mapException : listExceptions) {
					String timeString = formatTime.format(mapException.get("GETDATATIME"));
					System.out.println(timeString);
					Date captureTime = new Date();
					try {
						captureTime = formatTime.parse(timeString);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					System.out.println(captureTime);
			}
		}
	}
    
    public static void testUsersExceptions(){
    	Date captureTime = new Date();
		SimpleDateFormat formatTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		OracleDao oracleDao = new OracleDao();
		List<Map<String, Object>> listExceptions = oracleDao.getIsThereExceptionsP();
		if (listExceptions.size() > 0) {
			for (Map<String, Object> mapException : listExceptions) {
				try {
					captureTime = formatTime.parse(formatTime.format(mapException.get("GETDATATIME")));
					System.out.println("captureTime:" + captureTime);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				List<Map<String, Object>> listUsers = oracleDao.getUsersByFloorP(mapException.get("EFLOOR").toString());
				if (listUsers.size() > 0) {
					for (Map<String, Object> mapUser : listUsers) {
						List<Map<String, Object>> UserExceptions = oracleDao
								.getUserExceptionsP(mapUser.get("USERNAME").toString(), captureTime);
						if (UserExceptions.size() > 0) {
							System.out.println("single exceptions");
							for (Map<String, Object> mapUserExceptions : UserExceptions) {
								System.out.println("-----------------");
								System.out.println(mapUserExceptions.get("EFLOOR"));
								System.out.println(mapUserExceptions.get("DECIVEDETAIL"));
								System.out.println(mapUserExceptions.get("EDETAIL"));
								System.out.println(formatTime.format(mapUserExceptions.get("CAPTURETIME")));
								System.out.println("-----------------");
							}
						}
					}
				}
			}
		}
	}
//			System.out.println(mapUserExceptions.get("EFLOOR"));
//			System.out.println(mapUserExceptions.get("DECIVEDETAIL"));
//			System.out.println(mapUserExceptions.get("EDETAIL"));
//			System.out.println(mapUserExceptions.get("CAPTURETIME"));

    public static void testUserDeviceTokenP(){
    	OracleDao oracleDao = new OracleDao();
    	List<Map<String, Object>> listDeviceTokens = oracleDao.getUserDeviceTokenP("MLB001");
        for (Map<String, Object> mapDT : listDeviceTokens) {
			System.out.println(mapDT.get("DEVICETOKENNAME").toString());
		}    
    }

    public static void testTimer1() {
    	     Timer timer = new Timer();
    	     timer.schedule(new TimerTask() {
    	       public void run() {
    	         System.out.println("-------设定要指定任务--------");
    	       }
    	     },1,60000);// 设定指定的时间time,此处为2000毫秒
    	   }
}
