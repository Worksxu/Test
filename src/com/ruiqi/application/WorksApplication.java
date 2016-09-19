package com.ruiqi.application;

import org.xutils.x;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;

public class WorksApplication extends Application{
	public static int versionCode;

	@Override
	public void onCreate() {
		super.onCreate();
		x.Ext.init(this);//xutils的初始化
		//初始化自定义扑捉异常
		//CustomCrash mCustomCrash = CustomCrash.getInstance();
	//	mCustomCrash.setCustomCrashInfo(this);
		
		 JPushInterface.setDebugMode(false);
		 JPushInterface.init(this);
	}
}
