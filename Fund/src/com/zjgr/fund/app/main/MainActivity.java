package com.zjgr.fund.app.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.zjgr.fund.R;
import com.zjgr.fund.app.BaseActivity;

public class MainActivity extends BaseActivity implements OnItemClickListener
{
	private GridView fundGridView;
	private ImageAdapter imageAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initView();
	}
	
	private void initView()
	{
		fundGridView = (GridView) this.findViewById(R.id.gridView);
		imageAdapter = new ImageAdapter(this);
		fundGridView.setAdapter(imageAdapter);
		fundGridView.setOnItemClickListener(this);
	}
	
	/**
	 * The main adapter that backs the GridView. This is fairly standard except
	 * the number of columns in the GridView is used to create a fake top row of
	 * empty views as we use a transparent ActionBar and don't want the real top
	 * row of images to start off covered by it.
	 */
	private class ImageAdapter extends BaseAdapter
	{
		
		private final Context mContext;
		private int mItemHeight = 0;
		private int mNumColumns = 0;
		private int mActionBarHeight = -1;
		private GridView.LayoutParams mImageViewLayoutParams;
		private int[] imageArrays = new int[]
		{R.drawable.ic_launcher, R.drawable.ic_launcher,
		        R.drawable.ic_launcher, R.drawable.ic_launcher,
		        R.drawable.ic_launcher, R.drawable.ic_launcher};
		
		public ImageAdapter(Context context)
		{
			super();
			mContext = context;
			mImageViewLayoutParams = new GridView.LayoutParams(
			        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		}
		
		@Override
		public int getCount()
		{
			// Size of adapter + number of columns for top empty row
			// return mImageWorker.getAdapter().getSize() + mNumColumns;
			return imageArrays.length;
		}
		
		@Override
		public Object getItem(int position)
		{
			/*
			 * return position < mNumColumns ? null :
			 * mImageWorker.getAdapter().getItem(position - mNumColumns);
			 */
			return null;
		}
		
		@Override
		public long getItemId(int position)
		{
			return position < mNumColumns ? 0 : position - mNumColumns;
		}
		
		@Override
		public int getViewTypeCount()
		{
			// Two types of views, the normal ImageView and the top row of empty
			// views
			return 2;
		}
		
		@Override
		public int getItemViewType(int position)
		{
			return (position < mNumColumns) ? 1 : 0;
		}
		
		@Override
		public boolean hasStableIds()
		{
			return true;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup container)
		{
			// First check if this is the top row
			if (position < mNumColumns)
			{
				if (convertView == null)
				{
					convertView = new View(mContext);
				}
				// Calculate ActionBar height
				if (mActionBarHeight < 0)
				{
					TypedValue tv = new TypedValue();
					if (mContext.getTheme().resolveAttribute(
					        android.R.attr.actionBarSize, tv, true))
					{
						mActionBarHeight = TypedValue
						        .complexToDimensionPixelSize(tv.data, mContext
						                .getResources().getDisplayMetrics());
					}
					else
					{
						// No ActionBar style (pre-Honeycomb or ActionBar not in
						// theme)
						mActionBarHeight = 0;
					}
				}
				// Set empty view with height of ActionBar
				convertView.setLayoutParams(new AbsListView.LayoutParams(
				        ViewGroup.LayoutParams.MATCH_PARENT, mActionBarHeight));
				return convertView;
			}
			
			// Now handle the main ImageView thumbnails
			ImageView imageView;
			if (convertView == null)
			{ // if it's not recycled, instantiate and initialize
				imageView = new ImageView(mContext);
				// imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setLayoutParams(mImageViewLayoutParams);
			}
			else
			{ // Otherwise re-use the converted view
				imageView = (ImageView) convertView;
			}
			
			// Check the height matches our calculated column width
			if (imageView.getLayoutParams().height != mItemHeight)
			{
				imageView.setLayoutParams(mImageViewLayoutParams);
			}
			
			imageView.setImageResource(imageArrays[position]);
			
			// Finally load the image asynchronously into the ImageView, this
			// also takes care of
			// setting a placeholder image while the background thread runs
			// mImageWorker.loadImage(position - mNumColumns, imageView);
			return imageView;
		}
		
		/**
		 * Sets the item height. Useful for when we know the column width so the
		 * height can be set to match.
		 * 
		 * @param height
		 */
		public void setItemHeight(int height)
		{
			if (height == mItemHeight)
			{
				return;
			}
			mItemHeight = height;
			mImageViewLayoutParams = new GridView.LayoutParams(
			        LayoutParams.MATCH_PARENT, mItemHeight);
			// mImageWorker.setImageSize(height);
			notifyDataSetChanged();
		}
		
		public void setNumColumns(int numColumns)
		{
			mNumColumns = numColumns;
		}
		
		public int getNumColumns()
		{
			return mNumColumns;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
	        long id)
	{
		if (position == 0)
		{
			ComponentName componetName = new ComponentName("com.zjgr.fund",
			        "com.zjgr.fund.app.detail.FundDetailActivity");
			Intent intent = new Intent();
			intent.setComponent(componetName);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
			        | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		else
		{
			Toast.makeText(this, "功能完善中，敬请期待~", Toast.LENGTH_SHORT).show();
		}
		
	}
}
