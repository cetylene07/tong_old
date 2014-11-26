package model.tong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHelper {
	private static final String DATABASE_NAME = "call.db";
	private static final int DATABASE_VERSION = 1;
	public static SQLiteDatabase mDB;
	private DatabaseHelper mDBHelper;
	private Context mCtx;
	
	private class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		// 최초 DB를 만들 때 한번만 호출된다.
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DataBases.CreateDB._CREATE);		
			Log.d("test", "Table onCreate Complete");
		}

		// 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS" + DataBases.CreateDB._TABLENAME);
			onCreate(db);			
			Log.d("test", "Table onUpgrade Complete");
		}

	}
	
	public DbOpenHelper(Context context)	{
		this.mCtx = context;
	}
	
	public DbOpenHelper open() throws SQLException	{
		mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
		mDB = mDBHelper.getWritableDatabase();	// DB를 읽어나 쓸 수 있는 권한 부여
		return this;
	}
	
	public void close()	{
		mDB.close();
	}
	
	// Insert DB
	public long insertColumn(String callID, String name, String date, String duration, String type)	{
		ContentValues values = new ContentValues();
		values.put(DataBases.CreateDB.callID, callID);
		values.put(DataBases.CreateDB.NAME, name);
		values.put(DataBases.CreateDB.DATE, date);
		values.put(DataBases.CreateDB.DURATION, duration);
		values.put(DataBases.CreateDB.TYPE, type);
		
		return mDB.insert(DataBases.CreateDB._TABLENAME, null, values);
	}
	
	// Update DB
	public boolean updateColumn(long id, String callID, String name, String date, String duration, String type)	{
		ContentValues values = new ContentValues();
		values.put(DataBases.CreateDB.callID, callID);
		values.put(DataBases.CreateDB.NAME, name);
		values.put(DataBases.CreateDB.DATE, date);
		values.put(DataBases.CreateDB.DURATION, duration);
		values.put(DataBases.CreateDB.TYPE, type);
		
		return mDB.update(DataBases.CreateDB._TABLENAME, values, "_ID="+id, null) > 0;
	}
	
	// Delete DB
	public boolean deleteColumn(long id)	{
		return mDB.delete(DataBases.CreateDB._TABLENAME, "_ID="+id, null) > 0;
	}
	
	// Select All
	public Cursor getAllColumns()	{
		return mDB.query(DataBases.CreateDB._TABLENAME, null, null, null, null, null, null);
	}
	
	public Cursor getAllSums()	{
		return null;
	}
	
	
} 
