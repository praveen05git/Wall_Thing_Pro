package com.hencesimplified.wallpaperpro;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            =  new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment=null;

            switch (item.getItemId()) {
                case R.id.weekly:
                    fragment=new weekly_fg();
                    break;
                case R.id.unlocked:
                    fragment=new unlocked_fg();
                    break;
                case R.id.locked:
                    fragment=new locked_fg();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);

        SharedPreferences preferences=getApplicationContext().getSharedPreferences("WallProPref",0);
        int page=preferences.getInt("Page",-1);

        if(page==1) {
            loadFragment(new weekly_fg());
            navView.setSelectedItemId(R.id.weekly);
        }
        else if(page==2)
        {
            loadFragment(new unlocked_fg());
            navView.setSelectedItemId(R.id.unlocked);
        }
        else if(page==3)
        {
            loadFragment(new locked_fg());
            navView.setSelectedItemId(R.id.locked);
        }
        else
        {
            loadFragment(new weekly_fg());
            navView.setSelectedItemId(R.id.weekly);
        }

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public boolean checkPermission(String permission)
    {
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        int check= ContextCompat.checkSelfPermission(this,permission);
        return (check== PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }
    private boolean loadFragment(Fragment fragment)
    {
        if(fragment!=null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fg_container,fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed()
    {
        final AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Warning!");
        alertDialog.setMessage("Are you sure you want to exit?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                Intent ExitIntent=new Intent(Intent.ACTION_MAIN);
                ExitIntent.addCategory(Intent.CATEGORY_HOME);
                ExitIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ExitIntent);
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


}
