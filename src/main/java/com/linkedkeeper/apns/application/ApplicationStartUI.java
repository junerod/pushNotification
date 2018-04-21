package com.linkedkeeper.apns.application;

import java.awt.Button;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.linkedkeeper.apns.service.ApnsHttp2Service;

@SuppressWarnings("serial")
public class ApplicationStartUI extends Frame {
	public ApplicationStartUI(String str) {
		super(str);
	}

	static ApplicationStartUI fr = new ApplicationStartUI("ButtonFrame");

	public static void main(String[] args) {
		fr.setSize(500, 500);
		fr.setLocation(200, 200);
		fr.setBackground(null);
		fr.setLayout(null);

		Button button = new Button("StartMonitorException");
		button.setSize(150, 30);
		button.setLocation(12, 35);
		button.addActionListener(new ActionListener() {
			// 单击按钮执行的方法
			public void actionPerformed(ActionEvent e) {
				// closeThis();
				JOptionPane pane = new JOptionPane("Successfully!");
				JDialog dialog = pane.createDialog(fr, "Alert!");
				dialog.show();
				timerPushNotification();

			}
		});

		fr.add(button);
		fr.setVisible(true);

		fr.addWindowListener(new WindowAdapter() // 为了关闭窗口
		{
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public static void closeThis() {
		fr.dispose();
	}

	public static void timerPushNotification() { // per Minute to getData.
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			public void run() {
				Date captureTime = new Date();
				SimpleDateFormat formatTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				System.out.println("Run Timer: " + formatTime.format(new Date()));
				OracleDao oracleDao = new OracleDao();
				List<Map<String, Object>> listExceptions = oracleDao.getIsThereExceptionsP();
				System.out.println("Exceptions:" + listExceptions.size());
				if (listExceptions.size() > 0) {
					for (Map<String, Object> mapException : listExceptions) {
						try {
							captureTime = formatTime.parse(formatTime.format(mapException.get("GETDATATIME")));
							System.out.println("EXCEPTIONFLOOR:" + mapException.get("EFLOOR").toString());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						List<Map<String, Object>> listUsers = oracleDao
								.getUsersByFloorP(mapException.get("EFLOOR").toString());
						if (listUsers.size() > 0) {
							for (Map<String, Object> mapUser : listUsers) {
								System.out.println("username:" + mapUser.get("USERNAME").toString());
								List<Map<String, Object>> UserExceptions = oracleDao
										.getUserExceptionsP(mapUser.get("USERNAME").toString(), captureTime);
								System.out.println("UserExceptions11:" + UserExceptions.size());
								List<Map<String, Object>> listDeviceTokens = oracleDao
										.getUserDeviceTokenP(mapUser.get("USERNAME").toString());
								System.out.println("DeviceTokens:" + listDeviceTokens.size());
								for (Map<String, Object> mapUserException : UserExceptions) {
									ApnsHttp2Service service = new ApnsHttp2Service();
									System.out.println(mapUserException);
									System.out.println(listDeviceTokens.get(0).get("DEVICETOKENNAME"));
									service.pushNotification(mapUserException, listDeviceTokens);
								}
							}
						}
					}
				}
			}
		}, 1, 60000);
	}

}
