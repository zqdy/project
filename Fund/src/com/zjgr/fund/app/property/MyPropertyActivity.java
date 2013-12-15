package com.zjgr.fund.app.property;

import android.os.Bundle;
import android.view.Menu;

import com.zjgr.fund.R;
import com.zjgr.fund.app.BaseActivity;

public class MyPropertyActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_property);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_property, menu);
		return true;
	}

}
