package com.soccer.bpl;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TopAssistsActivity extends Activity {
	private static final String PLAYERDETAILS="playerdetails";
	private static final String CLUBDETAILS="clubdetails";
	private static final String MOSTASSISTS="mostassists";
	private static final String PLAYERNAME="name";
	private static final String CLUBNAME="clubname";
	private static final String ASSISTS="assists";
	private static final float textSize=15;
	private static final float textSizeData=14;
	private static final int left=1,top=10,right=0,bottom=10;
	private Context mContext;
	private TableLayout assistsTable;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topassists);
		mContext=this;
		
		assistsTable = (TableLayout)findViewById(R.id.topassiststable);
		assistsTable.setStretchAllColumns(true);  
		assistsTable.setShrinkAllColumns(true);
		populateAssists();
		
	}
	
	public void populateAssists(){
		
		/*TableRow rowTitle = new TableRow(mContext);
		TextView playerTitle = new TextView(mContext);
		playerTitle.setText(R.string.player_name);
		playerTitle.setGravity(Gravity.LEFT);
		playerTitle.setTextAppearance(mContext, R.style.boldText);
		playerTitle.setBackgroundColor(Color.rgb(0, 140, 205));
		playerTitle.setPadding(left, top, right, bottom);	
		playerTitle.setTextSize(textSize);		
		rowTitle.addView(playerTitle);
		
		TextView clubTitle = new TextView(mContext);
		clubTitle.setText(R.string.club);
		clubTitle.setGravity(Gravity.LEFT);
		clubTitle.setTextAppearance(mContext, R.style.boldText);
		clubTitle.setBackgroundColor(Color.rgb(0, 140, 205));
		clubTitle.setPadding(left, top, right, bottom);	
		clubTitle.setTextSize(textSize);
		rowTitle.addView(clubTitle);
		
		TextView assistsTitle = new TextView(mContext);
		assistsTitle.setText(R.string.assists);
		assistsTitle.setGravity(Gravity.CENTER);
		assistsTitle.setTextAppearance(mContext, R.style.boldText);
		assistsTitle.setBackgroundColor(Color.rgb(0, 140, 205));
		assistsTitle.setPadding(left, top, right, bottom);	
		assistsTitle.setTextSize(textSize);		
		rowTitle.addView(assistsTitle);
		
		assistsTable.addView(rowTitle);*/
		soccerDBHelper opensoccerHelperClass = new soccerDBHelper(this);
		opensoccerHelperClass.dbHelper.createDataBase();
			
		String queryString="select t1.name as name,t1.assists as assists,club.clubname as " +
				"clubname from (select  pl.pname as name,gls.assists as assists ,pl.clubid as " +
				"clubid from "+MOSTASSISTS+" as gls inner join "+PLAYERDETAILS+" as pl where"
				+" gls.playerid = pl.playerid) as t1 inner join "+CLUBDETAILS+" as club where club.clubid=t1.clubid order by CAST(assists as integer) desc;";
		
		try{
			SQLiteDatabase sqliteDatabase = opensoccerHelperClass.dbHelper.openDB();
			Cursor cur = sqliteDatabase.rawQuery(queryString, null);
			
			while(cur.moveToNext()){
				String player = cur.getString(cur.getColumnIndex(PLAYERNAME));
				String club = cur.getString(cur.getColumnIndex(CLUBNAME));
				String assists = cur.getString(cur.getColumnIndex(ASSISTS));
				
				TableRow row1 = new TableRow(mContext);
				TextView player1 = new TextView(mContext);
				player1.setText(player);
				player1.setGravity(Gravity.LEFT);
				player1.setTextAppearance(mContext, R.style.dataText);
				player1.setTextSize(textSizeData);	
				player1.setPadding(left, top, right, bottom);				
				row1.addView(player1);
				
				TextView club1 = new TextView(mContext);
				club1.setText(club.replace(" Football Club", "").replace(" Association", ""));
				club1.setGravity(Gravity.LEFT);
				club1.setTextAppearance(mContext, R.style.dataText);
				club1.setTextSize(textSizeData);	
				club1.setPadding(left, top, right, bottom);				
				row1.addView(club1);
				
				TextView assists1 = new TextView(mContext);
				assists1.setText(assists);
				assists1.setGravity(Gravity.CENTER);
				assists1.setTextAppearance(mContext, R.style.dataText);
				assists1.setTextSize(textSizeData);	
				assists1.setPadding(left, top, right, bottom);					
				row1.addView(assists1);
				
				assistsTable.addView(row1);
			}
			cur.close();
			sqliteDatabase.close();
		}catch (SQLiteException e) {
			e.printStackTrace();
		}
	}
}
