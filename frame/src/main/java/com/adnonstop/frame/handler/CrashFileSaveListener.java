package com.adnonstop.frame.handler;

/**
 * 崩溃文件保存监听
 */
public interface CrashFileSaveListener {
    /**
     * 崩溃之后调用
     *
     * @param filePath 文件地址
     */
    void crashFileSaveTo(String filePath);
}
