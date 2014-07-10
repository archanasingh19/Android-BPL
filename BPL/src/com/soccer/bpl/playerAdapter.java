package com.soccer.bpl;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class playerAdapter extends BaseAdapter {
	private final ArrayList<String> items;
	private Context ctx;
	
	public playerAdapter(Context ctx,ArrayList<String> items){
		this.ctx = ctx;
		this.items = items;
	}
	public int getCount() {
		return items.size();
	}

	public Object getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return position+1;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TextView txtView;
        if (convertView == null) {  
        	txtView = new TextView(ctx);
        } else {
        	txtView = (TextView) convertView;
        }
        txtView.setText(getItem(position).toString());
        txtView.setTextAppearance(ctx, R.style.boldTxt);
		return txtView;
	}

}
