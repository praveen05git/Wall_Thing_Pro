package com.hencesimplified.wallpaperpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class about extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        Intent page_intent=new Intent(about.this,MainActivity.class);
        startActivity(page_intent);
        return true;
    }

    @Override

    public void onBackPressed()
    {
        finish();
        Intent page_intent=new Intent(about.this,MainActivity.class);
        startActivity(page_intent);
    }

}
