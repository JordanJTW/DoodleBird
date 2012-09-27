package com.jordan.game.bubble;

public class Camera {
	private float mScreenWidth, mScreenHeight;
	private float mGameWidth, mGameHeight;
	
	private float mX, mY;
	
	private float mDX, mDY;
	
	public Camera(float screenWidth, float screenHeight, 
					float gameWidth, float gameHeight) {
		
		mScreenWidth = screenWidth;
		mScreenHeight = screenHeight;
		mGameWidth = gameWidth;
		mGameHeight = gameHeight;		
		
		mX = mY = 0;
	}
	
	public void move(float dX, float dY) {
		mDX = dX;
		mDY = dY;
		
		if(mX + dX > mGameWidth) {
			mX = mGameWidth - mScreenWidth;
			mDX -= (mX + dX) - mScreenWidth;
		}
			
		if(mX + dX < 0) {
			mX = 0;
			mDX += -1 * (mX + dX);
		}
		
		if(mY + dY > mGameHeight) {
			mY = mGameHeight - mScreenHeight;
			mDY -= (mY + dY) - mScreenHeight;
		}
			
		if(mY + dY < 0) {
			mY = 0;
			mDY += -1 * (mY + dY);
		}
	}
	
	
}
