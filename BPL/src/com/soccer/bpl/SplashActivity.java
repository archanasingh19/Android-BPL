package com.soccer.bpl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Window;

public class SplashActivity extends Activity {
	private Context mContext;
	private DbAsyncTask task;
	private static final String FEED_URL="http://bplandriod.wordpress.com/feed/";
	private static final int NO_INTERNET_DIALOG=1;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splashscreen);
		mContext =this;
		updateDB();
	}
	public void updateDB(){
		if(!isOnline()){
			showDialog(NO_INTERNET_DIALOG);
		}else{
			task = new DbAsyncTask(mContext,true,SplashActivity.this);
			task.execute(new String[]{FEED_URL});
		}
	}
	
	
	@Override
    public Dialog onCreateDialog(int id){
    	switch (id) {
		case NO_INTERNET_DIALOG:
			
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(getString(R.string.network_error_title));
			dialog.setMessage(R.string.network_error_message);
			dialog.setNegativeButton(mContext.getString(R.string.close), new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					Intent mainIntent = new Intent(mContext,SoccerActivity.class);
					mContext.startActivity(mainIntent);
					SplashActivity.this.finish();
				}
				
			});
			return dialog.create();
		}
    	return null;
    }
	public boolean isOnline(){
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}
}
