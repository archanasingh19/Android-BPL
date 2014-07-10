package com.soccer.bpl;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TopScorerActivity extends Activity {
	private static final String PLAYERDETAILS="playerdetails";
	private static final String CLUBDETAILS="clubdetails";
	private static final String MOSTGOALS="mostgoals";
	private static final String PLAYERNAME="name";
	private static final String CLUBNAME="clubname";
	private static final String GOALS="goals";
	private Context mContext;
	private TableLayout scorerTable;
	private static final float textSizeData=14;
	private static final int left=1,top=10,right=0,bottom=10;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topscrorer);
		mContext=this;
		
		scorerTable = (TableLayout)findViewById(R.id.topscorertable);
		scorerTable.setStretchAllColumns(true);  
		scorerTable.setShrinkAllColumns(true);
		populateScorers();
		
	}
	
	public void populateScorers(){
		
	
		soccerDBHelper opensoccerHelperClass = new soccerDBHelper(this);
		opensoccerHelperClass.dbHelper.createDataBase();
			
		String queryString="select t1.name as name,t1.goals as goals,club.clubname as " +
				"clubname from (select  pl.pname as name,gls.goals as goals ,pl.clubid as " +
				"clubid from "+ MOSTGOALS+" as gls inner join "+PLAYERDETAILS+" as pl where"
				+" gls.playerid = pl.playerid) as t1 inner join "+CLUBDETAILS+" as club where club.clubid=t1.clubid order by CAST(goals as integer) desc;";
		
		try{
			SQLiteDatabase sqliteDatabase = opensoccerHelperClass.dbHelper.openDB();
			Cursor cur = sqliteDatabase.rawQuery(queryString, null);
			
			while(cur.moveToNext()){
				String player = cur.getString(cur.getColumnIndex(PLAYERNAME));
				String club = cur.getString(cur.getColumnIndex(CLUBNAME));
				String goals = cur.getString(cur.getColumnIndex(GOALS));
								
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
				
				TextView goals1 = new TextView(mContext);
				goals1.setText(goals);
				goals1.setGravity(Gravity.CENTER);
				goals1.setTextAppearance(mContext, R.style.dataText);
				goals1.setTextSize(textSizeData);	
				goals1.setPadding(left, top, right, bottom);
				row1.addView(goals1);
				
				scorerTable.addView(row1);
			}
			cur.close();
			sqliteDatabase.close();
		}catch (SQLiteException e) {
			e.printStackTrace();
		}
	}
}
