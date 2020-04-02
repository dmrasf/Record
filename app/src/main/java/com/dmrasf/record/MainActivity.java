package com.dmrasf.record;

import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "创建了一个主界面 ----- MainActivity", Toast.LENGTH_SHORT).show();

        Log.e("==========", "onCreate MainActivity");

        // 通过bottom 绑定到不同的Fragment
        NavController navController = Navigation.findNavController(this, R.id.main_fragment);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("==========", "onStop MainActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("==========", "onResume MainActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("==========", "onDestroy MainActivity");
    }
}
