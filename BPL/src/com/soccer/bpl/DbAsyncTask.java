package com.soccer.bpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;

import android.util.Log;
import android.widget.Toast;

public class DbAsyncTask extends AsyncTask<String, Void, String> {
	private JSONArray scheduleJSONArray;
	private JSONArray standingsJSONArray;
	private JSONArray goalsJSONArray;
	private JSONArray assistsJSONArray;
	private JSONArray schChangeJSONArray;
	private JSONArray playerJSONArray;
	private static final String SCHEDULE="matchschedule";
	private static final String STANDINGS="clubstandings";
	private static final String GOALS="mostgoals";
	private static final String ASSISTS="mostassists";
	private static final String PLAYERDETAILS = "playerdetails";
	private static final int SCHEDULE_INT=0;
	private static final int STANDINGS_INT=1;
	private static final int GOALS_INT=2;
	private static final int ASSISTS_INT=3;
	private static final int SCH_CHNG_INT=4;
	private static final int PLYR_CHNG_INT=5;
	private static final String FILE_READ_FLAG="file_read";

	SQLiteDatabase sqliteDatabase;
	private Context mContext;
	private boolean splashFlag;
	private ProgressDialog dialog;
	private Activity lastActivity;
	private StringBuffer stringBuffer;
	private AssetManager assetManager;
	private InputStream inpStr;
	private BufferedReader buffReader;
	private SharedPreferences sharedPrefs;
	private String fileReadFlag="false";
	
	public DbAsyncTask(Context ctx,boolean flag,Activity act){
		mContext = ctx;
		splashFlag=flag;
		lastActivity = act;
		scheduleJSONArray = new JSONArray();
		standingsJSONArray = new JSONArray();
		goalsJSONArray = new JSONArray();
		assistsJSONArray = new JSONArray();
		schChangeJSONArray = new JSONArray();
		playerJSONArray= new JSONArray();
		sharedPrefs = mContext.getSharedPreferences(FILE_READ_FLAG, Activity.MODE_PRIVATE);
		fileReadFlag = sharedPrefs.getString("fileReadFlag", "false");
		stringBuffer = new StringBuffer();
		assetManager = mContext.getAssets();
		if(fileReadFlag.equals("true")){
			try{
				inpStr = assetManager.open("changes.txt");
				buffReader = new BufferedReader(new InputStreamReader(inpStr));
			}catch(IOException e){
				Log.d("file error", "file not found");
			}
		}
	}
		
	@Override
	protected String doInBackground(String... urls) {
		
		try {
			if(fileReadFlag.equals("true")){
				String str="";
				try{
					if(inpStr!=null){
						while((str = buffReader.readLine())!=null){
							stringBuffer.append(str);
						}
					}
					inpStr.close();
				}catch(IOException e){
					Log.d("file ", "read error");
				}
				Log.d("file ",stringBuffer.toString());
				Editor editor = sharedPrefs.edit();
			    editor.putString("fileReadFlag", "true");
			    editor.commit();
			}
			URL url = new URL(urls[0]);
			URLConnection connection = url.openConnection();
			HttpURLConnection httpConnection =(HttpURLConnection)connection;
			soccerDBHelper opensoccerHelperClass = new soccerDBHelper(mContext);
			opensoccerHelperClass.dbHelper.createWritableDataBase();
			sqliteDatabase = opensoccerHelperClass.dbHelper.openWritableDB();
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
						String content_str = ((Element)entry.getElementsByTagName("content:encoded").item(0)).getChildNodes().item(0).getNodeValue();
						content_str=content_str.replaceAll("\\<.*?\\>", "").replace("&#8220;", "\"").replace("&#8221;", "\"").replaceAll("&#8243;", "\"");
						if(content_str.contains("scheduledb2")){

							try{
								JSONObject contentjson = new JSONObject(content_str);
								if(contentjson.has("scheduledb2")){
									scheduleJSONArray= new JSONArray();
									JSONArray contJsonArray = contentjson.getJSONArray("scheduledb2");
									
									scheduleJSONArray = contJsonArray.getJSONObject(0).getJSONArray("schedule2");
									updateDB(SCHEDULE_INT);
								}

							}catch(JSONException e){
								Log.d("json ", e.getMessage());
							}

						}else if(content_str.contains("updatedb")){

							try{
								JSONObject contentjson = new JSONObject(content_str);
								if(contentjson.has("updatedb")){
									JSONArray contJsonArray = contentjson.getJSONArray("updatedb");
									standingsJSONArray = contJsonArray.getJSONObject(0).getJSONArray("clubstandings");
									goalsJSONArray = contJsonArray.getJSONObject(1).getJSONArray("mostgoals");
									assistsJSONArray = contJsonArray.getJSONObject(2).getJSONArray("mostassists");
									schChangeJSONArray = contJsonArray.getJSONObject(3).getJSONArray("schdchng");
									playerJSONArray = contJsonArray.getJSONObject(4).getJSONArray("playerchng");
								}

							}catch(JSONException e){
								Log.d("json ", e.getMessage());
							}
						}
					}
					updateDB(STANDINGS_INT);
					updateDB(GOALS_INT);
					updateDB(ASSISTS_INT);
					updateDB(SCH_CHNG_INT);
					updateDB(PLYR_CHNG_INT);
					sqliteDatabase.close();
					
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
		return "";
	}

