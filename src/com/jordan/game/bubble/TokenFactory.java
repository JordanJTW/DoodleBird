package com.jordan.game.bubble;

import java.util.Random;

import android.graphics.Bitmap;

public class TokenFactory {
	private Bitmap mPointsToken;
	
	private float mScreenWidth, mScreenHeight;
	
	public TokenFactory(Bitmap points, float width, float height) {
		mPointsToken = points;
		
		mScreenWidth = width;
		mScreenHeight = height;
	}
	
	public Perk[] getPerk(Bubble bubble) {	
		int n = new Random().nextInt(10);
		
		float x = bubble.getBox().centerX();
		float y = bubble.getBox().centerY();
		
		Perk[] perks = new Perk[1];
		
		switch(n) {
		case 0:
		case 1:
		case 2:
			perks = new Perk[3];
			perks[0] = new Perk(mPointsToken, Perk.Type.TWENTY, x, y, mScreenWidth, mScreenHeight);
			perks[0].setVelocities(20, -20);
			perks[1] = new Perk(mPointsToken, Perk.Type.TWENTY, x, y, mScreenWidth, mScreenHeight);
			perks[1].setVelocities(0, -20);
			perks[2] = new Perk(mPointsToken, Perk.Type.TWENTY, x, y, mScreenWidth, mScreenHeight);
			perks[2].setVelocities(-20, -20);
			break;
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
			perks[0] = new Perk(mPointsToken, Perk.Type.TWENTY, x, y, mScreenWidth, mScreenHeight);
			break;
		default:
			perks[0] = new Perk(null, Perk.Type.NONE, x, y, mScreenWidth, mScreenHeight);
			break;
		}
		
		return perks;
	}
	
	public Perk[] getProgressPerk(Bubble bubble) {		
		float x = bubble.getBox().centerX();
		float y = bubble.getBox().centerY();
		
		Perk[] perks = new Perk[8];
		
		for(int i=0; i<perks.length; i++) {
			perks[i] = new Perk(mPointsToken, Perk.Type.FIFTY, x, y, mScreenWidth, mScreenHeight);
			perks[i].setVelocities(-40 + i*10, -20);
		}
		
		return perks;
	}
}
