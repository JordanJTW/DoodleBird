package com.jordan.game.bubble;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CloudDialog extends Dialog {
	private TextView mTitle, mMessage;
	
	private CloudButton mAccept, mCancel;
	
	public CloudDialog(Context context) {
		super(context);
		
    	requestWindowFeature(Window.FEATURE_NO_TITLE);

    	getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
		
		setContentView(R.layout.cloud_dialog);
		
		mTitle 		=	(TextView) findViewById(R.id.title);
		mMessage 	=	(TextView) findViewById(R.id.message); 
		
		mAccept 	= 	(CloudButton) findViewById(R.id.accept);
		mCancel		=	(CloudButton) findViewById(R.id.cancel);
	
        Typeface pencil = Typeface.createFromAsset(getContext().getAssets(), "pencilgrid.ttf");
        pencil = Typeface.create(pencil, Typeface.BOLD);
		
		mTitle.setTypeface(pencil);
		mMessage.setTypeface(pencil);
	}
		
	public void setMessage(String message) {
		mMessage.setText(message);
	}
	
	public void setPositiveButton(int resid, View.OnClickListener l) {
		setButton(mAccept, resid, l);
	}
	
	public void setNegativeButton(int resid, View.OnClickListener l) {
		setButton(mCancel, resid, l);
	}
	
	private void setButton(Button button, int resid, View.OnClickListener listener) {
		button.setOnClickListener(listener);
		
		button.setText(resid);
	}

	public void setTitle(String title) {
		mTitle.setText(title);
	}
}
