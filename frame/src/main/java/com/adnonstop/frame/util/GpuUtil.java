package com.adnonstop.frame.util;

import android.app.Activity;
import android.graphics.Paint;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Gpu工具类
 *
 * @author ikould on 2018/2/11.
 */

public class GpuUtil {

    /**
     * 开启Activity Window的硬件加速
     *
     * @param ac Activity
     */
    public static void openWindowHardware(Activity ac) {
        try {
            Field field = WindowManager.LayoutParams.class.getField("FLAG_HARDWARE_ACCELERATED");
            if (field != null) {
                int FLAG_HARDWARE_ACCELERATED = field.getInt(null);
                Method method = Window.class.getMethod("setFlags", int.class, int.class);
                if (method != null) {
                    method.invoke(ac.getWindow(), FLAG_HARDWARE_ACCELERATED, FLAG_HARDWARE_ACCELERATED);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭View的硬件加速
     *
     * @param v View
     */
    public static void cancelViewHardware(View v) {
        try {
            Field field = View.class.getField("LAYER_TYPE_SOFTWARE");
            if (field != null) {
                int LAYER_TYPE_SOFTWARE = field.getInt(null);
                Method method = View.class.getMethod("setLayerType", int.class, Paint.class);
                if (method != null) {
                    method.invoke(v, LAYER_TYPE_SOFTWARE, null);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 只有APP开启了GPU加速才有效
     *
     * @param v
     */
    public static void openViewHardware(View v) {
        try {
            Field field = View.class.getField("LAYER_TYPE_HARDWARE");
            if (field != null) {
                int LAYER_TYPE_HARDWARE = field.getInt(null);
                Method method = View.class.getMethod("setLayerType", int.class, Paint.class);
                if (method != null) {
                    method.invoke(v, LAYER_TYPE_HARDWARE, null);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
