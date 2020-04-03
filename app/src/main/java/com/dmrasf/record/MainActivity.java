package com.dmrasf.record;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "创建了一个主界面 ----- MainActivity", Toast.LENGTH_SHORT).show();

        Log.e("==========", "onCreate MainActivity");

        // 通过bottom 绑定到不同的Fragment
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom);
        NavController navController = Navigation.findNavController(this, R.id.main_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


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
