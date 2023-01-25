package com.example.miniproj;
import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button gallery, takeimg;
    Bitmap showBitmap;
    int x=0;
    private static final int PERMISSION_CODE=1000;
    private static final int IMAGE_CAPTURE=1001;
    Uri image_uri;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gallery = (Button) findViewById(R.id.button1);
        takeimg = (Button) findViewById(R.id.button2);
        gallery.setOnClickListener(this);
        takeimg.setOnClickListener(this);
    }
    void imageChooser() {
        int select = 200;
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), select);
    }

    public void onClick(View v) {
        if (v.equals(gallery)) {
           imageChooser();
        }
       if(v.equals(takeimg)){
           x=1;
           if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
               if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED||
               checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
    String[] permission={Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    requestPermissions(permission,PERMISSION_CODE);
               }
               else{
                  openCamera();
               }
           }
       }
    }

    private void openCamera(){
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the camera");
        image_uri= getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent camint=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camint.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(camint,IMAGE_CAPTURE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (x == 1) {
            if (resultCode == RESULT_OK) {
                if (null != image_uri) {
                    x=0;
                    Intent it = new Intent(this, page2.class);
                    it.putExtra("image", image_uri.toString());
                    startActivity(it);
                }
            }
        }
        if (x == 0) {
            if (resultCode == RESULT_OK) {
                if (requestCode == 200) {
                    // Get the url of the image from data
                    Uri selectedImageUri = data.getData();
                    try {
                        showBitmap = getBitmapFromUri(selectedImageUri);
                        saveBitmapToCache(showBitmap);
                    } catch (IOException e){
                        Log.e("tag", e.toString());
                    }
                    if (null != selectedImageUri) {
                        Intent it = new Intent(this, page2.class);
                        it.putExtra("image", selectedImageUri.toString());
                        startActivity(it);
                    }
                }
            }
        }
    }
public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
        case PERMISSION_CODE: {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public void saveBitmapToCache(Bitmap bitmap) throws IOException {
        String filename = "final_image.jpg";
        File cacheFile = new File(getApplicationContext().getCacheDir(), filename);
        OutputStream out = new FileOutputStream(cacheFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, (int)100, out);
        out.flush();
        out.close();
    }
}
