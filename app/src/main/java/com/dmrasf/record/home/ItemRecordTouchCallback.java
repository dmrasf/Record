package com.dmrasf.record.home;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.dmrasf.record.data.RecordDbHelper;

public class ItemRecordTouchCallback extends ItemTouchHelper.Callback {

    private ItemRecordAdapter mItemRecordAdapter;

    public ItemRecordTouchCallback(ItemRecordAdapter itemRecordAdapter) {
        mItemRecordAdapter = itemRecordAdapter;
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

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//        mItemRecordAdapter.onItemRemove(viewHolder.getAdapterPosition());
    }
}
