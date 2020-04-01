package com.dmrasf.record;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class DayDetailActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Dialog dialog;
    private int dayImageSource;

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

        //获取具体每天的信息
        Intent intentFromItemDay = getIntent();
        dayImageSource = intentFromItemDay.getIntExtra("transport", 0);

        init();

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.show();
                Toast.makeText(DayDetailActivity.this, String.valueOf(dayImageSource), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        //根 itemDay 显示具体每一天的图片
        mImageView = (ImageView) findViewById(R.id.day_detail_image_view);
        mImageView.setImageResource(dayImageSource);
//
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
