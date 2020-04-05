package com.dmrasf.record;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import com.dmrasf.record.aboutme.AboutMeFragment;
import com.dmrasf.record.home.ItemRecordFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ItemRecordFragment itemRecordFragment = new ItemRecordFragment();
    private AboutMeFragment aboutMeFragment = new AboutMeFragment();
    private FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

    private String mRecordTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("==========", "onCreate MainActivity");

        // 通过bottom 绑定到不同的Fragment
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom);

        // 默认显示itemRecordFragment
        if (!itemRecordFragment.isAdded() && !aboutMeFragment.isAdded()) {
            ft.add(R.id.main_fragment, itemRecordFragment);
            ft.add(R.id.main_fragment, aboutMeFragment);
        }
        ft.hide(aboutMeFragment).show(itemRecordFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // 新建一个事务 可以再次commit
                        ft = getSupportFragmentManager().beginTransaction();
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                if (itemRecordFragment.isHidden()) {
                                    ft.hide(aboutMeFragment).show(itemRecordFragment).commit();
                                } else {
                                    // 回到顶部
                                    RecyclerView recyclerView = Objects.requireNonNull(itemRecordFragment.getActivity()).findViewById(R.id.list_record);
                                    recyclerView.scrollToPosition(0);
                                }
                                return true;
                            case R.id.navigation_about:
                                ft.hide(itemRecordFragment).show(aboutMeFragment).commit();
                                return true;
                        }
                        return false;
                    }
                });

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_view);
        //侧边
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //点击后关闭
                item.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });

//        // 可以去掉bottom的选项
//        bottomNavigationView.getMenu().removeItem(R.id.navigation_about);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
            if (requestCode == 1) {
                Toast.makeText(this, "拍照返回", Toast.LENGTH_SHORT).show();
                Bundle bundle = data.getExtras();

                bundle.size();

                Bitmap bitmap = (Bitmap) bundle.get("data");


                savePicture(bitmap);
            }
            else if (requestCode == 2) {
                Toast.makeText(this, "相册返回", Toast.LENGTH_SHORT).show();
                Uri uri = data.getData();
                String path = uri.getPath();

                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void savePicture(Bitmap bitmap) {

    }

    public void setRecordTitle(String recordTitle) {
        mRecordTitle = recordTitle;
    }
}
