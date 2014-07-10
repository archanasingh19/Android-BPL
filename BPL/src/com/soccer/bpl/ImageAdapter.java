package com.soccer.bpl;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private  Integer[] mThumbsId;
	public ImageAdapter(Context ctx){
		this.mContext=ctx;
		this.mThumbsId = new Integer[]{
				R.drawable.arsenal,R.drawable.astonvilla,
				R.drawable.chelsea,R.drawable.everton,
				R.drawable.fulham,R.drawable.liverpool,
				R.drawable.manchestercity,R.drawable.manchesterunited,
				R.drawable.newcastleunited,R.drawable.norwichcity,
				R.drawable.queensparkrangers,R.drawable.reading,
				R.drawable.southampton,R.drawable.stokecity,
				R.drawable.sunderland,R.drawable.swanseacity,
				R.drawable.tottenhamhotspur,R.drawable.westbromwichalbion,
				R.drawable.westhamunited,R.drawable.wiganathletic
				};
		}
	public int getCount() {
		return this.mThumbsId.length;
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageButton imageView;
        if (convertView == null) {  
            imageView = new ImageButton(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageButton) convertView;
        }
        imageView.setImageResource(this.mThumbsId[position]);
        imageView.setTag(position+1);
        imageView.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ClubDetailsActivity.class);
				intent.putExtra(ScheduleAdapter.CLUBID,Integer.parseInt( v.getTag().toString()));
				mContext.startActivity(intent);				
			}
		});
        return imageView;
	}

}
