package com.jordan.game.bubble;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

public class ProgressBubble extends Bubble {
	public ProgressBubble(Bitmap bmp, float x, float y, float scr_width, float scr_height) {
		super(bmp, x, y, scr_width, scr_height);

		mPaint = new Paint(Color.YELLOW);
		ColorFilter filter = new LightingColorFilter(Color.YELLOW, 1);
		mPaint.setColorFilter(filter);
	}
}
