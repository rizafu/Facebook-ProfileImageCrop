package com.rizafu.imagecrop.ImageCrop;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.Scroller;

/**
 * Created by RizaFu on 8/22/15.
 */
public class ImageCrop extends ImageView{

	private static final String DEBUG = "DEBUG";

	//
	// SuperMin and SuperMax multipliers. Determine how much the image can be
	// zoomed below or above the zoom boundaries, before animating back to the
	// min/max zoom boundary.
	//
	private static final float SUPER_MIN_MULTIPLIER = .75f;
	private static final float SUPER_MAX_MULTIPLIER = 1.25f;

	//
	// Scale of image ranges from minScale to maxScale, where minScale == 1
	// when the image is stretched to fit view.
	//
	private float normalizedScale;

	//
	// Matrix applied to image. MSCALE_X and MSCALE_Y should always be equal.
	// MTRANS_X and MTRANS_Y are the other values used. prevMatrix is the matrix
	// saved prior to the screen rotating.
	//
	private Matrix matrix, prevMatrix;

	public static enum State { NONE, DRAG, ZOOM, FLING, ANIMATE_ZOOM };
	private State state;

	private float minScale;
	private float maxScale;
	private float superMinScale;
	private float superMaxScale;
	private float[] m;

	private Context context;
	private Fling fling;

	//
	// Size of view and previous view size (ie before rotation)
	//
	private int viewWidth, viewHeight, prevViewWidth, prevViewHeight;

	//
	// Size of image when it is stretched to fit view. Before and After rotation.
	//
	private float matchViewWidth, matchViewHeight, prevMatchViewWidth, prevMatchViewHeight;

	//
	// After setting image, a value of true means the new image should maintain
	// the zoom of the previous image. False means it should be resized within the view.
	//
	private boolean maintainZoomAfterSetImage;

	//
	// True when maintainZoomAfterSetImage has been set to true and setImage has been called.
	//
	private boolean setImageCalledRecenterImage;

	private ScaleGestureDetector mScaleDetector;
	private GestureDetector mGestureDetector;

	private float w;

	private float x0;

	private float y0;

	public ImageCrop(Context context){
		super(context);
	}

	public ImageCrop(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public ImageCrop(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}

	public void setState(State state){
		this.state = state;
	}

	public int getViewHeight(){
		return viewHeight;
	}

	/**
	 * Fling launches sequential runnables which apply
	 * the fling graphic to the image. The values for the translation
	 * are interpolated by the Scroller.
	 * @author Ortiz
	 *
	 */
	private class Fling implements Runnable {

		Scroller scroller;
		int currX, currY;

		Fling(int velocityX, int velocityY) {
			setState(State.FLING);
			scroller = new Scroller(context);
			matrix.getValues(m);

			int startX = (int) m[Matrix.MTRANS_X];
			int startY = (int) m[Matrix.MTRANS_Y];
			int minX, maxX, minY, maxY;

			if (getImageWidth() > viewWidth) {
				minX = viewWidth - (int) getImageWidth();
				maxX = 0;

			} else {
				minX = maxX = startX;
			}

			if (getImageHeight() > viewHeight) {
				minY = viewHeight - (int) getImageHeight();
				maxY = 0;

			} else {
				minY = maxY = startY;
			}

			scroller.fling(startX, startY, (int) velocityX, (int) velocityY, minX,
			               maxX, minY, maxY);
			currX = startX;
			currY = startY;
		}

		public void cancelFling() {
			if (scroller != null) {
				setState(State.NONE);
				scroller.forceFinished(true);
			}
		}

		@Override
		public void run() {
			if (scroller.isFinished()) {
				scroller = null;
				return;
			}

			if (scroller.computeScrollOffset()) {
				int newX = scroller.getCurrX();
				int newY = scroller.getCurrY();
				int transX = newX - currX;
				int transY = newY - currY;
				currX = newX;
				currY = newY;
				matrix.postTranslate(transX, transY);
				fixTrans();
				setImageMatrix(matrix);
				compatPostOnAnimation(this);
			}
		}
	}
}
