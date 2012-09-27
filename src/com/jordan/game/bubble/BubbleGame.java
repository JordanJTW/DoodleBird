package com.jordan.game.bubble;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class BubbleGame extends Activity {
	static final int NEW_GAME = 101;
	static int DPI;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
    							WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	   	
        setContentView(R.layout.main);
        
 //       new CloudDialog(this).show();
        
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        DPI = metrics.densityDpi;
        
        final GameView bubbleGame = (GameView) findViewById(R.id.Game);       
        
        bubbleGame.setHandler(new UIHandler(bubbleGame, new CloudDialog(this)));
    }
    
    static class UIHandler extends Handler {
    	GameView mGameView;
    	CloudDialog mNewGameDialog;

    	public UIHandler(GameView gameView, CloudDialog dialog)
    	{
    		mGameView = gameView;
    		mNewGameDialog = dialog;
    	}
    	
    	public void dispatchMessage(Message m)
        {
        	switch(m.what)
        	{
        	case NEW_GAME:
        		mNewGameDialog.setTitle("New Game");
        		mNewGameDialog.setMessage("Would you like to start a new game?");
        		mNewGameDialog.setPositiveButton(android.R.string.yes, new View.OnClickListener() {
					public void onClick(View v) {
						mGameView.startGame();
						mNewGameDialog.dismiss();
					}
	        	});
        		mNewGameDialog.setNegativeButton(android.R.string.no, new View.OnClickListener() {
					public void onClick(View v) {
						mNewGameDialog.cancel();
					}
        		});
        		mNewGameDialog.show();
    			break;
    		}
        }
    }
}