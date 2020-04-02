package com.dmrasf.record.home;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dmrasf.record.R;
import com.dmrasf.record.data.RecordAndDayContract;
import com.dmrasf.record.data.RecordDbHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


public class ItemRecordFragment extends Fragment {

    private ArrayList<Record> itemRecords = new ArrayList<>();
    private ItemRecordAdapter itemRecordAdapter;
    private ContentValues values = new ContentValues();
    private SQLiteDatabase db;
    //想要从数据库中查询的数据
    private String[] projection = {
            RecordAndDayContract.RecordEntry.COLUMN_TITLE,
            RecordAndDayContract.RecordEntry.COLUMN_DATE,
            RecordAndDayContract.RecordEntry.COLUMN_DAYS,
    };

    public ItemRecordFragment() {
        Log.e("==========", "new ItemRecordFragment");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_record, container, false);

        Toast.makeText(getActivity(), "ItemRecordFragment 里的 onCreateView", Toast.LENGTH_SHORT).show();

        Log.e("==========", "onCreateView ItemRecordFragment");

        initToolbar(rootView);

        //数据库 并从数据库中提取数据
        initDatabase(rootView);
        updateItemRecordsFromDb(itemRecords);

        // 许多record
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list_record);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        itemRecordAdapter = new ItemRecordAdapter(getContext(), itemRecords);
        recyclerView.setAdapter(itemRecordAdapter);
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemRecordTouchCallback(itemRecordAdapter));
//        itemTouchHelper.attachToRecyclerView(recyclerView);

        return rootView;
    }

    private void initToolbar(View rootView) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add_new: {
                        // 添加record  异步
                        showInput();
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

    private void initDatabase(View rootView) {
        RecordDbHelper recordDbHelper = new RecordDbHelper(rootView.getContext());
        db = recordDbHelper.getReadableDatabase();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    //新建一个record 并存到数据库中 同时更新 itemRecordAdapter
    private void showInput() {
        final EditText editText = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext())).setTitle("取个标题：").setView(editText)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
        values.put(RecordAndDayContract.RecordEntry.COLUMN_TITLE, newRecordTitle);
        values.put(RecordAndDayContract.RecordEntry.COLUMN_DATE, new Date().getTime());
        values.put(RecordAndDayContract.RecordEntry.COLUMN_DAYS, 0);
        values.put(RecordAndDayContract.RecordEntry.COLUMN_DAY_TABLE, "null");
        db.insert(RecordAndDayContract.RecordEntry.TABLE_NAME, null, values);
    }

    //根据数据库更新itemRecordAdapter
    private void updateItemRecordsFromDb(ArrayList<Record> itemRecords) {
        //添加数据前先清理数据
        itemRecords.clear();
        Cursor cursor = db.query(RecordAndDayContract.RecordEntry.TABLE_NAME, projection,
                null, null,
                null, null, null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            String newTitle = cursor.getString(cursor.getColumnIndex(RecordAndDayContract.RecordEntry.COLUMN_TITLE));
            long createDate = cursor.getLong(cursor.getColumnIndex(RecordAndDayContract.RecordEntry.COLUMN_DATE));
            itemRecords.add(new Record(newTitle, R.drawable.cheese_2, createDate));
        }
        cursor.close();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("==========", "onStart ItemRecordFragment");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("==========", "onStop ItemRecordFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("==========", "onResume ItemRecordFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("==========", "onDestroy ItemRecordFragment");
    }
}
