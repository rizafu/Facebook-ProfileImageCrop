package com.rizafu.imagecrop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rizafu.imagecrop.R;
import com.rizafu.imagecrop.customview.ImageCroppingView;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MyActivity extends Activity {

	@InjectView(R.id.imageCroppingView)
	ImageCroppingView mImageCroppingView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		ButterKnife.inject(this);
		/**
		 * set image from drawable
		 */
		mImageCroppingView.setImageResource(R.drawable.oro_oro_ombo);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_crop) {
			/**
			 * get croped image from image crop view.
			 * the result is byte format, and send to ResultCropImage activity for display the image croped.
			 */
			byte[] bytesResultImage = mImageCroppingView.getCroppedImageBytes();
			Intent intent=new Intent(this,ResultCropImage.class);
			intent.putExtra("result",mImageCroppingView.getCroppedImageBytes());
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
