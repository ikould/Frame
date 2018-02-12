package com.adnonstop.frame.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 用于判断网络状态的工具
 */
public class NetWorkUtil {

    /**
     * 检测网络是否可用
     *
     * @param context 上下文
     * @return 是否可用
     */
    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                //当前网络是连着的
                if (info.getState() == NetworkInfo.State.CONNECTED
                        || info.getState() == NetworkInfo.State.CONNECTING) {
                    //当前网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断连接着的是否是Wifi
     *
     * @param context 上下文
     * @return tf
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        }
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }
}
