package com.jordan.game.bubble;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Bubble extends Entity {
	private static float WIDTH = 40, HEIGHT = 40;
	
	public Bubble(Bitmap bmp, float x, float y, float scr_width, float scr_height) {
		super(bmp, x, y, WIDTH, HEIGHT, scr_width, scr_height);
		
		float bX = x - 30;
		float bY = y - 30;
		
		this.setBoundries(bX, bY, 60, 60);
		
		float dX = 15 * new Random().nextInt(3) - 1;
		float dY = 15 * new Random().nextInt(3) - 1;
		this.setVelocities(dX, dY);
	}
	
	public Bubble(float x, float y, float scr_width, float scr_height) {
		super(Color.BLUE, x, y, WIDTH, HEIGHT, scr_width, scr_height);
	}
}
