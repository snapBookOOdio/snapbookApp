package com.example.snapbookapp;



import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;














import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ImagesFromGalaryChoose extends Activity{
	
	String action,paths;
	private ImageLoader imageLoader;
	Handler handler;
	GridView gridGallery;
	GalleryAdapter adapter;
	ImageView imgNoMedia;
	LinearLayout myGallery;
	int window_width,window_height;
	TextView number_of_images;
	 
	MyClass horiPath[]=new MyClass[20];;
	int curr_pos_of_image;
	ArrayList<String> path_for_sending;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.galary_pick);
		myGallery = (LinearLayout)findViewById(R.id.mygallery);
		
		paths="";
		for(int i=0;i<20;i++){
			horiPath[i]=new MyClass();
			horiPath[i].index=i;
			horiPath[i].position=-1;
		}
		curr_pos_of_image=0;


		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		window_height = displaymetrics.heightPixels /4;
		window_width = displaymetrics.widthPixels /4;
		window_height=(window_height>window_width)?window_height:window_width;
		window_width=window_height;
		action=getIntent().getAction();
		if (action == null) {
			finish();
		}
		initImageLoader();
		init();
	}
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.next_seesnapbook, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.title_nextPage:
			
		}
		return true;
	}*/
	private void initImageLoader(){
		try{
			String CACHE_DIR = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/.temp_tmp";
			new File(CACHE_DIR).mkdirs();
			File cacheDir = StorageUtils.getOwnCacheDirectory(getBaseContext(),
					CACHE_DIR);
			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
			ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
					getBaseContext())
					.defaultDisplayImageOptions(defaultOptions)
					.discCache(new UnlimitedDiscCache(cacheDir))
					.memoryCache(new WeakMemoryCache());
			ImageLoaderConfiguration config = builder.build();
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);
		}catch(Exception e){
			
		}
	}
	
	private void init(){
		handler = new Handler();
		gridGallery=(GridView)findViewById(R.id.gridGallery);
		gridGallery.setFastScrollEnabled(true);
		number_of_images=(TextView)findViewById(R.id.textView2);
		adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
		PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader,
				true, true);
		gridGallery.setOnScrollListener(listener);
		gridGallery.setOnItemClickListener(mItemMulClickListener);
		adapter.setMultiplePick(true);
		gridGallery.setAdapter(adapter);
		imgNoMedia = (ImageView) findViewById(R.id.imgNoMedia);
		new Thread() {

			@Override
			public void run() {
				Looper.prepare();
				handler.post(new Runnable() {

					@Override
					public void run() {
						adapter.addAll(getGalleryPhotos());
						checkImageStatus();
					}
				});
				Looper.loop();
			};

		}.start();
	}
	private void checkImageStatus() {
		if (adapter.isEmpty()) {
			imgNoMedia.setVisibility(View.VISIBLE);
		} else {
			imgNoMedia.setVisibility(View.GONE);
		}
	}
	AdapterView.OnItemClickListener mItemMulClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> l, View v, int position, long id) {
			
			adapter.changeSelection(v, position);
			
			ArrayList<CustomGallery> selected = adapter.getSelected();
			String[] allPath = new String[selected.size()];
			path_for_sending=new ArrayList<String>(Arrays.asList(allPath));
			String temp="";
			int i;
			for (i = 0; i < allPath.length; i++) {
				allPath[i] = selected.get(i).sdcardPath;
			//	path_for_sending.add(i, allPath[i]);
				temp+=allPath[i]+"`";
			}
			
			if(paths==""){
				String newTemp=temp.substring(0, temp.length()-1);
				myGallery.addView(insertPhoto(newTemp,curr_pos_of_image));
				 
				horiPath[curr_pos_of_image].position=position;
				curr_pos_of_image++;
			}else if(temp.length()>paths.length()){
				int lenTemp=temp.length();
				int lenPaths=paths.length();
				String newPath=paths.substring(0, lenPaths-1);
				String newTemp=temp.substring(0, lenTemp-1);
				myGallery.addView(insertPhoto(findLongestPrefixSuffix(newTemp, newPath),curr_pos_of_image));
				horiPath[curr_pos_of_image].position=position;
				curr_pos_of_image++;
			}else if(temp.length()<paths.length()){
				for(i=0;i<20;i++){
					if(horiPath[i].position==position){
						myGallery.removeViewAt(horiPath[i].index);
						if(i==19)
							horiPath[i].position=-1;
						else
							for(int j=i;j<19;j++){
								horiPath[j].position=horiPath[j+1].position;
								if(horiPath[j].position==-1){
									break;
								}
							}
						break;
					}
				}
				curr_pos_of_image--;
				temp="";
				for (i = 0; i < allPath.length; i++) {
					allPath[i] = selected.get(i).sdcardPath;
					temp+=allPath[i]+"`";
				}
			}
			for (i = 0; i < allPath.length; i++) {
				path_for_sending.add(i, allPath[i]);
			}
			
			paths=temp;
			number_of_images.setText(i+"/20");
		}
	};
	public static String findLongestPrefixSuffix(String s1, String s2) {
		int x;
		int i,j;
	   String[] split1=s1.split("\\`");
	   String[] split2=s2.split("\\`");
	
	   for(i=0;i<split1.length;i++){
		   x=0;
		   for(j=0;j<split2.length;j++){
			   	if(split1[i].equals(split2[j])){
			   		x=1;
			   	}
		   }
		   if(x==0){
			   return split1[i];
		   }
	   }
	   return null;
		}
	private ArrayList<CustomGallery> getGalleryPhotos() {
		ArrayList<CustomGallery> galleryList = new ArrayList<CustomGallery>();

		try {
			final String[] columns = { MediaColumns.DATA,
					BaseColumns._ID };
			final String orderBy = BaseColumns._ID;

			Cursor imagecursor = managedQuery(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
					null, null, orderBy);

			if (imagecursor != null && imagecursor.getCount() > 0) {

				while (imagecursor.moveToNext()) {
					CustomGallery item = new CustomGallery();

					int dataColumnIndex = imagecursor
							.getColumnIndex(MediaColumns.DATA);

					item.sdcardPath = imagecursor.getString(dataColumnIndex);

					galleryList.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// show newest photo at beginning of the list
		Collections.reverse(galleryList);
		return galleryList;
	}
	
	View insertPhoto(String path,int id){
		
	     Bitmap bm = decodeSampledBitmapFromUri(path, window_width-30, window_height-30);
	     
	     LinearLayout layout = new LinearLayout(getApplicationContext());
	     layout.setLayoutParams(new LayoutParams(window_width, window_height));
	   
	     layout.setGravity(Gravity.CENTER);
	     
	     ImageView imageView = new ImageView(getApplicationContext());
	     imageView.setLayoutParams(new LayoutParams(window_width-30, window_height-30));
	     imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	     imageView.setImageBitmap(bm);
	     imageView.setId(id);
	 //    imageView.setOnClickListener(new OnImageClickListener(id));
	     layout.addView(imageView);
	     return layout;
	    }
	class OnImageClickListener implements OnClickListener {

		int _postion;
		
		// constructor
		public OnImageClickListener(int position) {
			this._postion = position;
		}

		@Override
		public void onClick(View v) {
			// on selecting grid view image
			// launch full screen activity
			Intent i = new Intent(getApplicationContext(), FullScreenView.class);
			i.putExtra("position", _postion);
			i.putStringArrayListExtra("lists", path_for_sending);
			startActivity(i);
		}

	}
	public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
	     Bitmap bm = null;
	     
	     // First decode with inJustDecodeBounds=true to check dimensions
	     final BitmapFactory.Options options = new BitmapFactory.Options();
	     options.inJustDecodeBounds = true;
	     BitmapFactory.decodeFile(path, options);
	     
	     // Calculate inSampleSize
	     options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	     
	     // Decode bitmap with inSampleSize set
	     options.inJustDecodeBounds = false;
	     bm = BitmapFactory.decodeFile(path, options); 
	     
	     return bm;  
	    }
	    
	    public int calculateInSampleSize(
	      
	     BitmapFactory.Options options, int reqWidth, int reqHeight) {
	     // Raw height and width of image
	     final int height = options.outHeight;
	     final int width = options.outWidth;
	     int inSampleSize = 1;
	        
	     if (height > reqHeight || width > reqWidth) {
	      if (width > height) {
	       inSampleSize = Math.round((float)height / (float)reqHeight);   
	      } else {
	       inSampleSize = Math.round((float)width / (float)reqWidth);   
	      }   
	     }
	     
	     return inSampleSize;   
	    }
	    private static class MyClass{
	    	int position;
	    	int index;
	    	
	    }
}
 