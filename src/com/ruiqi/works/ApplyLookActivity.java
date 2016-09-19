package com.ruiqi.works;

import com.ruiqi.fragment.ApplyPjLookFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 查看某条记录的申请项
 * @author Administrator
 *
 */
public class ApplyLookActivity extends ApplyPeiJianConfirm{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("申请详情");
		
		tv_confrim.setVisibility(View.GONE);
	}

	@Override
	public Fragment initFragment() {
		return new ApplyPjLookFragment();
	}

}
