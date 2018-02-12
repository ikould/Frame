package com.adnonstop.frame.util;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;

/**
 * MetaData 获取工具类
 *
 * @author ikould on 2018/2/11.
 */

public class MetaDataUtil {

    /**
     * 获取Application的MetaData
     *
     * @param context 上下文
     * @return MetaData
     */
    public static Bundle getApplicationMetaData(Context context) {
        ApplicationInfo info = null;
        try {
            info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            return info.metaData;
        }
        return null;
    }

    /**
     * 获取Activity的MetaData
     *
     * @param context 上下文
     * @return MetaData
     */
    public static Bundle getActivityMetaData(Context context) {
        ActivityInfo info = null;
        try {
            info = context.getPackageManager().getActivityInfo(((Activity) context).getComponentName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            return info.metaData;
        }
        return null;
    }

    /**
     * 获取Service的MetaData
     *
     * @param context 上下文
     * @param clazz   Class
     * @return MetaData
     */
    public static Bundle getServiceMetaData(Context context, Class<? extends Service> clazz) {
        ComponentName componentName = new ComponentName(context, clazz);
        ServiceInfo info = null;
        try {
            info = context.getPackageManager().getServiceInfo(componentName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            return info.metaData;
        }
        return null;
    }
}
