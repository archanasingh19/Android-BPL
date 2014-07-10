package com.soccer.bpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ResultsActivity extends Activity {

	private ListView sListView;
	private ArrayList<String> clubList;
	private ArrayList<Result> sArrayList;
	private static final String SCHEDULETABLE = "matchschedule";
	private static final String CLUBDETAILS = "clubdetails";
	private static final String SSCHEDULEID = "scheduleid";
	private static final String SDATE = "sdate";
	private static final String STIME = "stime";
	private static final String STEAM1ID = "team1id";
	private static final String STEAM2ID = "team2id";
	private static final String STEAM1NAME = "team1name";
	private static final String STEAM2NAME = "team2name";
	private static final String BESTPLAYER = "bestplayer";
	private static final String STEAM1LOGO = "club1logo";
	private static final String STEAM2LOGO = "club2logo";
	private static final String SVENUE = "venue";
	private static final String SCORE = "score";
	private static final String CLUBNAME ="clubname";
	private Calendar mCalendar;
	private static final String DATE_TIME_FORMAT = "EEEE dd MMMM yyyy";
	private static final String MONTH_FORMAT = "MMMM";
	private static final int DATE_PICKER_DIALOG=0;
	public static final int RESULT_INFO_DIALOG=1;
	private Context mContext;
	
	private Spinner clubSpinner;
	private ArrayAdapter<String> clubAdapter;
	private static final int FILTERBYDATE=0;
	private static final int FILTERBYCLUB=1;
	private static final int FILTERBYMONTH=2;
	
	private ImageButton dateSel1;
	private ImageButton teamSel1;
	public Result selectedResult;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.results_activity);
		mCalendar = Calendar.getInstance();
		mContext = this;
		//Initialize UI components
		sListView = (ListView) findViewById(R.id.resultslistView1);
		
		clubSpinner = (Spinner)findViewById(R.id.res_clubspinner);
		
		clubList=new ArrayList<String>();
		clubList.add(mContext.getString(R.string.all_clubs));
		populateClubList();
		clubAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item);
		for(int i=0;i<clubList.size();i++){
			clubAdapter.add(clubList.get(i));
		}
		clubAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		clubSpinner.setAdapter(clubAdapter);
		sArrayList = new ArrayList<Result>();
		//sArrayList = populateScheduleList(FILTERBYMONTH);
		
		sListView.setAdapter(new ResultAdapter(mContext, sArrayList));
			
		
		
		clubSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(position!=0){
					sArrayList = populateScheduleList(FILTERBYCLUB);
				}else if(position==0){
					sArrayList = populateScheduleList(FILTERBYMONTH);
				}
				sListView.setAdapter(new ResultAdapter(mContext, sArrayList));
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		sListView.setCacheColorHint(Color.TRANSPARENT);
		
		dateSel1 = (ImageButton)findViewById(R.id.dateSelBtn1);
		dateSel1.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				showDialog(DATE_PICKER_DIALOG); 			
			}
			
		});
		teamSel1 =(ImageButton)findViewById(R.id.teamSelBtn1);
		teamSel1.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				clubSpinner.performClick();		
			}
		});
	}
	
	@Override
	protected Dialog onCreateDialog(int id) { 
		switch(id) {
		case DATE_PICKER_DIALOG: 
			return showDatePicker();
		case RESULT_INFO_DIALOG:
			View resultDetailsView = getLayoutInflater().inflate(R.layout.goals,null);
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setView(resultDetailsView);
		
			SimpleDateFormat dfaq = new SimpleDateFormat("EEEE dd MMM yyyy   HH:mm");
			dfaq.setTimeZone(TimeZone.getTimeZone("GMT"));
			String dateStr =  selectedResult.getsdate()+" "+selectedResult.getstime();
			Log.d("date string   ", selectedResult.getsdate());
			Log.d("date string   ", dateStr);
			Log.d("date string   ", dateStr);
			Date dt=new Date();
			try {
				dt = dfaq.parse(dateStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
			destFormat.setTimeZone(TimeZone.getDefault());
			String title = destFormat.format(dt);
			
			dialog.setTitle(title);
			//dialog.setc
			dialog.setNegativeButton(mContext.getString(R.string.close), new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
				
					dialog.cancel();
				}
				
			});
			return dialog.create();
		}
		return super.onCreateDialog(id);
	}
	
	@Override
    public void onPrepareDialog(int id,Dialog dialog){
    	switch (id) {
		case RESULT_INFO_DIALOG:
			AlertDialog resDialog =(AlertDialog)dialog;
			SimpleDateFormat dfaq = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm");
			dfaq.setTimeZone(TimeZone.getTimeZone("GMT"));
			String dateStr =  selectedResult.getsdate()+" "+selectedResult.getstime();
			Date dt= new Date();
			try {
				dt = dfaq.parse(dateStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
			destFormat.setTimeZone(TimeZone.getDefault());
			String title = destFormat.format(dt);
			
			dialog.setTitle(title);
			TextView dttime = (TextView)resDialog.findViewById(R.id.resinfodttime);
			//dttime.setText(selectedResult.getsdate()+" - "+selectedResult.getstime());
			dttime.setVisibility(View.GONE);
			TextView venue = (TextView)resDialog.findViewById(R.id.resinfovenue);
			venue.setText(selectedResult.getsvenue());
			//venue.setVisibility(View.GONE);
			TextView team1Name = (TextView)resDialog.findViewById(R.id.resinfoteam1Name);
			team1Name.setText(selectedResult.getsteam1name());
			TextView team2Name = (TextView)resDialog.findViewById(R.id.resinfoTeam2Name);
			team2Name.setText(selectedResult.getsteam2name());
			ImageButton team1_img = (ImageButton)resDialog.findViewById(R.id.resinfoteam1logo);
			int resID1 = mContext.getResources().getIdentifier(selectedResult.getteam1logo(), "drawable",mContext.getPackageName());
			team1_img.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),resID1));
			team1_img.setTag(selectedResult.getsteam1id());
			TextView score = (TextView)resDialog.findViewById(R.id.resinfoTeamscore);
			score.setText(selectedResult.getScore());
			ImageButton team2_img = (ImageButton)resDialog.findViewById(R.id.resinfoteam2logo);
			int resID2 = mContext.getResources().getIdentifier(selectedResult.getteam2logo(), "drawable",mContext.getPackageName());
			team2_img.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),resID2));
			team2_img.setTag(selectedResult.getsteam2id());
			TextView scorerTeam1 = (TextView)resDialog.findViewById(R.id.resinfoteam1scorers);
			scorerTeam1.setText(selectedResult.getTeamGoals("1"));
			TextView scorerTeam2 = (TextView)resDialog.findViewById(R.id.resinfoteam2scorers);
			scorerTeam2.setText(selectedResult.getTeamGoals("2"));
			
		}
    }
	private DatePickerDialog showDatePicker() {
		DatePickerDialog datePicker = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() { 
			public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
				mCalendar.set(Calendar.YEAR, year);
				mCalendar.set(Calendar.MONTH, monthOfYear);
				mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				sArrayList = populateScheduleList(FILTERBYDATE);
				if(sArrayList.size()==0){
					Toast.makeText(mContext, mContext.getString(R.string.no_matches), Toast.LENGTH_SHORT).show();
				}
				sListView.setAdapter(new ResultAdapter(mContext, sArrayList));
			}
		}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
		mCalendar.get(Calendar.DAY_OF_MONTH)); 

		return datePicker; 
	}


	private ArrayList<Result> populateScheduleList(int filterType) {
		sArrayList = new ArrayList<Result>();
		String queryString="";
		switch (filterType) {
		case FILTERBYDATE:
			SimpleDateFormat dtFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
			String setdate = dtFormat.format(mCalendar.getTime());
			queryString = " select tb1.*, club2.clubname as team2name, club2.logo as club2logo from ( "+
					"select sch.scheduleid as scheduleid, sch.sdate as sdate, sch.stime as stime, sch.team1id as team1id, club.clubname as team1name, "+
					"sch.team2id as team2id, sch.venue as venue, sch.bestplayer as bestplayer ,sch.score as score, club.logo as club1logo "+
					"from " + SCHEDULETABLE + "  as sch inner join " + CLUBDETAILS + " as club where sch.team1id = club.clubid "+
					" and sch.score !='') as tb1 inner join " + CLUBDETAILS + " club2 where tb1.team2id = club2.clubid and tb1.sdate='" + setdate + "' order by scheduleid desc;";

			break;
		case FILTERBYCLUB:
			String clubName = clubSpinner.getSelectedItem().toString();
			queryString = "  select * from (select tb1.*, club2.clubname as team2name, club2.logo as club2logo from "+
							  " (select sch.scheduleid as scheduleid, sch.sdate as sdate, sch.stime as stime, sch.team1id as team1id, "+
							  " club.clubname as team1name , sch.team2id as team2id, sch.venue as venue, sch.bestplayer as bestplayer ,sch.score as score, "+
							    "club.logo as club1logo from " + SCHEDULETABLE + " as sch inner join  " + CLUBDETAILS + "  as club "+ 
							    " where sch.team1id = club.clubid and  sch.score !='') as tb1 "+
							    "inner join  " + CLUBDETAILS + " club2 where tb1.team2id = club2.clubid ) as tbl2 where "+
							     "tbl2.team1name like '%"+clubName+"%' or tbl2.team2name like '%"+clubName+"%' order by scheduleid desc;";
			break;
		case FILTERBYMONTH:
			SimpleDateFormat dtFormat1 = new SimpleDateFormat(MONTH_FORMAT);
			String monthName = dtFormat1.format(mCalendar.getTime());
			Date date = mCalendar.getTime();
			date.setMonth(date.getMonth()-1);
			String monthName2 = dtFormat1.format(date);
			
			queryString = " select tb1.*, club2.clubname as team2name, club2.logo as club2logo from ( "+
					"select sch.scheduleid as scheduleid, sch.sdate as sdate, sch.stime as stime, sch.team1id as team1id, club.clubname as team1name, "+
					"sch.team2id as team2id, sch.venue as venue, sch.bestplayer as bestplayer ,sch.score as score, club.logo as club1logo "+
					"from " + SCHEDULETABLE + "  as sch inner join " + CLUBDETAILS + " as club where sch.team1id = club.clubid "+
					" and sch.score !='') as tb1 inner join " + CLUBDETAILS + " club2 where tb1.team2id = club2.clubid and (tb1.sdate like'%" + monthName + "%' or tb1.sdate like'%" + monthName2 + "%' ) order by scheduleid desc;";
			
		}		
		// First we need to make contact with the database we have created using the DbHelper class
		soccerDBHelper opensoccerHelperClass = new soccerDBHelper(this);
		// Then we need to get a readable database
		opensoccerHelperClass.dbHelper.createDataBase();
		try{
			SQLiteDatabase sqliteDatabase = opensoccerHelperClass.dbHelper.openDB();
			Cursor cur = sqliteDatabase.rawQuery(queryString, null);
			while(cur.moveToNext()){
				int sscheduleid = cur.getInt(cur.getColumnIndex(SSCHEDULEID));
				String sdate = cur.getString(cur.getColumnIndex(SDATE));
				String stime = cur.getString(cur.getColumnIndex(STIME));
				String steam1id = cur.getString(cur.getColumnIndex(STEAM1ID));
				String steam2id = cur.getString(cur.getColumnIndex(STEAM2ID));
				String steam1name = cur.getString(cur.getColumnIndex(STEAM1NAME));
				String steam2name = cur.getString(cur.getColumnIndex(STEAM2NAME));
				String bestplayer = cur.getString(cur.getColumnIndex(BESTPLAYER));
				String slocation = cur.getString(cur.getColumnIndex(SVENUE));
				String steam1logo = cur.getString(cur.getColumnIndex(STEAM1LOGO));
				String steam2logo = cur.getString(cur.getColumnIndex(STEAM2LOGO));
				String scr = cur.getString(cur.getColumnIndex(SCORE));
				String goals = "";
				String score = "";
				if(scr.contains("@@@")){
					goals = scr.split("@@@")[1];
					goals = goals.replace("|||", "###");
					score = scr.split("@@@")[0];
				}else{
					score=scr;
				}
				
				Result res = new Result(mContext,sscheduleid, sdate, stime, steam1id, steam2id, steam1name, steam2name, slocation, bestplayer, steam1logo, steam2logo,score,goals);
				sArrayList.add(res);
			}
			cur.close();
			sqliteDatabase.close();
		}catch (SQLiteException e) {
			e.printStackTrace();
		}		
		return sArrayList;
	}

	private void populateClubList(){
		soccerDBHelper opensoccerHelperClass = new soccerDBHelper(this);
		opensoccerHelperClass.dbHelper.createDataBase();
		try{
			SQLiteDatabase sqliteDatabase = opensoccerHelperClass.dbHelper.openDB();
			String queryString = " select "+CLUBNAME+" from "+ CLUBDETAILS+" ;";
			Cursor cur = sqliteDatabase.rawQuery(queryString, null);
			while(cur.moveToNext()){
				String clubName = cur.getString(cur.getColumnIndex(CLUBNAME));
				clubName =clubName.replace(" Football Club", "").replace(" Association", "");
				clubList.add(clubName);
			}
			cur.close();
			sqliteDatabase.close();
		}catch (SQLiteException e) {
			e.printStackTrace();
		}
	}
	public class ResultAdapter extends BaseAdapter {
		private final ArrayList<Result> items;
		private final LayoutInflater inflator;
		private Context ctx;
		
		public static final String CLUBID="clubid";
		
		public ResultAdapter(Context cntxt,ArrayList<Result> item ){
			inflator = (LayoutInflater)cntxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ctx = cntxt;
			items=item;
		}
		
		public int getCount() {
			return items.size();
		}

		
		public Object getItem(int pos) {
			return items.get(pos);
		}

		
		public long getItemId(int pos) {
			return pos+1;
		}

		
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view = inflator.inflate(R.layout.resultslistitem, null);
			TextView dateTime_tv = (TextView)view.findViewById(R.id.rdatetime);
			TextView team1_tv =(TextView)view.findViewById(R.id.rtxtteam1);
			TextView team2_tv =(TextView)view.findViewById(R.id.rtxtteam2);
			TextView stadium_tv =(TextView)view.findViewById(R.id.rtxtstadium);
			ImageView team1_img = (ImageView)view.findViewById(R.id.rimgteam1);
			ImageView team2_img = (ImageView)view.findViewById(R.id.rimgteam2);
			TextView score_tv = (TextView)view.findViewById(R.id.score);
			Result res = (Result)getItem(arg0);
			dateTime_tv.setText(res.getstime() + " - " + res.getsdate());
			team1_tv.setText(res.getsteam1name().trim());
			team2_tv.setText(res.getsteam2name().trim());
			stadium_tv.setText(res.getsvenue().trim());
			score_tv.setText(res.getScore());
			score_tv.setTag(res);
			score_tv.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					selectedResult = (Result)v.getTag();
					showDialog(RESULT_INFO_DIALOG);
				}
			});
			int resID1 = ctx.getResources().getIdentifier(res.getteam1logo(), "drawable",ctx.getPackageName());
			team1_img.setImageBitmap(BitmapFactory.decodeResource(ctx.getResources(),resID1));
			team1_img.setTag(res.getsteam1id());
			int resID2 = ctx.getResources().getIdentifier(res.getteam2logo(), "drawable",ctx.getPackageName());
			team2_img.setImageBitmap(BitmapFactory.decodeResource(ctx.getResources(),resID2));
			team2_img.setTag(res.getsteam2id());
			view.setDrawingCacheBackgroundColor(Color.TRANSPARENT);
			team1_img.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					getClubInfo(Integer.parseInt((v.getTag().toString())));
				}
			});
			team2_img.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					getClubInfo(Integer.parseInt((v.getTag().toString())));
				}
			});
			
			return view;
		}
		
		public void getClubInfo(int clubid){
			Intent intent = new Intent(ctx, ClubDetailsActivity.class);
			intent.putExtra(CLUBID, clubid);
			ctx.startActivity(intent);
		}
	}


}
