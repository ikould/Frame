package com.adnonstop.frame.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.adnonstop.frame.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Activity基类
 * <p>
 * Created by ikould on 2017/5/31.
 */
public abstract class FrameActivity extends AppCompatActivity {

    // 权限
    private Map<Integer, OnPermissionResultListener> permissionMap;

    /**
     * 代替Activity自带的onCreate方法
     *
     * @param savedInstanceState 保存的Bundle
     */
    protected abstract void onBaseCreate(Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏系统标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
        onBaseCreate(savedInstanceState);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (lifeListener != null)
            lifeListener.onLifeCall(OnActivityLifeListener.ON_CREATE);
    }

    /**
     * 设置沉浸式
     */
    public void setImmersive() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * Activity重定向
     *
     * @param clazz    跳转的Activity
     * @param tfFinish 当前Activity是否finish
     */
    public void goToActivity(Class<? extends FrameActivity> clazz, boolean tfFinish) {
        goToActivity(clazz, tfFinish, R.anim.anim_in_right, R.anim.anim_out_left);
    }

    /**
     * Activity重定向
     *
     * @param clazz     跳转的Activity
     * @param tfFinish  当前Activity是否finish
     * @param enterAnim 开启Activity的动画
     * @param outAnim   退出Activity的动画
     */
    public void goToActivity(Class<? extends FrameActivity> clazz, boolean tfFinish, @AnimRes int enterAnim, @AnimRes int outAnim) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        if (tfFinish) {
            finish();
        }
        overridePendingTransition(enterAnim, outAnim);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (lifeListener != null)
            lifeListener.onLifeCall(OnActivityLifeListener.ON_START);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (lifeListener != null)
            lifeListener.onLifeCall(OnActivityLifeListener.ON_RESTART);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lifeListener != null)
            lifeListener.onLifeCall(OnActivityLifeListener.ON_RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lifeListener != null)
            lifeListener.onLifeCall(OnActivityLifeListener.ON_PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (lifeListener != null)
            lifeListener.onLifeCall(OnActivityLifeListener.ON_STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lifeListener != null)
            lifeListener.onLifeCall(OnActivityLifeListener.ON_DESTROY);
        lifeListener = null; // 销毁时主动置null
    }

    /**
     * 权限申请
     *
     * @param permissions 权限
     * @param listener    执行完毕后的监听
     */
    public void checkPermission(String[] permissions, OnPermissionResultListener listener) {
        boolean isRequest = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    isRequest = true;
                    break;
                }
            }
            if (isRequest) {
                int code = 100;
                if (permissionMap == null) {
                    permissionMap = new HashMap<>();
                } else {
                    Set<Integer> integerSet = permissionMap.keySet();
                    int max = code;
                    for (int integer : integerSet) {
                        if (integer > max)
                            max = integer;
                    }
                    code = max + 1;// 在原先最大值上加1
                }
                permissionMap.put(code, listener);
                ActivityCompat.requestPermissions(this, permissions, code);
                return;
            }
        }
        if (listener != null)
            listener.permissionResult(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isSuccess = true;
        Log.d("PermissionUtil", "permissionResult: grantResults = " + Arrays.toString(grantResults));
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isSuccess = false;
            }
        }
        if (permissionMap != null && permissionMap.keySet().contains(requestCode)) {
            OnPermissionResultListener listener = permissionMap.get(requestCode);
            if (listener != null)
                listener.permissionResult(isSuccess);
        }
    }

    // ======== 监听 ========

    private OnActivityLifeListener lifeListener;

    public void setOnActivityLifeListener(OnActivityLifeListener lifeListener) {
        this.lifeListener = lifeListener;
    }
}
