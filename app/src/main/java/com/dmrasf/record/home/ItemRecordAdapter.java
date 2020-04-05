package com.dmrasf.record.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.dmrasf.record.MainActivity;
import com.dmrasf.record.R;
import com.dmrasf.record.home.item_day.ItemDayActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;



// 每个record的操作  侧滑删除  按钮添加 ...
public class ItemRecordAdapter extends
        RecyclerView.Adapter<ItemRecordAdapter.ViewHolder> {

    private MainActivity mActivity;
    private ArrayList<Record> mRecords;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View view;
//        public final ImageView imageView;
        public final TextView textView;
        public final TextView dateTextView;
        public final Button button;

        public ViewHolder(View v) {
            super(v);
            view = v;
//            imageView = v.findViewById(R.id.item_record_image_view);
            textView = v.findViewById(R.id.item_record_text_view);
            dateTextView = v.findViewById(R.id.item_record_date_text_view);
            button = v.findViewById(R.id.item_record_button);
        }
    }

    public ItemRecordAdapter(Activity activity, ArrayList<Record> records) {
        mActivity = (MainActivity) activity;
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
        final Record currentRecord = mRecords.get(position);
        holder.textView.setText(currentRecord.getTitle());
//        holder.imageView.setImageResource(currentRecord.getRecordImage());
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentRecord.getDate());
        holder.dateTextView.setText(date);

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
                Toast.makeText(mActivity, "button" + holder.textView.getText(), Toast.LENGTH_SHORT).show();
                showList(currentRecord.getTitle());

            }
        });
    }

    public void onItemRemove(int position) {
        mRecords.remove(position);
    }

    //选择照相机 相册
    private void showList(final String recordTitle) {
        final String[] items = {"相机", "相册"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(mActivity, "你点击的内容为： " + items[i], Toast.LENGTH_LONG).show();
                        mActivity.setRecordTitle(recordTitle);
                        if (i == 0) {
                            openCamera();
                        } else {
                            openPicture();
                        }
                        // 选择图片后进入写模式
                    }
                });
        builder.create().show();
    }
    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
        mActivity.startActivityForResult(intent, 1);
    }

    private void openPicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        mActivity.startActivityForResult(intent, 2);
    }
}
