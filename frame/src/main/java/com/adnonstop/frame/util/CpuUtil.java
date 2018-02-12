package com.adnonstop.frame.util;

import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Cpu相关工具类
 */
public class CpuUtil {

    private static final String FILE_CPU = "/proc/cpuinfo";

    private static final String HIGH_CPU =
            // -------联发科------
            "MT6755" + //P10
                    "MT6755M" + //P10
                    "MT6755T" + //P15
                    "MT6795" + //X10
                    "MT6757" + //P20
                    "MT6767CD" + //P25
                    "MT6797" + //X20
                    "MT6797M" + //X20
                    "MT6797D" + //X23
                    "MT6797T" + //X25
                    "MT6797X" + //X27
                    "MT6799" +  //X30
                    // -------三星------
                    "Exynos 5433" +
                    "Exynos 5800" +
                    "Exynos 7420" +
                    "Exynos 7580" +
                    "Exynos 7870" +
                    "Exynos 8890" +
                    "Exynos 8895" +
                    // -------华为------ //获取不到
                    "hi3650" + //950 955
                    "hi3660" + //960
                    // -------小米------
                    "澎湃S1" +
                    // -------高通------
                    "MSM8953" + //625
                    "MSM8953Pro" + //626
                    "MSM8956" + //650
                    "MSM8976" + //652 653
                    "MSM8976Pro" + //660
                    "SDM660" + //660 oppoR11实测
                    "MSM8992" + //808
                    "MSM8994" + //810
                    "MSM8996" + //820
                    "MSM8996Pro" + //821
                    "MSM8998" + //835
                    "";

    private static String cpuInfo;//手机CPU型号

    /**
     * 获取CPU信息;
     *
     * @return CPU信息
     */
    public static String getCpuInfo() {
        if (TextUtils.isEmpty(cpuInfo)) {
            try {
                FileReader fr = new FileReader(FILE_CPU);
                BufferedReader br = new BufferedReader(fr);
                String text;
                while ((text = br.readLine()) != null) {
                    if (text.contains("Hardware")) {
                        break;
                    }
                }
                if (!TextUtils.isEmpty(text)) {
                    String[] array = text.split(":", 2);
                    String cpu = array[1].trim();
                    if (cpu.contains("MSM")) {
                        //高通系列
                        String[] msms = cpu.split("MSM");
                        cpuInfo = "MSM" + msms[1];
                    } else if (cpu.contains("SDM")) {
                        String[] msms = cpu.split("SDM");
                        cpuInfo = "SDM" + msms[1];
                    } else if (cpu.contains("MT")) {
                        //联发科系列
                        cpuInfo = cpu;
                    } else if (cpu.contains("Exynos")) {
                        //三星系列
                        String[] exynos = cpu.split("Exynos");
                        cpuInfo = "Exynos " + exynos[1];
                    } else {
                        cpuInfo = cpu;
                    }
                } else {
                    //华为要另外的方式获取
                    if (Build.HARDWARE.contains("hi")) {
                        //华为芯片
                        cpuInfo = Build.HARDWARE;
                    } else {
                        cpuInfo = "";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cpuInfo;
    }

    /**
     * 是否是高性能cpu
     *
     * @return tf
     */
    public static boolean isHighCpu() {
        return isHighCpu(getCpuInfo());
    }

    /**
     * 是否是高性能cpu
     *
     * @param cpu cpu型号
     * @return tf
     */
    public static boolean isHighCpu(String cpu) {
        if (TextUtils.isEmpty(cpu)) {
            return false;
        } else {
            if (HIGH_CPU.contains(cpu)) {
                return true;
            } else {
                return false;
            }
        }
    }

}
