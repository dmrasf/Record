package com.dmrasf.record.home.item_day.day_detail;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
    private String mRecordTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_detail);

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

        //获取具体每天的信息  图片位置及内容
        Intent intentFromItemDay = getIntent();
        mDayImagePath = intentFromItemDay.getStringExtra("imgPath");
        mText = intentFromItemDay.getStringExtra("text");
        mRecordTitle = intentFromItemDay.getStringExtra("recordTitle");

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
        new MenuInflater(this).inflate(R.menu.day_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initImgText() {
        //根据 itemDay 显示具体每一天的图片
        File path = getExternalFilesDir(mRecordTitle);
        if (path == null) {
            Log.e("==========", "DayDetail 读取文件夹路径错误");
            return;
        }
        String filePath = path.getPath() + File.separator + mDayImagePath;
        File file = new File(filePath);
        Uri picUri = Uri.fromFile(file);

        mImageView = findViewById(R.id.day_detail_image_view);

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picUri.getPath(), opt);
        if (opt.outHeight > 1500) {
            opt.inSampleSize = (int) opt.outHeight / 1500;
        }
        opt.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(picUri.getPath(), opt);
        Toast.makeText(this, picUri.getPath().toString(), Toast.LENGTH_SHORT).show();
        mImageView.setImageBitmap(bitmap);

        // 显示内容
        mTextView = findViewById(R.id.day_detail_text_view);
        mTextView.setText(mText);

        // 其他方法读取图片
//        File dayPic = new File(filePath);
//        Bitmap bitmap = null;
//        try {
//            FileInputStream fs = new FileInputStream(dayPic);
//            bitmap = BitmapFactory.decodeStream(fs);
////            bitmap = BitmapFactory.decodeFile(filePath);
//            fs.close();
//        } catch (IOException e) {
//            Log.e("==========", "DayDetail 读取图片错误！");
//            Toast.makeText(this, "图片文件找不到", Toast.LENGTH_LONG).show();
//        } finally {
//            mImageView = findViewById(R.id.day_detail_image_view);
////            mImageView.setImageBitmap(bitmap);
//            mImageView.setImageURI(picUri);
//        }

//        dialog = new Dialog(this);
//        View view = View.inflate(this, R.layout.activity_image, null);
//        ImageView imageView = view.findViewById(R.id.day_detail_image_view);
//        imageView.setImageResource(dayImageSource);
//        dialog.setContentView(view);

    }

    //解决toolbar返回上一级信息丢失
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            Intent intent = new Intent();
//            intent.putExtra("data", "你好，time");
//            setResult(RESULT_OK, intent);
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
