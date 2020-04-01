package com.dmrasf.record;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


// 每个record的操作  侧滑删除  按钮添加 ...
public class ItemRecordAdapter extends
        RecyclerView.Adapter<ItemRecordAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Record> mRecords;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View view;
        public final ImageView imageView;
        public final TextView textView;
        public final Button button;

        public ViewHolder(View v) {
            super(v);
            view = v;
            imageView = v.findViewById(R.id.item_record_image_view);
            textView = v.findViewById(R.id.item_record_text_view);
            button = v.findViewById(R.id.item_record_button);
        }
    }

    public ItemRecordAdapter(Context context, ArrayList<Record> records) {
        mContext = context;
        mRecords = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Record currentRecord = mRecords.get(position);
        holder.textView.setText(currentRecord.getTitle());
        holder.imageView.setImageResource(currentRecord.getRecordImage());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ItemDayActivity.class);
                // 具体的每个record的信息  需要根据她来从文件中提取信息
                intent.putExtra("transport", holder.textView.getText());
                v.getContext().startActivity(intent);
            }
        });

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "button" + holder.textView.getText(), Toast.LENGTH_SHORT).show();
                showList();
            }
        });
    }

    //选择照相机 相册
    private void showList() {
        final String[] items = {"相机", "相册"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(mContext, "你点击的内容为： " + items[i], Toast.LENGTH_LONG).show();
                        if (i == 0) {

                        } else {

                        }
                    }
                });
        builder.create().show();
    }
    @Override
    public int getItemCount() {
        return mRecords.size();
    }
}
