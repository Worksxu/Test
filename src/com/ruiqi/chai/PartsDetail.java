package com.ruiqi.chai;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.SerializableMap;
import com.ruiqi.utils.T;
import com.ruiqi.works.MainActivity;
import com.ruiqi.works.R;

public class PartsDetail extends Activity implements
		android.view.View.OnClickListener {

	private String status;
	private HashMap<String, String> data;

	private SerializableMap serializableMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parts_detail);
		Bundle bundle = getIntent().getExtras();
		serializableMap = (SerializableMap) bundle.get("sMap");
		initView();
	}

	private ImageView ivBack;
	private TextView tv_back, tv_next;
	private ListView lv_parts;
	private ArrayList<HashMap<String, String>> arrayList;

	private void initView() {
		initData();
		status=serializableMap.getMap().get("status");
		ivBack = (ImageView) findViewById(R.id.ivBack);
		tv_back = (TextView) findViewById(R.id.tv_back);
		tv_next = (TextView) findViewById(R.id.tv_next);
		lv_parts = (ListView) findViewById(R.id.lv_parts);
		if (status.equals("1")) {
			tv_next.setVisibility(View.GONE);
		}

		lv_parts.setAdapter(new ListViewAdapterText2(this, arrayList));

		tv_back.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		tv_next.setOnClickListener(this);
	}

	private void initData() {
		arrayList=new ArrayList<HashMap<String,String>>();
		
		HashMap<String, String> hm0=new HashMap<String, String>();
		hm0.put("title1", "配件");
		hm0.put("title2","数量");
		arrayList.add(hm0);
		HashMap<String, String> hm=new HashMap<String, String>();
		hm.put("title1", serializableMap.getMap().get("good_name"));
		hm.put("title2", serializableMap.getMap().get("good_num"));
		arrayList.add(hm);
		
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result);
		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
		case R.id.ivBack:
			finish();
			break;
		case R.id.tv_next:// 确认领配件
			confrimParts();
			break;

		default:
			break;
		}
	}
	public void confrimParts(){
		String shiper_id = (String) SPUtils.get(this, SPutilsKey.SHIP_ID, "error");
		String shop_id = (String) SPUtils.get(this, SPutilsKey.SHOP_ID, "error");
		String mobile = (String) SPUtils.get(this, SPutilsKey.MOBILLE, "error");
		String name = (String) SPUtils.get(this, "shipper_name", "error");
		
		RequestParams params = new RequestParams(RequestUrl.APPLY_PJ);
		params.addBodyParameter("shipper_id", shiper_id);
		//params.addBodyParameter("shipper_mobile", mobile);
	//	params.addBodyParameter("shipper_name", name);
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("product_no",serializableMap.getMap().get("title1"));
		params.addBodyParameter("id",serializableMap.getMap().get("id"));
		tv_next.setEnabled(false);
		HttpUtil.PostHttp(params, handler, new ProgressDialog(this));
	}

	private void paraseData(String result) {
		tv_next.setEnabled(true);
		System.out.println("result=" + result);
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if (resultCode == 1) {
				T.showShort(this, "申领成功");
				finish();
//				Intent intent = new Intent(this, MainActivity.class);
//				startActivity(intent);
			} else {
				T.showShort(this, "申领失败");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
