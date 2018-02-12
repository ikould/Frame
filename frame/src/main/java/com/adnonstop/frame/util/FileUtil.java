package com.adnonstop.frame.util;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件相关工具类
 * 1. 读取文件 readFile readFileToList
 * 2. 写入文件 writeFile
 * 3. 删除文件 deleteFile
 * 4. 移动文件 moveFile
 * 5. 复制文件 copyFile
 * 6. 判断文件（文件夹）是否存在，获取文件名称、后缀名，创建文件或者文件夹
 * 7. 获取sd卡目录，判断sd是否可用，获取sd卡剩余空间
 */
public class FileUtil {




    private FileUtil() {
        throw new AssertionError();
    }

    /**
     * 读取文件，结果以String形式转出
     *
     * @param filePath 文件地址
     * @return 输出结果
     */
    public static String readFile(String filePath) {
        return readFile(filePath, "UTF-8");
    }

    /**
     * 读取文件，结果以String形式转出
     *
     * @param filePath    文件地址
     * @param charsetName 编码格式
     * @return 输出结果
     */
    public static String readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (!file.isFile()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent.toString();
        } catch (IOException e) {
            Log.e("FileUtil", "readFile: e = " + e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("FileUtil", "readFile: e = " + e);
                }
            }
        }
        return null;
    }

    /**
     * 读取文件，以List返回文件每一行结果
     *
     * @param filePath    文件地址
     * @param charsetName 编码 {@link java.nio.charset.Charset
     * @return 以List记录每一行的结果
     */
    public static List<String> readFileToList(String filePath, String charsetName) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<>();
        if (!file.isFile()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
        } catch (IOException e) {
            Log.e("FileUtil", "readFileToList1: e = " + e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("FileUtil", "readFileToList2: e = " + e);
                }
            }
        }
        return fileContent;
    }

    /**
     * 写文件 将字符串写入对应的文件中去
     *
     * @param filePath 文件地址
     * @param content  待写入的自负床
     * @param append   是否在文件之前的基础上写入，否则清空文件重写
     * @return 操作是否成功
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        FileWriter fileWriter = null;
        try {
            makeFileParents(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            Log.e("FileUtil", "writeFile: e = " + e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    Log.e("FileUtil", "writeFile: e = " + e);
                }
            }
        }
        return false;
    }

    /**
     * 写文件 将字符串写入对应的文件中去
     *
     * @param filePath 文件地址
     * @param content  待写入的自负床
     * @return 操作是否成功
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    /**
     * 写文件 将字符串写入对应的文件中去
     *
     * @param filePath 文件地址
     * @param stream   输入流
     * @return 操作是否成功
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    /**
     * 写文件 将字符串写入对应的文件中去
     *
     * @param file   文件
     * @param stream 输入流
     * @return 操作是否成功
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

    /**
     * 写文件 将字符串写入对应的文件中去
     *
     * @param filePath 文件地址
     * @param stream   输入流
     * @param append   是否在文件之前的基础上写入，否则清空文件重写
     * @return 操作是否成功
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * 写文件 将字符串写入对应的文件中去
     *
     * @param file   文件
     * @param stream 输入流
     * @param append 是否在文件之前的基础上写入，否则清空文件重写
     * @return 操作是否成功
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            makeFileParents(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * 文件移动
     *
     * @param sourceFilePath 文件地址
     * @param destFilePath   目标地址
     */
    public static void moveFile(String sourceFilePath, String destFilePath) {
        if (TextUtils.isEmpty(sourceFilePath) || TextUtils.isEmpty(destFilePath)) {
            throw new RuntimeException("Both sourceFilePath and destFilePath cannot be null.");
        }
        moveFile(new File(sourceFilePath), new File(destFilePath));
    }

    /**
     * 文件移动
     *
     * @param srcFile  文件
     * @param destFile 目标文件
     */
    public static void moveFile(File srcFile, File destFile) {
        boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            deleteFile(srcFile.getAbsolutePath());
        }
    }

    /**
     * 文件拷贝
     *
     * @param sourceFilePath 文件地址
     * @param destFilePath   目标地址文件地址
     * @return 拷贝是否成功
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            Log.e("FileUtil", "copyFile: e = " + e);
            return false;
        }
        return writeFile(destFilePath, inputStream);
    }

    /**
     * 获取文件名
     *
     * @param filePath 文件地址
     * @return 文件名
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int extenPosi = filePath.lastIndexOf(".");
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }

    /**
     * 文件夹的路径
     * 获取文件（具体文件或者文件夹）所在文件夹的路径
     *
     * @param filePath 文件地址
     * @return 所在文件夹路径
     */
    public static String getFileParentPath(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * 后缀名
     * 获取文件后缀名
     *
     * @param filePath 文件地址
     * @return 后缀名
     */
    public static String getFileExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int extenPosi = filePath.lastIndexOf(".");
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * 创建文件夹
     * 创建文件所在的文件夹
     *
     * @param filePath 文件地址
     * @return 最终文件夹是否存在
     */
    public static boolean makeFileParents(String filePath) {
        String folderPath = getFileParentPath(filePath);
        if (TextUtils.isEmpty(folderPath)) {
            return false;
        }
        File folder = new File(folderPath);
        return (folder.exists() && folder.isDirectory()) || folder.mkdirs();
    }

    /**
     * 文件是否存在
     * 注意：是文件不是文件夹
     *
     * @param filePath 文件地址
     * @return 是否存在
     */
    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * 文件夹是否存在
     * 注意：是文件夹不是文件
     *
     * @param directoryPath 文件夹地址
     * @return 是否存在
     */
    public static boolean isFolderExist(String directoryPath) {
        if (TextUtils.isEmpty(directoryPath)) {
            return false;
        }
        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * 删除文件或者文件夹（包括文件夹下的所有文件）
     *
     * @param path 文件路径
     * @return 是否删除成功
     */
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }
        File file = new File(path);
        return deleteFile(file);
    }

    /**
     * 删除文件或者文件夹（包括文件夹下的所有文件）
     *
     * @param file 文件
     * @return 是否删除成功
     */
    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            deleteFile(f);
        }
        return file.delete();
    }

    /**
     * 获取文件大小
     *
     * @param path 文件地址
     * @return 文件大小
     */
    public static long getFileSize(String path) {
        if (TextUtils.isEmpty(path)) {
            return -1;
        }
        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }

    /**
     * 初始化文件（不区别文件或者文件夹）
     *
     * @param filePath 文件或者文件夹地址
     * @return 最终文件是否存在
     */
    public static boolean initFileAnyway(String filePath) {
        boolean result;
        File file = new File(filePath);
        if (!file.exists()) {
            if (isFile(filePath)) { // 文件
                result = initFile(filePath);
            } else { // 文件夹
                result = initDirectory(filePath);
            }
        } else {
            result = true;
        }
        return result;
    }

    /**
     * 初始化文件
     *
     * @param filePath 文件地址
     * @return 最终的文件是否存在
     */
    public static boolean initFile(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return false;
        boolean result = false;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                String parentFilePath = getFileParentPath(filePath);
                boolean isExist = initDirectory(parentFilePath);
                result = isExist;
                if (!isExist) {
                    result = new File(filePath).createNewFile();
                }
            }
        } catch (IOException e) {
            result = false;
            Log.e("FileUtil", "initFile: e = " + e);
        }
        return result;
    }

    /**
     * 是否是文件
     * 根据文件末尾是否有“.”存在判断是否属于文件
     *
     * @param path 文件地址
     * @return 是否是文件
     */
    public static boolean isFile(String path) {
        if (TextUtils.isEmpty(path))
            return false;
        int lastSeparatorPos = path.lastIndexOf(File.separator);
        if (lastSeparatorPos < path.length() && lastSeparatorPos > 0) {
            String lastStr = path.substring(lastSeparatorPos, path.length());
            return lastStr.contains(".");
        }
        return false;
    }

    /**
     * 创建一个文件夹
     *
     * @param fileFolderPath 文件夹地址
     * @return 最终文件是否存在
     */
    public static boolean initDirectory(String fileFolderPath) {
        if (TextUtils.isEmpty(fileFolderPath))
            return false;
        File file = new File(fileFolderPath);
        if (!file.exists()) {
            return file.mkdirs();
        } else {
            return true;
        }
    }

    /**
     * SD卡是否可用
     *
     * @return 是否可用
     */
    public static boolean sdCardIsAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sd = new File(Environment.getExternalStorageDirectory().getPath());
            return sd.canWrite();
        } else
            return false;
    }

    /**
     * 得到Sd根目录
     * 没有sd卡，返回手机内粗存储
     *
     * @return 返回文件地址
     */
    public static String getSdCardPath() {
        if (sdCardIsAvailable()) {
            // 取得sdcard文件路径
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return Environment.getDataDirectory().getAbsolutePath();
        }
    }

    /**
     * 获取SD卡的剩余容量，单位是Byte
     *
     * @return
     */
    public static long getSDAvailableSize() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File pathFile = Environment.getExternalStorageDirectory();
            StatFs statfs = new StatFs(pathFile.getPath());
            // 获取SDCard上每一个block的SIZE
            long nBlockSize = statfs.getBlockSize();
            // long nAvailBlock = statfs.getAvailableBlocksLong();
            long nAvailBlock = statfs.getAvailableBlocks();
            // 计算SDCard剩余大小Byte
            return nAvailBlock * nBlockSize;
        }
        return 0;
    }

    /**
     * 将输入流转换成字符串
     *
     * @param input 输入流
     * @return 输出的字符串
     */
    public static String inputStream2String(InputStream input) {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            Log.e("FileEro", "IOException inputStream2String" + e);
        }
        return builder.toString();
    }

}
