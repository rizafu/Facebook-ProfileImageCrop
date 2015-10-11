package com.rizafu.imagecrop.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rizafu.imagecrop.R;

/**
 * Created by RizaFu on 5/11/15.
 */
public class Cover extends FrameLayout{

	private Context context;
	private CoverContent coverContent;
	private E_CoverType coverType;
	private Bitmap bitmap,editedBitmap;
	private String text;
	private int color,width,height;
	private Listener listener;

	public enum  E_CoverType{
		Type0(0),Type1 (1),Type2 (2),Type3 (3),Type4 (4),Type5 (5),Type6 (6),Type7 (7),Type8 (8),Type9 (9);
		final int value;
		E_CoverType(int type) {
			value = type;
		}
	}

	public interface Listener{
		void listen(View view);
	}

	public Cover(Context context){
		super(context);
		this.context = context;
	}

	public Cover(Context context, AttributeSet attrs){
		super(context, attrs);
		init(context,attrs);
	}

	public Cover(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
		init(context,attrs);
	}

	private void init(Context context,AttributeSet attrs){
		this.context = context;
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Cover);
		text = array.getString(R.styleable.Cover_text);
		int type = array.getInt(R.styleable.Cover_coverType, 0);
		coverType = E_CoverType.values()[type];
		color = array.getColor(R.styleable.Cover_titleColor, getColorDefault(coverType));
		coverContent = new CoverContent();
	}

	@Override
	protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		if (widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED) {
			throw new IllegalStateException("this view should have at least one specific or max size");
		}
		final int neededSize;
		if (widthMode == MeasureSpec.UNSPECIFIED) {
			neededSize = heightSize;
		}
		else if (heightMode == MeasureSpec.UNSPECIFIED) {
			neededSize = widthSize;
		}
		else {
			neededSize = Math.min(widthSize, heightSize);
		}

		super.onMeasure(MeasureSpec.makeMeasureSpec(neededSize, MeasureSpec.EXACTLY),
		                MeasureSpec.makeMeasureSpec(neededSize, MeasureSpec.EXACTLY));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b){
		width = r - l;
		height = b - t;
		coverContent.setLayout(coverType, width, height);
	}

	public E_CoverType getCurentLayoutType(){
		return coverType;
	}

	protected int getColorDefault(E_CoverType coverType){
		if (coverType == E_CoverType.Type2|| coverType == E_CoverType.Type3|| coverType == E_CoverType.Type4||
		    coverType == E_CoverType.Type7){
			return Color.BLACK;
		} else {
			return Color.WHITE;
		}
	}

	public void setCoverType(E_CoverType coverType){
		this.coverType = coverType;
//	    startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.abc_shrink_fade_out_from_bottom));
		removeAllViewsInLayout();
//		startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.abc_grow_fade_in_from_bottom));
		coverContent = new CoverContent();
		coverContent.setLayout(coverType,width,height);
		if (editedBitmap!=null){
			coverContent.imageView.setImageBitmap(editedBitmap);
		}
		coverContent.textView.setText(text);
		setTitleColorResource(getColorDefault(coverType));
	}

	public void setImageResource(int imageResource){
		coverContent.imageView.setImageResource(imageResource);
	}

	public byte[] getCroppedImageBytes(){
		return coverContent.imageView.getCroppedImageBytes();
	}

	public void setImageUrl(String imageUrl){
//		Glide.with(context).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>(){
//			@Override
//			public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation){
//				coverContent.imageView.setImageBitmap(resource);
//				bitmap = editedBitmap = resource;
//			}
//		});
	}

	public void setImageBitmap(Bitmap bitmap){
		this.bitmap = editedBitmap = bitmap;
		coverContent.imageView.setImageBitmap(bitmap);
	}

	public void rotateImage(){
		editedBitmap = BitmapUtil.rotateImage(editedBitmap,true);
		coverContent.imageView.setImageBitmap(editedBitmap);
	}

	public void flipImage(BitmapUtil.Flip flip){
		editedBitmap = BitmapUtil.flipBitmap(flip,editedBitmap);
		coverContent.imageView.setImageBitmap(editedBitmap);
	}

	public CharSequence getTitle(){
		return coverContent.textView.getText();
	}

	public void setTitle(String title){
		text = title;
		coverContent.textView.setText(text);
	}

	public void setTitleEneble(boolean eneble){
		coverContent.textView.setEnabled(eneble);
	}
	public void setTitle(String title,int color){
		setTitle(title);
		setTitleColorResource(color);
	}

	public void setTitleColorResource(int color){
		this.color= color;
		coverContent.textView.setTextColor(this.color);
//		coverContent.textView.setBackground(coverContent.customDrawable(coverType, this.color));
	}

	public void setTitleFont(Typeface typeface){
		coverContent.textView.setTypeface(typeface);
	}

	public void setOnTitleClick(Listener onTitleClick){
		this.listener = onTitleClick;
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility){
		super.onWindowVisibilityChanged(visibility);
		coverContent.setLayout(coverType,width,height);
	}

	public class CoverContent{

		private int width,height;
		FrameLayout frameView;
		ImageCroppingView imageView;
		TextView textView;
		LayoutParams layoutParams;

		public CoverContent() {
			frameView = new FrameLayout(getContext());
			frameView.setBackgroundColor(0xFFFFFF);
			imageView = new ImageCroppingView(frameView.getContext());
			imageView.setBackgroundColor(0x000000);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			textView = new TextView(frameView.getContext());
			textView.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view){
					listener.listen(view);
				}
			});

			addView(frameView);
			frameView.addView(imageView);
			frameView.addView(textView);

			layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			setLayout(coverType, width, height);
		}

		private void setLayout(E_CoverType coverType,int width, int height) {
			this.width =width;this.height=height;
			frameView.layout(0, 0, width, height);
			frameView.setTag(this);

			int l, t, r = width, b = height;

			switch (coverType){
				default: case Type0: case Type1: case Type4: case Type5: case Type6: case Type8: case Type9:
					l = (int) (width * 0.06);
					t = (int) (height * 0.06);
					r = r-(int) (r * 0.06);
					b = b-(int) (b * 0.06);
					break;
				case Type2:
					l = (int) (width * 0.06);
					t = (int) (height * 0.06);
					r = r-(int) (r * 0.06);
					b = b-(int) (b * 0.28);
					break;
				case Type3:
					l = (int) (width * 0.06);
					t = (int) (height * 0.28);
					r = r-(int) (r * 0.06);
					b = b-(int) (b * 0.06);
					break;
				case Type7:
					l = (int) (width * 0.10);
					t = (int) (height * 0.10);
					r = r-(int) (r * 0.10);
					b = b-(int) (b * 0.30);
					break;
			}

			imageView.layout(l, t, r, b);
			setTextviewLayout(coverType);
		}

		@SuppressLint("NewApi")
		private void setTextviewLayout(E_CoverType coverType){
			int mL, mT, mR, mB,p,tSize;
			String sT=text;

			textView.setGravity(Gravity.CENTER);
			textView.setMaxLines(1);
			switch (coverType){
				case Type1:
					mL = (int) (width * 0.15); mT = (int) (height * 0.28);
					mR = (int) (width * 0.15); mB = (int) (height * 0.5);
					p = (int)(width * 0.05); tSize = (int)(width * 0.04);
					text=text.replace("\n"," ");
					break;
				case Type2:
					mL = (int) (width * 0.06); mT = (int) (height * 0.75);
					mR = (int) (width * 0.06); mB = (int) (height * 0.06);
					p = (int)(width * 0.03); tSize = (int)(width * 0.04);
					textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
					text=text.replace("\n"," ");
					break;
				case Type3:
					mL = (int) (width * 0.15); mT = (int) (height * 0.07);
					mR = (int) (width * 0.15); mB = (int) (height * 0.8);
					p = (int)(width * 0.01); tSize = (int)(width * 0.04);
					text=text.replace("\n"," ");
					break;
				case Type4:
					mL = (int) (width * 0.2); mT = (int) (height * 0.2);
					mR = (int) (width * 0.2); mB = (int) (height * 0.6);
					p = (int)(width * 0.05); tSize = (int)(width * 0.04);
					text=text.replace("\n"," ");
					break;
				case Type5:
					mL = (int) (width * 0.32); mT = (int) (height * 0.32);
					mR = (int) (width * 0.32); mB = (int) (height * 0.32);
					p = (int)(width * 0.03); tSize = (int)(width * 0.04);
					textView.setMaxLines(4);
					break;
				case Type6:
					mL = (int) (width * 0.55); mT = (int) (height * 0.55);
					mR = (int) (width * 0.12); mB = (int) (height * 0.12);
					p = (int)(width * 0.1); tSize = (int)(width * 0.05);
					textView.setMaxLines(4);
					String[] tt=sT.trim().split("\n");
					sT=sT.replace(tt[0],"");
					sT="<u>"+tt[0]+"</u><small>"+sT+"</small>";
					break;
				case Type7:
					mL = (int) (width * 0.10); mT = (int) (height * 0.75);
					mR = (int) (width * 0.10); mB = (int) (height * 0.10);
					p = 0; tSize = (int)(width * 0.04);
					textView.setGravity(Gravity.CENTER_VERTICAL| Gravity.LEFT);
					text=text.replace("\n"," ");
					break;
				case Type8:
					mL = (int) (width * 0.1); mT = (int) (height * 0.22);
					mR = (int) (width * 0.1); mB = (int) (height * 0.5);
					p = (int) (width * 0.05); tSize = (int)(width * 0.04);
					textView.setMaxLines(4);
					break;
				case Type9:
					mL = (int) (width * 0.19); mT = (int) (height * 0.06);
					mR = (int) (width * -0.075); mB = (int) (height * 0.8);
					p = (int)(width * 0.02);tSize = (int)(width * 0.05);
					Animation animation = new RotateAnimation(90f, 90f, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(0);
					textView.setAnimation(animation);
					textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
					textView.setTypeface(Typeface.DEFAULT_BOLD);
					text=text.replace("\n"," ");
					break;
				case Type0:
				default:
					mL = (int) (width * 0.06); mT = (int) (height * 0.06);
					mR = (int) (width * 0.06); mB = (int) (height * 0.06);
					p = (int)(width * 0.05); tSize = (int)(width * 0.05);
					textView.setVisibility(GONE);
					break;
			}
//			AutofitHelper.create(textView).setMaxTextSize(tSize);
			layoutParams.setMargins(mL, mT, mR, mB);
			textView.setLayoutParams(layoutParams);
			textView.setPadding(p, p, p, p);
			if (coverType != E_CoverType.Type6){
				textView.setText(text);
			} else {
				textView.setText(Html.fromHtml(sT));
			}
			textView.setTextColor(color);
			textView.setBackground(customDrawable(coverType, color));
			textView.layout(textView.getLeft(), textView.getTop(), textView.getRight(), textView.getBottom());
		}

		private LayerDrawable customDrawable(E_CoverType coverType,int color){
			int w;
			Drawable[] dArr;
			ShapeDrawable sh1,sh2,sh3;
			switch (coverType){
				case Type1:
					w = (int)(width * 0.02);
					sh1 = new ShapeDrawable(new RectShape());
					sh1.getPaint().setColor(color);
					sh1.getPaint().setStyle(Paint.Style.STROKE);
					sh1.getPaint().setStrokeWidth(w);

					dArr = new Drawable[] { sh1 };
					break;
				case Type2:case Type7:

					w = (int)(width * 0.01);
					sh1 = new ShapeDrawable(new RectShape());
					sh1.getPaint().setColor(color);
					sh1.getPaint().setStyle(Paint.Style.FILL);

					sh2 = new ShapeDrawable(new RectShape());
					sh2.getPaint().setColor(Color.WHITE);
					sh2.getPaint().setStyle(Paint.Style.FILL);

					sh1.setPadding(0, w, 0, w);

					dArr = new Drawable[] {sh1,sh2};
					break;
				case Type3:
					w = (int)(width * 0.01);
					sh1 = new ShapeDrawable(new RectShape());
					sh1.getPaint().setColor(color);
					sh1.getPaint().setStyle(Paint.Style.FILL);

					sh2 = new ShapeDrawable(new RectShape());
					sh2.getPaint().setColor(Color.WHITE);
					sh2.getPaint().setStyle(Paint.Style.FILL);

					sh1.setPadding(0, 0, 0, w);

					dArr = new Drawable[] { sh1,sh2 };
					break;
				case Type4:
					w = (int)(width * 0.01);
					sh1 = new ShapeDrawable(new RectShape());
					sh1.getPaint().setColor(Color.WHITE);
					sh1.getPaint().setStyle(Paint.Style.FILL);

					sh2 = new ShapeDrawable(new RectShape());
					sh2.getPaint().setColor(color);
					sh2.getPaint().setStyle(Paint.Style.FILL);

					sh3 = new ShapeDrawable(new RectShape());
					sh3.getPaint().setColor(Color.WHITE);
					sh3.getPaint().setStyle(Paint.Style.FILL);

					sh1.setPadding(w, w, w, w);
					sh2.setPadding(w, w, w, w);

					dArr = new Drawable[] {sh1,sh2,sh3};
					break;
				case Type5:
					sh1 = new ShapeDrawable(new OvalShape());
					sh1.getPaint().setColor(0xFFFFFF);
					dArr = new Drawable[] { sh1 };
					break;
				case Type6:
					w = (int)(width * 0.01);
					sh1 = new ShapeDrawable(new OvalShape());
					sh1.getPaint().setColor(Color.TRANSPARENT);
					sh1.getPaint().setStyle(Paint.Style.FILL);
					sh1.getPaint().setStrokeWidth(w);

					sh2 = new ShapeDrawable(new OvalShape());
					sh2.getPaint().setColor(color);
					sh2.getPaint().setStyle(Paint.Style.STROKE);
					sh2.getPaint().setStrokeWidth(w);

					sh1.setPadding(w, w, w, w);

					dArr = new Drawable[] { sh1,sh2 };
					break;
				default:case Type8:case Type9:
					sh1 = new ShapeDrawable(new RectShape());
					sh1.getPaint().setColor(Color.TRANSPARENT);
					dArr = new Drawable[] { sh1 };
					break;
			}

			return new LayerDrawable(dArr);
		}
	}
}