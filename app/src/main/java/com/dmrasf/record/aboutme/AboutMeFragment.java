package com.dmrasf.record.aboutme;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.dmrasf.record.R;

public class AboutMeFragment extends Fragment {

    public AboutMeFragment() {
        Log.e("==========", "new AboutMeFragment");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View aboutMeView = inflater.inflate(R.layout.fragment_aboutme, container, false);

        Log.e("==========", "onCreate AboutMeFragment");

        return aboutMeView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("==========", "onDestroy AboutMeFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("==========", "onResume AboutMeFragment");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("==========", "onStop AboutMeFragment");
    }
}
