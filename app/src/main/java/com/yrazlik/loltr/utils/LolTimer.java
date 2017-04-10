package com.yrazlik.loltr.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yrazlik on 29/03/17.
 */

public class LolTimer {

    public interface TimerTickListener {
        void onTimerTick(int currentSecond);
    }

    private int timerCurrentValue = TIMER_START_VALUE;

    private static final long TIMER_DELAY = 0;
    private static final long TIMER_PERIOD = 1000;
    private static final int TIMER_START_VALUE = 15;

    private TimerTickListener timerTickListener;
    private static LolTimer mInstance;
    private Timer timer;

    public static LolTimer getInstance() {
        if(mInstance == null) {
            mInstance = new LolTimer();
        }
        return mInstance;
    }

    public LolTimer setTimerTickListener(TimerTickListener timerTickListener) {
        mInstance.timerTickListener = timerTickListener;
        return mInstance;
    }

    private LolTimer() {
        timer = new Timer();
    }

    public void stopTimer() {
        timer.cancel();
        timer.purge();
        timer = new Timer();
    }

    public void startTimer() {
        stopTimer();
        timerCurrentValue = TIMER_START_VALUE;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timerCurrentValue --;
                if(timerTickListener!= null) {
                    timerTickListener.onTimerTick(timerCurrentValue);
                }
            }
        }, TIMER_DELAY, TIMER_PERIOD);
    }

}