	@Override
	protected void onPreExecute(){
		if(!splashFlag){
			dialog = ProgressDialog.show(mContext, mContext.getString(R.string.title_activity_soccer), mContext.getString(R.string.db_loading_message) );
		}
	}

	@Override
	protected void onProgressUpdate(Void... values){
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(String result){
		if(!splashFlag){
			dialog.dismiss();
		}
		lastActivity.finish();
		Intent mainIntent = new Intent(mContext,SoccerActivity.class);
		mContext.startActivity(mainIntent);
	}

	public void updateDB(int tablename){
		switch (tablename) {
		case SCHEDULE_INT:
			try{
				for(int i=0;i<scheduleJSONArray.length();i++){
					try{
						JSONObject schObj = scheduleJSONArray.getJSONObject(i);
						String schid = schObj.getString("sid");
						String score = schObj.getString("sc");
						String goals = schObj.getString("goals");
						if(!goals.equalsIgnoreCase("")){
							score=score.concat("@@@" + goals);
						}

						if(!score.equalsIgnoreCase("")){
							String queryString="update "+ SCHEDULE + " set score= '"+score+"' where scheduleid='"+schid+"'";
							sqliteDatabase.execSQL(queryString);
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
			}catch (SQLiteException e) {
				e.printStackTrace();
			}
			break;
		case STANDINGS_INT:
			try{
				String queryString="delete from "+STANDINGS;
				sqliteDatabase.execSQL(queryString);
				for(int i=0;i<standingsJSONArray.length();i++){
					try{
						JSONObject stObj = standingsJSONArray.getJSONObject(i);
						String teamid = stObj.getString("tid");
						String position = stObj.getString("pos");
						String played = stObj.getString("pl");
						String win = stObj.getString("w");
						String draw = stObj.getString("d");
						String lost = stObj.getString("l");
						String points = stObj.getString("pts");
						queryString="insert into "+ STANDINGS +" values ('"+teamid+"','"+position+"','"+played+"','"+win+"','"+draw+"','"+lost+"','"+points+"')";
						sqliteDatabase.execSQL(queryString);

					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}catch (SQLiteException e) {
				e.printStackTrace();
			}
			break;
		case GOALS_INT:
			try{
				String queryString="delete from "+GOALS;
				sqliteDatabase.execSQL(queryString);
				for(int i=0;i<goalsJSONArray.length();i++){
					try{
						JSONObject goalsObj = goalsJSONArray.getJSONObject(i);
						String playerid = goalsObj.getString("pid");
						String goals = goalsObj.getString("gls");

						queryString="insert into "+ GOALS +" values ('"+playerid+"','"+goals+"')";
						sqliteDatabase.execSQL(queryString);

					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}catch (SQLiteException e) {
				e.printStackTrace();
			}
			break;
		case ASSISTS_INT:
			try{
				String queryString="delete from "+ASSISTS;
				sqliteDatabase.execSQL(queryString);
				for(int i=0;i<assistsJSONArray.length();i++){
					try{
						JSONObject assistsObj = assistsJSONArray.getJSONObject(i);
						String playerid = assistsObj.getString("playerid");
						String goals = assistsObj.getString("assists");

						queryString="insert into "+ ASSISTS +" values ('"+playerid+"','"+goals+"')";
						sqliteDatabase.execSQL(queryString);

					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}catch (SQLiteException e) {
				e.printStackTrace();
			}
			break;
		case SCH_CHNG_INT:
			try{
				for(int i=0;i<schChangeJSONArray.length();i++){
					try{
						JSONObject schObj = schChangeJSONArray.getJSONObject(i);
						String schid = schObj.getString("schid");
						String time = schObj.getString("time");
						String dt = schObj.getString("dt");

						String queryString="update "+ SCHEDULE + " set sdate= '"+dt+"' and stime= '"+ time +"' where scheduleid='"+schid+"'";;
						sqliteDatabase.execSQL(queryString);

					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}catch (SQLiteException e) {
				e.printStackTrace();
			}
			break;
		case PLYR_CHNG_INT:
			try{

				for(int i=0;i<playerJSONArray.length();i++){
					try{
						JSONObject plyrObj = playerJSONArray.getJSONObject(i);
						String playerid = plyrObj.getString("pid");
						String name = plyrObj.getString("pname");
						String clubid = plyrObj.getString("clubid");
						String queryString="";
						String rawQuery = "select * from "+ PLAYERDETAILS +" where playerid="+playerid;
						Cursor cur = sqliteDatabase.rawQuery(rawQuery, null);
						if(cur.getCount()==0){
							queryString="insert into "+ PLAYERDETAILS +" values ("+playerid+",'"+name+"','"+clubid+"','','','','','')";
						}else{
							queryString="update "+ PLAYERDETAILS +" set clubid= '"+clubid+"' where playerid="+playerid;
						}
						sqliteDatabase.execSQL(queryString);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}catch (SQLiteException e) {
				e.printStackTrace();
			}
			break;
		}
	}

}
