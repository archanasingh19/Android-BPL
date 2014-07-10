package com.soccer.bpl;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class SoccerActivity extends TabActivity {
	private Context mContext;
	private GridView gridView;
	private static final int ABOUT_DIALOG=0;
	private static final int NO_INTERNET_DIALOG=1;
	//private static final String FEED_URL="http://bplandriod.wordpress.com/feed/";
	private static final String FEED_URL="http://laligaupdates2013.wordpress.com/feed/";
	private DbAsyncTask task;
	@Override
	public void onCreate(Bundle savedBundleInstance){
		super.onCreate(savedBundleInstance);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_soccer);
		
		
		AdView adview = (AdView)findViewById(R.id.adView);
		AdRequest re = new AdRequest();
		adview.loadAd(re);
		
		
		
		mContext=this;
		ImageView aboutImg = (ImageView)findViewById(R.id.aboutImg);
		aboutImg.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				showDialog(ABOUT_DIALOG);
			}
		});
		ImageView refreshView = (ImageView)findViewById(R.id.refreshView);
		refreshView.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				refreshDB();
			}
		});
		Resources res= getResources();
		TabHost tabHost = getTabHost();
		TabSpec scheduleTab = tabHost.newTabSpec("Schedule");
		TabSpec pointsTab = tabHost.newTabSpec("Points");        
        TabSpec resultsTab = tabHost.newTabSpec("Results");
        TabSpec newsTab = tabHost.newTabSpec("News");
        TabSpec rulesTab = tabHost.newTabSpec("Rules");
        scheduleTab.setIndicator("",res.getDrawable(R.drawable.scheduleicon)).setContent(new Intent(mContext,ScheduleActivity.class ));      
        pointsTab.setIndicator("",res.getDrawable(R.drawable.point_icon)).setContent(new Intent(mContext,Stats_Activity.class ));
        rulesTab.setIndicator("",res.getDrawable(R.drawable.ruleicon)).setContent(new Intent(mContext,RulesActivity.class));
        resultsTab.setIndicator("",res.getDrawable(R.drawable.results1icon)).setContent(new Intent(mContext,ResultsActivity.class));
        newsTab.setIndicator("",res.getDrawable(R.drawable.newsicon)).setContent(new Intent(mContext,NewsActivity.class));
        tabHost.addTab(scheduleTab);
        tabHost.addTab(pointsTab);        
        tabHost.addTab(resultsTab);
        tabHost.addTab(newsTab);
        tabHost.addTab(rulesTab);
        gridView = (GridView)findViewById(R.id.drawerContent);
		gridView.setAdapter(new ImageAdapter(mContext));
	}
	@Override
    public Dialog onCreateDialog(int id){
    	switch (id) {
		case ABOUT_DIALOG:
			
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(getString(R.string.about_title));
			dialog.setMessage(R.string.about_info);	
			dialog.setNegativeButton(mContext.getString(R.string.close), new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
				
			});
			return dialog.create();
		}
    	return null;
    }
	
	public void refreshDB(){
		if(!isOnline()){
			showDialog(NO_INTERNET_DIALOG);
		}else{
			task = new DbAsyncTask(mContext,false,SoccerActivity.this);
			task.execute(new String[]{FEED_URL});
		}
	}
	public boolean isOnline(){
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}
}


