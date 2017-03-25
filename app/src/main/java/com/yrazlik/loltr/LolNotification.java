package com.yrazlik.loltr;

import android.content.Intent;
import android.net.Uri;

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
        isPush = true;
        Uri data = i.getData();
        if(data != null) {
            this.notificationAction = getNotificationAction(data.getHost());
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
}
