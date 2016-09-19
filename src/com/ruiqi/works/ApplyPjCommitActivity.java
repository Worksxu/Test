package com.ruiqi.works;

import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.bean.PeiJian;
import com.ruiqi.bean.PeiJianTypeMoney;
import com.ruiqi.bean.Type;
import com.ruiqi.fragment.ApplyPjCommitFragment;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 配件申请的提交界面
 * @author Administrator
 *
 */


public class ApplyPjCommitActivity extends ApplyPeiJianConfirm{
	private List<PeiJian> mData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("提交申请");
		tv_confrim.setText("确认");
		tv_confrim.setOnClickListener(this);
		
	}
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result);
		}

		
	};
	
	private boolean flag = true;
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_confrim:
			if(flag){
				
				commit();
			}
			break;

		default:
			break;
		}
	}

	private void commit() {
		if(mData.size()==0){
			T.showShort(ApplyPjCommitActivity.this, "还没选择任何配件");
		}else{
			flag = false;
			
			String shiper_id = (String) SPUtils.get(ApplyPjCommitActivity.this, SPutilsKey.SHIP_ID, "error");
			String shop_id = (String) SPUtils.get(ApplyPjCommitActivity.this, SPutilsKey.SHOP_ID, "error");
			String mobile = (String) SPUtils.get(ApplyPjCommitActivity.this, SPutilsKey.MOBILLE, "error");
			String name = (String) SPUtils.get(ApplyPjCommitActivity.this, "shipper_name", "error");
			
			RequestParams params = new RequestParams(RequestUrl.APPLY_PJ);
			params.addBodyParameter("shipper_id", shiper_id);
			params.addBodyParameter("shipper_mobile", mobile);
			params.addBodyParameter("shipper_name", name);
			params.addBodyParameter("shop_id", shop_id);
			params.addBodyParameter("data", getJsonData(mData));
			System.out.println("data="+getJsonData(mData));
			System.out.println("shipper_id="+shiper_id);
			System.out.println("shipper_mobile="+mobile);
			System.out.println("shipper_name="+name);
			System.out.println("shop_id="+shop_id);
			HttpUtil.PostHttp(params, handler, new ProgressDialog(ApplyPjCommitActivity.this));
		}
	}
	
	private void paraseData(String result) {
		System.out.println("result="+result);
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				T.showShort(ApplyPjCommitActivity.this, "申领成功");
				Intent intent = new Intent(ApplyPjCommitActivity.this, MainActivity.class);
				startActivity(intent);
			}else{
				T.showShort(ApplyPjCommitActivity.this, "申领失败");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Fragment initFragment() {
		mData = (List<PeiJian>) getIntent().getSerializableExtra("mData");
		System.out.println("mData====="+mData);
		ApplyPjCommitFragment apf = new ApplyPjCommitFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("mData",(Serializable) mData);
		apf.setArguments(bundle);
		return apf;
	}
	
	 /**
	 * 拼接json字符串
	 */
	private String getJsonData(List<PeiJian> popupList) {
		JSONArray arr = new JSONArray();
		// 订单
		for (int i = 0; i < popupList.size(); i++) {
			JSONObject obj1 = new JSONObject();
			List<PeiJianTypeMoney> list = popupList.get(i).getmList();
			for(int j = 0;j<list.size();j++){
				
				try {
					obj1.put("good_id", list.get(j).getId());
					obj1.put("good_num", list.get(j).getNum());
					obj1.put("good_name", list.get(j).getName());
					obj1.put("good_type", list.get(j).getType());
					obj1.put("good_kind", list.get(j).getNorm_id());
					obj1.put("good_typename", list.get(j).getTypename());
					// obj1.put("wb", weightBottle);
					arr.put(obj1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return arr.toString();
	}

}
