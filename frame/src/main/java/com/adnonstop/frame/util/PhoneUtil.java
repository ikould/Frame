package com.adnonstop.frame.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * 手机相关工具类
 */
public class PhoneUtil {

    private static final String FILE_MEMORY = "/proc/meminfo";

    private static String IMEI;//手机的IMEI;
    private static int phoneType = -1;//手机的制式类型，GSM OR CDMA 手机
    private static String netWorkCountryIso;//手机网络国家编码
    private static String netWorkOperator;//手机网络运营商ID
    private static String netWorkOperatorName;//手机网络运营商名称
    private static String netWorkType;//手机的数据链接类型
    private static String macAddress; // mac地址
    private static String connectNetType;// 联网方式
    private static String androidId;// AndroidId, 16位

    /**
     * 获取手机的IMEI
     *
     * @param context 上下文
     * @return Imei
     */
    public static String getIMEI(Context context) {
        if (TextUtils.isEmpty(IMEI)) {
            TelephonyManager manager = (TelephonyManager) context
                    .getSystemService(Activity.TELEPHONY_SERVICE);
            IMEI = "000000000000000";
            if (manager != null && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                IMEI = manager.getDeviceId();
            }
        }
        return IMEI;
    }

    /**
     * 获取网络制式
     * like :GSM/CDMA/unKnow;
     *
     * @param context 上下文
     * @return 网络制式
     */
    public static int getPhoneType(Context context) {
        if (phoneType == -1) {
            TelephonyManager manager = (TelephonyManager) context
                    .getSystemService(Activity.TELEPHONY_SERVICE);
            if (manager != null)
                phoneType = manager.getPhoneType();
        }
        return phoneType;
    }

