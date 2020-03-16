package com.example.ema.classs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AdapterBitmap {


    public static String adaptToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);

    }

    public static Image adaptToImage(String s) throws IOException, BadElementException {
        byte [] encodeByte=Base64.decode(s,Base64.DEFAULT);
        Bitmap bitmap2= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.PNG, 100 , stream2);
        return Image.getInstance(stream2.toByteArray());
    }
}
