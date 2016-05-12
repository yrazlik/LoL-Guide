package com.yrazlik.loltr.billing;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.android.vending.billing.IInAppBillingService;

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
}
