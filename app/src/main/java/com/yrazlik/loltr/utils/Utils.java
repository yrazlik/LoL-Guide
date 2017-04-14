package com.yrazlik.loltr.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yrazlik on 27/03/17.
 */

public class Utils {

    public static boolean isValidString(String s) {
        return  s != null && s.length() > 0;
    }

    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }

    public static String getTuesday(){
        Calendar c = Calendar.getInstance();

        if(c.get(Calendar.DAY_OF_WEEK) < 3){
            c.add(Calendar.DATE, -7);
            c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", new Locale(getLocaleForMonthName()));
            Date d = c.getTime();
            String start = sdf.format(d);
            c.add(Calendar.DATE, 7);
            Date d2 = c.getTime();
            String end = sdf.format(d2);
            return start + " - " + end;
        }else{
            c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", new Locale(getLocaleForMonthName()));
            Date d = c.getTime();
            String start = sdf.format(d);
            c.add(Calendar.DATE, 7);
            Date d2 = c.getTime();
            String end = sdf.format(d2);
            return start + " - " + end;
        }
    }

    public static String getLocaleForMonthName(){
        String locale = LocalizationUtils.getInstance().getLocale();
        if(locale.toLowerCase().startsWith("en")) {
            return "en";
        } else if(locale.toLowerCase().startsWith("tr")) {
            return "tr";
        } else if(locale.toLowerCase().startsWith("pt")) {
            return "pt";
        }
        return "en";
    }

}
