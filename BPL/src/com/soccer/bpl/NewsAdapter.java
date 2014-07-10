package com.soccer.bpl;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
//import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {
	private final ArrayList<News> items;
	private final LayoutInflater inflator;
//	private Context ctx;
	
	public NewsAdapter(Context cntxt,ArrayList<News> list){
//		this.ctx=cntxt;
		this.items=list;
		inflator = (LayoutInflater)cntxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public int getCount() {
		return items.size();
	}

	public Object getItem(int arg0) {
		return items.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0+1;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = inflator.inflate(R.layout.newslistitem, null);
		TextView titleView = (TextView)view.findViewById(R.id.newsTitle);
		TextView descView = (TextView)view.findViewById(R.id.newsDesc);
		TextView dateView = (TextView)view.findViewById(R.id.newsDate);
		//ImageView imgView = (ImageView)view.findViewById(R.id.newsImage);
		News n = (News)getItem(position);
		titleView.setText(n.getTitle());
		descView.setText(n.getDescription());
		dateView.setText(n.getPubDate());
		//imgView.setImageBitmap(new NewsActivity().imagesList.get(position));		
				
		return view;
	}

}
