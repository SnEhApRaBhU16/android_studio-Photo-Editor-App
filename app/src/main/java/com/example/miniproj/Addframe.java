package com.example.miniproj;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class Addframe extends AppCompatActivity implements AddFrameListener,AddStickerListener {

    PhotoEditorView phtoview;
    PhotoEditor phtoeditor;
   private Bitmap bitmap;
   private int width=0;
   private int height=0;
   private int[] pixels;
   private int pixelCount=0;
   LinearLayout linearLayout;
    String image_uri = "";
    static{
        System.loadLibrary("photoEditor");
    }
    private static native void red(int[] pixels,int width,int height);
    private static native void green(int[] pixels,int width,int height);
    private static native void blue(int[] pixels,int width,int height);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addframe);
        linearLayout=findViewById(R.id.linear);

        phtoview=findViewById(R.id.photoview1);
        Intent intnt = getIntent();
            String imagep = intnt.getStringExtra("image");
            Uri file = Uri.parse(imagep);
       phtoview.getSource().setImageURI(file);
        phtoeditor=new PhotoEditor.Builder(this,phtoview).setPinchTextScalable(true).build();

        bitmap=null;
        final   BitmapFactory.Options bmpOptions=new BitmapFactory.Options();
        InputStream input=null;
        bmpOptions.inBitmap=bitmap;
        try{
            input=getContentResolver().openInputStream(file);
        }catch(FileNotFoundException e){
            e.printStackTrace();
            recreate();
            return;
        }
        bitmap=BitmapFactory.decodeStream(input,null,bmpOptions);
        width=bitmap.getWidth();
        height=bitmap.getHeight();
        bitmap=bitmap.copy(Bitmap.Config.ARGB_8888,true);
        pixelCount=width*height;
        pixels=new int[pixelCount];
        bitmap.getPixels(pixels,0,width,0,0,width,height);

        Button addFrame=findViewById(R.id.AddFrames);
        Button addSticker=findViewById(R.id.AddSticker);
        Button addFilterR=findViewById(R.id.AddFilterRed);
        Button addFilterG=findViewById(R.id.AddFilterGreen);
        Button addFilterB=findViewById(R.id.AddFilterBlue);
        Button save=findViewById(R.id.SaveImage);
        Button rotate=findViewById(R.id.RotateImg);


        addFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameFragment framefragment=frameFragment.getInstance();
                framefragment.setListener(Addframe.this::onAddFrame);
                framefragment.show(getSupportFragmentManager(),framefragment.getTag());
            }
        });
        addSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StickerFragment stickerfragment=StickerFragment.getInstance();
                stickerfragment.setListener(Addframe.this::onAddSticker);
                stickerfragment.show(getSupportFragmentManager(),stickerfragment.getTag());
            }
        });
        addFilterR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new Thread(){
                    public void run(){
                        red(pixels,width,height);
                        bitmap.setPixels(pixels,0,width,0,0,width,height);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                phtoview.getSource().setImageBitmap(bitmap);
                            }
                        });
                    }
                }.start();
            }
        });
        addFilterG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run(){
                        green(pixels,width,height);
                        bitmap.setPixels(pixels,0,width,0,0,width,height);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                phtoview.getSource().setImageBitmap(bitmap);
                            }
                        });
                    }
                }.start();
            }
        });
        addFilterB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run(){
                        blue(pixels,width,height);
                        bitmap.setPixels(pixels,0,width,0,0,width,height);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                phtoview.getSource().setImageBitmap(bitmap);
                            }
                        });
                    }
                }.start();
            }
        });

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bitmap showBitmap = getBitmapFromCache();
                    Bitmap rotatedImage = rotateBitmap(showBitmap);
                    saveBitmapToCache(rotatedImage);
                    phtoview.getSource().setImageBitmap(getBitmapFromCache());
                } catch (IOException e){
                    Log.e("tag", e.toString());
                }


            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            saveImage();

            }
        });
}


private void saveImage() {
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if(multiplePermissionsReport.areAllPermissionsGranted()){
                    phtoeditor.saveAsBitmap(new OnSaveBitmap() {
                        @Override
                        public void onBitmapReady(Bitmap saveBitmap) {
                            phtoview.getSource().setImageBitmap(saveBitmap);
                            try {
                                final String path=BitmapClass.insertImage(getContentResolver(),saveBitmap,
                                        System.currentTimeMillis() +"_profile.jpg",null);
                                if(!TextUtils.isEmpty(path)){

                                    Snackbar snackbar= Snackbar.make(linearLayout,"Image saved successfully"
                                            ,Snackbar.LENGTH_LONG).setAction("OPEN", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openImageLocation(path);
                                        }
                                    });
                                 snackbar.show();
                                }else{
                                    Snackbar snackbar= Snackbar.make(linearLayout,"Unable to save image",Snackbar.LENGTH_LONG);
                                    snackbar.show();

                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    });
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                 permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    private void openImageLocation(String path) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path),"image/*");
        startActivity(intent);
    }

    public void onAddFrame(int frame){
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),frame);
        phtoeditor.addImage(bitmap);
    }
    public void onAddSticker(int Sticker){
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),Sticker);
        phtoeditor.addImage(bitmap);
    }
    public void saveBitmapToCache(Bitmap bitmap) throws IOException {
        String filename = "final_image.jpg";
        File cacheFile = new File(getApplicationContext().getCacheDir(), filename);
        OutputStream out = new FileOutputStream(cacheFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, (int)100, out);
        out.flush();
        out.close();
    }

    public Bitmap getBitmapFromCache(){
        File cacheFile = new File(getApplicationContext().getCacheDir(), "final_image.jpg");
        Bitmap myBitmap = BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
        return myBitmap;
    }

    public Bitmap rotateBitmap(Bitmap bitmap){
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.postScale((float)1, (float)1);
        matrix.postRotate(90);

        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return bitmap2;
    }

}