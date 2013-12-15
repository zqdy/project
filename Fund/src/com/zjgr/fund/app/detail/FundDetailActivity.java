package com.zjgr.fund.app.detail;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zjgr.fund.R;
import com.zjgr.fund.app.BaseActivity;

public class FundDetailActivity extends BaseActivity implements OnClickListener{

	private Button gotoFundListBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fund_detail);
		initView();
	}
	
	private void initView(){
		gotoFundListBtn = (Button) findViewById(R.id.button1);
		gotoFundListBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.button1:{
			ComponentName componetName = new ComponentName("com.zjgr.fund",
					"com.zjgr.fund.app.property.MyPropertyActivity");
			Intent intent = new Intent();
			intent.setComponent(componetName);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		}
	}
	
	

}
