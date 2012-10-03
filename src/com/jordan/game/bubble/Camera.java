package com.jordan.game.bubble;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.RectF;

public class Camera {	
	private float mScreenWidth, mScreenHeight;
	private float mGameWidth, mGameHeight;

	private float mInitX, mInitY;
	private float mDX, mDY;
	
	private boolean mTouchLock = false;
	
	public Camera(float screenWidth, float screenHeight, 
					float gameWidth, float gameHeight) {
		
		mScreenWidth = screenWidth;
		mScreenHeight = screenHeight;
		mGameWidth = gameWidth;
		mGameHeight = gameHeight;		
		
		mInitX = mInitY = 0;
		mDX = mDY = -100000;
	}
	
	public Pair correct(float x, float y) {
		float X = -1 * mDX + x;
		float Y = -1 * mDY + y;
		
		return new Pair(X, Y);
	}
	
	public void move(float x, float y, boolean first) {
		if(first) {
			mInitX = x;
			mInitY = y;
		} else {
			mDX += x - mInitX;
			mDY += y - mInitY;
			
			mInitX = x;
			mInitY = y;
		}
	}
	
	public void moveTo(float x, float y) {
		mDX = x - mDX;
		mDY = y - mDY;
	}
		
	public void track(ArrayList<? extends Entity> objs) {		
		if(!mTouchLock) {	
			float maxX, maxY, minX, minY;
			
			maxX = maxY = minX = minY = 0;
			
			for(Entity e : objs) {
				RectF b = e.getBox();
				
				maxX = Math.max(maxX, b.centerX());
				minX = Math.min(minX, b.centerX());
				maxY = Math.max(maxY, b.centerY());
				minY = Math.min(minY, b.centerY());
			}
			
			float x = Math.abs(maxX - minX);
			float y = Math.abs(maxY - minY);
			
			
			float dX = x - (mScreenWidth / 2);
			float dY = y - (mScreenHeight / 2);
			
			dX *= -1;
			dY *= -1;
			
			mDX = dX;
			mDY = dY;
		}
	}
	
	public void track(Entity obj) {			
		if(!mTouchLock) {
			float x = obj.getBox().centerX();
			float y = obj.getBox().centerY();
			
			
			float dX = x - (mScreenWidth / 2);
			float dY = y - (mScreenHeight / 2);
			
			dX *= -1;
			dY *= -1;
			
			mDX = dX;
			mDY = dY;
		}
	}
	
	public void setTouchLock(boolean lock) {
		mTouchLock = lock;
	}
		
	public void translate(Canvas canvas) {		
		if(mDX > 0)
    		mDX = 0;
    	
    	if(mDY > 0)
    		mDY = 0;
    	
    	if(mDX < -1 * mGameWidth + mScreenWidth)
    		mDX = -1 * mGameWidth + mScreenWidth;
    	
    	if(mDY < -1 * mGameHeight + mScreenHeight)
    		mDY = -1 * mGameHeight + mScreenHeight;	
    	
    	canvas.translate(mDX, mDY);
	}
	
	class Pair {
		float x, y;
		
		public Pair(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}
}
