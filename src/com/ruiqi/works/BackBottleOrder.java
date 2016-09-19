package com.ruiqi.works;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.bean.BackBottle;
import com.ruiqi.bean.Bottle;
import com.ruiqi.bean.Order;
import com.ruiqi.db.BackBottleDao;
import com.ruiqi.fragment.UnfinshBottle;
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
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

//退瓶订单界面
public class BackBottleOrder extends BaseOrderActivity implements
		OnCheckedChangeListener {

	public static List<BackBottle> mData;
	private List<BackBottle> mList;

	private BackBottleDao bbd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bbd = BackBottleDao.getInstances(BackBottleOrder.this);
		setTitle("退瓶订单");
		initData();
		setFragment();
	}

	private void initData() {

		mData = new ArrayList<BackBottle>();
		mList = new ArrayList<BackBottle>();

	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = null;
		switch (v.getId()) {
		case R.id.rl_grass_order:// 全部订单
			intent = new Intent(BackBottleOrder.this, AllOrder.class);
			intent.putExtra("mData", (Serializable) mList);
			intent.putExtra(SPutilsKey.KEY, "BackBottleOrder");
			break;

		default:
			break;
		}
		if (intent != null) {

			startActivity(intent);
		}
	}

	@Override
	public Fragment initFragment() {
		return new UnfinshBottle();
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
	public int initInteger() {
		return 2;
	}

	@Override
	public List<Order> initListData() {
		// TODO Auto-generated method stub
		return null;
	}

}
