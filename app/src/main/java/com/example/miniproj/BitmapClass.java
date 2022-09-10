package com.example.miniproj;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.content.ContentUris;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class BitmapClass {
    public static String insertImage(ContentResolver contentResolver, Bitmap source, String title, String description) throws IOException {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, title);
        values.put(MediaStore.Images.Media.MIME_TYPE, title);
        values.put(MediaStore.Images.Media.DATE_ADDED, title);
        values.put(MediaStore.Images.Media.DATE_TAKEN, title);
        Uri uri = null;
        String stringurl = null;
        try {
            uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (source != null) {
                OutputStream outputStream = contentResolver.openOutputStream(uri);
                source.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

                long id = ContentUris.parseId(uri);
                Bitmap mini = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                storeThumbnail(contentResolver, mini, id, 50f, 50f, MediaStore.Images.Thumbnails.MICRO_KIND);
            } else {
                contentResolver.delete(uri, null, null);
                uri = null;
            }
        } catch (FileNotFoundException e) {
            if (uri != null) {
                contentResolver.delete(uri, null, null);
                uri = null;
            }
            e.printStackTrace();
        }
        if(uri!=null){
            stringurl=uri.toString();
        }
        return stringurl;
    }
    private static final Bitmap storeThumbnail(ContentResolver contentResolver,Bitmap source,long id,float width,float height,int microKind){
        Matrix matrix=new Matrix();
        float X=width/source.getWidth();
        float Y=height/source.getHeight();
        matrix.setScale(X,Y);
        Bitmap bitmap=Bitmap.createBitmap(source,0,0,source.getWidth(),source.getHeight(),matrix,true);
        ContentValues contentValues=new ContentValues(4);
        contentValues.put(MediaStore.Images.Thumbnails.KIND,microKind);
        contentValues.put(MediaStore.Images.Thumbnails.IMAGE_ID, id);
        contentValues.put(MediaStore.Images.Thumbnails.HEIGHT, height);
        contentValues.put(MediaStore.Images.Media.WIDTH, width);
       Uri uri=contentResolver.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,contentValues);
        try {
            OutputStream outputStream=contentResolver.openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.close();
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}