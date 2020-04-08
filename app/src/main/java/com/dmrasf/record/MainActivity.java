package com.dmrasf.record;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import com.dmrasf.record.aboutme.AboutMeFragment;
import com.dmrasf.record.data.DayDbHelper;
import com.dmrasf.record.data.DayProvider;
import com.dmrasf.record.data.RecordAndDayContract;
import com.dmrasf.record.home.ItemRecordFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ItemRecordFragment itemRecordFragment = new ItemRecordFragment();
    private AboutMeFragment aboutMeFragment = new AboutMeFragment();
    private FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

    public String recordTitle = "";

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
            ImageView textView = aboutMeFragment.getActivity().findViewById(R.id.about_text_view);
            if (requestCode == 1) {
                Toast.makeText(this, "拍照返回", Toast.LENGTH_SHORT).show();
                Bundle bundle = data.getExtras();
                // 获取图片
                Bitmap bitmap = (Bitmap) bundle.get("data");

//                textView.setImageResource();
                // 保存
                savePicture(bitmap);
            } else if (requestCode == 2) {
                Toast.makeText(this, "相册返回", Toast.LENGTH_SHORT).show();
                Uri uri = data.getData();
                String path = uri.getPath();

                textView.setImageURI(uri);
                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void savePicture(Bitmap bitmap) {

        if (bitmap == null) {
            Log.e("==========", "从相册或相机读取到的文件为空");
            return;
        }

        // 保存图片到实际路径
        // 获取文件路径 不存在时会创建
        File path = getExternalFilesDir(recordTitle);
        // 新建一个文件名  以日期为名字
        String pictureName = getPictureName();
        String filePath = "";
        filePath = path.getPath() + File.separator + pictureName;
        File newDay = new File(filePath);

        OutputStream os = null;
        try {
            //完整大图
            os = new FileOutputStream(newDay);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            // 缩略图
            addPicToDayDb(bitmap, pictureName);
        } catch (IOException e) {
            Log.e("==========", "保存文件时出错");
        }
    }

    private void addPicToDayDb(Bitmap bitmap, String pictureName) {
        // 添加记录到数据库
        DayProvider dayProvider = new DayProvider(this);
        ContentValues values = new ContentValues();
        values.put(RecordAndDayContract.DayEntry.COLUMN_IMG_PATH, pictureName);
        values.put(RecordAndDayContract.DayEntry.COLUMN_DATE, new Date().getTime());
        // bitmap to byte[]
        ByteArrayOutputStream bitmapByte = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, bitmapByte);
        values.put(RecordAndDayContract.DayEntry.COLUMN_IMG, bitmapByte.toByteArray());
        dayProvider.insert(Uri.withAppendedPath(RecordAndDayContract.BASE_CONTENT_URI, recordTitle), values);
    }

    @SuppressLint("SimpleDateFormat")
    private String getPictureName() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime()) + ".jpg";
    }

    public void setRecordTitle(String title) {
        recordTitle = title;
    }
}
