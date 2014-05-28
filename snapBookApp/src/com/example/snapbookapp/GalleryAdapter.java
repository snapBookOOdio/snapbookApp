package com.example.snapbookapp;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class GalleryAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater infalter;
	private ArrayList<CustomGallery> data = new ArrayList<CustomGallery>();
	ImageLoader imageLoader;
	private boolean isActionMultiplePick;
	
	public GalleryAdapter(Context c, ImageLoader imageLoader){
		infalter = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext=c;
		this.imageLoader=imageLoader;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public CustomGallery getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	public void setMultiplePick(boolean isMultiplePick) {
		this.isActionMultiplePick = isMultiplePick;
	}
	
	public void selectAll(boolean selection) {
		for (int i = 0; i < data.size(); i++) {
			data.get(i).isSeleted = selection;

		}
		notifyDataSetChanged();
	}
	public boolean isAllSelected() {
		boolean isAllSelected = true;

		for (int i = 0; i < data.size(); i++) {
			if (!data.get(i).isSeleted) {
				isAllSelected = false;
				break;
			}
		}

		return isAllSelected;
	}
	
	public boolean isAnySelected() {
		boolean isAnySelected = false;

		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).isSeleted) {
				isAnySelected = true;
				break;
			}
		}

		return isAnySelected;
	}
	
	public ArrayList<CustomGallery> getSelected() {
		ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).isSeleted) {
				dataT.add(data.get(i));
			}
		}

		return dataT;
	}
	public void addAll(ArrayList<CustomGallery> files) {

		try {
			this.data.clear();
			this.data.addAll(files);

		} catch (Exception e) {
			e.printStackTrace();
		}

		notifyDataSetChanged();
	}
	public void changeSelection(View v, int position) {
		
		
		if (data.get(position).isSeleted) {
			
		//	((ViewHolder)v.getTag()).imgQueue.setBackgroundColor(Color.TRANSPARENT);
			data.get(position).isSeleted = false;
		} else {
		//	((ViewHolder)v.getTag()).imgQueue.setBackgroundColor(Color.GREEN);
			data.get(position).isSeleted = true;
		}

		((ViewHolder) v.getTag()).imgQueueMultiSelected.setSelected(data
				.get(position).isSeleted);
	}
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if(arg1==null){
			arg1=infalter.inflate(R.layout.galaryitem, null);
			holder = new ViewHolder();
			holder.imgQueue = (ImageView) arg1
					.findViewById(R.id.imgQueue);
			holder.imgQueueMultiSelected=(ImageView) arg1.findViewById(R.id.imgQueueMultiSelected);
			holder.layout=(FrameLayout)arg1.findViewById(R.id.frmQueue);
			if (isActionMultiplePick) {
				holder.imgQueueMultiSelected.setVisibility(View.VISIBLE);
			}
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		holder.imgQueue.setTag(arg0);
		final ImageView imageView = (ImageView) arg1.findViewById(R.id.imgQueue);
		try {

			imageLoader.displayImage("file://" + data.get(arg0).sdcardPath,
					holder.imgQueue, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.imgQueue
									.setImageResource(R.drawable.no_media);
							super.onLoadingStarted(imageUri, view);
						}
						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
							imageView.setAnimation(anim);
							anim.start();
						}
					});

			if (isActionMultiplePick) {

				holder.imgQueueMultiSelected
						.setSelected(data.get(arg0).isSeleted);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return arg1;
		
	}
	public class ViewHolder {
		ImageView imgQueue;
		ImageView imgQueueMultiSelected;
		FrameLayout layout;
	}

	public void clearCache() {
		imageLoader.clearDiscCache();
		imageLoader.clearMemoryCache();
	}

	public void clear() {
		data.clear();
		notifyDataSetChanged();
	}
}
