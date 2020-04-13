package com.dmrasf.record.home;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemRecordTouchCallback extends ItemTouchHelper.Callback {

    private ItemRecordAdapter mItemRecordAdapter;

    private int mCurrentScrollX;
    private int mCurrentScrollXWhenInactive;
    private float mInitXWhenInactive;
    private boolean mFirstInactive;
    private Context mContext;

    public ItemRecordTouchCallback(ItemRecordAdapter itemRecordAdapter, Context context) {
        mItemRecordAdapter = itemRecordAdapter;
        mContext = context;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.LEFT;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    // 取消原有侧滑删除操作
    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return Integer.MAX_VALUE;
    }
    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        // dp to dpi
        final float scale =  mContext.getResources().getDisplayMetrics().density;
        int mDefaultScrollX  = Math.abs((int) (70 * scale + 0.5f));

        Log.e("==============", String.valueOf(dX) + "   " + isCurrentlyActive);

        // 首次滑动时，记录下ItemView当前滑动的距离
        if (dX == 0) {
            mCurrentScrollX = viewHolder.itemView.getScrollX();
            mFirstInactive = true;
        }
        if (isCurrentlyActive) { // 手指滑动
            // 基于当前的距离滑动
            viewHolder.itemView.scrollTo(mCurrentScrollX + (int) -dX, 0);
        } else { // 动画滑动
            if (mFirstInactive) {
                mFirstInactive = false;
                mCurrentScrollXWhenInactive = viewHolder.itemView.getScrollX();
                mInitXWhenInactive = dX;
            }
            if (viewHolder.itemView.getScrollX() >= mDefaultScrollX) {
                // 当手指松开时，ItemView的滑动距离大于给定阈值，那么最终就停留在阈值，显示删除按钮。
                viewHolder.itemView.scrollTo(Math.max(mCurrentScrollX + (int) -dX, mDefaultScrollX), 0);
            } else {
                // 这里只能做距离的比例缩放，因为回到最初位置必须得从当前位置开始，dx不一定与ItemView的滑动距离相等
                viewHolder.itemView.scrollTo((int) (mCurrentScrollXWhenInactive * dX / mInitXWhenInactive), 0);
            }
        }

    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//        mItemRecordAdapter.onItemRemove(viewHolder.getAdapterPosition());
    }
}
