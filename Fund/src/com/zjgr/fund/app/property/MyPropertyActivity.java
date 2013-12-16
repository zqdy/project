package com.zjgr.fund.app.property;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjgr.fund.R;
import com.zjgr.fund.app.BaseActivity;

public class MyPropertyActivity extends BaseActivity implements OnClickListener {
	/** Called when the activity is first created. */

	public static final int ACCOUNT = 0; // 账户
	public static final int POSITION = 1; // 持仓
	public static final int TRADE = 2; // 交易
	public static final int ORDER_CANCEL = 3; // 撤单
	public static final int MORE = 4; // 更多

	private LinearLayout bodyView;
	private LinearLayout naviView;
	private View[] naviItems;
	private View oldView;

	private int currentItem = 0;
	public static final String KEY_CURRENT_ITEM = "current_item";
	public static final String LAUCH_TAG = "lauch_tag";
	public static final boolean UMENG_WORKED = false;
	private LocalActivityManager mLocalActivityManager;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_property);
		mLocalActivityManager = getLocalActivityManager();
		initComponent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ActivityGroup#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * 更换菜单栏背景
	 * 
	 * */
	private void changeViewBackground(View view) {
		if (oldView != null) {

			oldView.setClickable(true);
			oldView.setBackgroundResource(R.drawable.btn_bg);
			TextView oldTextView = (TextView) oldView;
			switch (oldView.getId()) {
			case R.id.accountTxt:
				
				break;
			case R.id.positionTxt:

				break;
			case R.id.tradeTxt:

				break;
			case R.id.orderCancelTxt:

				break;

			case R.id.moreTxt:

				break;

			}
		}
		view.setClickable(false);
		TextView newTextView = (TextView) view;
		view.setBackgroundResource(R.drawable.bg_active);
	}

	/**
	 * 初始化菜单项
	 * 
	 * */
	private void initComponent() {
		bodyView = (LinearLayout) findViewById(R.id.layoutBody);
		naviView = (LinearLayout) findViewById(R.id.layoutBodyBtn);
		int childCount = naviView.getChildCount();
		naviItems = new View[childCount];
		View accountView = findViewById(R.id.accountTxt);
		addView2Array(ACCOUNT, accountView, naviItems);
		addView2Array(POSITION, findViewById(R.id.positionTxt), naviItems);
		View tradeView = findViewById(R.id.tradeTxt);
		addView2Array(TRADE, tradeView, naviItems);
		addView2Array(ORDER_CANCEL, findViewById(R.id.orderCancelTxt), naviItems);
		addView2Array(MORE, findViewById(R.id.moreTxt), naviItems);

		//
		naviView.removeAllViews();
		for (int i = 0; i < naviItems.length; i++) {
			naviView.addView(naviItems[i], i);
			naviItems[i].setOnClickListener(this);
		}
		accountView.performClick();
	}

	/**
	 * 组合菜单项与资源id
	 * */
	private void addView2Array(int index, View view, View[] dest) {
		if (index >= 0) {
			dest[index] = view;
		}
	}

	/**
	 * 展示菜单项
	 * 
	 * @param cls
	 * @param intent
	 * @param isClearTop
	 * 
	 */
	public void showView(Class<?> cls, Intent intent, boolean isClearTop) {
		if (isClearTop) {
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
		View view = mLocalActivityManager.startActivity(cls.getSimpleName(),
				intent).getDecorView();
		bodyView.removeAllViews();
		bodyView.addView(view);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		changeViewBackground(v);
		switch (v.getId()) {

		// 账户
		case R.id.accountTxt:
			 intent = new Intent(this, AccountActivity.class);
			 showView(AccountActivity.class, intent, true);
			break;

		// 持仓
		case R.id.positionTxt:
			// loadDb();
//			intent = new Intent(this, PropertyActivity.class);
//			showView(PropertyActivity.class, intent, true);
			break;

		// 交易
		case R.id.tradeTxt:
//			intent = new Intent(this, LifeActivity.class);
//			showView(LifeActivity.class, intent, true);
			break;

		// 撤单
		case R.id.orderCancelTxt:
//			intent = new Intent(this, DiscountActivity.class);
//			showView(DiscountActivity.class, intent, true);
			break;

		// 更多
		case R.id.moreTxt:
			intent = new Intent(this, MoreActivity.class);
			showView(MoreActivity.class, intent, true);
			break;

		default:
			break;
		}
		oldView = v;
	}

}
