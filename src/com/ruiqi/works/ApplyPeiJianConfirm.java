package com.ruiqi.works;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.widget.TextView;

/**
 * 申请和领取的基类
 * @author Administrator
 *
 */
public abstract class ApplyPeiJianConfirm extends BaseActivity{
	
	public TextView tv_confrim;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_peijian_confrim);
		setTitle("配件确定领取");
		tv_confrim = (TextView) findViewById(R.id.tv_confrim);
		getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, initFragment()).commit();
	}

	@Override 
	public Activity getActivity() {
		return this;
	}

	@Override
	public Handler[] initHandler() {
		return null;
	}
	
	public abstract Fragment initFragment();
}
