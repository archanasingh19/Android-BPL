package com.soccer.bpl;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Stats_Activity extends TabActivity {
	private Context mContext;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats_activity);
		mContext=this;
		Resources res= getResources();
		TabHost tabHost = getTabHost();
		
		TabSpec pointsTab = tabHost.newTabSpec("Points");
        TabSpec rulesTab = tabHost.newTabSpec("Top Scorer");
        TabSpec newsTab = tabHost.newTabSpec("Top Assists");
             
        pointsTab.setIndicator("",res.getDrawable(R.drawable.point_icon1)).setContent(new Intent(mContext,ClubStandingActivity.class ));
        rulesTab.setIndicator("",res.getDrawable(R.drawable.topscoricon)).setContent(new Intent(mContext,TopScorerActivity.class));
        newsTab.setIndicator("",res.getDrawable(R.drawable.topassisticon)).setContent(new Intent(mContext,TopAssistsActivity.class));
                       
        tabHost.addTab(pointsTab);
        tabHost.addTab(rulesTab);
        tabHost.addTab(newsTab);
        
	}
}
