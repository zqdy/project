/**  
 * @fileName: MoreActivity.java
 * @description: 更多模块
 * @author：Jin Pang
 * @date：2013-12-15
 * @version：1.0
 */
package com.zjgr.fund.app.property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.zjgr.fund.R;
import com.zjgr.fund.app.BaseActivity;

public class MoreActivity extends BaseActivity implements OnClickListener {

	private ListView moreList;
	private List<Map<String, Object>> mData;
	private SimpleAdapter adapter;
	private static final int LOCK = 1;
	private static final int MY_RESERVATION = 2;
	private static final int SYSTEM = 3;
	private static final int TRANSFER = 4;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.more);
		init();
	}

	private void init(){
		moreList = (ListView)findViewById(R.id.moreList);
		moreList.setOnItemClickListener(itemClick);
		mData = getData();
		adapter = new SimpleAdapter(this, mData,
				R.layout.more_list, new String[] { "txtIcon", "textView1",
						"textView2" }, new int[] { R.id.txtIcon, R.id.textView1,
						R.id.textView2});
		moreList.setAdapter(adapter);
	}
	
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> tmp = new HashMap<String, Object>();
		tmp.put("txtIcon", R.drawable.ic_lock);
		tmp.put("propertyId", LOCK);
		tmp.put("textView1", getString(R.string.lock));
		tmp.put("textView2", "");
		list.add(tmp);
		tmp = new HashMap<String, Object>();
		tmp.put("txtIcon", R.drawable.ic_system);
		tmp.put("propertyId", SYSTEM);
		tmp.put("textView1", getString(R.string.system));
		tmp.put("textView2", "");
		list.add(tmp);
		
		tmp = new HashMap<String, Object>();
		tmp.put("txtIcon", R.drawable.ic_transfer);
		tmp.put("propertyId", TRANSFER);
		tmp.put("textView1", getString(R.string.transfer));
		tmp.put("textView2", "");
		list.add(tmp);
		
		tmp = new HashMap<String, Object>();
		tmp.put("txtIcon", R.drawable.ic_transfer);
		tmp.put("propertyId", TRANSFER);
		tmp.put("textView1", getString(R.string.more_account));
		tmp.put("textView2", "");
		list.add(tmp);
		
		tmp = new HashMap<String, Object>();
		tmp.put("txtIcon", R.drawable.ic_transfer);
		tmp.put("propertyId", TRANSFER);
		tmp.put("textView1", getString(R.string.about_information));
		tmp.put("textView2", "");
		list.add(tmp);
		
		return list;
	}
	
	private OnItemClickListener itemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// 创建intent对象
			Intent intent = new Intent();
			ComponentName componetName;
			Integer propertyId = (Integer) mData.get(position).get("propertyId");
			switch(propertyId){
			case LOCK:
				break;
			case MY_RESERVATION:
				break;
				
			case SYSTEM:
				break;
				
			case TRANSFER:
				break;
				default:
					break;
			}
		}

	};
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {

	}
}
