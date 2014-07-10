package com.soccer.bpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class soccerDBHelper {
	private static String DB_PATH = "/data/data/com.soccer.bpl/databases/";
	public DataBaseHelper dbHelper;

	public soccerDBHelper(Context ctx) {
		dbHelper = new DataBaseHelper(ctx);
	}

	public class DataBaseHelper extends SQLiteOpenHelper {
		// The Android's default system path of your application database.
		private static final String DB_NAME = "soccer";
		private SQLiteDatabase myDataBase;
		private final Context myContext;

		/**
		 * Constructor Takes and keeps a reference of the passed context in
		 * order to access to the application assets and resources.
		 * 
		 * @param context
		 */
		public DataBaseHelper(Context context) {

			super(context, DB_NAME, null, 1);
			this.myContext = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

		private boolean checkDataBase() {
			// SQLiteDatabase checkDB=null;
			boolean checkDB = false;
			try {
				String myPath = DB_PATH + DB_NAME;
				File file = new File(myPath);
				// checkDB = SQLiteDatabase.openDatabase(myPath, null,
				// SQLiteDatabase.OPEN_READONLY);
				checkDB = file.exists();

			} catch (SQLiteException ex) {
				ex.printStackTrace();
			}

			/*
			 * if(checkDB != null){ checkDB.close(); }
			 */
			return checkDB != false ? true : false;

		}

		/**
		 * Copies your database from your local assets-folder to the just
		 * created empty database in the system folder, from where it can be
		 * accessed and handled. This is done by transfering bytestream.
		 * */
		private void copyDataBase() throws IOException {

			// Open your local db as the input stream
			String dbName = DB_NAME + ".s3db";
			InputStream myInput = myContext.getAssets().open(dbName);

			// Path to the just created empty db
			String outFileName = DB_PATH + DB_NAME;

			// Open the empty db as the output stream
			OutputStream myOutput = new FileOutputStream(outFileName);

			// transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			// Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();

		}

		public void createDataBase() throws SQLException {

			if (!checkDataBase()) {
				this.getReadableDatabase();
				try {
					copyDataBase();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public void createWritableDataBase() throws SQLException {
			if (!checkDataBase()) {
				this.getWritableDatabase();
				try {
					copyDataBase();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public SQLiteDatabase openDB() {
			String myPath = DB_PATH + DB_NAME;
			myDataBase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.NO_LOCALIZED_COLLATORS);
			return myDataBase;
		}

		public SQLiteDatabase openWritableDB() {
			String myPath = DB_PATH + DB_NAME;
			myDataBase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			return myDataBase;
		}

		@Override
		public synchronized void close() {

			if (myDataBase != null)
				myDataBase.close();
			super.close();
		}
	}
}