﻿package com.zjgr.fund;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

public class FundApplication extends Application {

	private static final String TAG = "FundApplication";
	public static Context mContext;
	private static FundApplication instance;
	private static List<Activity> activityList = new LinkedList<Activity>();

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		instance = this;
		if (Constant.IS_DEBUG)
			Log.d(TAG, "-----------app init-----------");
	}

	// 添加Activity容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 退出程序
	public void exit() {
		for (Activity ac : activityList) {
			if (ac != null) {
				ac.finish();
			}
		}
		System.exit(0);
	}

	/**
	 * 获取app的设备上下文
	 * 
	 * @return
	 */
	public static Context getContext() {
		return mContext;
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		if (Constant.IS_DEBUG)
			Log.d(TAG, "-----------onLowMemory-----------");
		System.gc();
	}

	/**
	 * 判断activity是否在最前端
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isTopActivity(String clazz) {
		ActivityManager am = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		if (cn != null && cn.getClassName().equals(clazz)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static FundApplication getInstance() {
		if (null == instance) {
			instance = new FundApplication();
			mContext = instance;
			return instance;
		}
		return instance;
	}

}
