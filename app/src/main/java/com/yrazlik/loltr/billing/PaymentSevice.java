package com.yrazlik.loltr.billing;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.android.vending.billing.IInAppBillingService;
import com.yrazlik.loltr.MainActivity;

/**
 * Created by yrazlik on 12/05/16.
 */
public class PaymentSevice {

    private static PaymentSevice mInstance;
    private IInAppBillingService mService;
    private ServiceConnection mServiceConn;
    private Context mContext;

    public static PaymentSevice getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new PaymentSevice(context);
        }
        return mInstance;
    }

    private PaymentSevice(Context context) {
        this.mContext = context;
        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
            }
        };
    }

    public ServiceConnection getServiceConnection() {
        return mServiceConn;
    }

    public IInAppBillingService getService() {
        return mService;
    }

    public Bundle getBuyIntentBundle(String sku) {
        try {
            Bundle buyIntentBundle = mService.getBuyIntent(3, mContext.getPackageName(), sku, "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
            return buyIntentBundle;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bundle buyRemoveAdsItem(String sku) {
        Bundle buyIntentBundle = getBuyIntentBundle(sku);
        if(buyIntentBundle != null) {
            int response = buyIntentBundle.getInt("RESPONSE_CODE");
            if(response == 0) {
                return buyIntentBundle;
            } else {
                return null;
            }
        }
        return null;
    }
}
