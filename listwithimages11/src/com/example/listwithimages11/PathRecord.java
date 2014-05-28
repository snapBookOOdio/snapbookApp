package com.example.listwithimages11;

import java.util.HashMap;

import com.example.listwithimages11.PathRecordMetaData.pathMetaData;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Browser.BookmarkColumns;

public class PathRecord extends ContentProvider {

	private static final String TAG="PathRecord";
	private static HashMap<String,String> pathProjectionMap;
	static{
		pathProjectionMap=new HashMap<String, String>();
		pathProjectionMap.put(pathMetaData._ID, pathMetaData._ID);
		pathProjectionMap.put(pathMetaData.PATH, pathMetaData.PATH);
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
