/**
 * 
 */
package com.zjgr.fund.app;

import com.zjgr.fund.FundApplication;

import android.app.Activity;
import android.os.Bundle;

/**
 * @function BaseActivity.java
 * @author Jin Pang
 * @date 2013-12-15
 */

public class BaseActivity extends Activity {

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
