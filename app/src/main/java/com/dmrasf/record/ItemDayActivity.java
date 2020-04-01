package com.dmrasf.record;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Objects;

public class ItemDayActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_item_day);

        // 初始化toolbar
        initToolbar();

        // 获取上一级的信息 哪个record
        String recordTitle = getIntentFromRecord();

        Toast.makeText(this, "ItemDayActivity" + recordTitle, Toast.LENGTH_SHORT).show();

        final ArrayList<Day> itemDay = new ArrayList<>();
        itemDay.add(new Day(recordTitle + " " + String.valueOf(1), R.drawable.cheese_1));
        itemDay.add(new Day(recordTitle + " " + String.valueOf(2), R.drawable.cheese_2));
        itemDay.add(new Day(recordTitle + " " + String.valueOf(3), R.drawable.cheese_3));
        itemDay.add(new Day(recordTitle + " " + String.valueOf(4), R.drawable.cheese_4));
        itemDay.add(new Day(recordTitle + " " + String.valueOf(5), R.drawable.cheese_5));

        final ItemDayAdapter itemRecordAdapter =
                new ItemDayAdapter(this, itemDay);
        ListView listView = (ListView) findViewById(R.id.list_day);
        listView.setAdapter(itemRecordAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ItemDayActivity.this, DayDetailActivity.class);
                intent.putExtra("transport", itemDay.get(position).getDayImage());
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

    private String getIntentFromRecord() {
        Intent intentFromItemRecord = getIntent();
        return intentFromItemRecord.getStringExtra("transport");
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
