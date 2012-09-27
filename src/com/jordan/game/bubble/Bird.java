package com.jordan.game.bubble;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;

public class Bird extends Entity {
	final static float WIDTH = 65, HEIGHT = 65;
	
	Bitmap mDeadBirdBmp;
	
	int mImgCount = 0;
	int mImgWidth, mImgHeight;
	
	Matrix mMatrix = new Matrix();
	
	boolean mKilled;
		
	public Bird(Bitmap bird, Bitmap dead, float x, float y, float scr_width, float scr_height) {
		super(bird, x, y, WIDTH, HEIGHT, scr_width, scr_height);
				
		mMatrix.setScale(1, -1);
		mDeadBirdBmp = Bitmap.createBitmap(dead, 0, 0, dead.getWidth(), dead.getHeight(), mMatrix, true);
		
		mImgWidth = bird.getWidth() / 3;
		mImgHeight = bird.getHeight();
		
		mMatrix.setScale(-1, 1);
	}
	
	public void killed() {
		if(!mKilled) {
			mDeltaX = mDeltaY = 0;
			mMatrix.setScale(1, -1);
		    mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), mMatrix, true);
		    mKilled = true;
		}
	}
	
	public void update(long timestamp) {
		super.update(timestamp);
		
		mImgCount = ((mImgCount+1)%30);
		
		if(mBox.left < mBounds.left || mBox.right > mBounds.right) {
			mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
					mBitmap.getHeight(), mMatrix, true);
			mDeadBirdBmp = Bitmap.createBitmap(mDeadBirdBmp, 0, 0, mDeadBirdBmp.getWidth(),
					mDeadBirdBmp.getHeight(), mMatrix, true);
		}

	}
		
	public void draw(Canvas canvas) {	
		int i = mImgCount / 10;
		
		Rect srcBox = new Rect(i*mImgWidth, 0, i*mImgWidth + mImgWidth, mImgHeight);
		
		if(mHitCount <= 4)
			canvas.drawBitmap(mBitmap, srcBox, mBox, mPaint);
		else
			canvas.drawBitmap(mDeadBirdBmp, null, mBox, mPaint);
		
		canvas.drawRect(new RectF(mBox.left, mBox.bottom - 5, mBox.right, mBox.bottom), mBackLifeBar);
		canvas.drawRect(new RectF(mBox.left, mBox.bottom - 5, mBox.right -  mBox.width() * (mHitCount / 5f), mBox.bottom), mFrontLifeBar);
	}
}