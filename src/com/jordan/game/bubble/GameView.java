package com.jordan.game.bubble;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {	
	private static final int POINTS_TO_LIFE = 400;
	
	private float mWidth, mHeight;
	
	private int mNumLives = 3;
	private int mPreviousTotalPoints = 0;
	private int mTotalPoints = 0;
	
	private boolean DEBUG = false, DEBUG_VIS = false;
	
	private PhysicsThread mThread;
		
	private int mCurrentRate = 0, mFrameCount = 0;
	private long mPreviousTime = 0l;
	
	private Bitmap mBirdBmp, mDeadBirdBmp, mBubbleBmp, mHopperBmp, 
				mSlingFrontBmp, mSlingBottomBmp, mBottomBmp, mPointsToken;
	private Paint mDebugText, mElasticPaint, mOverlayPaint;
	private RectF mBackgroundRect;
	
	private int mLevel = 0;
	private long mDisplayTime = 0l;

	private Handler mHandler;
	
	public GameView(Context context, AttributeSet set) {
		super(context, set);
		init();
	}  
	
	private void init() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        
        mBirdBmp 		= 	BitmapFactory.decodeResource(getResources(), R.drawable.bird);
        mDeadBirdBmp	= 	BitmapFactory.decodeResource(getResources(), R.drawable.deadbird);
        mBubbleBmp 		= 	BitmapFactory.decodeResource(getResources(), R.drawable.bubble);
        mHopperBmp 		= 	BitmapFactory.decodeResource(getResources(), R.drawable.enemy);
        mSlingFrontBmp  =  	BitmapFactory.decodeResource(getResources(), R.drawable.frontsling);
        mSlingBottomBmp = 	BitmapFactory.decodeResource(getResources(), R.drawable.backsling);
        mBottomBmp 		= 	BitmapFactory.decodeResource(getResources(), R.drawable.bottom);
        
        mPointsToken	=	BitmapFactory.decodeResource(getResources(), R.drawable.cointoken);
        
        Typeface pencil = Typeface.createFromAsset(getContext().getAssets(), "pencilgrid.ttf");
        pencil = Typeface.create(pencil, Typeface.BOLD);
        
        mDebugText = new Paint();
        mDebugText.setColor(Color.parseColor("#454343"));
        mDebugText.setTextSize(36f);
        mDebugText.setAntiAlias(true);
        mDebugText.setTypeface(pencil);
        mDebugText.setShadowLayer(5f, 0f, 0f, Color.RED);
        
        mElasticPaint = new Paint();
        mElasticPaint.setColor(Color.parseColor("#CD853F"));
        mElasticPaint.setStrokeWidth(6f);
        mElasticPaint.setStrokeCap(Cap.ROUND);
        
        mOverlayPaint = new Paint();
        mOverlayPaint.setColor(Color.BLACK);
        mOverlayPaint.setAlpha(175);
    }
		
	private class PhysicsThread extends Thread {
		private SurfaceHolder mSurfaceHolder;	
		private boolean mRun = true,
					mLevelProgress = false,
					mGameOver = false;
		
		private Launcher mLauncher;
//		private PauseBubble mPauseBubble;
		
		private ArrayList<Bird> mBirds;
		private Bird mCurrentBird = null;
		
		private ArrayList<Bubble> mBubbles;
		private ArrayList<ProgressBubble> mProBubbles;
		private ArrayList<Perk> mPerks;
		
		private ArrayList<Enemy> mEnemies;
		
		private TokenFactory mTokenFactory;
		private Random mRandom;
		
		public PhysicsThread(SurfaceHolder holder, Context context) {
			mSurfaceHolder = holder;
					
			mRandom = new Random();
			
			startGame();
		}
		
		public void startGame() {
			mBirds = new ArrayList<Bird>();
			mBubbles = new ArrayList<Bubble>();
			mProBubbles = new ArrayList<ProgressBubble>(); 
			mPerks = new ArrayList<Perk>();
			mEnemies = new ArrayList<Enemy>();
						
			mLevel = 0;
			mNumLives = 3;
			mPreviousTotalPoints = mTotalPoints = 0;
			
			mGameOver = false;
		}
		
	    public void nextLevel() {			
	    	long deltaPoints = mTotalPoints - mPreviousTotalPoints;
	    	
	    	mNumLives += deltaPoints / POINTS_TO_LIFE;
	    	
			mPreviousTotalPoints = mTotalPoints;
	    	
			int pb = 30 - (mLevel/2) * 2;
			int pe = 1 + (mLevel/2);
			
			if(mLevel == 0)
				pb = pe = 0;
			
			mLevel++;
			
	    	int b = 30 - (mLevel/2) * 2;
	    	int e = 1 + (mLevel/2);
	    	
	    	synchronized(mProBubbles) {
	    		for(int i=0; i<3; i++)
	    			makeProgressBubble();
	    	}
	    		    	
	    	synchronized(mBubbles) {
		    	for(int i=pb; i<b; i++) {
					makeBubble();
				}
	    	}
			
	    	synchronized(mEnemies) {
	    		for(int i=pe; i<e; i++) {
	    			addEnemy();
				}
	    	}
	    }
	    	    
	    public void makeBubble() {
			float x = mRandom.nextInt((int) (mWidth - mLauncher.mWidth)) * 1f + mLauncher.mWidth;
			float y = mRandom.nextInt((int) mHeight) * 1f;
			mBubbles.add(new Bubble(mBubbleBmp, x, y, mWidth, mHeight));
	    }
	    
	    public void makeProgressBubble() {
			float x = mRandom.nextInt((int) (mWidth - mLauncher.mWidth)) * 1f + mLauncher.mWidth;
			float y = mRandom.nextInt((int) mHeight) * 1f;
			mProBubbles.add(new ProgressBubble(mBubbleBmp, x, y, mWidth, mHeight));
	    }
	    	    
	    public void addEnemy() {
	    	int spaces_width = (int) (mWidth - mLauncher.mWidth);
	    	int num_spaces = mEnemies.size() + 1;
	    	int space_size = spaces_width / num_spaces;
	    	
	    	if(DEBUG)
	    		Log.d("Bubble", String.format("Num Enemies: %d Num Spaces: %d", mEnemies.size(), num_spaces));
	    	
	    	boolean[] occupied = new boolean[num_spaces];
	    	
	    	if(DEBUG)
	    		Log.d("Bubble", String.format("Total width: %f Launcher width: %f Space size: %d", mWidth, mLauncher.mWidth, space_size));
	    	
	    	int i=0;
	    	synchronized(mEnemies)
	    	{
	    		for(Enemy e : mEnemies) {
	    			float x = e.getBox().left;
	    			int pos = (int)(x - mLauncher.mWidth) / space_size;
	    			i++;
	    			
	    			if(DEBUG) {
		    			Log.d("Bubble", String.format("%d. X: %f Box: %d", i+1, x, pos));
	    				Log.d("Bubble", String.format("Location: %f Inside spaces: %f Occupies: %d", e.getBox().left, e.getBox().left - mLauncher.mWidth, ((int)(e.getBox().left - mLauncher.mWidth) / space_size)));
	    			}
	    			
	    			occupied[pos] = true;
	    		}
	    	}
	    	
	    	int index = num_spaces - 1;
	    	while(index >= 0 && occupied[index]) index--;
	    	float x = mRandom.nextInt(space_size) + (space_size * index) + mLauncher.mWidth;
			float p = mRandom.nextFloat();
			Enemy e = new Enemy(mHopperBmp, x, mHeight, mWidth, mHeight);
			e.setVelocity(p);
			
			synchronized(mEnemies) 
			{
				mEnemies.add(e);
			}
	    }
					
		public void setRunning(boolean run) {
			mRun = run;
		}
	
	    @Override
		public void run() {
	        while (mRun) {
	            Canvas c = null;
	            try {
	            	long timestamp = System.currentTimeMillis();
		                
		            synchronized (mBirds) {
		             	for(int i=0; i<mBirds.size(); i++) {
		               		mBirds.get(i).update(timestamp);
		               		if(mBirds.get(i).getHitCount() > 5)
		               			mBirds.remove(i);
		               	}
		            }
		                
		            synchronized (mBubbles) {	                	
		              	for(Bubble bubble : mBubbles)
		              		bubble.update(timestamp);
		            }
		            
		            synchronized (mProBubbles) {	                	
		              	for(Bubble bubble : mProBubbles)
		              		bubble.update(timestamp);
		            }
		                
		            synchronized (mPerks) {
		              	for(int i=0; i<mPerks.size(); i++) {
		              		mPerks.get(i).update(timestamp);
		              		
		              		if(mPerks.get(i).mHitCount == 2)	
		              			mPerks.remove(i);
		              	}
		            }
		                
		            synchronized (mEnemies) {	                	
		               	for(Enemy enemy : mEnemies)
		               		enemy.update(timestamp);
		            }
		                
		            synchronized (mBirds) {
		               	for(Bird bird : mBirds) {
		               		synchronized (mBubbles) {
		               			for(int i=0; i<mBubbles.size(); i++) {
		               				if(RectF.intersects(bird.getBox(),
		               						mBubbles.get(i).getBox())) {
		               					Perk[] perks = mTokenFactory.getPerk(mBubbles.get(i));
			                			for(Perk perk : perks) {
		               						switch(perk.getType())
			                				{
			                				case TWENTY:
			                					mTotalPoints += 20;
			                					break;
			                				case FIFTY:
			                					mTotalPoints += 50;
			                					break;
			                				}
			                				mPerks.add(perk);
			                			}
		                				mBubbles.remove(i);                					
		                				makeBubble();
		                				mTotalPoints++;	                				                					
		                			}
		                		}
		                	}
		                }
		               	
			            synchronized (mBirds) {
			               	for(Bird bird : mBirds) {
			               		synchronized (mProBubbles) {
			               			for(int i=0; i<mProBubbles.size(); i++) {
			               				if(RectF.intersects(bird.getBox(),
			               						mProBubbles.get(i).getBox())) {
			               								                				
			               					Perk[] perks = mTokenFactory.getProgressPerk(mProBubbles.get(i));
				                			for(Perk perk : perks) {
			                					mTotalPoints += 20;
				                				mPerks.add(perk);
				                			}
				                			
				                			mProBubbles.remove(i);	 

					        				if(mProBubbles.size() == 0)
					        					mLevelProgress = true;
			                			}
			                		}
			                	}
			                }
			            }
			           		                
		                synchronized (mEnemies) {
		                	for(Enemy enemy : mEnemies) {
		                		synchronized (mBirds) {
		                			for(int i=0; i<mBirds.size(); i++) {
		                				if(RectF.intersects(enemy.getBox(),
		                						mBirds.get(i).getBox())) {
		                					mBirds.get(i).mHitCount = 5;
		                					mBirds.get(i).killed();
		                				}	                					
		                			}
		                		}
		                	}
		                }
		                
		                if(mBirds.size() == 0 && mPerks.size() == 0 && mLevelProgress) {
		                	nextLevel();
							mDisplayTime = System.currentTimeMillis() + 2000;
							mLevelProgress = false;
		                }
		               		                
        				if(mNumLives == 0 && mBirds.size() == 0 && !mLevelProgress && !mGameOver) {
    						mHandler.sendEmptyMessage(BubbleGame.NEW_GAME);
    						mGameOver = true;
    					}	
	            	}
	                
	                c = mSurfaceHolder.lockCanvas(null);
	                
	                synchronized (mSurfaceHolder) {	         
	                	if(mRun)
	                		doDraw(c);
	                }
	            } finally {
	                if (c != null) {
	                    mSurfaceHolder.unlockCanvasAndPost(c);
	                }
	            }
	        }
	    }
	    
	    public void setSurfaceSize(int width, int height) {    	
	    	mWidth = width;
	    	mHeight = height;
	    	
	    	float launcherSize = Math.min(mWidth, mHeight) * 0.40f;
	    	
			mLauncher = new Launcher(mSlingFrontBmp, mSlingBottomBmp, 0, mHeight, launcherSize, launcherSize);
//			mPauseBubble = new 
	        mBackgroundRect	= 	new RectF(0, mHeight - 40f, mWidth, mHeight);
			
	        mTokenFactory = new TokenFactory(mPointsToken, mWidth, mHeight);
	        
			nextLevel();
	    }

	    public boolean doTouch(MotionEvent event) {
	    	float x = event.getX(), y = event.getY();
	    	
	    	switch(event.getAction()) {
	    	case MotionEvent.ACTION_DOWN:
	    		if(mNumLives > 0 && !mLevelProgress)
		    		if(mLauncher.getBox().contains(x, y)) {
		    			mCurrentBird = new Bird(mBirdBmp, mDeadBirdBmp, x, y, mWidth, mHeight);
		    			return true;
		    		}
	    		
	    		if(DEBUG_VIS)
	    			addEnemy();
	    		
	    		return true;
	    		
	    	case MotionEvent.ACTION_MOVE:
	    		if(mCurrentBird != null) {
	    			if(!mLauncher.elasticBarrier(x, y)) {
	    				mCurrentBird = null;
	    				return true;
	    			}

	    			if(x > 0 && y + Bird.HEIGHT < mHeight)
	    				mCurrentBird.offsetTo(x, y);
	    			
	    			return true;
	    		}

	    		return true;
	    		
	    	case MotionEvent.ACTION_UP:
	    		if(mCurrentBird != null) {
	    			float dX = mLauncher.getXVelocity(x) * 3;
	    			float dY = mLauncher.getYVelocity(y) * 3;
	    			
	    			mCurrentBird.setVelocities(dX, dY);
	    			mCurrentBird.setGravity(100);
	    			
	    			synchronized(mBirds) {
		    			mBirds.add(mCurrentBird);
		    			mNumLives--;
		    			
		    			synchronized(mCurrentBird)
		    			{
		    				mCurrentBird = null;
		    			}
	    			}
	    		}
	    		return true;
	    	}
	    		    	
	    	return false;
	    }
	    
	    private void drawLines(Canvas canvas) {
	    	Paint p = new Paint();
	    	p.setColor(Color.parseColor("#454343"));//#0099FF"));
	    	
	    	int x = 0;
	    	while(x < mWidth) {
	    		x += 30;
	    		canvas.drawLine(x, 0, x, mHeight, p);
	    	}
	    	int y = 0;
	    	while(y < mHeight) {
	    		y += 30;
	    		canvas.drawLine(0, y, mWidth, y, p);
	    	}
	    }
	    
	    private void doDraw(Canvas canvas) {
	    	canvas.drawColor(Color.parseColor("#FFFFFF"));
	    	drawLines(canvas);
	    	canvas.drawBitmap(mBottomBmp, null, mBackgroundRect, null);
	    	
	    	Paint p = new Paint();
    		RectF r;
	    	
    		if(DEBUG_VIS) {
		    	int spaces_width = (int) (mWidth - mLauncher.mWidth);
		    	int num_spaces = mEnemies.size() > 0 ? mEnemies.size() : 2;
		    	int space_size = spaces_width / num_spaces;
		    	    		
		    	for(int i=0; i<mEnemies.size(); i++) {
		    		r = new RectF(i*space_size + mLauncher.mWidth, 0, i*space_size + mLauncher.mWidth + space_size, mHeight);
		    		p = new Paint();
		    		p.setColor(Color.BLACK);
		    		p.setStyle(Style.STROKE);
		    		p.setStrokeWidth(6f);
		    		canvas.drawRect(r, p);
		    	}
    		}
    				    	
	    	for(int i=0; i<mNumLives; i++) {
	    		r = new RectF(i*40+i*5, mHeight-65, i*40 + 40 + i*5, mHeight-25);
	    		int width = mBirdBmp.getWidth() / 3;
	    		int height = mBirdBmp.getHeight();
	    		canvas.drawBitmap(mBirdBmp, new Rect(0, 0, width, height), r, null);
	    	}
	    	
	    	mLauncher.drawBack(canvas);
	    	if(mCurrentBird != null) {
	    		synchronized(mCurrentBird) {
		    		RectF bound = mCurrentBird.getBox();
		    		
		    		if(bound != null)
		    			canvas.drawLine(mLauncher.getBox().right, mLauncher.getBox().top + 20,
		    				mCurrentBird.getBox().left, mCurrentBird.getBox().centerY(), mElasticPaint);
		    		
		    		mCurrentBird.draw(canvas);
		    		
		    		bound = mCurrentBird.getBox();
		    		
		    		if(bound != null)
		    			canvas.drawLine(mLauncher.getBox().right - 30, mLauncher.getBox().top + 22,
		    				mCurrentBird.getBox().left, mCurrentBird.getBox().centerY(), mElasticPaint);	    		
	    		}
	    	}
	    	    		
	    	synchronized(mBirds) {
	    		for(Entity tmp : mBirds)
	    			tmp.draw(canvas);
	    	}
	    	
	    	mLauncher.drawFront(canvas);
	    	
	    	synchronized(mBubbles) {
	    		for(Entity tmp : mBubbles)
	    			tmp.draw(canvas);
	    	}
	    	
	       	synchronized(mProBubbles) {
	    		for(Entity tmp : mProBubbles)
	    			tmp.draw(canvas);
	    	}
	    	
	    	synchronized(mPerks) {
	    		for(Perk tmp : mPerks)
	    			tmp.draw(canvas);
	    	}
	    	
	    	synchronized(mEnemies) {
	    		for(Entity tmp : mEnemies)
	    			tmp.draw(canvas);
	    	}
	    	
	    	String header = String.format("Level: %d Points: %d", mLevel, mTotalPoints);
	    	
	    	canvas.drawText(header, mWidth - mDebugText.measureText(header), 38, mDebugText);
	  	    	
	    	if(DEBUG) {
		    	long curtime = System.currentTimeMillis();
		    	if((curtime - mPreviousTime) < 1000)
		    		mFrameCount++;
		    	else {
		    		mPreviousTime = curtime;
		    		mCurrentRate = mFrameCount;
		    		mFrameCount = 0;
		    	}
		    	
	    		canvas.drawText("FPS: "+mCurrentRate, 0, 38, mDebugText);
	    	}
	    	
    		if(mDisplayTime > System.currentTimeMillis()) {
	    	 	String nextlvl = "Next level!";
	    	 	
	    	 	mDebugText.setTextSize(56f);
	    	 	
		    	canvas.drawText(nextlvl, mWidth / 2 - mDebugText.measureText(nextlvl) / 2, mHeight / 2 - 56, mDebugText);
		    	
		    	mDebugText.setTextSize(38f);
    		}
    		
    		if(mLevelProgress || mDisplayTime > System.currentTimeMillis())
    			canvas.drawRect(new RectF(0, 0, mWidth, mHeight), mOverlayPaint);
	    }
	}
		
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mThread.doTouch(event);		
	}
		
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		mThread.setSurfaceSize(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
        mThread = new PhysicsThread(holder, getContext());
		mThread.setRunning(true);
        mThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        mThread.setRunning(false);
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }
	}

	public void setHandler(Handler handler) {
		mHandler = handler;		
	}

	public void startGame() {
		mThread.startGame();		
		mThread.nextLevel();
	}	
}