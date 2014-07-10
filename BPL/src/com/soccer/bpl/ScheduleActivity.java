package com.soccer.bpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ScheduleActivity extends Activity {

	private ListView sListView;
	private ArrayList<String> clubList;
	private ArrayList<Schedule> sArrayList;
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
	private static final String CLUBNAME ="clubname";
	private Calendar mCalendar;
	private static final String DATE_TIME_FORMAT = "EEEE dd MMMM yyyy";
	private static final String MONTH_FORMAT = "MMMM";
	private static final int DATE_PICKER_DIALOG=0;
	private Context mContext;
	private Spinner clubSpinner;
	private ArrayAdapter<String> clubAdapter;
	private static final int FILTERBYDATE=0;
	private static final int FILTERBYCLUB=1;
	private static final int FILTERBYMONTH=2;
	private ImageButton dateSel;
	private ImageButton teamSel;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);
		mCalendar = Calendar.getInstance();
		mContext = this;
		//Initialize UI components
		sListView = (ListView) findViewById(R.id.schduleListView);
		sListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> av,View view, int index,long arg3){
				Log.d("soccer", "onclick");
			}
		});
		sArrayList = new ArrayList<Schedule>();
		clubSpinner = (Spinner)findViewById(R.id.clubspinner);
		clubList=new ArrayList<String>();
		clubList.add(mContext.getString(R.string.all_clubs));
		populateClubList();
		clubAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item);
		
		for(int i=0;i<clubList.size();i++){
			clubAdapter.add(clubList.get(i));
		}
		clubAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		clubSpinner.setAdapter(clubAdapter);
		clubSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(position!=0){
					sArrayList = populateScheduleList(FILTERBYCLUB);
				}else if(position==0){
					sArrayList = populateScheduleList(FILTERBYMONTH);
				}
				sListView.setAdapter(new ScheduleAdapter(mContext, sArrayList));
			}
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
					
		
		sListView.setCacheColorHint(Color.TRANSPARENT);
		//sArrayList = populateScheduleList(FILTERBYMONTH);
		
		sListView.setAdapter(new ScheduleAdapter(mContext, sArrayList));
		
		dateSel = (ImageButton)findViewById(R.id.dateSelBtn);
		dateSel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				showDialog(DATE_PICKER_DIALOG); 			
			}
			
		});
		teamSel =(ImageButton)findViewById(R.id.teamSelBtn);
		teamSel.setOnClickListener(new View.OnClickListener() {
			
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
		}
		return super.onCreateDialog(id);
	}
	private DatePickerDialog showDatePicker() {
		DatePickerDialog datePicker = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() { 
			public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
				mCalendar.set(Calendar.YEAR, year);
				mCalendar.set(Calendar.MONTH, monthOfYear);
				mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				
				sArrayList = populateScheduleList(FILTERBYDATE);
				sListView.setAdapter(new ScheduleAdapter(mContext, sArrayList));
				if(sArrayList.size()==0){
					Toast.makeText(mContext, mContext.getString(R.string.no_matches), Toast.LENGTH_SHORT).show();
				}
			}
		}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
		mCalendar.get(Calendar.DAY_OF_MONTH)); 

		return datePicker; 
	}


	private ArrayList<Schedule> populateScheduleList(int filterType) {
		sArrayList = new ArrayList<Schedule>();
		String queryString="";
		switch (filterType) {
		case FILTERBYDATE:
			SimpleDateFormat dtFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
			String setdate = dtFormat.format(mCalendar.getTime());
			queryString = " select tb1.*, club2.clubname as team2name, club2.logo as club2logo from ( "+
					"select sch.scheduleid as scheduleid, sch.sdate as sdate, sch.stime as stime, sch.team1id as team1id, club.clubname as team1name, "+
					"sch.team2id as team2id, sch.venue as venue, sch.bestplayer as bestplayer , club.logo as club1logo "+
					"from " + SCHEDULETABLE + "  as sch inner join " + CLUBDETAILS + " as club where sch.team1id = club.clubid "+
					" and sch.score ='') as tb1 inner join " + CLUBDETAILS + " club2 where tb1.team2id = club2.clubid and tb1.sdate='" + setdate + "' order by scheduleid ;";

			break;
		case FILTERBYCLUB:
			String clubName = clubSpinner.getSelectedItem().toString();
			queryString = "  select * from (select tb1.*, club2.clubname as team2name, club2.logo as club2logo from "+
					  " (select sch.scheduleid as scheduleid, sch.sdate as sdate, sch.stime as stime, sch.team1id as team1id, "+
					  " club.clubname as team1name , sch.team2id as team2id, sch.venue as venue, sch.bestplayer as bestplayer ,sch.score as score, "+
					    "club.logo as club1logo from " + SCHEDULETABLE + " as sch inner join  " + CLUBDETAILS + "  as club "+ 
					    " where sch.team1id = club.clubid and  sch.score ='') as tb1 "+
					    "inner join  " + CLUBDETAILS + " club2 where tb1.team2id = club2.clubid ) as tbl2 where "+
					     "tbl2.team1name like '%"+clubName+"%' or tbl2.team2name like '%"+clubName+"%'; order by scheduleid ";
			break;
		case FILTERBYMONTH:
			SimpleDateFormat dtFormat1 = new SimpleDateFormat(MONTH_FORMAT);
			String monthName = dtFormat1.format(mCalendar.getTime());
			Date date = mCalendar.getTime();
			date.setMonth(date.getMonth()+1);
			String monthName2 = dtFormat1.format(date);
			queryString = " select tb1.*, club2.clubname as team2name, club2.logo as club2logo from ( "+
					"select sch.scheduleid as scheduleid, sch.sdate as sdate, sch.stime as stime, sch.team1id as team1id, club.clubname as team1name, "+
					"sch.team2id as team2id, sch.venue as venue, sch.bestplayer as bestplayer , club.logo as club1logo "+
					"from " + SCHEDULETABLE + "  as sch inner join " + CLUBDETAILS + " as club where sch.team1id = club.clubid "+
					" and sch.score ='') as tb1 inner join " + CLUBDETAILS + " club2 where tb1.team2id = club2.clubid and (tb1.sdate like'%" + monthName + "%' or tb1.sdate like'%" + monthName2 + "%' )order by scheduleid ;";
			
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
				Schedule sch = new Schedule(sscheduleid, sdate, stime, steam1id, steam2id, steam1name, steam2name, slocation, bestplayer, steam1logo, steam2logo);
				sArrayList.add(sch);
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



}
