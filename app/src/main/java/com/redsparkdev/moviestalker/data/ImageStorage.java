package com.redsparkdev.moviestalker.data;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Red on 12/06/2017.
 */

public class ImageStorage {

    private static final String TAG = ImageStorage.class.getSimpleName();
    private static final String imageFolder = ".thumbnail";
    private static final String STORAGE_DIR = Environment.getExternalStorageDirectory().toString();


    private static String getDir(){
        File storageDir = new File(STORAGE_DIR, imageFolder);
        if(!storageDir.exists()){
            if(!storageDir.mkdir()){
                Log.d(TAG, ":Failed to Created image folder");
                return null;
            }
        }
        return storageDir.toString();
    }
    private static  Bitmap convertToBitmap(Drawable image){
        Bitmap bitmap = null;

        if (image instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) image;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(image.getIntrinsicWidth() <= 0 || image.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap
            // will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(image.getIntrinsicWidth(), image.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        image.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        image.draw(canvas);
        return bitmap;

    }

    public static void saveImage(Drawable image, String name){
       Bitmap bitImage  = convertToBitmap(image);
        if(bitImage == null){
            Log.d(TAG, ":Failed to convert image");
        }
        File file = new File(getDir(), name+".png");
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static File getImageLocation(int id){
        File file = new File(getDir(), id+".png");
        if(file.exists()){
            DebugPring(file.toString());
            return file;
        }
        Log.v(TAG, ":File not found");
        return null;
    }
    public static void deleteImage(String movieID) {
        File file = new File(getDir(), movieID+".png");
        if(file.exists()){
            file.delete();
        }
    }
    private static void DebugPring(String s){
        Log.v(TAG, s);
    }


}
