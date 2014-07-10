package com.soccer.bpl;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class Club {
	private int clubid;
	private String abbr;
	private String clubName;
	private String nickName;
	private String founded;
	private String manager;
	private String homeground;
	private String titles;
	private String bestrank;
	private String wins;
	private String draw;
	private String lost;
	private String points;
	private String website;
	private String sponsor;
	private String logo;
	private ArrayList<Player> playersList;
	private static final String PLAYERDETAILS="playerdetails";
	private static final String CLUBID="clubid";
	private static final String PLAYERID="playerid";
	private static final String PLAYERNAME="pname";
	private static final String POSITION="position";
	private static final String NATIONALITY="nationality";
	private static final String DOB="dob";
	private static final String OTHERS="others";
	private static final String PIC="pic";
	private Context mContext;
	
	public Club(Context ctx,int id, String abbr,String clubname,String nickname,String founded,
			String manager,String homeground,
			String titles,String bestrank,String wins,String draw,String lost,String points,
			String website,String sponsor,String logo){
		this.mContext=ctx;
		this.clubid=id;
		this.abbr=abbr;
		this.clubName=clubname;
		this.nickName=nickname;
		this.founded=founded;
		this.manager=manager;
		this.homeground=homeground;
		this.titles=titles;
		this.bestrank=bestrank;
		this.wins=wins;
		this.draw=draw;
		this.lost=lost;
		this.points=points;
		this.website=website;
		this.sponsor=sponsor;
		this.logo=logo;
		this.playersList=new ArrayList<Player>();
		populatePlayerList();
		Log.d("Clubdetails   ", ((Integer)this.playersList.size()).toString());
	}
	
	public int getClubId(){
		return this.clubid >-1?this.clubid:-1;
	}
	
	public String getClubName(){
		return this.clubName.equals("")?"":this.clubName;
	}
	
	public String getNickName(){
		return this.nickName.equals("")?"":this.nickName;
	}
	
	public String getFounded(){
		return this.founded.equals("")?"":this.founded;
	}
	
	public String getManager(){
		return this.manager.equals("")?"":this.manager;
	}
	
	public String getHomeground(){
		return this.homeground.equals("")?"":this.homeground;
	}
	
	public String getTitles(){
		return this.titles.equals("")?"":this.titles;
	}
	
	public String getBestrank(){
		return this.bestrank.equals("")?"":this.bestrank;
	}
	
	public String getWins(){
		return this.wins.equals("")?"":this.wins;
	}
	
	public String getDraw(){
		return this.draw.equals("")?"":this.draw;
	}
	
	public String getPoints(){
		return this.points.equals("")?"":this.points;
	}
	
	public String getLosts(){
		return this.lost.equals("")?"":this.lost;
	}
	
	public String getWebsite(){
		return this.website.equals("")?"":this.website;
	}
	
	public String getSponsor(){
		return this.sponsor.equals("")?"":this.sponsor;
	}
	
	public String getLogo(){
		return this.logo.equals("")?"":this.logo;
	}
	
	public String getAbbr(){
		return this.abbr.equals("")?"":this.abbr;
	}
	
	private void populatePlayerList(){
		soccerDBHelper opensoccerHelperClass = new soccerDBHelper(mContext);
		// Then we need to get a readable database
		opensoccerHelperClass.dbHelper.createDataBase();
		String queryString="select * from "+PLAYERDETAILS+" where "+CLUBID+"="+this.clubid;
		try{
			SQLiteDatabase sqliteDatabase = opensoccerHelperClass.dbHelper.openDB();
			Cursor cur = sqliteDatabase.rawQuery(queryString, null);
			while(cur.moveToNext()){
				int playerid = cur.getInt(cur.getColumnIndex(PLAYERID));
				String pname =cur.getString(cur.getColumnIndex(PLAYERNAME));
				int clbid = cur.getInt(cur.getColumnIndex(CLUBID));
				String position =cur.getString(cur.getColumnIndex(POSITION));
				String nationality =cur.getString(cur.getColumnIndex(NATIONALITY));
				String dob =cur.getString(cur.getColumnIndex(DOB));
				String others =cur.getString(cur.getColumnIndex(OTHERS));
				String pic =cur.getString(cur.getColumnIndex(PIC));
				pname = pname==null?"":pname;
				position = position==null?"":position;
				nationality = nationality==null?"":nationality;
				dob = dob==null?"":dob;
				others = others==null?"":others;
				pic = pic==null?"":pic;
				Player player = new Player(playerid, pname, clbid, position, nationality, dob, others, pic);
				this.playersList.add(player);
			}
			cur.close();
			sqliteDatabase.close();
			}catch (SQLiteException e) {
				e.printStackTrace();
			}	
	}
	
	public ArrayList<String> getPlayerList(){
		ArrayList<String> names=new ArrayList<String>();
		for(int i=0;i<this.playersList.size();i++){
			Player pl = this.playersList.get(i);
			names.add(pl.getPlayerName());
		}
		return names;
	}
}
