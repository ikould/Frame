package com.adnonstop.frame.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 内存相关信息获取
 * TODO 不靠谱
 */
public class MemoryUtil {

    /**
     * 获得Rom总大小
     *
     * @param context 上下文
     * @return Rom总大小
     */
    public static String getRomTotalSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    /**
     * 获得可用Rom大小
     *
     * @param context 上下文
     * @return 可用Rom大小
     */
    public static String getRomAvailableSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }

    /**
     * 获得可用内存大小
     *
     * @param context 上下文
     * @return 内存大小(Mb)
     */
    public static float getRamAvailableSize(Context context) {
        float ramAvailable;
        // 得到ActivityManager
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);  // 创建ActivityManager.MemoryInfo对象
        if (am == null)
            return 0;
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // 取得剩余的内存空间
        ramAvailable = mi.availMem * 1.0f / (1024 * 1024);
//        Formatter.formatFileSize(context, mi.availMem);
        return ramAvailable;
    }

    /**
     * 获取内存大小
     *
     * @param context 上下文
     * @return 内存大小(Mb)
     */
    public static float getRamThresholde(Context context) {
        float ramThreshold;
        // 得到ActivityManager
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);  // 创建ActivityManager.MemoryInfo对象
        if (am == null)
            return 0;
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // 取得剩余的内存空间
        ramThreshold = mi.threshold * 1.0f / (1024 * 1024);
        return ramThreshold;
    }

    /**
     * 获取总内存大小
     *
     * @return 总内存大小
     */
    public static float getRamTotalSize() {
        float mTotal;
        // /proc/meminfo读出的内核信息进行解释
        String path = "/proc/meminfo";
        String content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path), 8);
            String line;
            if ((line = br.readLine()) != null) {
                content = line;
            }
        } catch (Exception e) {
            Log.e("MemoryUtil", "getRamTotalSize: e = " + e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Log.e("MemoryUtil", "getRamTotalSize: e = " + e);
                }
            }
        }
        // beginIndex
        int begin = content.indexOf(':');
        // endIndex
        int end = content.indexOf('k');
        // 截取字符串信息
        content = content.substring(begin + 1, end).trim();
        mTotal = Integer.parseInt(content) * 1.0f / 1024 / 1024;
        return mTotal;
    }

}
