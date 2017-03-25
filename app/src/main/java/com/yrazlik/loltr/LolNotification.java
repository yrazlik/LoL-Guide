package com.yrazlik.loltr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by yrazlik on 25/03/17.
 */

public class LolNotification {

    public static final String PARAMETER_IS_PUSH = "isPush";

    public static final String DEEPLINK_HOME_PAGE = "home";
    public static final String DEEPLINK_REMOVE_ADS = "remove_ads";
    public static final String DEEPLINK_DISCOUNTS = "discounts";
    public static final String DEEPLINK_NEWS = "news";
    public static final String DEEPLINK_LIVE_CHANNELS = "live_channels";


    //push notification variables
    public static final String DEEPLINK_EXTRA = "dl";  //***
    public static final String MESSAGE_EXTRA = "msg";
    public static final String PUSH_NOTIFICATION_LANGUAGE = "language"; //***
    public static final String MESSAGE_EXTRA_ENGLISH = "msgEnglish";
    public static final String PUSH_NOTIFICATION_TICKER = "ticker"; //***
    public static final String PUSH_NOTIFICATION_TICKER_ENGLISH = "tickerEnglish"; //***
    public static final String PUSH_NOTIFICATION_BODY = "body"; //***
    public static final String PUSH_NOTIFICATION_BODY_ENGLISH = "bodyEnglish"; //***

    private String language;
    private String deeplink;
    private String msg;
    private String msgEnglish;
    private String ticker;
    private String tickerEnglish;
    private String body;
    private String bodyEnglish;


    public enum NOTIFICATION_ACTION {
        ACTION_HOME,
        ACTION_REMOVE_ADS,
        ACTION_DISCOUNTS,
        ACTION_NEWS,
        ACTION_LIVE_CHANNELS
    }

    private NOTIFICATION_ACTION notificationAction = NOTIFICATION_ACTION.ACTION_HOME;
    private boolean isPush = false;

    public LolNotification(Intent i) {
        createNotification(i);
    }

    public NOTIFICATION_ACTION getNotificationAction() {
        return notificationAction;
    }

    private void createNotification(Intent i) {
        boolean isPush = i.getBooleanExtra(PARAMETER_IS_PUSH, false);
        if(isPush) { // handle push notification (not deeplink)
            handlePushData(i);
        } else { // handle deeplink
            handleDeeplinkData(i);
        }
    }

    private void handlePushData(Intent i) {
        this.isPush = true;

        Bundle extras = i.getExtras();

        if(extras != null) {
            try {
                this.deeplink = (String) extras.get(LolNotification.DEEPLINK_EXTRA);
                Log.d("LolApplication", "Deeplink extra is: " + this.deeplink);
                Uri deeplinkUri = Uri.parse(deeplink);
                this.notificationAction = getNotificationAction(deeplinkUri.getHost());
            }catch (Exception ignored) {}

            try {
                this.language = (String) extras.get(LolNotification.PUSH_NOTIFICATION_LANGUAGE);
            }catch (Exception ignored) {}

            try {
                this.msg = (String) extras.get(LolNotification.MESSAGE_EXTRA);
            }catch (Exception ignored) {}

            try {
                this.msgEnglish = (String) extras.get(LolNotification.MESSAGE_EXTRA_ENGLISH);
            }catch (Exception ignored) {}

            try {
                this.ticker = (String) extras.get(LolNotification.PUSH_NOTIFICATION_TICKER);
            }catch (Exception ignored) {}

            try {
                this.tickerEnglish = (String) extras.get(LolNotification.PUSH_NOTIFICATION_TICKER_ENGLISH);
            }catch (Exception ignored) {}

            try {
                this.body = (String) extras.get(LolNotification.PUSH_NOTIFICATION_BODY);
            }catch (Exception ignored) {}

            try {
                this.bodyEnglish = (String) extras.get(LolNotification.PUSH_NOTIFICATION_BODY_ENGLISH);
            }catch (Exception ignored) {}
        }
    }

    private void handleDeeplinkData(Intent i) {
        isPush = false;
        Uri data = i.getData();
        if(data != null) {
            this.notificationAction = getNotificationAction(data.getHost());
        }
    }

    private NOTIFICATION_ACTION getNotificationAction(String deeplinkHost) {
        if(deeplinkHost != null) {
            if(deeplinkHost.equalsIgnoreCase(DEEPLINK_HOME_PAGE)) {
                return NOTIFICATION_ACTION.ACTION_HOME;
            } else if(deeplinkHost.equalsIgnoreCase(DEEPLINK_REMOVE_ADS)) {
                return NOTIFICATION_ACTION.ACTION_REMOVE_ADS;
            } else if(deeplinkHost.equalsIgnoreCase(DEEPLINK_DISCOUNTS)) {
                return NOTIFICATION_ACTION.ACTION_DISCOUNTS;
            } else if(deeplinkHost.equalsIgnoreCase(DEEPLINK_NEWS)) {
                return NOTIFICATION_ACTION.ACTION_NEWS;
            } else if(deeplinkHost.equalsIgnoreCase(DEEPLINK_LIVE_CHANNELS)) {
                return NOTIFICATION_ACTION.ACTION_LIVE_CHANNELS;
            }
        }
        return NOTIFICATION_ACTION.ACTION_HOME;
    }

    public String getMsg() {
        return msg;
    }

    public String getMsgEnglish() {
        return msgEnglish;
    }

    public String getTicker() {
        return ticker;
    }

    public String getTickerEnglish() {
        return tickerEnglish;
    }

    public String getBody() {
        return body;
    }

    public String getBodyEnglish() {
        return bodyEnglish;
    }

    public boolean isPush() {
        return isPush;
    }
}
