package com.ruiqi.works;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
//我的订单界面
public class MyOrderActivity extends BaseActivity {
	private TextView tv_myorder_grass,tv_myorder_botter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myorder);
		setTitle("我的订单");
		tv_myorder_grass = (TextView) findViewById(R.id.tv_myorder_grass);
		tv_myorder_botter = (TextView) findViewById(R.id.tv_myorder_botter);
		tv_myorder_grass.setOnClickListener(this);
		tv_myorder_botter.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = null;
		switch (v.getId()) {
		case R.id.tv_myorder_grass:
			intent = new Intent(MyOrderActivity.this, GrassOrder.class);
			break;
		case R.id.tv_myorder_botter:
			intent = new Intent(MyOrderActivity.this, BackBottleOrder.class);
			break;
		default:
			break;
		}
		if(intent!=null){
			
			startActivity(intent);
		}
	}

	@Override
	public Activity getActivity() {
		return this;
	}
	
	@Override
	public void jumpPage() {
		Intent intent = new Intent(MyOrderActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public Handler[] initHandler() {
		return null;
	}

}
