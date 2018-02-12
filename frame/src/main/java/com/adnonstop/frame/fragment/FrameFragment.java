package com.adnonstop.frame.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adnonstop.frame.util.KeyBoardUtils;

import java.util.List;


/**
 * 基础Fragment
 * <p>
 * Created by ikould on 2017/5/31.
 */
public abstract class FrameFragment extends Fragment {

    protected View mContentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onBaseCreateView(inflater, container, savedInstanceState);
        return mContentView;
    }

    /**
     * 代替基类实现的onCreate方法
     *
     * @param inflater           布局加载器
     * @param container          父布局
     * @param savedInstanceState 保存的Bundle
     */
    protected abstract void onBaseCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * 获取ContentView
     */
    protected void setContentView(@LayoutRes int layoutId) {
        mContentView = LayoutInflater.from(getActivity()).inflate(layoutId, null);
    }

    /**
     * 获取ContentView
     */
    protected void setContentView(View view) {
        mContentView = view;
    }

    /**
     * 在Fragment内部嵌套Fragment
     *
     * @param layoutId 资源Id
     * @param fragment 要替换的Fragment
     * @param isDoAnim 是否做动画
     * @param anims    动画，长度要求为2，0表示进入，1表示退出
     */
    protected void replaceChildFragment(int layoutId, Fragment fragment, boolean isDoAnim, int[] anims) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        if (isDoAnim) {
            fragmentTransaction.setCustomAnimations(anims[0], anims[1]);
        }
        fragmentTransaction.replace(layoutId, fragment);
        KeyBoardUtils.closeKeyboard(getActivity());
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 更换Activity上容器的Fragment
     *
     * @param id       Activity上的资源id
     * @param fragment 要替换的Fragment
     * @param tag      Fragment标记 为null表示不加入回退栈中
     */
    protected void replaceFragment(int id, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (tag == null) {
            fragmentTransaction.replace(id, fragment);
        } else {
            fragmentTransaction.replace(id, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
        }
        KeyBoardUtils.closeKeyboard(getActivity());
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 判断当前fragment是否存活
     *
     * @param fragment 传入fragment
     * @return 是否存活
     */
    @SuppressLint("RestrictedApi")
    public boolean isAlive(Fragment fragment) {
        try {
            FragmentManager sFm = getActivity().getSupportFragmentManager();
            List<Fragment> fragments = sFm.getFragments();
            return fragments.contains(fragment);
        } catch (NullPointerException e) {
            return false;
        }
    }

}
