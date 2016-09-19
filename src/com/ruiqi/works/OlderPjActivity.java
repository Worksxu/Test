package com.ruiqi.works;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.ruiqi.bean.OutIn;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.fragment.OlderPjFragment;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;

/**
 * 领取配件界面
 * @author Administrator
 *
 */
public class OlderPjActivity extends ApplyPeiJianConfirm{
	
	private OlderPjFragment olderPjFragment;
	private  List<TableInfo> mDatas;
	private  List<OutIn> mList;
	private OutIn outIn;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result);
		}   

		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		tv_confrim.setText("确定领取");
		tv_confrim.setOnClickListener(this);
		this.outIn=(OutIn) getIntent().getExtras().get("mData");
		this.mDatas=this.outIn.getmTableInfo();
		olderPjFragment=new OlderPjFragment(mDatas,mList);
		getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, initFragment()).commit();
		
	}
	
	@Override
	public Fragment initFragment() {  
		return olderPjFragment;
	}
	
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_confrim://确认领配件
			confrimTakeParts();
			break;
		default:
			break;
		}
	}
	public void confrimTakeParts(){
		String shiper_id = (String) SPUtils.get(this, SPutilsKey.SHIP_ID, "error");
		String shop_id = (String) SPUtils.get(this, SPutilsKey.SHOP_ID, "error");
		String mobile = (String) SPUtils.get(this, SPutilsKey.MOBILLE, "error");
		String name = (String) SPUtils.get(this, "shipper_name", "error");
		RequestParams params = new RequestParams(RequestUrl.APPLY_PJ);
		params.addBodyParameter("shipper_id", shiper_id);
		params.addBodyParameter("shipper_mobile", mobile);
		params.addBodyParameter("shipper_name", name);
		params.addBodyParameter("shop_id", shop_id);
	//	params.addBodyParameter("product_no", shop_id);
	//	params.addBodyParameter("data", getJsonData(mData));
		System.out.println("shipper_id="+shiper_id);
		System.out.println("shipper_mobile="+mobile);
		System.out.println("shipper_name="+name);
		System.out.println("shop_id="+shop_id);
		HttpUtil.PostHttp(params, handler, new ProgressDialog(this));
	}
	
	private void paraseData(String result) {
		System.out.println("result="+result);
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				T.showShort(this, "申领成功");
				Intent intent = new Intent(this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}else{
				T.showShort(this, "申领失败");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