    /**
     * 获取联网方式
     * “0”->unknow，“1”->wifi，“2”->2G，‘3’->3G，‘4’->4G
     *
     * @return 联网方式
     */
    public static String getNetType(Context context) {
        if (TextUtils.isEmpty(connectNetType)) {
            connectNetType = "0";
            NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    connectNetType = "1"; // wifi
                } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    String _strSubTypeName = networkInfo.getSubtypeName();
                    // TD-SCDMA   networkType is 17
                    int networkType = networkInfo.getSubtype();
                    switch (networkType) {
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                            connectNetType = "2"; // 2G
                            break;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                        case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                        case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                            connectNetType = "3"; // 3G
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                            connectNetType = "4"; // 4G
                            break;
                        default:
                            // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                            if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                connectNetType = "3"; // 3G
                            } else {
                                connectNetType = _strSubTypeName;
                            }
                            break;
                    }
                }
            }
        }
        return connectNetType;
    }

    /**
     * 获取系统OS版本
     *
     * @return 系统OS版本
     */
    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机系统SDK版本
     *
     * @return SDK版本
     */
    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 返回国家代码
     *
     * @param context 上下文
     * @return 国家代码
     */
    public static String getNetWorkCountryIso(Context context) {
        if (TextUtils.isEmpty(netWorkCountryIso)) {
            TelephonyManager manager = (TelephonyManager) context
                    .getSystemService(Activity.TELEPHONY_SERVICE);
            if (manager != null)
                netWorkCountryIso = manager.getNetworkCountryIso();
        }
        return netWorkCountryIso;
    }

    /**
     * 返回手机网络运营商ID
     *
     * @param context 上下文
     * @return 运营商ID
     */
    public static String getNetWorkOperator(Context context) {
        if (TextUtils.isEmpty(netWorkOperator)) {
            TelephonyManager manager = (TelephonyManager) context
                    .getSystemService(Activity.TELEPHONY_SERVICE);
            if (manager != null) {
                netWorkOperator = manager.getNetworkOperator();
            }
        }
        return netWorkOperator;
    }

    /**
     * 返回手机网络运营商名称
     *
     * @param context 上下文
     * @return 运营商名称
     */
    public static String getNetWorkOperatorName(Context context) {
        if (TextUtils.isEmpty(netWorkOperatorName)) {
            TelephonyManager manager = (TelephonyManager) context
                    .getSystemService(Activity.TELEPHONY_SERVICE);
            if (manager != null)
                netWorkOperatorName = manager.getNetworkOperatorName();
        }
        return netWorkOperatorName;
    }

    /**
     * 返回当前网络cnwap/cnnet/wifi;
     *
     * @param context 上下文
     * @return 网络类型
     */
    public static String getNetworkType(Context context) {
        if (TextUtils.isEmpty(netWorkType)) {
            netWorkType = "unKnow";
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobNetInfo != null) {
                netWorkType = mobNetInfo.getExtraInfo();
            }
        }
        return netWorkType;
    }

    /**
     * 获取User-Agent
     *
     * @return User-Agent
     */
    public static String getUserAgent() {
        return System.getProperty("http.agent");
    }

    /**
     * 判断当前网络是否可用
     *
     * @param context 上下文
     * @return 网络是否可用
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Activity.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            return info != null && info.isConnected();
        } else {
            return false;
        }
    }

    /**
     * 返回当前的数据链接类型
     *
     * @param context 上下文
     * @return 数据链接类型
     */
    public static String getConnectTypeName(Context context) {
        if (!isOnline(context)) {
            return "OFFLINE";
        }
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Activity.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            return info.getTypeName();
        } else {
            return "OFFLINE";
        }
    }

    /**
     * 返回剩余内存
     *
     * @param context 上下文
     * @return 剩余内存
     */
    public static long getFreeMem(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Activity.ACTIVITY_SERVICE);
        MemoryInfo info = new MemoryInfo();
        if (manager != null)
            manager.getMemoryInfo(info);
        return info.availMem / 1024 / 1024;
    }

    /**
     * 返回可用内存;
     *
     * @return 可用内存大小
     */
    public static long getTotalMem() {
        try {
            FileReader fr = new FileReader(FILE_MEMORY);
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split("\\s+");
            return Long.valueOf(array[1]) / 1024;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取设备名字
     *
     * @return 设备名字
     */
    public static String getProductName() {
        return Build.PRODUCT;
    }

    /**
     * 获取基带版本
     *
     * @return 基带版本
     */
    public static String getBasebandVersion() {
        return Build.VERSION.INCREMENTAL;
    }

    /**
     * 获取手机指纹识别
     *
     * @return 手机指纹识别
     */
    public static String getFingerprint() {
        return Build.FINGERPRINT;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getModelName() {
        return Build.MODEL;
    }

    /**
     * 返回制造商名
     *
     * @return 制造商名
     */
    public static String getManufacturerName() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取本地Mac
     *
     * @return 本地Mac
     */
    public static String getMacAddress() {
        if (TextUtils.isEmpty(macAddress)) {
            try {
                List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface nif : all) {
                    if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        macAddress = "";
                    }
                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:", b));
                    }
                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    macAddress = res1.toString();
                }
            } catch (Exception ex) {
                Log.e("PhoneTools", "getMacAddress: ex = " + ex);
            }
        }
        return macAddress;
    }


    /**
     * 获取AndroidId
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        if (TextUtils.isEmpty(androidId)) {
            androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return androidId;
    }

   /* *//**
     * 返回一个PhoneInfo实例;
     *
     * @param context
     * @return
     *//*
    public static PhoneTools getPhoneInfo(Context context) {
        if (instance == null) {
            instance = new PhoneTools();
            try {
                instance.IMEI = getIMEI(context);
                instance.phoneType = getPhoneType(context);
                instance.mSdkVersion = getSdkVersion();
                instance.mOsVersion = getOsVersion();
                instance.mAppName = getAppName(context);
                instance.mAppVersion = getAppVersion(context);
                instance.netWorkCountryIso = getNetWorkCountryIso(context);
                instance.netWorkOperator = getNetWorkOperator(context);
                instance.netWorkOperatorName = getNetWorkOperatorName(context);
                instance.netWorkType = getNetworkType(context);
                instance.mIsOnLine = isOnline(context);
                instance.mConnectTypeName = getConnectTypeName(context);
                instance.mFreeMem = getFreeMem(context);
                instance.mTotalMem = getTotalMem(context);
                instance.cpuInfo = getCpuInfo();
                instance.mIsHightCpu = CpuUtil.getIsHighCpu(instance.cpuInfo);
                Log.e("fuck", "getPhoneInfo: " + instance.cpuInfo);
                Log.e("fuck", "getPhoneInfo: " + instance.mIsHightCpu);
                instance.mProductName = getProductName();
                instance.mModelName = getModelName();
                instance.mManufacturerName = getManufacturerName();
                instance.mBasebandVersion = getBasebandVersion();
                instance.mFingerprint = getFingerprint();
                instance.macAddress = getMacAddress();
                instance.mNetType = getNetType(context);
                instance.androidId = getAndroidId(context);
                instance.mUA = getUserAgent(context);
            } catch (Exception e) {
                Log.e("PhoneTools", "getPhoneInfo: e = " + e);
            }
        }
        return instance;
    }*/
}
