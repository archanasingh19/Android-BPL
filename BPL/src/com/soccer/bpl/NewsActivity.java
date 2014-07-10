package com.soccer.bpl;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

public class NewsActivity extends Activity {
	private ListView news_listview;
	private ArrayList<News> newsList;
	private Context mContext;
	private NewsAsyncTask task;
	
	private static final String NEWS_FEED ="http://feeds.bbci.co.uk/sport/0/football/rss.xml?edition=int";
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_activity);
		mContext=this;
		news_listview =(ListView)findViewById(R.id.newsListView);
		newsList= new ArrayList<News>();
		task = new NewsAsyncTask();
		task.execute(new String[] { NEWS_FEED });
		news_listview.setCacheColorHint(Color.TRANSPARENT);
	}
		
	private class NewsAsyncTask extends AsyncTask<String, Void, ArrayList<News>> {
		private ProgressDialog dialog;
		@Override
		protected ArrayList<News> doInBackground(String... urls) {
			newsList = new ArrayList<News>();
			
			try {
				URL url = new URL(urls[0]);
				URLConnection connection = url.openConnection();
				HttpURLConnection httpConnection =(HttpURLConnection)connection;
				if(httpConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
					InputStream inpStr =  httpConnection.getInputStream();
	    			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    			DocumentBuilder db = dbf.newDocumentBuilder();
	    			Document dom = db.parse(inpStr);
	    			Element elem = dom.getDocumentElement();
	    			NodeList nl = elem.getElementsByTagName("item");
	    			if(nl !=null && nl.getLength()>0){
	    				for(int i=0;i<nl.getLength();i++){
	    					Element entry = (Element)nl.item(i);
	    					String title =((Element)entry.getElementsByTagName("title").item(0)).getFirstChild().getNodeValue();
	    					String description =((Element)entry.getElementsByTagName("description").item(0)).getFirstChild().getNodeValue();
	    					String link =((Element)entry.getElementsByTagName("link").item(0)).getFirstChild().getNodeValue();
	    					String pubDate =((Element)entry.getElementsByTagName("pubDate").item(0)).getFirstChild().getNodeValue();
	    					String img = ((Element)entry.getElementsByTagName("media:thumbnail").item(0)).getAttribute("url");
	    					News news = new News(title, description, link, pubDate, img);
	    					
	    					newsList.add(news);
	    				}
	    			}
				}
			}catch(MalformedURLException e){
	    		e.printStackTrace();
	    	}catch(IOException e){
	    		e.printStackTrace();
	    	}catch(ParserConfigurationException e){
	    		e.printStackTrace();
	    	}catch(SAXException e){
	    		e.printStackTrace();
	    	}finally{
	    		
	    	}
			return newsList;
		}
		
		@Override
		protected void onPreExecute(){
			dialog = ProgressDialog.show(mContext, mContext.getString(R.string.title_activity_soccer), mContext.getString(R.string.loading_message) );
		}
		
		@Override
		protected void onPostExecute(ArrayList<News> result){
			dialog.dismiss();
			news_listview.setAdapter(new NewsAdapter(mContext, result));
			news_listview.setCacheColorHint(Color.TRANSPARENT);
		} 
		 @Override
         protected void onProgressUpdate(Void... values) {
         	 super.onProgressUpdate(values);
         }
	}

}
