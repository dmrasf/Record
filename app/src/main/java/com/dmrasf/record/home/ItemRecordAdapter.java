package com.dmrasf.record.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.dmrasf.record.MainActivity;
import com.dmrasf.record.R;
import com.dmrasf.record.data.DayProvider;
import com.dmrasf.record.data.RecordAndDayContract;
import com.dmrasf.record.data.RecordProvider;
import com.dmrasf.record.home.item_day.ItemDayActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


// 每个record的操作  侧滑删除  按钮添加 ...
public class ItemRecordAdapter extends
        RecyclerView.Adapter<ItemRecordAdapter.ViewHolder> {

    private MainActivity mActivity;
    private ArrayList<Record> mRecords;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View view;
        public final TextView textView;
        public final TextView dateTextView;
        public final Button addButton;
        public final Button removeButton;

        public ViewHolder(View v) {
            super(v);
            view = v;
            textView = v.findViewById(R.id.item_record_text_view);
            dateTextView = v.findViewById(R.id.item_record_date_text_view);
            addButton = v.findViewById(R.id.item_record_add_button);
            removeButton = v.findViewById(R.id.item_record_remove_button);
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
                intent.putExtra("title", currentRecord.getTitle());
                intent.putExtra("createDate", "_" + String.valueOf(currentRecord.getDate()));
                v.getContext().startActivity(intent);
            }
        });

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showList(currentRecord.getTitle(), currentRecord.getDate());

            }
        });

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 弹出提示框
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(mActivity)).setTitle(R.string.deleteConfirmation)
                        .setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 确认后 先删除数据库 根据recordTitle
                                removeRecordFromDbAndFile(position);
                                mRecords.remove(position);
                                // 更新 adapter
                                notifyDataSetChanged();
                            }
                        }).setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.create().show();
            }
        });
    }

    private void removeRecordFromDbAndFile(int position) {
        // 获取当前标题
        String recordTitle = mRecords.get(position).getTitle();
        // day表
        String dayTableName = "_" + String.valueOf(mRecords.get(position).getDate());
        // 删除真实文件
        File dir = mActivity.getExternalFilesDir(dayTableName);
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            Toast.makeText(mActivity, R.string.deleteFail, Toast.LENGTH_SHORT).show();
            return;
        }
        if (dir.listFiles() != null) {
            for (File img : dir.listFiles()) {
                if (img.isFile()) {
                    img.delete();
                }
            }
            // 删除record表里的一行
            RecordProvider recordProvider = new RecordProvider(mActivity);
            recordProvider.delete(RecordAndDayContract.RecordEntry.CONTENT_URI, RecordAndDayContract.RecordEntry.COLUMN_TITLE + "=?", new String[]{recordTitle});
            // 删除day 整个表
            DayProvider dayProvider = new DayProvider(mActivity, dayTableName);
            dayProvider.deleteTable();
        }
        // 删除文件夹
        dir.delete();
    }

    //选择照相机 相册
    private void showList(final String recordTitle, final long createDate) {
        final String[] items = {mActivity.getString(R.string.camera), mActivity.getString(R.string.album)};
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mActivity.setRecordTitle(recordTitle, createDate);
                        if (i == 0) {
                            openCamera();
                        } else {
                            openPicture();
                        }
                    }
                });
        builder.create().show();
    }
    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    private void openCamera() {
        File path = mActivity.getExternalFilesDir(null);
        String tempPath = path.getPath() + File.separator + "temp.jpg";
        File tempFile = new File(tempPath);
        Uri tempUri = FileProvider.getUriForFile(mActivity, "com.dmrasf.record.fileProvider", tempFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        mActivity.startActivityForResult(intent, 1);
    }

    private void openPicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        mActivity.startActivityForResult(intent, 2);
    }
}
