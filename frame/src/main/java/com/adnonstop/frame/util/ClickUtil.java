package com.adnonstop.frame.util;

/**
 * 点击工具类
 */
public class ClickUtil {

    /**
     * 两次点击按钮之间的点击间隔不能少于1000毫秒
     */
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    // 上一次点击时间
    private static long lastClickTime;

    /**
     * 解决重复点击view的问题,1秒内重复点击算最后一次点击
     *
     * @return 是否是快速点击
     */
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

}
