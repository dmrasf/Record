package com.dmrasf.record.aboutme;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.dmrasf.record.R;

import java.util.Objects;

public class AboutMeFragment extends Fragment {

    public AboutMeFragment() {
        Log.e("==========", "new AboutMeFragment");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View aboutMeView = inflater.inflate(R.layout.fragment_aboutme, container, false);

        Log.e("==========", "onCreate AboutMeFragment");

        initToolbar(aboutMeView);

        return aboutMeView;
    }

    private void initToolbar(final View rootView) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
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
