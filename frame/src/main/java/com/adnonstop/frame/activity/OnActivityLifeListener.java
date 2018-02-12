package com.adnonstop.frame.activity;

/**
 * Activity生命周期監聽
 * <p>
 * Created by liudong on 2017/9/7.
 */

public interface OnActivityLifeListener {

    /**
     * Activity onCreate()
     */
    int ON_CREATE  = 0x01;
    /**
     * Activity onStart()
     */
    int ON_START   = 0x02;
    /**
     * Activity onRestart()
     */
    int ON_RESTART = 0x03;
    /**
     * Activity onResume()
     */
    int ON_RESUME  = 0x04;
    /**
     * Activity onPause()
     */
    int ON_PAUSE   = 0x05;
    /**
     * Activity onStop()
     */
    int ON_STOP    = 0x06;
    /**
     * Activity onDestroy()
     */
    int ON_DESTROY = 0x07;

    void onLifeCall(int lifeType);
}
