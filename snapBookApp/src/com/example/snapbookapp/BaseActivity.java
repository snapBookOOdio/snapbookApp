package com.example.snapbookapp;



import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;

/**
 * @author Paresh Mayani (@pareshmayani)
 */
public abstract class BaseActivity extends Activity {

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
}
