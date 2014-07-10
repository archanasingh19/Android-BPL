package com.soccer.bpl;

public class News {
	private String title;
	private String description;
	private String link;
	private String pubDate;
	private String img;
	
	public News(String title,String desc,String link,String date,String img){
		this.title=title;
		this.description=desc;
		this.link=link;
		this.pubDate=date;
		this.img =  img;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public String getLink(){
		return this.link;
	}
	
	public String getPubDate(){
		return this.pubDate;
	}
	
	public String getimg(){
		return this.img;
	}
}
