package com.soccer.bpl;

public class Schedule {
	private int scheduleid;
	private String sdate;
	private String stime;
	private String steam1id;
	private String steam2id;
	private String steam1name;
	private String steam2name;
	private String svenue;
	private String bestplayer;
	private String steam1logo;
	private String steam2logo;

	public Schedule(int Scheduleid, String Sdate, String Stime,
			String Steam1id, String Steam2id, String Steam1name,
			String Steam2name, String Svenue, String Sbestplayer,
			String Steam1logo, String Steam2logo) {
		this.scheduleid = Scheduleid;
		this.sdate = Sdate;
		this.stime = Stime;
		this.steam1id = Steam1id;
		this.steam2id = Steam2id;
		this.steam1name = Steam1name.replace(" Football Club", "").replace(" Association", "");
		this.steam2name = Steam2name.replace(" Football Club", "").replace(" Association", "");
		this.svenue = Svenue;
		this.bestplayer = Sbestplayer;
		this.steam1logo = Steam1logo;
		this.steam2logo = Steam2logo;
	}

	@Override
	public String toString() {
		return "";
	}

	public int getscheduleid() {
		return this.scheduleid;
	}

	public String getsdate() {
		return this.sdate == null ? "" : this.sdate;
	}

	public String getstime() {
		return this.stime == null ? "" : this.stime;
	}

	public String getsteam1id() {
		return this.steam1id == null ? "" : this.steam1id;
	}

	public String getsteam2id() {
		return this.steam2id == null ? "" : this.steam2id;
	}

	public String getsteam1name() {
		return this.steam1name == null ? "" : this.steam1name;
	}

	public String getsteam2name() {
		return this.steam2name == null ? "" : this.steam2name;
	}

	public String getsvenue() {
		return this.svenue == null ? "" : this.svenue;
	}

	public String getbestplayer() {
		return this.bestplayer == null ? "" : this.bestplayer;
	}

	public String getteam1logo() {
		return this.steam1logo == null ? "" : this.steam1logo;
	}

	public String getteam2logo() {
		return this.steam2logo == null ? "" : this.steam2logo;
	}

}
