package com.yrazlik.loltr.utils;

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

}
