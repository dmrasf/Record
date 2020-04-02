package com.dmrasf.record.home;

import android.content.DialogInterface;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dmrasf.record.R;

import java.util.ArrayList;
import java.util.Objects;


public class ItemRecordFragment extends Fragment {

    private ArrayList<Record> itemRecords = new ArrayList<>();
    private ItemRecordAdapter itemRecordAdapter;

    public ItemRecordFragment() {
        // record 的标题 简略信息  不一定是  string
        for (int i = 0; i < 5; i++) {
            itemRecords.add(new Record(String.valueOf(i), R.drawable.cheese_1));
        }
        Log.e("==========", "new ItemRecordFragment");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_record, container, false);

        Toast.makeText(getActivity(), "ItemRecordFragment 里的 onCreateView", Toast.LENGTH_SHORT).show();

        Log.e("==========", "onCreateView ItemRecordFragment");

        initToolbar(rootView);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list_record);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        itemRecordAdapter = new ItemRecordAdapter(getContext(), itemRecords);
        recyclerView.setAdapter(itemRecordAdapter);

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    private void showInput() {
        final EditText editText = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext())).setTitle("娶个名字：").setView(editText)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "输入内容为：" + editText.getText().toString() + " " + String.valueOf(i)
                                , Toast.LENGTH_LONG).show();
                        //根据输入新建一个record
                        itemRecords.add(new Record(editText.getText().toString(), R.drawable.cheese_4));
                        //刷新界面
                        itemRecordAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "输入内容为：" + editText.getText().toString() + " " + String.valueOf(which)
                                , Toast.LENGTH_LONG).show();
                    }
                });
        builder.create().show();
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
