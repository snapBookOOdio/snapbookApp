package com.example.listwithimages11;

import android.net.Uri;
import android.provider.BaseColumns;

public class PathRecordMetaData {
	public static final String AUTHORITY="com.example.provider.imagePath";
	
	public static final String DATABASE_NAME="paths.db";
	public static final int DATABASE_VERSION=1;
	public static final String TABLE_NAME="insertedpaths";
	
	private PathRecordMetaData(){}
	
	public static final class pathMetaData implements BaseColumns{
		public static final String TABLE_NAME="insertedpaths";
		public static final Uri CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/insertedpaths");
		public static final String CONTENT_TYPE="vnd.android.cursor.dir/vnd.androidbook.paths";
		public static final String CONTENT_ITEM_TYPE="vnd.android.cursor.item/vnd.androidbook.paths";
		
		public static final String PATH="path";
	//	public static final String KEY_ID="id";
	}
}

