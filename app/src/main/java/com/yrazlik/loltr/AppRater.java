package com.yrazlik.loltr;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by yrazlik on 18/05/16.
 */
public class AppRater {

    public static final String DONT_SHOW_AGAIN = "com.yrazlik.loltr.dontshowagain";
    public static final String SHARED_PREFS_APPRATER = "com.yrazlik.loltr.apprater";

    private static String APP_PNAME;
    private final static int DAYS_UNTIL_PROMPT = 1;
    private final static int LAUNCHES_UNTIL_PROMPT = 1;
    private static long launch_count;
    private static Long date_firstLaunch;
    private static boolean willShowFirstTime = false;
    private static boolean dontShowAgain = false;

    public interface DialogDismissedListener{
        void onDialogDismissed();
    }

    public static void app_launched(Context mContext) {
        APP_PNAME = mContext.getApplicationContext().getPackageName();
        SharedPreferences prefs = mContext.getApplicationContext().getSharedPreferences(SHARED_PREFS_APPRATER, 0);
        dontShowAgain = prefs.getBoolean(DONT_SHOW_AGAIN, false);
        if (dontShowAgain) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();
        launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            willShowFirstTime = true;
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
        editor.commit();
    }

    public static boolean showRateDialog(final Context mContext, final SharedPreferences.Editor editor, final DialogDismissedListener listener) {
        if(dontShowAgain) {
            return false;
        }
        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if(willShowFirstTime) {
                willShowFirstTime = false;
                show(mContext, editor, listener);
                return true;
            }else {
                if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                    show(mContext, editor, listener);
                    return true;
                }
            }
        }
        return false;
    }

    private static void show(final Context mContext, final SharedPreferences.Editor editor, final DialogDismissedListener listener) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle(mContext.getResources().getString(R.string.giveRating));

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
       

        TextView tv = new TextView(mContext);
        tv.setText(mContext.getResources().getString(R.string.rateUs));
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);

        Button b1 = new Button(mContext);
        b1.setText(mContext.getResources().getString(R.string.giveRating));
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                dialog.dismiss();
                listener.onDialogDismissed();
            }
        });
        ll.addView(b1);

        Button b2 = new Button(mContext);
        b2.setText(mContext.getResources().getString(R.string.remindmelater));
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                listener.onDialogDismissed();
            }
        });
        ll.addView(b2);

        Button b3 = new Button(mContext);
        b3.setText(mContext.getResources().getString(R.string.dontshowagain));
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean(DONT_SHOW_AGAIN, true);
                    editor.commit();
                    dontShowAgain = true;
                }
                dialog.dismiss();
                listener.onDialogDismissed();
            }
        });
        ll.addView(b3);

        dialog.setContentView(ll);
        dialog.show();
    }
}
