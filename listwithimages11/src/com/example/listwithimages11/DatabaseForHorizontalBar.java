package com.example.listwithimages11;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseForHorizontalBar extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION=1;
	private static final String DATABASE_NAME="snapbook12.db";
	public static final String TABLE_NAME="pathsCollecter";
	
	public static final String KEY_ID="id";
	public static final String PATH="path";
	private String[] allColumns = {  KEY_ID,
			  PATH };
	private static final String CREATE_TABLE="CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY autoincrement,"
            + PATH + " TEXT UNIQUE "
            +")"; 
	public DatabaseForHorizontalBar(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE if exists " + TABLE_NAME);
		onCreate(db);
	}
	public void addPath(String path){
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(PATH, path);
		db.insert(TABLE_NAME, path, values);
		db.close();
	}
	public List<String> getAllComments() {
		List<String> comments = new ArrayList<String>();
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor = db.query(DatabaseForHorizontalBar.TABLE_NAME,
		        allColumns, null, null, null, null, null);
		cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      String paths=cursor.getString(1);
	      comments.add(paths);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return comments;
	}
	public void deletePath(String path){
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(TABLE_NAME, KEY_ID+" = "+getId(path), null);
		db.close();
	}
	public int getId(String path){
		String query="SELECT * FROM "+TABLE_NAME;
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME,
		        allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		String paths;
		int id;
	    while (!cursor.isAfterLast()) {
	      paths=cursor.getString(1);
	      id=cursor.getInt(0);
	      if(paths.equals(path)){
	    	  return id;
	      }
	      
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return -1;
	}
	public int getRowNumbers(){
		String query="SELECT * FROM "+TABLE_NAME;
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery(query, null);
		int rowcount=cursor.getCount();
		db.close();
		cursor.close();
		return rowcount;
	}
	public void resetTables(){
        // Delete All Rows
		SQLiteDatabase db=this.getReadableDatabase();
        db.delete( TABLE_NAME, null, null);
     }
}
