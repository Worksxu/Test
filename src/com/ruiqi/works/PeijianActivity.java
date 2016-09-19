package com.ruiqi.works;

import com.ruiqi.fragment.PeijianInFragment;
import com.ruiqi.fragment.PeijianOutFragment;
import com.ruiqi.fragment.WeightOutFragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

/**
 * 配件出库界面
 * @author Administrator
 *
 */
public class PeijianActivity extends BaseActivity{
	
	private int from; //用于判定是出库还是入库
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_peijian_out);
		from = getIntent().getIntExtra("from", 0);
		
		if(from==1){//入库
			setTitle("配件入库记录");
			getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, new PeijianInFragment()).commit();
		}else if(from==2){//出库
			setTitle("配件出库记录");
			getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, new PeijianOutFragment()).commit();
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

}
