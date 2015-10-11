package com.rizafu.imagecrop.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by RizaFu on 5/19/15.
 */
public class BitmapUtil{

	public static Bitmap viewToBitmap(View view) {
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);
		return bitmap;
	}

	public static String saveJPG(Context context,String pathNameDir, Bitmap bitmap){
		File file;
//		Tools.createDir(context,pathNameDir);
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			file = new File(Environment.getExternalStorageDirectory()+pathNameDir, System.currentTimeMillis()+".jpg");
		} else {
			file = new File(context.getCacheDir()+pathNameDir, System.currentTimeMillis()+".jpg");
		}
		try {
			FileOutputStream output = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, output);
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "file://"+file.getAbsolutePath();
	}

	public static Bitmap rotateImage(Bitmap bitmap,boolean reverse){
		Matrix matrix = new Matrix();
		matrix.postRotate(reverse ? -90 : 90);
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
		Bitmap rotatedBitmap = Bitmap
				.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
		return rotatedBitmap;
	}

	public static Bitmap flipBitmap(Flip flip, Bitmap bitmap){
		Matrix matrix = new Matrix();
		if (flip== Flip.Horizontal){
			matrix.preScale(-1, 1);
		} else if (flip== Flip.Vertical){
			matrix.preScale(1, -1);
		}
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
		Bitmap mirrorBitmap = Bitmap
				.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, false);
		mirrorBitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);
		return mirrorBitmap;
	}

	public static Bitmap fromDrawable(Context context, int resource){
		return BitmapFactory.decodeResource(context.getResources(), resource);
	}

//	public static void reqBitmap(Context context, String imageUrl,Listener1<Bitmap> response,Listener1<Exception> error){
//		Glide.with(context).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>(){
//			@Override
//			public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation){
//				response.listen(resource);
//			}
//
//			@Override
//			public void onLoadFailed(Exception e, Drawable errorDrawable){
//				super.onLoadFailed(e, errorDrawable);
//				error.listen(e);
//			}
//		});
//	}

	public enum Flip{Horizontal,Vertical}
}