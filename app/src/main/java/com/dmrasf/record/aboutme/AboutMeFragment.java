package com.dmrasf.record.aboutme;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.dmrasf.record.R;
import com.dmrasf.record.data.DayProvider;
import com.dmrasf.record.data.RecordAndDayContract;
import com.dmrasf.record.data.RecordProvider;

import java.util.Objects;

public class AboutMeFragment extends Fragment {

    private TextView mRecordTextView;
    private TextView mDayTextView;

    public AboutMeFragment() {
        Log.e("==========", "new AboutMeFragment");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_aboutme, container, false);

        initToolbar();

        mRecordTextView = rootView.findViewById(R.id.about_record_num);
        mDayTextView = rootView.findViewById(R.id.about_day_num);

        return rootView;
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.aboutme_menu, menu);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            String[] projectionRecord = {
                    RecordAndDayContract.RecordEntry.COLUMN_DATE,
            };
            // 更新数据 从数据库
            RecordProvider recordProvider = new RecordProvider(getContext());
            Cursor cursorRecord = recordProvider.query(RecordAndDayContract.RecordEntry.CONTENT_URI, projectionRecord,
                    null, null, null);
            mRecordTextView.setText(String.valueOf(cursorRecord.getCount()));
            int daySum = 0;
            for (int i = 0; i < cursorRecord.getCount(); i++) {
                cursorRecord.moveToPosition(i);
                long createDate = cursorRecord.getLong(cursorRecord.getColumnIndex(RecordAndDayContract.RecordEntry.COLUMN_DATE));
                String dayTableName = "_" + String.valueOf(createDate);
                DayProvider dayProvider = new DayProvider(getContext(), dayTableName);
                Cursor cursorDay = dayProvider.query(Uri.withAppendedPath(RecordAndDayContract.BASE_CONTENT_URI, dayTableName), null,
                        null, null, null);
                daySum += cursorDay.getCount();
                cursorDay.close();
            }
            mDayTextView.setText(String.valueOf(daySum));
            cursorRecord.close();
        }
    }
}
