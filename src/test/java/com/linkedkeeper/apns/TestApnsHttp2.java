package com.linkedkeeper.apns;

import com.linkedkeeper.apns.client.ApnsHttp2;
import com.linkedkeeper.apns.data.ApnsPushNotification;
import com.linkedkeeper.apns.data.ApnsPushNotificationResponse;
import com.linkedkeeper.apns.data.Payload;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import javax.net.ssl.SSLException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author frank@linkedkeeper.com on 2016/12/28.
 */
public class TestApnsHttp2 {

    static final String PUSH_PATH_FILE = "Certificates.p12";
    static final boolean product = true;
    static final String pwd = "MLBAEIOS";

    static final String goodToken = "<88562a02194c65512a8740aa206d824bd6a0584ec8ee1ad033f87d25b68d91d0>";
                                    //8d5e09d576d64672c7e5a51dd3761c368a65289e58c1f3285d284668909c476f

    public static void main(String[] args) {
        try {
            ApnsHttp2 client = new ApnsHttp2(new FileInputStream(generatePushFile()), pwd)
            		.sandboxMode();
                    //.productMode();

            for (int i = 0; i < 1; i++) {
                String paylaod = Payload.newPayload()
                        .alertBody("test#1 apns-http2, i = " + i + " " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"))
                        .badge(1)
                        .sound()
                        .build();

                Future<ApnsPushNotificationResponse<ApnsPushNotification>> response = client.pushMessageAsync(paylaod, splitDeviceToken(goodToken));
                ApnsPushNotificationResponse<ApnsPushNotification> notification = response.get();
                boolean success = notification.isAccepted();
                System.out.println(success);

//                Thread.sleep(1000);
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

    private static String splitDeviceToken(String deviceToken) {
        return StringUtils.remove(StringUtils.remove(StringUtils.remove(deviceToken, "<"), ">"), " ");
    }

    private static String generatePushFile() {
        String path = "e:/";
        String pushFile = path + PUSH_PATH_FILE;
        return pushFile;
    }
}
