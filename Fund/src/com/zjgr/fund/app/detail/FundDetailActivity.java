package com.zjgr.fund.app.detail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.zjgr.fund.R;
import com.zjgr.fund.app.BaseActivity;

public class FundDetailActivity extends BaseActivity implements
        OnItemClickListener
{
	
	private ListView fundList;
	private FundDetailAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fund_detail);
		initView();
	}
	
	private void initView()
	{
		fundList = (ListView) this.findViewById(R.id.fundDetailList);
		
		adapter = new FundDetailAdapter(this, getData(),
		        R.layout.fund_detail_item, new String[]
		        {"fundCode", "dayIncreased", "fundName", "yearIncreased",
		                "netValue", "fundLevel"}, new int[]
		        {R.id.fundCode, R.id.dayIncreased, R.id.fundName,
		                R.id.yearIncreased, R.id.netValue, R.id.fundLevel});
		fundList.setAdapter(adapter);
		fundList.setOnItemClickListener(this);
	}
	
	class FundDetailAdapter extends SimpleAdapter
	{
		
		public FundDetailAdapter(Context context,
		        List<? extends Map<String, ?>> data, int resource,
		        String[] from, int[] to)
		{
			super(context, data, resource, from, to);
		}
		
	}
	
	private List<Map<String, Object>> getData()
	{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fundCode", "511883");
		map.put("dayIncreased", "0.0410");
		map.put("fundName", "银华日利");
		map.put("yearIncreased", "69.8800");
		map.put("netValue", "最新净值");
		map.put("fundLevel", "A1");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("fundCode", "511883");
		map.put("dayIncreased", "0.0410");
		map.put("fundName", "银华日利");
		map.put("yearIncreased", "69.8800");
		map.put("netValue", "最新净值");
		map.put("fundLevel", "A1");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("fundCode", "511883");
		map.put("dayIncreased", "0.0410");
		map.put("fundName", "银华日利");
		map.put("yearIncreased", "69.8800");
		map.put("netValue", "最新净值");
		map.put("fundLevel", "A1");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("fundCode", "511883");
		map.put("dayIncreased", "0.0410");
		map.put("fundName", "银华日利");
		map.put("yearIncreased", "69.8800");
		map.put("netValue", "最新净值");
		map.put("fundLevel", "A1");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("fundCode", "511883");
		map.put("dayIncreased", "0.0410");
		map.put("fundName", "银华日利");
		map.put("yearIncreased", "69.8800");
		map.put("netValue", "最新净值");
		map.put("fundLevel", "A1");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("fundCode", "511883");
		map.put("dayIncreased", "0.0410");
		map.put("fundName", "银华日利");
		map.put("yearIncreased", "69.8800");
		map.put("netValue", "最新净值");
		map.put("fundLevel", "A1");
		list.add(map);
		
		return list;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
	        long id)
	{
		Toast.makeText(this, "onItemClick >>> " + position, Toast.LENGTH_SHORT)
		        .show();
		
		ComponentName componetName = new ComponentName("com.zjgr.fund",
				 "com.zjgr.fund.app.property.MyPropertyActivity"); 
		Intent intent = new
				 Intent(); intent.setComponent(componetName);
				 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
				 Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(intent); 
	}
	
	/*
	 * @Override public void onClick(View v) { switch (v.getId()) { case
	 * R.id.button1: { ComponentName componetName = new
	 * ComponentName("com.zjgr.fund",
	 * "com.zjgr.fund.app.property.MyPropertyActivity"); Intent intent = new
	 * Intent(); intent.setComponent(componetName);
	 * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
	 * Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(intent); break; } } }
	 */
	
}
