package com.rizafu.imagecrop.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.rizafu.imagecrop.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by RizaFu on 10/3/14.
 */
public class ResultCropImage extends Activity {
	@InjectView(R.id.imageView)
	ImageView mImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_crop);
		ButterKnife.inject(this);
		Intent intent=getIntent();
		byte[] bytesResult = intent.getByteArrayExtra("result");
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytesResult,0,bytesResult.length);

		mImageView.setImageBitmap(bitmap);
	}
}
