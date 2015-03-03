package com.yrazlik.loltr;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by yrazlik on 3/3/15.
 */
public class LolApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Parse.initialize(this, "tjNvuPzFqKLUGV3KjxKnsIK7qztkvorEkDCrn0Bz", "T0iDbKd213pDduIWFupDYVCusdwKeoSJUAWoRwSR");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
