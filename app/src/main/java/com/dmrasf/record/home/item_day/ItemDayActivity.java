package com.dmrasf.record.home.item_day;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_item_day);

        // 初始化toolbar
        initToolbar();

        // 获取上一级的信息 哪个record
        getIntentFromRecord();

        Toast.makeText(this, "ItemDayActivity" + mRecordTitle, Toast.LENGTH_SHORT).show();

        final ArrayList<Day> itemDay = new ArrayList<>();

        DayProvider dayProvider = new DayProvider(this, mRecordTitle);
        Cursor cursor = dayProvider.query(Uri.withAppendedPath(RecordAndDayContract.BASE_CONTENT_URI, mRecordTitle), projection,
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
                intent.putExtra("recordTitle", mRecordTitle);
                startActivityForResult(intent, 1);
            }
        });
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
    }

    private void getIntentFromRecord() {
        Intent intentFromItemRecord = getIntent();
        mRecordTitle = intentFromItemRecord.getStringExtra("transport");
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
