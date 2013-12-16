/**  
 * @fileName: AccountActivity.java
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

public class AccountActivity extends BaseActivity implements OnClickListener {

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
		super.setContentView(R.layout.account);
		init();
	}

	private void init(){
		moreList = (ListView)findViewById(R.id.list);
		moreList.setOnItemClickListener(itemClick);
		mData = getData();
		adapter = new SimpleAdapter(this, mData,
				R.layout.profit_list, new String[] { "textView", "textView1",
						"textView2", "textView3" }, new int[] { R.id.textView, R.id.textView1,
						R.id.textView2, R.id.textView3});
		moreList.setAdapter(adapter);
	}
	
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> tmp = new HashMap<String, Object>();
		tmp.put("textView", "周盈利率");
		tmp.put("propertyId", LOCK);
		tmp.put("textView1", "0.53%");
		tmp.put("textView2", "排行0");
		tmp.put("textView3", "3");
		list.add(tmp);
		tmp = new HashMap<String, Object>();
		tmp.put("textView", "月盈利率");
		tmp.put("propertyId", LOCK);
		tmp.put("textView1", "0.33%");
		tmp.put("textView2", "排行0");
		tmp.put("textView3", "8");
		list.add(tmp);
		
		tmp = new HashMap<String, Object>();
		tmp.put("textView", "总盈利率");
		tmp.put("propertyId", LOCK);
		tmp.put("textView1", "0.43%");
		tmp.put("textView2", "排行0");
		tmp.put("textView3", "5");
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
