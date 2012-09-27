package com.jordan.game.bubble;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class CloudButton extends Button {
	private Paint mLinePaint;
	
	public CloudButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
        Typeface pencil = Typeface.createFromAsset(getContext().getAssets(), "pencilgrid.ttf");
        pencil = Typeface.create(pencil, Typeface.BOLD);
        
        setTypeface(pencil);
        setTextSize(25);
        
	    mLinePaint = new Paint();
	    mLinePaint.setColor(Color.DKGRAY);
	    mLinePaint.setStrokeCap(Paint.Cap.ROUND);
	    mLinePaint.setStrokeWidth(6f);
        
    	setBackgroundResource(android.R.color.transparent);
	}
	
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int w = canvas.getWidth();
		
		canvas.drawLine(0, 50,w, 50, mLinePaint);
	}
}
