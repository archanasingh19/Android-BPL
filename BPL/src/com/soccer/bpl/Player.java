package com.soccer.bpl;

public class Player {
	private int playerid;
	private String pname;
	private int clubid;
	private String position;
	private String nationality;
	private String dob;
	private String others;
	private String pic;
	
	public Player(int id,String name ,int clubid,String position ,String nationality , 
			String dob,String others,String pic){
		this.playerid=id>-1?id:-1;
		this.pname=name.equals("")?"":name;
		this.clubid=clubid>-1?id:-1;
		this.position=position.equals("")?"":position;
		this.nationality=nationality.equals("")?"":nationality;
		this.dob = dob.equals("")?"":dob;
		this.others =others.equals("")?"":others;
		this.pic=pic.equals("")?"":pic;		
	}
	
	public int getPlayerId(){
		return this.playerid >-1?this.playerid:-1;
	}
	
	public String getPlayerName(){
		return this.pname.equals("")?"":this.pname;
	}
	
	public int getClubId(){
		return this.clubid >-1?this.clubid:-1;
	}
	
	public String getPosition(){
		return this.position.equals("")?"":this.position;
	}
	
	public String getNationality(){
		return this.nationality.equals("")?"":this.nationality;
	}
	
	public String getDOB(){
		return this.dob.equals("")?"":this.dob;
	}
	
	public String getOthers(){
		return this.others.equals("")?"":this.others;
	}
	
	public String getPic(){
		return this.pic.equals("")?"":this.pic;
	}
}
