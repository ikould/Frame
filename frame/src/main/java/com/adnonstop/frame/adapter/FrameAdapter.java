package com.adnonstop.frame.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * 继承于RecyclerView.Adapter 实现item点击监听
 * <p>
 * Created by xlp on 2017-5-22.
 */
public abstract class FrameAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    //item点击事件
    private OnItemClickListener     mOnItemClickListener;
    //item长按事件
    private OnItemLongClickListener mOnItemLongClickListener;

    @Override
    public abstract T onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(final T holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {//item点击事件
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {//item长按事件
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
                }
                return true;
            }
        });
        onBaseBindViewHolder(holder, position);
    }

    /**
     * 重新实现的onBindViewHolder 方法
     *
     * @param holder   ViewHolder
     * @param position 位置
     */
    protected abstract void onBaseBindViewHolder(T holder, int position);

    @Override
    public abstract int getItemCount();


    // ======== 监听 ========

    /**
     * item点击事件
     */
    public interface OnItemClickListener {
        /**
         * Item点击监听
         *
         * @param view     Item View
         * @param position 位置
         */
        void onItemClick(View view, int position);
    }

    /**
     * item长按事件
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    /**
     * 设置item点击事件
     *
     * @param onItemClickListener item 点击事件
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 设置item长按事件
     *
     * @param onItemLongClickListener 长点击监听
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

}
