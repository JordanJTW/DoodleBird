package com.jordan.game.bubble;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class BubbleGame extends Activity {
	static final int NEW_GAME = 101, ADVANCE = 111, PAUSE = 121;
	static int DPI;
	
	private CloudDialog mGenericDialog;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
    							WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	   	
        setContentView(R.layout.main);
        
        mGenericDialog = new CloudDialog(this);
                
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        DPI = metrics.densityDpi;
        
        final GameView bubbleGame = (GameView) findViewById(R.id.Game);       
        
        bubbleGame.setHandler(new UIHandler(bubbleGame, mGenericDialog));
    }
    
    public void onPause() {
    	super.onPause();
    	
    	mGenericDialog.dismiss();
    }
    
    static class UIHandler extends Handler {
    	GameView mGameView;
    	CloudDialog mDialog;
    	
    	private String GameOverTitle, GameOverMessage, AdvanceTitle, PauseTitle;

    	public UIHandler(GameView gameView, CloudDialog dialog)
    	{
    		mGameView = gameView;
    		mDialog = dialog;
    		
			Resources res = mGameView.getResources();
			
			GameOverTitle = res.getString(R.string.gameover_title);
			GameOverMessage = res.getString(R.string.gameover_message);
			
			AdvanceTitle = res.getString(R.string.advance_title);
			PauseTitle = res.getString(R.string.pause_title);
    	}
    	
    	public void dispatchMessage(Message m)
        {
        	switch(m.what)
        	{
        	case NEW_GAME:
        		mDialog.set(GameOverTitle, GameOverMessage,
        				R.string.button_accept, R.string.button_cancel,
        				
        				new View.OnClickListener() {
							public void onClick(View v) {
								switch(v.getId()) {
								case R.id.accept:
									mGameView.startGame();
									mDialog.dismiss();
									break;
								case R.id.cancel:
									mDialog.dismiss();
									break;
								}
							}
						});
        		
        		mDialog.show();
    			break;
    			
        	case ADVANCE:
        		int lives = m.arg2 / GameView.POINTS_TO_LIFE;
        		String message = String.format("You completed level %d with %d points, you get %d new %s.",
        								m.arg1, m.arg2, lives, lives>1 ? "lives" : "life");
        		
        		mDialog.set(AdvanceTitle, message,
        				R.string.button_next, null,
        				
        				new View.OnClickListener() {
        					public void onClick(View v) {
        						switch(v.getId()) {
        						case R.id.accept:
        							mGameView.advanceGame();
        							mDialog.dismiss();
        							break;
        					}
        				}
        			});
        		
        		mDialog.show();
        		break;
        		
        	case PAUSE:
        		mGameView.pauseGame();
        		
        		mDialog.set(PauseTitle, null,
        				R.string.button_restart, R.string.button_resume,
        				
        				new View.OnClickListener() {
        					public void onClick(View v) {
        						switch(v.getId()) {
        						case R.id.accept:
        							mGameView.startGame();
        							mDialog.dismiss();
        							break;
        						case R.id.cancel:
        							mGameView.resumeGame();
        							mDialog.dismiss();
        							break;
        					}
        				}
        			});
        		
        		mDialog.show();        		
        		break;
    		}
        }
    }
}