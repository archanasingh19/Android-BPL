package com.soccer.bpl;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ClubDetailsActivity extends Activity {
	private Context mContext;
	private int clubId;
	private static final String CLUBDETAILS="clubdetails";
	private static final String CLUBID="clubid";
	private static final String CLUBABBR="abbr";
	private static final String CLUBNAME="clubname";
	private static final String NICKNAME="nickname";
	private static final String FOUNDED="founded";
	private static final String MANAGER="manager";
	private static final String HOMEGROUND="homeground";
	private static final String TITLES="titles";
	private static final String BESTRANK="bestrank";
	private static final String WIN="win";
	private static final String DRAW="draw";
	private static final String LOST="lost";
	private static final String POINTS="points";
	private static final String WEBSITE="website";
	private static final String SPONSOR="sponsor";
	private static final String LOGO="logo";
		
	@Override
	public void onCreate(Bundle savedBundleInstance){
		super.onCreate(savedBundleInstance);
		setContentView(R.layout.clubdetails);
		mContext=this;
		getClubDetails();
	}
	
	private void getClubDetails(){
		Bundle extras = getIntent().getExtras();
		clubId = extras.getInt(ScheduleAdapter.CLUBID);
		soccerDBHelper opensoccerHelperClass = new soccerDBHelper(this);
		// Then we need to get a readable database
		opensoccerHelperClass.dbHelper.createDataBase();
		String queryString="select * from "+CLUBDETAILS+" where "+CLUBID+"="+clubId;
		try{
			SQLiteDatabase sqliteDatabase = opensoccerHelperClass.dbHelper.openDB();
			Cursor cur = sqliteDatabase.rawQuery(queryString, null);
			
			while(cur.moveToNext()){
				int clbid = cur.getInt(cur.getColumnIndex(CLUBID));
				String abbr = cur.getString(cur.getColumnIndex(CLUBABBR));
				String clubname = cur.getString(cur.getColumnIndex(CLUBNAME));
				String nickname = cur.getString(cur.getColumnIndex(NICKNAME));
				String founded = cur.getString(cur.getColumnIndex(FOUNDED));
				String manager = cur.getString(cur.getColumnIndex(MANAGER));
				String homeground = cur.getString(cur.getColumnIndex(HOMEGROUND));
				String titles = cur.getString(cur.getColumnIndex(TITLES));
				String bestrank = cur.getString(cur.getColumnIndex(BESTRANK));
				String win = cur.getString(cur.getColumnIndex(WIN));
				String draw = cur.getString(cur.getColumnIndex(DRAW));
				String lost = cur.getString(cur.getColumnIndex(LOST));
				String points = cur.getString(cur.getColumnIndex(POINTS));
				String website = cur.getString(cur.getColumnIndex(WEBSITE));
				String sponsor = cur.getString(cur.getColumnIndex(SPONSOR));
				String logo = cur.getString(cur.getColumnIndex(LOGO));
				Club club = new Club(mContext,clbid, abbr, clubname, nickname, founded, manager,
						homeground, titles, bestrank,win, draw, lost, points, website, sponsor, logo);
				TextView clubName_tv =(TextView)findViewById(R.id.clubName_tv);
				clubName_tv.setText(clubname.replace(" Football Club", "").replace(" Association", ""));
				TextView nickName_tv =(TextView)findViewById(R.id.nickName_tv);
				nickName_tv.setText(nickname);
				ImageView logo_iv = (ImageView)findViewById(R.id.logo_iv);
				int resId = mContext.getResources().getIdentifier(logo, "drawable", mContext.getPackageName());
				logo_iv.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), resId));
				TextView founded_tv =(TextView)findViewById(R.id.founded_tv);
				founded_tv.setText(mContext.getString(R.string.founded)+" "+founded);
				TextView manager_tv =(TextView)findViewById(R.id.manager_tv);
				manager_tv.setText(mContext.getString(R.string.manager)+" "+manager);
				TextView homeground_tv =(TextView)findViewById(R.id.homeground_tv);
				homeground_tv.setText(mContext.getString(R.string.homeground)+" "+homeground);
				TextView abbr_tv =(TextView)findViewById(R.id.abbr_tv);
				abbr_tv.setText(abbr);
				TextView bestrank_tv =(TextView)findViewById(R.id.bestrank_tv);
				bestrank_tv.setText(mContext.getString(R.string.bestrank)+" "+bestrank);
				TextView wins_tv =(TextView)findViewById(R.id.wins_tv);
				wins_tv.setText(mContext.getString(R.string.wins)+" "+win);
				TextView draw_tv =(TextView)findViewById(R.id.draw_tv);
				draw_tv.setText(mContext.getString(R.string.draw)+" "+draw);
				TextView lost_tv =(TextView)findViewById(R.id.lost_tv);
				lost_tv.setText(mContext.getString(R.string.lost)+" "+lost);
				TextView titles_tv =(TextView)findViewById(R.id.titles_tv);
				titles_tv.setText(mContext.getString(R.string.titles)+" "+titles);
				TextView points_tv =(TextView)findViewById(R.id.points_tv);
				points_tv.setText(mContext.getString(R.string.points)+" "+points);
				TextView website_tv =(TextView)findViewById(R.id.website_tv);
				website_tv.setText(mContext.getString(R.string.website)+" "+website);
				TextView sponsor_tv =(TextView)findViewById(R.id.sponsor_tv);
				sponsor_tv.setText(mContext.getString(R.string.sponsor)+" "+sponsor);
				GridView playerList = (GridView)findViewById(R.id.playersList);
				ArrayList<String> playerNames = club.getPlayerList();
				playerList.setAdapter(new playerAdapter(mContext, playerNames));
			}
			cur.close();
			sqliteDatabase.close();
			}catch (SQLiteException e) {
				e.printStackTrace();
			}		
	}
	
	

}
