package com.dmrasf.record.home.item_day.day_detail;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import com.dmrasf.record.R;

import java.io.*;
import java.util.Objects;

public class DayDetailActivity extends AppCompatActivity {

    private ImageView mImageView;
    private TextView mTextView;
    private Dialog dialog;
    private String mDayImagePath;
    private String mText;
    private String mDayTableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_detail);

        initToolbar();

        //获取具体每天的信息  图片位置及内容
        Intent intentFromItemDay = getIntent();
        mDayImagePath = intentFromItemDay.getStringExtra("imgPath");
        mText = intentFromItemDay.getStringExtra("text");
        mDayTableName = intentFromItemDay.getStringExtra("dayTableName");

        initImgText();

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.show();
                Toast.makeText(DayDetailActivity.this, mDayImagePath, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.day_detail_menu, menu);
        return true;
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("data", "你好，time");
//                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initImgText() {
        mImageView = findViewById(R.id.day_detail_image_view);
        BitmapAsyncTask bitmapAsyncTask = new BitmapAsyncTask();
        bitmapAsyncTask.execute();

        // 显示内容
        mTextView = findViewById(R.id.day_detail_text_view);
        mTextView.setText(mText);
    }

    private class BitmapAsyncTask extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            return getBitmap();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mImageView.setImageBitmap(bitmap);
        }
    }

    private Bitmap getBitmap() {
        //根据 itemDay 显示具体每一天的图片
        File path = getExternalFilesDir(mDayTableName);
        if (path == null) {
            Log.e("==========", "DayDetail 读取文件夹路径错误");
            return null;
        }
        String filePath = path.getPath() + File.separator + mDayImagePath;
        File file = new File(filePath);
        Uri picUri = Uri.fromFile(file);

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picUri.getPath(), opt);
        if (opt.outHeight > 1000) {
            opt.inSampleSize = (int) opt.outHeight / 1000;
        }
        opt.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picUri.getPath(), opt);
    }
}
