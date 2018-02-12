package com.adnonstop.frame.application;

import android.app.Application;
import android.os.Handler;

import com.adnonstop.frame.handler.CrashFileSaveListener;
import com.adnonstop.frame.handler.CrashHandler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 实现自定义异常处理的Application
 */
public abstract class FrameApplication extends Application implements CrashFileSaveListener {

    protected CrashHandler crashHandler;

    // 整个应用统一调用
    public Handler  handler        = new Handler();
    // 单一线程池（可用于数据库读写操作）
    public Executor singleExecutor = Executors.newSingleThreadExecutor();
    // 最多含有5个线程的线程池
    public Executor mostExecutor   = Executors.newFixedThreadPool(5);

    @Override
    public void onCreate() {
        super.onCreate();
        onBaseCreate();
    }

    /**
     * 崩溃拦截
     *
     * @param tf 是否开启崩溃拦截
     */
    protected void setCrashHandlerEnable(boolean tf) {
        if (tf) {
            //  设置默认异常处理Handler
            crashHandler = CrashHandler.getInstance(this);
            crashHandler.init(getApplicationContext());
        }
    }

    protected abstract void onBaseCreate();

    @Override
    public void crashFileSaveTo(String filePath) {
        // 崩溃文件保存监听
    }
}
