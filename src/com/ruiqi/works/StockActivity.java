package com.ruiqi.works;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruiqi.fragment.PeijianStockFragment;
import com.ruiqi.fragment.PingStockFragment;
/**
 * 我的库存界面
 * @author Administrator
 *
 */
public class StockActivity extends BaseActivity implements OnCheckedChangeListener{
	
	private RadioGroup rg_select;
	private TextView tv_out,tv_in;
	private RelativeLayout rl_out,rl_in;
	
	private boolean flag = true; //判断是钢瓶还是配件的出入库记录
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_stock);
		setTitle("我的库存");
		init();
	}

	private void init() {
		
		getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, new PingStockFragment()).commit();
		rg_select = (RadioGroup) findViewById(R.id.rg_select);
		rg_select.setOnCheckedChangeListener(this);
		
		tv_out = (TextView) findViewById(R.id.tv_out);
		tv_in = (TextView) findViewById(R.id.tv_in);
		
		rl_out = (RelativeLayout) findViewById(R.id.rl_out);
		rl_out.setOnClickListener(this);
		rl_in = (RelativeLayout) findViewById(R.id.rl_in);
		rl_in.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_out://出库记录
			out();
			break;
		case R.id.rl_in://入库记录
			in();
			break;

		default:
			break;
		}
	}
	//入库记录
	private void in() {
		Intent intent = null;
		if(flag){//钢瓶入库记录
			intent = new Intent(StockActivity.this, com.ruiqi.chai.PingInActivity.class);
		}else{//配件
			intent = new Intent(StockActivity.this, com.ruiqi.chai.PeijianInActivity.class);
		//	intent = new Intent(StockActivity.this, PeijianActivity.class);
		//	intent.putExtra("from", 1);
		}
		startActivity(intent);
	}

	//出库记录
	private void out() {
		Intent intent = null ;
		if(flag){//钢瓶出库记录
			intent = new Intent(StockActivity.this, com.ruiqi.chai.PingOutActivity.class);
		}else{//配件
			intent = new Intent(StockActivity.this, com.ruiqi.chai.PeijianOutActivity.class);
			//intent.putExtra("from", 2);
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
	public Handler[] initHandler() {
		return null;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_ping://钢瓶库存
			getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, new PingStockFragment()).commit();
			tv_out.setText("钢瓶出库记录");
			tv_in.setText("钢瓶入库记录");
			flag = true;
			break;
		case R.id.rb_peijian://配件库存
			getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, new PeijianStockFragment()).commit();
			tv_out.setText("配件出库记录");
			tv_in.setText("配件入库记录");
			flag = false;
			break;
			
		default:
			break;
		}
	}

}
