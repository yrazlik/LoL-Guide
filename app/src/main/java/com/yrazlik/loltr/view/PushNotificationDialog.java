package com.yrazlik.loltr.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yrazlik.loltr.R;

/**
 * Created by yrazlik on 26/03/17.
 */

public class PushNotificationDialog extends Dialog{

    private Context mContext;
    private RobotoTextView pushNoificationText;
    private RobotoButton closeButton;

    private String text;

    public PushNotificationDialog(Context context, String text) {
        super(context);
        this.mContext = context;
        this.text = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_push_notification);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);

        pushNoificationText = (RobotoTextView) findViewById(R.id.pushNoificationText);
        closeButton         = (RobotoButton) findViewById(R.id.closeButton);

        pushNoificationText.setText(text);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
