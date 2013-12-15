/**
 * 
 */
package com.zjgr.fund.app;

import android.app.ActivityGroup;
import android.os.Bundle;

import com.zjgr.fund.FundApplication;

/**
 * @function BaseActivity.java
 * @author Jin Pang
 * @date 2013-12-15
 */

public class BaseActivity extends ActivityGroup {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FundApplication.getInstance().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
