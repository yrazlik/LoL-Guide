package com.yrazlik.loltr.utils;

import android.content.Context;

import com.yrazlik.loltr.commons.Commons;

/**
 * Created by yrazlik on 07/04/17.
 */

public class LocalizationUtils {



    private static LocalizationUtils mInstance;
    private Context mContext;

    public static void init(Context context) {
        getInstance(context);
    }

    private static LocalizationUtils getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new LocalizationUtils(context);
        }
        return mInstance;
    }

    public static LocalizationUtils getInstance() {
        if(mInstance == null) {
            mInstance = new LocalizationUtils();
        }
        return mInstance;
    }

    private LocalizationUtils(Context context) {
        this.mContext = context;
    }

    private LocalizationUtils() {}

    public String getLocale(){
        String locale = mContext.getApplicationContext().getResources().getConfiguration().locale.toString();
        if(locale.toLowerCase().startsWith("en")) {
            return "en_US";
        } else if(locale.toLowerCase().startsWith("tr")) {
            return "tr_TR";
        } else if(locale.toLowerCase().startsWith("pt")) {
            return "pt_BR";
        }
        return "en_US";
    }

    public String getRegion() {
       return Commons.SELECTED_REGION;
    }

}
