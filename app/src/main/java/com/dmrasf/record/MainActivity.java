package com.dmrasf.record;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.dmrasf.record.aboutme.AboutMeFragment;
import com.dmrasf.record.data.DayProvider;
import com.dmrasf.record.data.RecordAndDayContract;
import com.dmrasf.record.home.ItemRecordFragment;
import com.dmrasf.record.home.TextDialog;
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
    private MainActivity mainActivity = this;
    private long mPictureName;

    public String recordTitle = "";
    public String DayTableName = "";

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
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            // 可能需要放到后台
            if (requestCode == 1) {
                // 获取图片
                File path = getExternalFilesDir(null);
                String tempPath = path.getPath() + File.separator + "temp.jpg";
                File tempFile = new File(tempPath);
                uri = FileProvider.getUriForFile(this, "com.dmrasf.record.fileProvider", tempFile);
            } else if (requestCode == 2) {
                uri = data.getData();
            }
            SaveBitmapAsyncTask saveBitmapAsyncTask = new SaveBitmapAsyncTask();
            saveBitmapAsyncTask.execute(uri);

        }
    }

    private class SaveBitmapAsyncTask extends AsyncTask<Uri, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Uri... uris) {
            savePicture(uris[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            if (b) {
                Toast.makeText(mainActivity, "成功保存", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void savePicture(Uri uri) {
        Bitmap bitmap;

        ContentResolver cr = getContentResolver();
        try {
            bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
        } catch (FileNotFoundException e) {
            Log.e("Exception", e.getMessage(), e);
            return;
        }

        if (bitmap == null) {
            Log.e("==========", "从相册或相机读取到的文件为空");
            return;
        }
        // 保存图片到实际路径
        // 获取文件路径 不存在时会创建
        File path = getExternalFilesDir(DayTableName);
        // 新建一个文件名  以日期为名字
        mPictureName = new Date().getTime();
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
            // 获取bitmap 缩小保存到 数据库中
            addPicToDayDb(Uri.fromFile(newDay));
        } catch (IOException e) {
            Log.e("==========", "保存文件时出错");
        }

        bitmap.recycle();
    }

    private void addPicToDayDb(Uri uri) {
        if (uri == null) {
            return;
        }

        // 添加记录到数据库
        DayProvider dayProvider = new DayProvider(this);
        ContentValues values = new ContentValues();
        values.put(RecordAndDayContract.DayEntry.COLUMN_IMG_PATH, getPictureName());
        values.put(RecordAndDayContract.DayEntry.COLUMN_DATE, mPictureName);
        // 缩小
        Bitmap bitmap = resizeBitmap(uri);
        // bitmap to byte[]
        ByteArrayOutputStream bitmapByte = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 1, bitmapByte);
        values.put(RecordAndDayContract.DayEntry.COLUMN_IMG, bitmapByte.toByteArray());
        dayProvider.insert(Uri.withAppendedPath(RecordAndDayContract.BASE_CONTENT_URI, DayTableName), values);
    }

    private Bitmap resizeBitmap(Uri uri) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri.getPath(), opt);
        if (opt.outHeight > 60){
            opt.inSampleSize = (int) opt.outHeight / 100;
        }
        opt.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(uri.getPath(), opt);
    }

    @SuppressLint("SimpleDateFormat")
    private String getPictureName() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mPictureName) + ".jpg";
    }

    public void setRecordTitle(String title, long createTime) {
        recordTitle = title;
        DayTableName = "_" + String.valueOf(createTime);
    }
}
