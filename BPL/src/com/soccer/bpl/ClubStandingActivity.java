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

public class ClubStandingActivity extends Activity {

	private static final String CLUBSTANDINGSTABLE="clubstandings";
	private static final String CLUBDETAILSTABLE="clubdetails";
	private static final String POSITION="position";
	private static final String ABBR="abbr";
	private static final String PLAYED="played";
	private static final String WINS="win";
	private static final String DRAWS="draw";
	private static final String LOSTS="lost";
	private static final String POINTS="points";
	private Context mContext;
	private TableLayout standingTable;
	private static final float textSizeData=14;
	private static final int left=5,top=10,right=5,bottom=10;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clubstandings);
		mContext=this;
		
		standingTable = (TableLayout)findViewById(R.id.clubstandingtable);
		standingTable.setStretchAllColumns(true);  
		standingTable.setShrinkAllColumns(true);
		populateClubStandings();
		
	}
	public void populateClubStandings(){
			
		soccerDBHelper opensoccerHelperClass = new soccerDBHelper(this);
		opensoccerHelperClass.dbHelper.createDataBase();
			
		String queryString="select * from((select * from "+CLUBSTANDINGSTABLE+" )as t1 inner join "+
		"(select abbr,clubid from "+ CLUBDETAILSTABLE+" )as t2 on t1.teamid=t2.clubid ) order by CAST(position as integer)";
		
		try{
			SQLiteDatabase sqliteDatabase = opensoccerHelperClass.dbHelper.openDB();
			Cursor cur = sqliteDatabase.rawQuery(queryString, null);
			
			while(cur.moveToNext()){
				
				String position = cur.getString(cur.getColumnIndex(POSITION));
				String abbr = cur.getString(cur.getColumnIndex(ABBR));
				String played = cur.getString(cur.getColumnIndex(PLAYED));
				String wins = cur.getString(cur.getColumnIndex(WINS));
				String draws = cur.getString(cur.getColumnIndex(DRAWS));
				String losts = cur.getString(cur.getColumnIndex(LOSTS));
				String points = cur.getString(cur.getColumnIndex(POINTS));
				
				TableRow row1 = new TableRow(mContext);
				TextView position1 = new TextView(mContext);
				position1.setText(position);
				position1.setGravity(Gravity.CENTER);
				position1.setTextAppearance(mContext, R.style.dataText);
				position1.setTextSize(textSizeData);	
				position1.setPadding(left, top, right, bottom);
				
								
				row1.addView(position1);
				TextView club1 = new TextView(mContext);
				club1.setText(abbr);
				club1.setGravity(Gravity.LEFT);
				club1.setTextAppearance(mContext, R.style.dataText);
				club1.setTextSize(textSizeData);	
				club1.setPadding(left, top, right, bottom);
				
				row1.addView(club1);
				TextView played1 = new TextView(mContext);
				played1.setText(played);
				played1.setGravity(Gravity.CENTER);
				played1.setTextAppearance(mContext, R.style.dataText);
				played1.setTextSize(textSizeData);	
				played1.setPadding(left, top, right, bottom);
				
				row1.addView(played1);
				TextView wins1 = new TextView(mContext);
				wins1.setText(wins);
				wins1.setGravity(Gravity.CENTER);
				wins1.setTextAppearance(mContext, R.style.dataText);
				wins1.setTextSize(textSizeData);	
				wins1.setPadding(left, top, right, bottom);
				
				row1.addView(wins1);
				TextView draw1 = new TextView(mContext);
				draw1.setText(draws);
				draw1.setGravity(Gravity.CENTER);
				draw1.setTextAppearance(mContext, R.style.dataText);
				draw1.setTextSize(textSizeData);	
				draw1.setPadding(left, top, right, bottom);
				
				row1.addView(draw1);
				TextView lost1 = new TextView(mContext);
				lost1.setText(losts);
				lost1.setGravity(Gravity.CENTER);
				lost1.setTextAppearance(mContext, R.style.dataText);
				lost1.setTextSize(textSizeData);	
				lost1.setPadding(left, top, right, bottom);
				
				row1.addView(lost1);
				TextView points1 = new TextView(mContext);
				points1.setText(points);
				points1.setGravity(Gravity.CENTER);
				points1.setTextAppearance(mContext, R.style.dataText);
				points1.setTextSize(textSizeData);	
				points1.setPadding(left, top, right, bottom);
				
				row1.addView(points1);
				standingTable.addView(row1);
			}
			cur.close();
			sqliteDatabase.close();
		}catch (SQLiteException e) {
			e.printStackTrace();
		}
	}
}
