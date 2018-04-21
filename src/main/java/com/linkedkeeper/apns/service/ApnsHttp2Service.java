package com.linkedkeeper.apns.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.net.ssl.SSLException;


import com.linkedkeeper.apns.client.ApnsHttp2;
import com.linkedkeeper.apns.data.ApnsPushNotification;
import com.linkedkeeper.apns.data.ApnsPushNotificationResponse;
import com.linkedkeeper.apns.data.Payload;


public class ApnsHttp2Service {
	    static final String PUSH_PATH_FILE = "Certificates.p12";
	    static final boolean product = true;
	    static final String pwd = "MLBAEIOS";

//	    static final String goodToken = "<88562a02194c65512a8740aa206d824bd6a0584ec8ee1ad033f87d25b68d91d0>";
	                                    //8d5e09d576d64672c7e5a51dd3761c368a65289e58c1f3285d284668909c476f

	    public void pushNotification(Map<String, Object> mapUserExceptions,List<Map<String, Object>> userDevicetokens) {
	    	
	    	SimpleDateFormat formatTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    	try {
	            ApnsHttp2 client = new ApnsHttp2(new FileInputStream(generatePushFile()), pwd)
	            		.sandboxMode();
	                    //.productMode();
	            for (Map<String, Object> mapDeviceToken : userDevicetokens) {
	            	System.out.println(mapDeviceToken.get("DEVICETOKENNAME"));
	                String paylaod = Payload.newPayload()
	                        .alertBody("Floor" + mapUserExceptions.get("EFLOOR") 
	                                + ",ExceptionDevices:" + mapUserExceptions.get("DECIVEDETAIL")
	                                + ",ExceptionsTotal:" + mapUserExceptions.get("EXSTOTAL")
	                                + ".")
	                        .addField("floor", mapUserExceptions.get("EFLOOR"))
	                        .addField("time", formatTime.format(mapUserExceptions.get("CAPTURETIME")))
	                        .badge(1)
	                        .sound()
	                        .build();

	                Future<ApnsPushNotificationResponse<ApnsPushNotification>> response = client.pushMessageAsync(paylaod, mapDeviceToken.get("DEVICETOKENNAME").toString());
	                ApnsPushNotificationResponse<ApnsPushNotification> notification = response.get();
	                boolean success = notification.isAccepted();
	                System.out.println(success);

//	                Thread.sleep(1000);
	            }

	            client.disconnect();
	        } catch (SSLException e) {
	            e.printStackTrace();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (ExecutionException e) {
	            e.printStackTrace();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }

//	    private static String splitDeviceToken(String deviceToken) {
//	        return StringUtils.remove(StringUtils.remove(StringUtils.remove(deviceToken, "<"), ">"), " ");
//	    }

	    private static String generatePushFile() {
	        String path = "e:/";
	        String pushFile = path + PUSH_PATH_FILE;
	        return pushFile;
	    }
}
