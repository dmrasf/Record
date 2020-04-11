package com.dmrasf.record.home;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dmrasf.record.MainActivity;
import com.dmrasf.record.R;
import com.dmrasf.record.data.RecordAndDayContract;
import com.dmrasf.record.data.RecordProvider;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


public class ItemRecordFragment extends Fragment {

    private ArrayList<Record> itemRecords = new ArrayList<>();
    private ItemRecordAdapter itemRecordAdapter;
    //想要从数据库中查询的数据
    private String[] projection = {
            RecordAndDayContract.RecordEntry.COLUMN_TITLE,
            RecordAndDayContract.RecordEntry.COLUMN_DATE,
            RecordAndDayContract.RecordEntry.COLUMN_DAYS,
    };


    public ItemRecordFragment() {
        Log.e("==========", "new ItemRecordFragment");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("==========", "onCreate ItemRecordFragment");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_record, container, false);

        Log.e("==========", "onCreateView ItemRecordFragment");

        initToolbar();

        //数据库 并从数据库中提取数据
        updateItemRecordsFromDb(itemRecords);

        // 许多record
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list_record);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        itemRecordAdapter = new ItemRecordAdapter(getActivity(), itemRecords);
        recyclerView.setAdapter(itemRecordAdapter);

        //侧滑删除
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemRecordTouchCallback(itemRecordAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return rootView;
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        //使用自带的导航条  而且有动画
//        actionBarDrawerToggle.syncState();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add_new: {
                        // 添加record  异步
                        addNewRecord();
                        break;
                    }
                    case R.id.action_settings: {
                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    //新建一个record 并存到数据库中 同时更新 itemRecordAdapter
    private void addNewRecord() {
        final EditText editText = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext())).setTitle("取个标题：").setView(editText)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = editText.getText().toString();
                        //用 ContentValues类 向数据库添加数据 并更新adapter
                        insertNewRecordToDb(editText.getText().toString());
                        updateItemRecordsFromDb(itemRecords);
                        //刷新界面
                        itemRecordAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.create().show();
    }

    private void insertNewRecordToDb(String newRecordTitle) {
        RecordProvider recordProvider = new RecordProvider(getContext());
        ContentValues values = new ContentValues();
        values.put(RecordAndDayContract.RecordEntry.COLUMN_TITLE, newRecordTitle);
        values.put(RecordAndDayContract.RecordEntry.COLUMN_DATE, new Date().getTime());
        values.put(RecordAndDayContract.RecordEntry.COLUMN_DAYS, 0);
        values.put(RecordAndDayContract.RecordEntry.COLUMN_DAY_TABLE, "null");
        Uri newUri = recordProvider.insert(RecordAndDayContract.RecordEntry.CONTENT_URI, values);
    }

    //根据数据库更新itemRecordAdapter
    private void updateItemRecordsFromDb(ArrayList<Record> itemRecords) {
        //添加数据前先清理数据
        itemRecords.clear();
        RecordProvider recordProvider = new RecordProvider(getContext());
        Cursor cursor = recordProvider.query(RecordAndDayContract.RecordEntry.CONTENT_URI, projection,
                null, null, null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            String newTitle = cursor.getString(cursor.getColumnIndex(RecordAndDayContract.RecordEntry.COLUMN_TITLE));
            long createDate = cursor.getLong(cursor.getColumnIndex(RecordAndDayContract.RecordEntry.COLUMN_DATE));
            itemRecords.add(new Record(newTitle, R.drawable.cheese_2, createDate));
        }
        cursor.close();
    }
}
