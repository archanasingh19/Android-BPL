package com.soccer.bpl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;


public class Result extends Schedule {
	private String score;
	private String goals;
	private String team1Gls;
	private String team2Gls;
	private Context mContext;
	private static final String PLAYERDETAILS="playerdetails";
	public Result(Context mContext,int Scheduleid, String Sdate, String Stime, String Steam1id,
			String Steam2id, String Steam1name, String Steam2name,
			String Svenue, String Sbestplayer, String Steam1logo,
			String Steam2logo,String score,String goals) {
		super(Scheduleid, Sdate, Stime, Steam1id, Steam2id, Steam1name, Steam2name,
				Svenue, Sbestplayer, Steam1logo, Steam2logo);
		this.mContext=mContext;
		this.score=score;
		this.goals = goals;
		if(goals.contains("###")){
			String[] goalsArr = this.goals.split("###");
			if(goalsArr.length>=2){
				team1Gls = goalsArr[0].replace("&lt;", "<").replace("&gt;",">");
				team2Gls = goalsArr[1].replace("&lt;", "<").replace("&gt;",">");
			}else{
				team1Gls="";
				team2Gls="";
			}
		}else{
			team1Gls="";
			team2Gls="";
		}
	}
	
	public String getScore(){
		return this.score;
	}
	
	public String getTeamGoals(String team){
		String goalStr="";
		String[] goalArr={};
		if(team.equals("1")){
			if(team1Gls.trim().equalsIgnoreCase("")||team1Gls.equalsIgnoreCase("0")){
				goalStr="";
			}else{
				goalArr=team1Gls.split(";");
			}
		}else{
			if(team2Gls.trim().equalsIgnoreCase("")||team2Gls.equalsIgnoreCase("0")){
				goalStr="";
			}else{
				goalArr=team2Gls.split(";");
			}
		}
		Log.d("goalArr ", ((Integer)goalArr.length).toString());
		if(goalArr.length ==0){
			goalStr="   ";
		}
		for(int i=0;i<goalArr.length;i++){
			String str = goalArr[i].replace("(","").replace(")","" );
			String[] splitArr = str.split(",");
			String pid = "";
			String tm ="";
			if(splitArr.length>=2){
				pid=splitArr[0];
				tm = splitArr[1].replace("<", "(").replace(">", ")");
			}
			String pname="";
			if(pid.matches("-?\\d+")){
				if(pid.equalsIgnoreCase("-1")){
					pname = "TBC";
				}else if(!pid.equalsIgnoreCase("")){
					pname = getPlayerName(pid);
				}
			}
			else {
				pname=pid;
			}
			goalStr=goalStr.concat(pname+" "+ tm+" \n ");
		}
		return goalStr;
	}
	
	private String getPlayerName(String id){
		String name="TBC";
		soccerDBHelper opensoccerHelperClass = new soccerDBHelper(mContext);
		opensoccerHelperClass.dbHelper.createDataBase();
		try{
			SQLiteDatabase sqliteDatabase = opensoccerHelperClass.dbHelper.openDB();
			String queryString="select pname from "+PLAYERDETAILS+" where playerid=\""+id+"\";";
			Cursor cur = sqliteDatabase.rawQuery(queryString, null);
			while(cur.moveToNext()){
				 name = cur.getString(cur.getColumnIndex("pname"));
			}
			cur.close();
			sqliteDatabase.close();
		}catch (SQLiteException e) {
			e.printStackTrace();
		}
		return name;
	}
}
