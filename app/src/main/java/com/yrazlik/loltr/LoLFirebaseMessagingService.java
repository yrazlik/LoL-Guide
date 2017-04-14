package com.yrazlik.loltr;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yrazlik.loltr.activities.LolPushActivity;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.utils.LocalizationUtils;

import java.util.Date;

/**
 * Created by yrazlik on 25/03/17.
 */

public class LoLFirebaseMessagingService extends FirebaseMessagingService{

    public static final String TAG = "FirebaseMessage";
    public static final String FIREBASE_PUSH_TOPIC = "push_topic";


    private Intent notificationIntent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        try {
            notificationIntent = new Intent(this, LolPushActivity.class);
            notificationIntent.setAction(Long.toString(System.currentTimeMillis())); //THIS IS NECESSARY, OTHERWISE ONLY THE FIRST NOTIFICATION WILL WORK
            setParameters(remoteMessage);
        } catch (Exception e) {
            notificationIntent = null;
        }

        if (notificationIntent != null) {
            showNotification(remoteMessage);
        }
    }

    private void showNotification(RemoteMessage remoteMessage) {
        int notificationId = generateRandomUniqueInt();
        final NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pIntent = PendingIntent.getActivity(this, generateRandomUniqueInt(), notificationIntent, 0);

        String notificationLocale = getNotificationLocale(remoteMessage);
        if(Commons.isValidString(notificationLocale)) {
            notificationLocale = notificationLocale.toLowerCase();
            if(notificationLocale.equalsIgnoreCase(LocalizationUtils.getInstance().getLocale().toLowerCase())) {
                mgr.notify(notificationId, createPushNotification(getTicker(remoteMessage), getNotificationTitle(remoteMessage), getBody(remoteMessage), pIntent));
            }
        }
    }

    private String getNotificationTitle(RemoteMessage remoteMessage) {
        if(remoteMessage != null &&  remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
            try {
                return remoteMessage.getData().get(LolNotification.PUSH_NOTIFICATION_TITLE);
            }catch (Exception ignored) {}
        }
        return getString(R.string.app_name);
    }

    private String getNotificationLocale(RemoteMessage remoteMessage) {
        if(remoteMessage != null &&  remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
            try {
                return remoteMessage.getData().get(LolNotification.PUSH_NOTIFICATION_LOCALE);
            }catch (Exception ignored) {}
        }
        return "";
    }

    private String getTicker(RemoteMessage remoteMessage) {
        if (remoteMessage != null && remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
            if (Commons.isValidString(remoteMessage.getData().get(LolNotification.PUSH_NOTIFICATION_TICKER))) {
                return remoteMessage.getData().get(LolNotification.PUSH_NOTIFICATION_TICKER);
            }
        }
        return getString(R.string.app_name);
    }

    private String getBody(RemoteMessage remoteMessage) {
        if (remoteMessage != null && remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
            if (Commons.isValidString(remoteMessage.getData().get(LolNotification.PUSH_NOTIFICATION_BODY))) {
                return remoteMessage.getData().get(LolNotification.PUSH_NOTIFICATION_BODY);
            }
        }
        return getString(R.string.app_name);
    }

    private void setParameters(RemoteMessage remoteMessage) {

        try {
            notificationIntent.putExtra(LolNotification.PARAMETER_IS_PUSH, true);
        } catch (Exception ignored) {}

        if (remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {

            try {
                notificationIntent.putExtra(LolNotification.PUSH_NOTIFICATION_TITLE, remoteMessage.getData().get(LolNotification.PUSH_NOTIFICATION_TITLE));
            } catch (Exception ignored) {}

            try {
                notificationIntent.putExtra(LolNotification.PUSH_NOTIFICATION_LOCALE, remoteMessage.getData().get(LolNotification.PUSH_NOTIFICATION_LOCALE));
            } catch (Exception ignored) {}

            try {
                notificationIntent.putExtra(LolNotification.DEEPLINK_EXTRA, remoteMessage.getData().get(LolNotification.DEEPLINK_EXTRA));
            } catch (Exception ignored) {}

            try {
                notificationIntent.putExtra(LolNotification.PUSH_NOTIFICATION_TICKER, remoteMessage.getData().get(LolNotification.PUSH_NOTIFICATION_TICKER));
            } catch (Exception ignored) {}

            try {
                notificationIntent.putExtra(LolNotification.PUSH_NOTIFICATION_BODY, remoteMessage.getData().get(LolNotification.PUSH_NOTIFICATION_BODY));
            } catch (Exception ignored) {}
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }


    private Notification createPushNotification(CharSequence ticker, CharSequence title, CharSequence contentText, PendingIntent pIntent) {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)).setTicker(ticker).setContentTitle(title)
                    .setContentText(contentText).setContentIntent(pIntent).setStyle(new NotificationCompat.BigTextStyle().bigText(contentText));

            builder.setAutoCancel(true);
            Notification notif = builder.build();

            notif.defaults |= Notification.DEFAULT_SOUND;
            notif.defaults |= Notification.DEFAULT_LIGHTS;
            notif.defaults |= Notification.DEFAULT_VIBRATE;
            notif.defaults |= Notification.FLAG_AUTO_CANCEL;

            return notif;
        }else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.push_icon_lollipop)
                    .setTicker(ticker).setContentTitle(title).setContentText(contentText).setContentIntent(pIntent)
                    .setColor(getResources().getColor(R.color.button_blue)).setStyle(new NotificationCompat.BigTextStyle().bigText(contentText));;

            builder.setAutoCancel(true);
            Notification notif = builder.build();

            notif.defaults |= Notification.DEFAULT_SOUND;
            notif.defaults |= Notification.DEFAULT_LIGHTS;
            notif.defaults |= Notification.DEFAULT_VIBRATE;
            notif.defaults |= Notification.FLAG_AUTO_CANCEL;

            return notif;
        }
    }

    private int generateRandomUniqueInt() {
        return (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
    }
}
