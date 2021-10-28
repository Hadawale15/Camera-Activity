package com.example.camera_activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button camera,wallpaper;
    ImageView image;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        camera=findViewById(R.id.camera_id);
        wallpaper=findViewById(R.id.wallpaper_id);
        image=findViewById(R.id.image_id);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkPermission= ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
                if (checkPermission==RESULT_OK)
                {
                    OpenCameraMethod();
                }
                else
                {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},1);

                }
            }


        });

        wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getApplicationContext().setWallpaper(bitmap);
                    Toast.makeText(MainActivity.this, "Wallpaper Set", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Thread thread = new Thread() {

                    @Override
                    public void run() {
                        super.run();

                        try {
                            sleep(20000);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        finally {
                            Intent intent=new Intent(MainActivity.this,MainActivity.class);
                            startActivity(intent);
                        }

                    }

                };
                thread.start();

            }
        });
    }
    private void OpenCameraMethod() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 1:
            if (grantResults.length>=0 &&grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                OpenCameraMethod();
            }
            else {
                Toast.makeText(MainActivity.this, "You Don't have Permission", Toast.LENGTH_SHORT).show();
            }
            break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            Bundle bundle=data.getExtras();
            bitmap=(Bitmap) bundle.get("data");
            image.setImageBitmap(bitmap);
        }
    }
}