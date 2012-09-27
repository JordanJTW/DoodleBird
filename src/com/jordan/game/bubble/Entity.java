package com.jordan.game.bubble;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Entity {
	protected RectF mBox;
	protected RectF mBounds;
	protected Bitmap mBitmap = null;
	protected Paint mPaint;
	
	float mDeltaX, mDeltaY, mGravity;
	float mWidth, mHeight; 
	
	long mPreviousTime = 0;
	int mHitCount = 0;
	
	Paint mBackLifeBar, mFrontLifeBar;
	
	public Entity(Bitmap bmp, float x, float y, float width, float height, float scr_width, float scr_height) {
		mWidth = width * (BubbleGame.DPI / 160);
		mHeight = height * (BubbleGame.DPI / 160);
		
		mBox = new RectF(x, y, x+mWidth, y+mHeight);
		
		mPaint = new Paint();
		mBitmap = bmp;
		
		mBackLifeBar = new Paint();
		mBackLifeBar.setColor(Color.RED);
		
		mFrontLifeBar = new Paint();
		mFrontLifeBar.setColor(Color.GREEN);
		
		mBounds = new RectF(0, 0, scr_width, scr_height);
	}
		
	public Entity(int color, float x, float y, float width, float height, float scr_width, float scr_height) {
		mWidth = width;
		mHeight = height;
		
		mBox = new RectF(x, y, x+mWidth, y+mHeight);
		
		mPaint = new Paint();
		mPaint.setColor(color);
		
		mBounds = new RectF(0, 0, scr_width, scr_height);
	}
		
	public RectF getBox() {
		return mBox;
	}
	
	public float getDeltaX() {
		return mDeltaX;
	}
	
	public float getDeltaY() {
		return mDeltaY;
	}
	
	public int getHitCount() {
		return mHitCount;
	}
	
	public void offsetTo(float x, float y) {
		mBox.offsetTo(x, y);
	}
		
	public void setBoundries(float width, float height) {
		mBounds = new RectF(0, 0, width, height);
	}
	
	public void setBoundries(float x, float y, float width, float height) {
		mBounds = new RectF(x, y, x+width, y+height);
	}
	
	public void setDeltaX(float dX) {
		mDeltaX = dX;
	}
	
	public void setDeltaY(float dY) {
		mDeltaY = dY;
	}
	
	public void setGravity(float gravity) {
		mGravity = gravity;
	}
		
	public void setVelocities(float deltaX, float deltaY) {
		mDeltaX = deltaX;
		mDeltaY = deltaY;
	}
	
	public void update(long timestamp) {
		if(mPreviousTime != 0) {
			float delta_time = (timestamp - mPreviousTime) / 1000.0f;
								
			if(mBox.left < mBounds.left) {
				mBox.offset(mBounds.left - mBox.left, 0);
				mDeltaX *= getHorrizontalFriction();
				mHitCount++;
			}	
			
			if(mBox.right > mBounds.right) {
				mBox.offset(mBounds.right - mBox.right, 0);
				mDeltaX *= getHorrizontalFriction();
				mHitCount++;
			}
			
			if(mBox.top < mBounds.top) {
				mBox.offset(0, mBounds.top - mBox.top);
				mDeltaY *= getVerticalFriction();
				mHitCount++;
			}
			
			if(mBox.bottom > mBounds.bottom) {
				mBox.offset(0, mBounds.bottom - mBox.bottom);
				mDeltaY *= getVerticalFriction();
				mHitCount++;
			}
			
			mDeltaY += mGravity * delta_time;
			
			float x_offset = mDeltaX * delta_time;
			float y_offset = mDeltaY * delta_time;
			
			mBox.offset(x_offset, y_offset);				
		}
		
		mPreviousTime = timestamp;		
	}
	
	public float getVerticalFriction() {
		return -(1f/2);
	}
	
	public float getHorrizontalFriction() {
		return -(1f/2);
	}
	
	public void draw(Canvas canvas) {
		if(mBitmap == null) {
			canvas.drawRect(mBox, mPaint);
		} else {
			canvas.drawBitmap(mBitmap, null, mBox, mPaint);
		}
	}
}
