package com.jordan.game.bubble;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.FloatMath;

public class Launcher {
	float mWidth = 240, mHeight = 240;
	
	Bitmap mFrontBmp, mBackBmp;
	RectF mBmpBounds, mBox;
	
	float mStartX, mStartY;
		
	public Launcher(Bitmap front, Bitmap back, float x, float y, float w, float h) {
		mWidth 		= 	w;
		mHeight 	= 	h;
		
		mBox 		=	new RectF(x, y-mHeight, x+mWidth, y);
		
		mFrontBmp	=	front;
		mBackBmp	= 	back;
		mBmpBounds 	= 	new RectF(mWidth - 40, y-mHeight, mWidth + 20, y);
	}
		
	public RectF getBox() {
		return mBox;
	}

	public boolean elasticBarrier(float x, float y) {
//		if(mBox.contains(x, y)) {
			x = mBox.right - x;
			y = mBox.top - y;
			
			return FloatMath.sqrt((x * x) + (y * y)) < 320f;
//		}
		
//		return false;
	}
	
	public float getXVelocity(float x) {
		return mBox.right - x;
	}
	
	public float getYVelocity(float y) {
		return mBox.top - y;
	}
	
	public void drawFront(Canvas canvas) {
		canvas.drawBitmap(mFrontBmp, null, mBmpBounds, null);
	}
	
	public void drawBack(Canvas canvas) {
		canvas.drawBitmap(mBackBmp, null, mBmpBounds, null);
	}
}

