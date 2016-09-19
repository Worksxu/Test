package com.ruiqi.works;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.bean.Order;
import com.ruiqi.bean.Orderdeail;
import com.ruiqi.db.OrderDao;
import com.ruiqi.fragment.UnfinshFragment;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RadioGroup.OnCheckedChangeListener;

//订气订单
public class GrassOrder extends BaseOrderActivity implements OnCheckedChangeListener{
	
	private ProgressDialog pd;
	private OrderDao od;
	
	private boolean flag=true;
	
	public static List<Order> mData;
	private  List<Order> mList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pd = new ProgressDialog(this);
		pd.setMessage("正在加载......");
		od = OrderDao.getInstances(this);
		setTitle("订气订单");
		//rg_select.setOnCheckedChangeListener(this);
		initData();
		setFragment();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}


	/**
	 * 数据的初始化
	 */
	private void initData() {
		mData = new ArrayList<Order>();
		mList = new ArrayList<Order>();
		
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = null;
		switch (v.getId()) {
		case R.id.rl_grass_order:
			intent = new Intent(GrassOrder.this, AllOrder.class);
			intent.putExtra("mData", (Serializable)mData);
			intent.putExtra(SPutilsKey.KEY, "GrassOrder");
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
	public Handler[] initHandler() {
		return null;
	}


	//传递一个fragment给父类
	@Override
	public Fragment initFragment() {
		UnfinshFragment ufh = new UnfinshFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("mData", (Serializable) mList);
		ufh.setArguments(bundle);
		return ufh;
	}
	@Override
	public int initInteger() {
		return 1;
	}
	@Override
	public List<Order> initListData() {
		// TODO Auto-generated method stub
		return mData;
	}



}
