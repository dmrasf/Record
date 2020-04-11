package com.dmrasf.record.home.item_day;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.dmrasf.record.MainActivity;
import com.dmrasf.record.R;
import com.dmrasf.record.data.DayProvider;
import com.dmrasf.record.data.RecordAndDayContract;
import com.dmrasf.record.home.Record;
import com.dmrasf.record.home.item_day.day_detail.DayDetailActivity;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ItemDayActivity extends AppCompatActivity {
    private String[] projection = {
            RecordAndDayContract.DayEntry.COLUMN_TEXT,
            RecordAndDayContract.DayEntry.COLUMN_IMG,
            RecordAndDayContract.DayEntry.COLUMN_DATE,
            RecordAndDayContract.DayEntry.COLUMN_IMG_PATH,
    };

    private String mRecordTitle;
    private String mDayTableName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_item_day);

        // 初始化toolbar
        initToolbar();

        // 获取上一级的信息 哪个record 存到私有变量里
        getIntentFromRecord();

        Toast.makeText(this, "ItemDayActivity " + mDayTableName + " " + mRecordTitle, Toast.LENGTH_SHORT).show();

        final ArrayList<Day> itemDay = new ArrayList<>();

        getDayFromDb(itemDay);

        final ItemDayAdapter itemRecordAdapter =
                new ItemDayAdapter(this, itemDay);
        ListView listView = (ListView) findViewById(R.id.list_day);
        listView.setAdapter(itemRecordAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ItemDayActivity.this, DayDetailActivity.class);
                // 传过去 文件地址  显示大图
                intent.putExtra("imgPath", itemDay.get(position).getImagePath());
                intent.putExtra("text", itemDay.get(position).getText());
                intent.putExtra("dayTableName", mDayTableName);
                startActivityForResult(intent, 1);
            }
        });

        final ItemDayActivity itemDayActivity = this;
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Vibrator vibrator = (Vibrator) itemDayActivity.getSystemService(VIBRATOR_SERVICE);
                if (vibrator != null){
                    vibrator.vibrate(50);
                }
                // 弹出提示框
                AlertDialog.Builder builder = new AlertDialog.Builder(itemDayActivity).setTitle("删除，确定吗？")
                        .setNegativeButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (removeDayFromDbAndFile(position)){
                                    // 更新 adapter
                                    itemDay.remove(position);
                                    itemRecordAdapter.notifyDataSetChanged();
                                    Toast.makeText(itemDayActivity, "成功删除", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.create().show();
                return true;
            }
        });
    }

    private boolean removeDayFromDbAndFile(int position) {
        // 删除当前图片从数据库

        // 删除本地文件
        return true;
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 返回按钮
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.play) {
                    playRecord();
                }
                return true;
            }
        });
    }


    private void playRecord() {
    }

    private void getIntentFromRecord() {
        Intent intentFromItemRecord = getIntent();
        mRecordTitle = intentFromItemRecord.getStringExtra("title");
        mDayTableName = intentFromItemRecord.getStringExtra("createDate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_day_menu, menu);
        return true;
    }

    private void getDayFromDb(ArrayList<Day> itemDay) {
        DayProvider dayProvider = new DayProvider(this, mDayTableName);
        Cursor cursor = dayProvider.query(Uri.withAppendedPath(RecordAndDayContract.BASE_CONTENT_URI, mDayTableName), projection,
                null, null, null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            //分别读取 内容 时间 图片路径  图片缩略图
            String text = cursor.getString(cursor.getColumnIndex(RecordAndDayContract.DayEntry.COLUMN_TEXT));
            long createDate = cursor.getLong(cursor.getColumnIndex(RecordAndDayContract.DayEntry.COLUMN_DATE));
            String imgPath = cursor.getString(cursor.getColumnIndex(RecordAndDayContract.DayEntry.COLUMN_IMG_PATH));
            byte[] img = cursor.getBlob(cursor.getColumnIndex(RecordAndDayContract.DayEntry.COLUMN_IMG));
            itemDay.add(new Day(text, createDate, img, imgPath));
        }
        cursor.close();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
//            if (resultCode == RESULT_OK) {
//                recordTitle = data.getStringExtra("date");
//            }
//        }
//    }
}
