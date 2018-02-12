package com.adnonstop.frame.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * 处理文件解压、压缩的工具类
 */
public class ZipUtils {

    private static final String TAG = "ZipUtils";

    /**
     * 解压缩功能
     * 将zipFilePath文件解压到folderPath目录下
     *
     * @param zipFilePath 要被解压文件路径
     * @param folderPath  文件根目录
     * @param isCurrent   是否直接解压到根目录，或者以创建一个新一个和自己文件名相同的文件夹目录下
     * @return ZipEntry
     */
    public static ZipEntry UnZipFile(String zipFilePath, String folderPath, boolean isCurrent, OnChangeUnZipFileTypeListener l) {
        return UnZipFile(zipFilePath, folderPath, null, isCurrent, l);
    }

    /**
     * 解压缩功能,解压到zip文件的根目录
     * 将zipFilePath文件解压到folderPath目录下
     *
     * @param zipFilePath 要被解压文件完整路径
     * @param filter      修改压缩文件里面的文件夹过滤器
     * @param isCurrent   是否直接解压到根目录，或者以创建一个新一个和自己文件名相同的文件夹目录下
     * @param l           更改压缩文件里面文件的文件名，这里可以用来改解压出来的文件的文件后缀
     * @return ZipEntry
     */
    public static ZipEntry UnZipFile(String zipFilePath, HashMap<Integer, Object> filter, boolean isCurrent, OnChangeUnZipFileTypeListener l) {
        if (zipFilePath == null) {
            return null;
        }
        ZipEntry flag = null;
        File zipFile = new File(zipFilePath);
        if (zipFile != null) {
            String parentPath = zipFile.getParent();
            flag = UnZipFile(zipFilePath, parentPath, isCurrent, l);
        }
        return flag;
    }

    /**
     * 解压缩功能
     * 将zipFilePath文件解压到folderPath目录下
     *
     * @param zipFilePath      要被解压文件完整路径
     * @param folderPath       被解压文件的解压到的目标根目录完整路径
     * @param filter           修改压缩文件里面的文件夹过滤器
     * @param isCurrent        是否直接解压到根目录，或者以创建一个新一个和自己文件名相同的文件夹目录下
     * @param onChangeListener 更改压缩文件里面文件的文件名，这里可以用来改解压出来的文件的文件后缀
     * @return ZipEntry
     */
    public static ZipEntry UnZipFile(String zipFilePath, String folderPath, HashMap<Integer, Object> filter, boolean isCurrent, OnChangeUnZipFileTypeListener onChangeListener) {
        File zipFile = new File(zipFilePath);
        if (!isCurrent) {
            String zipFileTmp = zipFilePath.substring(zipFilePath.lastIndexOf("/") + 1, zipFilePath.lastIndexOf("."));
            folderPath = folderPath + File.separator + zipFileTmp;
        }
        try {
            ZipFile zFile = new ZipFile(zipFile);
            Enumeration<? extends ZipEntry> zList = zFile.entries();
            ZipEntry ze = null;
            byte[] buf = new byte[1024];
            while (zList.hasMoreElements()) {
                ze = zList.nextElement();
                if (ze.isDirectory()) {
                    PLog.d(TAG, "ze.getName()=" + ze.getName());
                    continue;
                }
                PLog.d(TAG, "ze.getName()=" + ze.getName());
                String tmp = ze.getName();
                if (!tmp.contains("MACOSX")) {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName(), filter, onChangeListener)));
                    BufferedInputStream bis = new BufferedInputStream(zFile.getInputStream(ze));
                    int len = 0;
                    while ((len = bis.read(buf)) != -1) {
                        bos.write(buf, 0, len);
                    }
                    bos.flush();
                    bis.close();
                    bos.close();
                }
            }
            zFile.close();
            PLog.d("UnZip", "finish unzip!");
            return ze;
        } catch (Exception e) {
            PLog.e("ZipEro", "ZipException" + e);
            return null;
        }
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名
     *
     * @param baseDir     指定跟目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return File
     */
    public static File getRealFileName(String baseDir, String absFileName, OnChangeUnZipFileTypeListener l) {
        return getRealFileName(baseDir, absFileName, null, l);
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名
     *
     * @param baseDir     指定跟目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return File
     */
    public static File getRealFileName(String baseDir, String absFileName, HashMap<Integer, Object> filter, OnChangeUnZipFileTypeListener l) {
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        String subStr = null;
        if (dirs.length > 0) {
            for (int i = 0; i < dirs.length - 1; i++) {
                subStr = dirs[i];
                if (filter != null) {
                    Object arg = filter.get(i);
                    if (arg != null) {
                        if (arg instanceof String) {
                            subStr = (String) arg;
                        } else if (arg instanceof OnChangeUnZipFileDirListener) {
                            OnChangeUnZipFileDirListener listener = (OnChangeUnZipFileDirListener) arg;
                            subStr = listener.onChangeDirFileName(subStr);
                        }
                    }
                }
                try {
                    //将str用gb2312进行重新编码，然后转换成ISO-8859-1格式
                    PLog.d(TAG, "subStr=" + subStr);
                    subStr = new String(subStr.getBytes("8859_1"), "GB2312");
                    PLog.d(TAG, "subStr=" + subStr);
                } catch (UnsupportedEncodingException e) {
                    PLog.e("ZipEro", "UnsupportedEncodingException" + e);
                }
                ret = new File(ret, subStr);
            }
            PLog.d(TAG, "1ret = " + ret);
            if (!ret.exists()) {
                ret.mkdirs();
            }
            subStr = dirs[dirs.length - 1];
            String tmp = subStr;
            if (l != null) {
                subStr = l.onChangeFileType(subStr);
                if (subStr == null) {
                    subStr = tmp;
                }
            }
            try {
                subStr = new String(subStr.getBytes("8859_1"), "GB2312");
            } catch (UnsupportedEncodingException e) {
                PLog.e("ZipEro", "UnsupportedEncodingException" + e);
            }
            ret = new File(ret, subStr);
            PLog.d(TAG, "2ret = " + ret);
            if (!ret.exists()) {
                try {
                    ret.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return ret;
        }
        return ret;
    }

    public interface OnChangeUnZipFileTypeListener {
        String onChangeFileType(String fileName);
    }

    public interface OnChangeUnZipFileDirListener {
        String onChangeDirFileName(String fileName);
    }
}
