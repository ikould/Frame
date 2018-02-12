package com.adnonstop.frame.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.adnonstop.frame.R;

/**
 * 基类Dialog
 *
 * @author ikould on 2018/1/18.
 */

public abstract class FrameDialog extends Dialog {

    /**
     * 确认类型
     */
    public static final int CONFIRM_TYPE = 0x01;
    /**
     * 删除类型
     */
    public static final int CANCEL_TYPE  = 0x02;

    protected Context mContext;
    // 主View
    protected View    mContentView;
    // 仅仅用于返回监听会执行两次这个问题用的
    private   boolean isShowUseBackKey;

    // 按键监听
    private OnKeyListener onKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (isShowUseBackKey) {
                    dismiss();
                    isShowUseBackKey = false;
                    return true;
                }
            }
            return false;
        }
    };

    public FrameDialog(@NonNull Context context) {
        this(context, R.style.FullDialogTheme);
    }

    private FrameDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        initWindow();
        setOnKeyListener(onKeyListener);
    }

    @Override
    public void setContentView(@NonNull View view) {
        mContentView = view;
        super.setContentView(view);
    }

    @Override
    public void show() {
        super.show();
        isShowUseBackKey = true;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        isShowUseBackKey = false;
    }

    /**
     * 直接消失，适应dismiss重写执行了动画等的情况
     */
    public void dismissDirect() {
        super.dismiss();
        isShowUseBackKey = false;
    }

    /**
     * 获取ContentView
     */
    public void setContentView(@LayoutRes int layoutId) {
        View view = LayoutInflater.from(mContext).inflate(layoutId, null);
        setContentView(view);
    }

    /**
     * 初始化配置
     */
    private void initWindow() {
        Window window = getWindow();  //获得当前窗体
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT; //设置宽度
            lp.height = WindowManager.LayoutParams.MATCH_PARENT; //设置高度
            window.setAttributes(lp);
        }
    }

    // ======== 监听 =========

    protected OnDialogListener dialogListener;

    public void setDialogListener(OnDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public interface OnDialogListener {
        /**
         * dialog状态改变
         *
         * @param type 类型
         */
        void onChange(int type);
    }
}
