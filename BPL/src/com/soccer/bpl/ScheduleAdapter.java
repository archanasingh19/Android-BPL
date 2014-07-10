package com.soccer.bpl;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ScheduleAdapter extends BaseAdapter {
	private final ArrayList<Schedule> items;
	private final LayoutInflater inflator;
	private Context ctx;
	
	public static final String CLUBID="clubid";
	
	public ScheduleAdapter(Context cntxt,ArrayList<Schedule> item ){
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
		View view = inflator.inflate(R.layout.simplelistview, null);
		TextView dateTime_tv = (TextView)view.findViewById(R.id.sdatetime);
		TextView team1_tv =(TextView)view.findViewById(R.id.txtteam1);
		TextView team2_tv =(TextView)view.findViewById(R.id.txtteam2);
		TextView stadium_tv =(TextView)view.findViewById(R.id.txtstadium);
		ImageView team1_img = (ImageView)view.findViewById(R.id.imgteam1);
		ImageView team2_img = (ImageView)view.findViewById(R.id.imgteam2);
		Schedule sch = (Schedule)getItem(arg0);
		dateTime_tv.setText(sch.getstime() + " - " + sch.getsdate());
		team1_tv.setText(sch.getsteam1name().trim());
		team2_tv.setText(sch.getsteam2name().trim());
		stadium_tv.setText(sch.getsvenue().trim());
		int resID1 = ctx.getResources().getIdentifier(sch.getteam1logo(), "drawable",ctx.getPackageName());
		team1_img.setImageBitmap(BitmapFactory.decodeResource(ctx.getResources(),resID1));
		team1_img.setTag(sch.getsteam1id());
		int resID2 = ctx.getResources().getIdentifier(sch.getteam2logo(), "drawable",ctx.getPackageName());
		team2_img.setImageBitmap(BitmapFactory.decodeResource(ctx.getResources(),resID2));
		team2_img.setTag(sch.getsteam2id());
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
