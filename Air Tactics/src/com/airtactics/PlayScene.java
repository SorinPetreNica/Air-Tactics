package com.airtactics;


import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.scoreloop.client.android.ui.EntryScreenActivity;
import airtactics.com.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


	public class PlayScene extends Activity{
		
		public static final int SINGLE_PLAYER = 1;
		public static final int MULTI_PLAYER = 2;
		public static int GAME_TYPE;
		AdView adView;
		AlertDialog alert;
		Boolean firstRun;
		
		Sprite background, singleButton,slButton, multiButton;
		int currentX, currentY, AI;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			firstRunPreferences();
			if (getFirstRun())
			{
				setRunned();
				firstRun = true;
			}
			else firstRun = false;
			requestWindowFeature(Window.FEATURE_NO_TITLE); 
	        updateFullscreenStatus(true);
			ScreenDisplay.playPanel = new Panel(this);
			adView = new AdView(this, AdSize.BANNER, "a14de914472599e"); 
			FrameLayout layout = new FrameLayout(this);
		    FrameLayout.LayoutParams gameParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
		            FrameLayout.LayoutParams.FILL_PARENT);
		    FrameLayout.LayoutParams adsParams =new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, 
		            FrameLayout.LayoutParams.WRAP_CONTENT, android.view.Gravity.BOTTOM); 
		    layout.addView(ScreenDisplay.playPanel, gameParams);
		    layout.addView(adView, adsParams); 
		    setContentView(layout);
		    
		    Display display = getWindowManager().getDefaultDisplay(); 
			ScreenDisplay.screenWidth = display.getWidth();
			ScreenDisplay.screenHeight = display.getHeight();
			ScreenDisplay.setDensity();
		    
		    AdRequest request = new AdRequest();
		    //request.addTestDevice("CF95DC53F383F9A836FD749F3EF439CD");
		    //request.setTesting(true);
		    adView.loadAd(request);
		    
			//setContentView(ScreenDisplay.playPanel);
			
			init();
		}
		
		public void init()
	    {
			background = new Sprite(getResources(), R.drawable.background, ScreenDisplay.playPanel);
			background.setPosition(background.width/2, background.height/2);
			ScreenDisplay.playPanel.addSprite(background);
			singleButton = new Sprite(getResources(), R.drawable.single_button, ScreenDisplay.playPanel);
			singleButton.setPosition(160,240);
			ScreenDisplay.playPanel.addSprite(singleButton);
			multiButton = new Sprite(getResources(), R.drawable.multi_button, ScreenDisplay.playPanel);
			multiButton.setPosition(160,300);
			ScreenDisplay.playPanel.addSprite(multiButton);
			slButton = new Sprite(getResources(), R.drawable.sl_button, ScreenDisplay.playPanel);
			slButton.setPosition(160,380);
			ScreenDisplay.playPanel.addSprite(slButton);
	    }
		
		
		
		
		
		/**
		 * get if this is the first run
		 *
		 * @return returns true, if this is the first run
		 */
		    public boolean getFirstRun() {
		    return mPrefs.getBoolean("firstRun", true);
		 }
		 
		 /**
		 * store the first run
		 */
		 public void setRunned() {
		    SharedPreferences.Editor edit = mPrefs.edit();
		    edit.putBoolean("firstRun", false);
		    edit.commit();
		 }
		 
		 SharedPreferences mPrefs;
		 
		 /**
		 * setting up preferences storage
		 */
		 public void firstRunPreferences() {
		    Context mContext = this.getApplicationContext();
		    mPrefs = mContext.getSharedPreferences("myAppPrefs", 0); //0 = mode private. only this app can read these preferences
		 }
		
		
		
		
		

		public void alertReceived()
		{
	    	
	    	final CharSequence[] items = {"Beginner", "Advanced"};

	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setTitle("How good are you?");
	    	builder.setItems(items, new DialogInterface.OnClickListener() {
	    	    public void onClick(DialogInterface dialog, int item) {
	    	        
	    	    	switch (item)
	    	    	{
		    	    	case 0:
		    	    	{
		        			AI = 1;
		        			Intent intent = new Intent(getBaseContext(), Air.class);
		        			intent.putExtra("AI", Integer.toString(AI));
		        			startActivity(intent);
		        			break;
		    	    	}
		    	    	case 1:
		    	    	{
		    	    		AI = 2;	
		    	    		Intent intent = new Intent(getBaseContext(), Air.class);
		    				intent.putExtra("AI", Integer.toString(AI));
		    				startActivity(intent);
		    	    	}
	    	    	}
	    	    	
	    	    }
	    	});
	    	AlertDialog alert = builder.create();
	    	alert.show();
		}
		
		
		private void singleScene() {
			GAME_TYPE = SINGLE_PLAYER;
			AirOpponent.firstTime = true;
			AirOpponent.selectedI = -1;
			AirOpponent.selectedJ = -1;
			alertReceived();
			
			//finish();
			
		}
		
		private void multiScene()
		{
			GAME_TYPE = MULTI_PLAYER;
			AirOpponent.firstTime = true;
			AirOpponent.selectedI = -1;
			AirOpponent.selectedJ = -1;
			AI = 1;	
    		Intent intent = new Intent(getBaseContext(), Air.class);
			intent.putExtra("AI", Integer.toString(AI));
			startActivity(intent);
		}
		private void scoreloopScreen()
		{
			startActivity(new Intent(getBaseContext(), EntryScreenActivity.class));
		}
		
		public void helpDialog()
		{      
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Help");
        	builder.setMessage(R.string.help_text)
        	       .setCancelable(false)
        	       .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	                singleScene();
        	                alert.cancel();
        	           }
        	       });
            alert = builder.create();
        	alert.show();
		}
		
		public boolean onTouchEvent(MotionEvent event) 
		{
	        int action = event.getAction();
	        currentX = (int)event.getX(0); 
	        currentY = (int)event.getY(0); 
	        //MotionEvent.
	        switch(action & MotionEvent.ACTION_MASK)
	        {
	        case MotionEvent.ACTION_DOWN:
	        {
	        	if (singleButton.touched(currentX, currentY))
	        	{
	        		if (firstRun) 
	        		{
	        			helpDialog();
	        			firstRun = false;
	        		}
	        		else singleScene();
	        	}
	        	else if (multiButton.touched(currentX, currentY))
	        	{
	        		multiScene();
	        	}
	        	else if (slButton.touched(currentX, currentY))
	        	{
	        		scoreloopScreen();
	        	}
	        	break;
	        }
	        case MotionEvent.ACTION_UP:
	        {
	        	
	        }
	        }
			return true;
	      }
		
		private void updateFullscreenStatus(Boolean bUseFullscreen)
		{   
		   if(bUseFullscreen)
		   {
		        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		    }
		    else
		    {
		        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		    }

		    //m_contentView.requestLayout();
		}
}
