package com.adnonstop.frame.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

/**
 * App相关工具类
 * <p>
 * Created by ikould on 2017/6/14.
 */

public class AppUtil {

    /**
     * 判断系统是否安装了浏览器
     *
     * @param context 上下文
     * @return tf
     */
    public static boolean hasBrowser(Context context) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://"));
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY); // TAG 待定
        final int size = (list == null) ? 0 : list.size();
        return size > 0;
    }

    /**
     * 广告点击，跳转浏览器
     *
     * @param context 上下文
     * @param jumpUrl 跳转地址
     */
    private boolean go2Browser(Context context, String jumpUrl) {
        if (HttpUtil.isUrlValid(jumpUrl)) {   // 匹配
            // 默认浏览器
            if (hasBrowser(context)) {
                Uri uri = Uri.parse(jumpUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
                return true;
            }
        }
        return false;
    }

    /**
     * 重启App
     *
     * @param context 上下文
     */
    public static void restartApp(Context context) {
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, restartIntent); // 1秒钟后重启应用
        System.exit(0);
    }

    /**
     * 清除之前所有数据
     *
     * @param context 上下文
     */
    public static void clearHistory(Context context) {
        // /data/user/0/包名/cache
        String cachePath = context.getCacheDir().getAbsolutePath();
        // /storage/emulated/0/Android/data/包名/cache
        String externalCachePath = null;
        if (context.getExternalCacheDir() != null) {
            externalCachePath = context.getExternalCacheDir().getAbsolutePath();
        }
        // /data/data/包名/databases
        String databasePath = "/data/data/" + context.getPackageName() + "/databases";
        // /data/data/包名/shared_prefs
        String spPath = "/data/data/" + context.getPackageName() + "/shared_prefs";
        // /data/user/0/包名/files
        String filesPath = context.getFilesDir().getAbsolutePath();
        FileUtil.deleteFile(cachePath);
        FileUtil.deleteFile(externalCachePath);
        FileUtil.deleteFile(databasePath);
        FileUtil.deleteFile(spPath);
        FileUtil.deleteFile(filesPath);
    }
}
