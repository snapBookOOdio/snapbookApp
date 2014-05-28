package com.example.listwithimages11;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PathDataSource {
	private SQLiteDatabase database;
	private DatabaseForHorizontalBar dbHelper;
	 private String[] allColumns = { DatabaseForHorizontalBar.KEY_ID,
			 DatabaseForHorizontalBar.PATH };

		  public PathDataSource(Context context) {
		    dbHelper = new DatabaseForHorizontalBar(context);
		  }

		  public void open() throws SQLException {
		    database = dbHelper.getWritableDatabase();
		  }

		  public void close() {
		    dbHelper.close();
		  }
		  public void addPath(String path){
			  ContentValues values = new ContentValues();
			  values.put(DatabaseForHorizontalBar.PATH, path);
			  database.insert(DatabaseForHorizontalBar.TABLE_NAME, path, values);
			  
			}
			public void deletePath(String path){
				
				database.delete(DatabaseForHorizontalBar.TABLE_NAME, DatabaseForHorizontalBar.PATH+" = "+path, null);
				
			}
			public int getRowNumbers(){
				String query="SELECT * FROM "+DatabaseForHorizontalBar.TABLE_NAME;
				
				Cursor cursor=database.rawQuery(query, null);
				int rowcount=cursor.getCount();
				database.close();
				cursor.close();
				return rowcount;
			}
			public void resetTables(){
		        // Delete All Rows
		        database.delete(DatabaseForHorizontalBar.TABLE_NAME, null, null);
		     }
			public List<String> getAllComments() {
				List<String> comments = new ArrayList<String>();
				Cursor cursor = database.query(DatabaseForHorizontalBar.TABLE_NAME,
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
}
