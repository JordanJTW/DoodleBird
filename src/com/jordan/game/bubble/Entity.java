package com.jordan.game.bubble;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Pair;

public class Entity {
	protected Bitmap mBitmap = null;
	protected Paint mPaint;
	
	protected RectF mBox;
	protected RectF mBounds;
	
	protected float mDeltaX, mDeltaY, mGravity;
	
	protected long mPreviousTime = 0;
	protected int mHitCount = 0;
	
	protected Paint mBackLifeBar, mFrontLifeBar;
	
	public Entity(Bitmap bmp, float x, float y, float width, float height, float scr_width, float scr_height) {
		initialize(x, y, width, height, scr_width, scr_height);
		
		mBitmap = bmp;
	}

	public Entity(int color, float x, float y, float width, float height, float scr_width, float scr_height) {
		initialize(x, y, width, height, scr_width, scr_height);
		
		mPaint.setColor(color);
	}
	
	private void initialize(float x, float y, float width, float height, float scr_width, float scr_height) {
		
		//Modify dimensions based on DPI of screen
		float mWidth = width * (BubbleGame.DPI / 160);
		float mHeight = height * (BubbleGame.DPI / 160);
		
		mBox = new RectF(x, y, x+mWidth, y+mHeight);
		mBounds = new RectF(0, 0, scr_width, scr_height);
		
		mPaint = new Paint();
		
		mBackLifeBar = new Paint();
		mBackLifeBar.setColor(Color.RED);
		
		mFrontLifeBar = new Paint();
		mFrontLifeBar.setColor(Color.GREEN);
	}
		
	public RectF getBox() {
		return mBox;
	}
	
	public Pair<Float, Float> getDelta() {
		return new Pair<Float, Float>(mDeltaX, mDeltaY);
	}
	
	public int getHitCount() {
		return mHitCount;
	}
	
	public float getHorrizontalFriction() {
		return -(1f/2);
	}
	
	public float getVerticalFriction() {
		return -(1f/2);
	}
	
	//Move bounding box (and Entity) to (x, y)
	public void offsetTo(float x, float y) {
		mBox.offsetTo(x, y);
	}
	
	//Modify bounds of Entity's "playing field"
	public void setBoundries(float width, float height) {
		mBounds = new RectF(0, 0, width, height);
	}
	
	public void setBoundries(float x, float y, float width, float height) {
		mBounds = new RectF(x, y, x+width, y+height);
	}
	
	public void setGravity(float gravity) {
		mGravity = gravity;
	}
	
	public void setVelocity(float dX, float dY) {
		if(dX != 0)
			mDeltaX = dX;
		
		if(dY != 0)
			mDeltaY = dY;
	}
	
	public void update(long timestamp) {
		if(timestamp - mPreviousTime > 1000)
			mPreviousTime = 0;
		
		if(mPreviousTime != 0) {
			
			//Find change in time since last update
			float delta_time = (timestamp - mPreviousTime) / 1000.0f;
								
			/*
			 * If the Entity "hits" the left side, keep it in play 
			 * and reverse direction with friction applied, increasing
			 * the hit count
			 */
			if(mBox.left < mBounds.left) {
				mBox.offset(mBounds.left - mBox.left, 0);
				mDeltaX *= getHorrizontalFriction();
				mHitCount++;
			}	
			
			/*
			 * If the Entity "hits" the right side, keep it in play 
			 * and reverse direction with friction applied, increasing
			 * the hit count
			 */
			if(mBox.right > mBounds.right) {
				mBox.offset(mBounds.right - mBox.right, 0);
				mDeltaX *= getHorrizontalFriction();
				mHitCount++;
			}
			
			/*
			 * If the Entity "hits" the top, keep it in play 
			 * and reverse direction with friction applied, increasing
			 * the hit count
			 */
			if(mBox.top < mBounds.top) {
				mBox.offset(0, mBounds.top - mBox.top);
				mDeltaY *= getVerticalFriction();
				mHitCount++;
			}
			
			/*
			 * If the Entity "hits" the bottom, keep it in play 
			 * and reverse direction with friction applied, increasing
			 * the hit count
			 */
			if(mBox.bottom > mBounds.bottom) {
				mBox.offset(0, mBounds.bottom - mBox.bottom);
				mDeltaY *= getVerticalFriction();
				mHitCount++;
			}
			
			//Apply gravity to the vertical velocity
			mDeltaY += mGravity * delta_time;
			
			//Offset the Entity (x = vel * time)
			float x_offset = mDeltaX * delta_time;
			float y_offset = mDeltaY * delta_time;
			
			mBox.offset(x_offset, y_offset);				
		}
		
		//Store old time
		mPreviousTime = timestamp;		
	}
	
	public void draw(Canvas canvas) {
		
		//Draw the position in a colored square if no Bitmap available
		if(mBitmap == null) {
			canvas.drawRect(mBox, mPaint);
		} 
		
		//Otherwise draw the Bitmap
		else {
			canvas.drawBitmap(mBitmap, null, mBox, mPaint);
		}
	}
}
