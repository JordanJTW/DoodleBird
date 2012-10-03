package com.jordan.game.bubble;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Perk extends Entity {
	final static float WIDTH = 30, HEIGHT = 30;
	
	public enum Type {
		EXTRA_LIFE, TWENTY, FIFTY, NONE;
	}
	
	Type mType;
	
	public Perk(Bitmap bmp, Type type, float x, float y, float scr_width, float scr_height) {
		super(bmp, x, y, WIDTH, HEIGHT, scr_width, scr_height);
		
		mType = type;
		
		setVelocity(0f, 0f);
		setGravity(40);
	}
	
	public Type getType() {
		return mType;
	}
	
	public void draw(Canvas canvas) {
		if(mBitmap != null)
			canvas.drawBitmap(mBitmap, null, mBox, mPaint);
	}
}
