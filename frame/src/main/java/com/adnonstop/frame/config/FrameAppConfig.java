package com.adnonstop.frame.config;

import android.content.Context;
import android.content.SharedPreferences;
import com.adnonstop.frame.util.SPUtil;
import android.os.Environment;
import android.text.TextUtils;

import com.adnonstop.frame.util.FileUtil;

import java.io.File;

/**
 * SharedPreferences文件保存
 * <p>
 * Created by ikould on 2017/6/1.
 */

public class FrameAppConfig {

    /**
     * 根目录
     */
    public static String ROOT_DIR;
    /**
     * 缓存目录
     */
    public static String CACHE_DIR;
    /**
     * 文件目录
     */
    public static String FILE_DIR;
    /**
     * 崩溃日志目录
     */

    public static String CRASH_DIR;
    /**
     * 下载文件保存目录
     */

    public static String DOWNLOAD_DIR;
    /**
     * 素材中心下载目录
     */
    public static String MATERIAL_DOWNLOAD_DIR;
    /**
     * 内容发布目录
     */
    public static String CONTENT_DIR;
    /**
     * 临时目录
     */
    public static String TEMP_DIR;

    private SharedPreferences sharedPrefs;

    // ================= 单例 ==============

    public static FrameAppConfig instance;

    public static FrameAppConfig getInstance() {
        if (instance == null) {
            synchronized (FrameAppConfig.class) {
                if (instance == null)
                    instance = new FrameAppConfig();
            }
        }
        return instance;
    }

    private FrameAppConfig() {
    }

    /**
     * 初始化
     *
     * @param context    上下文
     * @param appRootDir app地址，null：自行查找包名下的私有目录
     */
    public void init(Context context, String appRootDir) {
        String appName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        String sharedPreference = appName + "_preference";
        sharedPrefs = context.getSharedPreferences(sharedPreference, Context.MODE_PRIVATE);
        initDir(context, appRootDir);
    }

    /**
     * 初始化
     *
     * @param context    上下文
     * @param appName    SP的名字
     * @param appRootDir app地址，null：自行查找包名下的私有目录
     */
    public void init(Context context, String appName, String appRootDir) {
        String sharedPreference = appName + "_preference";
        sharedPrefs = context.getSharedPreferences(sharedPreference, Context.MODE_PRIVATE);
        initDir(context, appRootDir);
    }

    /**
     * 初始化目录
     *
     * @param context    上下文
     * @param appRootDir app地址，null：自行查找包名下的私有目录
     */
    private void initDir(Context context, String appRootDir) {
        if (TextUtils.isEmpty(appRootDir)) {
            ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + context.getPackageName() + File.separator + "Files";
        } else {
            ROOT_DIR = appRootDir;
        }
        CACHE_DIR = ROOT_DIR + File.separator + "Cache";
        FILE_DIR = ROOT_DIR + File.separator + "File";
        CRASH_DIR = ROOT_DIR + File.separator + "Crash";
        DOWNLOAD_DIR = ROOT_DIR + File.separator + "Download";
        MATERIAL_DOWNLOAD_DIR = ROOT_DIR + File.separator + "Download" + File.separator + "Material";
        CONTENT_DIR = ROOT_DIR + File.separator + "Content";
        TEMP_DIR = ROOT_DIR + File.separator + "Temp";

        FileUtil.initDirectory(ROOT_DIR);
        FileUtil.initDirectory(CACHE_DIR);
        FileUtil.initDirectory(FILE_DIR);
        FileUtil.initDirectory(CRASH_DIR);
        FileUtil.initDirectory(DOWNLOAD_DIR);
        FileUtil.initDirectory(CONTENT_DIR);
        FileUtil.initDirectory(TEMP_DIR);
    }

    /**
     * 获取SharedPreferences 键值对
     *
     * @param key          关键值
     * @param defaultValue 不存在时默认值
     * @return 查找的结果
     */
    public Object get(String key, Object defaultValue) {
        return SPUtil.get(sharedPrefs, key, defaultValue);
    }

    /**
     * 设置SharedPreferences 键值对
     *
     * @param key   关键值
     * @param value 值
     */
    public void put(String key, Object value) {
        SPUtil.put(sharedPrefs, key, value);
    }

    // ==============  获取和设置 ==================

    private static final String IS_DEBUG_MODE = "is_debug_mode";

    /**
     * 设置调试模式
     *
     * @param tf 是否是设置模式
     */
    public void setDebugMode(boolean tf) {
        FrameAppConfig.getInstance().put(IS_DEBUG_MODE, tf);
    }

    /**
     * 获取是否调试模式
     *
     * @return 结果：是否是设置模式
     */
    public Boolean getDebugMode() {
        return (Boolean) FrameAppConfig.getInstance().get(IS_DEBUG_MODE, false);
    }

}
