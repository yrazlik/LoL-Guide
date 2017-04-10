package com.yrazlik.loltr.utils;

import android.content.Context;

/**
 * Created by yrazlik on 07/04/17.
 */

public class LocalizationUtils {

    public static final String REGION_TR = "TR";
    public static final String REGION_NA = "NA";
    public static final String REGION_BR = "BR";

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
        String locale = mContext.getResources().getConfiguration().locale.toString();

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
        if(getLocale().startsWith("en")) {
            return REGION_NA;
        } else if(getLocale().startsWith("tr")) {
            return REGION_TR;
        } else if(getLocale().startsWith("pt")) {
            return REGION_BR;
        }
        return REGION_NA;
    }

}
