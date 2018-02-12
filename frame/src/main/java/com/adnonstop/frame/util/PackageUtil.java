package com.adnonstop.frame.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.util.List;

/**
 * 包相关的查询工具
 * <p>
 * Created by ikould on 2016/7/28.
 */
public class PackageUtil {

    private static String versionName;

    /**
     * 判断当前应用是否在前台
     *
     * @param context 上下文
     * @return tf
     */
    public static boolean isCurrentAppOnForeground(Context context) {
        String currentPackageName = context.getPackageName();
        return isPackageOnForeground(context, currentPackageName);
    }

    /**
     * 判断应用是否在前台
     *
     * @param context 上下文
     * @param pckName 包名
     * @return tf
     */
    public static boolean isPackageOnForeground(Context context, String pckName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null)
            return false;
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        String packageName = info.topActivity.getPackageName();
        return pckName.equals(packageName);
    }

    /**
     * 获取当前应用版本名
     *
     * @param context 上下文
     * @return 版本名
     */
    public static String getVersionName(Context context) {
        if (TextUtils.isEmpty(versionName)) {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = null;
            try {
                packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                versionName = packInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return versionName;
    }

    /**
     * 获取当前应用版本号
     *
     * @param application 上下文
     * @return 版本号
     */
    public static int getVersionCode(Application application) {
        PackageManager packageManager = application.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(application.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前正在运行的Activity名字
     *
     * @param context 上下文
     * @return 运行的Activity名字
     */
    public static String getRunningActivityName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null)
            return null;
        return activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
    }

    /**
     * 获取当前进程名称
     *
     * @param context 上下文对象
     * @return 返回包名
     */
    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null)
            return null;
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return null;
    }

    /**
     * 获取程序的名字
     *
     * @param context  上下文
     * @param packName 包名
     * @return 程序名
     */
    public String getAppName(Context context, String packName) {
        //包管理操作管理类
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(packName, 0);
            return info.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取app当前所有权限列表
     *
     * @param context  上下文
     * @param packName 包名
     * @return 权限列表
     */
    public String[] getAllPermissions(Context context, String packName) {
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            PackageInfo packInfo = pm.getPackageInfo(packName, PackageManager.GET_PERMISSIONS);
            //获取到所有的权限
            return packInfo.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.getStackTrace();
        }
        return null;
    }

    /**
     * 获取程序的签名
     *
     * @param context  上下文
     * @param packName 包名
     * @return 签名
     */
    public static String getAppSignature(Context context, String packName) {
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo packInfo = pm.getPackageInfo(packName, PackageManager.GET_SIGNATURES);
                //获取当前应用签名
                return packInfo.signatures[0].toCharsString();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取程序 图标
     *
     * @param context  上下文
     * @param packName 应用包名
     * @return Drawable
     */
    public Drawable getAppIcon(Context context, String packName) {
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            //获取到应用信息
            ApplicationInfo info = pm.getApplicationInfo(packName, 0);
            return info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 静默安装
     *
     * @param context  上下文
     * @param filePath apk文件地址
     * @return 是否安装成功
     */
    public static boolean installNormal(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (!file.exists() || !file.isFile() || file.length() <= 0) {
            return false;
        }
        i.setDataAndType(Uri.parse(filePath), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }
}
