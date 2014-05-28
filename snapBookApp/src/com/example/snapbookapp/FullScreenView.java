package com.example.snapbookapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class FullScreenView extends Activity{
	private FullScreenImageAdapter adapter;
	private ViewPager pager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);
		pager=(ViewPager)findViewById(R.id.pager);
		Intent i=getIntent();
		int position=i.getIntExtra("position", 0);
		ArrayList<String> paths=i.getStringArrayListExtra("lists");
		adapter=new FullScreenImageAdapter(FullScreenView.this,paths);
		pager.setAdapter(adapter);
		pager.setCurrentItem(position);
		
	}
}
