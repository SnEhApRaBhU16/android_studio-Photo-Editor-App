package com.example.miniproj;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class page2 extends AppCompatActivity implements View.OnClickListener {
    ImageView im1;
    int x=0;
    Button cropbtn,NextBtn;
    Uri file,uri;
    Bitmap myBitmap;
    String image_uri = "";
    Bitmap showBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);
        im1 = findViewById(R.id.imageView1);
        Intent intnt = getIntent();
        String imagep = intnt.getStringExtra("image");
        file = Uri.parse(imagep);
        uri=Uri.parse(imagep);
        try {
            showBitmap = getBitmapFromUri(file);
            saveBitmapToCache(showBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        im1.setImageURI(file);
        image_uri = file.toString();
        cropbtn = (Button) findViewById(R.id.button1);
        cropbtn.setOnClickListener(this);
        NextBtn=(Button) findViewById(R.id.button2);
        NextBtn.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.equals(cropbtn)) {
            x=1;
            Intent intent = new Intent(this, CropActivity.class);
            startActivity(intent);
        }
        if(v.equals(NextBtn)){
            Intent intent = new Intent(this, Addframe.class);
            intent.putExtra("image", uri.toString());
                startActivity(intent);
        }
    }


    protected void onStart() {
        super.onStart();
        if (!image_uri.equals("")){
            if(x==1)
            im1.setImageBitmap(getBitmapFromCache());
            Bitmap bt=getBitmapFromCache();
           uri=getImageUri(this,bt);
           x=0;
        }
    }


    public Bitmap getBitmapFromCache(){
        File cacheFile = new File(getApplicationContext().getCacheDir(), "final_image.jpg");
        myBitmap = BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
        return myBitmap;
    }

    public void saveBitmapToCache(Bitmap bitmap) throws IOException {
        String filename = "final_image.jpg";
        File cacheFile = new File(getApplicationContext().getCacheDir(), filename);
        OutputStream out = new FileOutputStream(cacheFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, (int)100, out);
        out.flush();
        out.close();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}






