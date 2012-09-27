package com.jordan.game.bubble;

import android.graphics.Bitmap;
import android.util.FloatMath;

public class Enemy extends Entity {
	static float WIDTH = 85, HEIGHT = 50;
	private float PERCENT_HGT = 0.9f;
	
	public Enemy(Bitmap bmp, float x, float y, float scr_width, float scr_height)
	{
		super(bmp, x, y, WIDTH, HEIGHT, scr_width, scr_height);
		
		setGravity(40f);
		setVelocity();
	}
	
	public void setVelocity(float percent)
	{
		PERCENT_HGT = percent;
		setVelocity();
	}
	
	public void setVelocity()
	{
		/*
		 * vf^2 = vi^2 + 2*a*y
		 * vi = sqrt(2ay);
		 */
		
		float height = (mBounds.height() - HEIGHT) * PERCENT_HGT;
		float vy = FloatMath.sqrt(2.0f * mGravity * height);
		this.setVelocities(0, vy);
	}
	
	public float getVerticalFriction() {
		return -1f;
	}
}
