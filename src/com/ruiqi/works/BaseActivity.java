package com.ruiqi.works;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.MyActivityManager;

public  abstract class  BaseActivity extends FragmentActivity implements OnClickListener{
	
	private LinearLayout parentLinearLayout;//把父类activity和子类activity的view都add到这里
	
	public RelativeLayout rl_back;//返回框
	
	private TextView tvTitle;//标题
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initContentView(R.layout.action_bar);
		MyActivityManager.getInstance().pushOneActivity(getActivity());
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
	}
	
	public void initView() {
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		rl_back.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			jumpPage();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 点击回退,默认关闭当前页面.
	 * 如果需要,可以重写,实现跳转
	 */
	public void jumpPage() {
		
		finish();
	}

	/**
	 * 设置ActionBar的布局
	 * 
	 * @param layoutId
	 *            布局Id
	 * */
	/**
     * 初始化contentview
     */
    private void initContentView(int layoutResID) {
    	ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        parentLinearLayout = new LinearLayout(this);
        parentLinearLayout.setOrientation(LinearLayout.VERTICAL);
        viewGroup.addView(parentLinearLayout);
        LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);


    }
    
    @Override
    public void setContentView(int layoutResID) {

        LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);

    }

    @Override
    public void setContentView(View view) {

        parentLinearLayout.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {

        parentLinearLayout.addView(view, params);

    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Handler [] mHandler = initHandler();
    	if(mHandler!=null){
    		for(int i = 0;i<mHandler.length;i++){
    			CurrencyUtils.recoveryHandler(mHandler[i]);
    		}
    	}
    }
    
    /**
     * 设置标题,子类去实现
     */
    public void setTitle(String str){
    	tvTitle.setText(str);
    }
    public void setNotShow(){
    	rl_back.setVisibility(View.GONE);
    }
    public  abstract Activity getActivity();
    
    public abstract Handler [] initHandler();
    
    
}
