package com.hencesimplified.wallpaperpro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class photo_view extends AppCompatActivity {

    private ImageView sample_img;
    private String url_img;
    private Button btn_download,btn_setwall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        btn_download=findViewById(R.id.btn_download);
        btn_setwall=findViewById(R.id.btn_setwall);
        sample_img=findViewById(R.id.imageView);
        Intent intent=getIntent();
        url_img=intent.getStringExtra("img_url");
        Load_img(url_img);

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(url_img)
                        .into(getTarget(url_img));
            }
        });

        btn_setwall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alert_dia1=new androidx.appcompat.app.AlertDialog.Builder(photo_view.this).create();
                alert_dia1.setTitle("Set as Wallpaper?");
                alert_dia1.setMessage("Are you sure to set this picture as wallpaper?");

                alert_dia1.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Picasso.get().load(url_img)
                                .into(setWall(url_img));

                    }
                });
                alert_dia1.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert_dia1.show();

            }
        });
    }

    private void Load_img(String url)
    {
        Picasso.get().load(url).into(sample_img);
    }

    private Target getTarget(final String url)
    {
        Target target=new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String state= Environment.getExternalStorageState();
                        if(Environment.MEDIA_MOUNTED.equals(state) && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            File sdCard = Environment.getExternalStorageDirectory();
                            String folder_main = "Hs_Wallpaper";

                            File f = new File(Environment.getExternalStorageDirectory()+"/"+folder_main);

                            if (!f.exists()) {
                                f.mkdirs();
                            }

                            try {

                                String uniqueID = UUID.randomUUID().toString();
                                File file=new File(f.getPath()+"/"+uniqueID+".jpg");
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                                ostream.flush();
                                ostream.close();
                                Toast.makeText(getApplicationContext(),"Downloaded",Toast.LENGTH_SHORT).show();

                            } catch (IOException e) {
                                Log.e("IO", e.getLocalizedMessage());
                            }

                        }


                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }

    private Target setWall(final String setUrl)
    {
        Target setwallTarget=new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                WallpaperManager manager=WallpaperManager.getInstance(getApplicationContext());
                try{
                    manager.setBitmap(bitmap);
                    Toast.makeText(getApplicationContext(),"Wallpaper applied",Toast.LENGTH_SHORT).show();
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Error Occurred",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return setwallTarget;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public boolean checkPermission(String permission)
    {
        ActivityCompat.requestPermissions(photo_view.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        int check= ContextCompat.checkSelfPermission(this,permission);
        return (check== PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

}
