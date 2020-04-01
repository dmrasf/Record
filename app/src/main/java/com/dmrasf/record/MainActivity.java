package com.dmrasf.record;

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

        Toast.makeText(this, "MainActivity", Toast.LENGTH_SHORT).show();

        // 通过bottom 绑定到不同的Fragment
        NavController navController = Navigation.findNavController(this, R.id.main_fragment);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}
