package com.adnonstop.frame.util;

import android.graphics.Rect;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

/**
 * View操作帮助工具
 * <p>
 * Created by liudong on 2017/8/7.
 */

public class ViewUtil {

    /**
     * 密码显隐状态管理
     *
     * @param editText EditText
     * @param checkBox CheckBox
     */
    public static void bindPasswordToEye(final EditText editText, CheckBox checkBox) {
        boolean checked = checkBox.isChecked();
        editText.setTransformationMethod(checked ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("PasswordUtil", "bindPasswordToEye: checked2 = " + isChecked);
                editText.setTransformationMethod(isChecked ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
                editText.setSelection(editText.length());
            }
        });
    }

    /**
     * 扩大View的触摸和点击响应范围,最大不超过其父View范围,最小范围不会小于view本身的范围
     *
     * @param view   需要扩展的View
     * @param top    顶部扩展大小
     * @param bottom 底部扩展大小
     * @param left   左侧扩展大小
     * @param right  右侧扩展大小
     */
    public static void expandViewTouchDelegate(View view, int top,
                                               int bottom, int left, int right) {
        Rect bounds = new Rect();
        view.setEnabled(true);
        view.getHitRect(bounds);
        bounds.top -= top;
        bounds.bottom += bottom;
        bounds.left -= left;
        bounds.right += right;
        TouchDelegate touchDelegate = new TouchDelegate(bounds, view);
        if (View.class.isInstance(view.getParent())) {
            ((View) view.getParent()).setTouchDelegate(touchDelegate);
        }
    }
}
