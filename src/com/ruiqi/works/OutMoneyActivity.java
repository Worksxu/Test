package com.ruiqi.works;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 上缴款项给门店界面
 * @author Administrator
 *
 */
public class OutMoneyActivity extends BaseActivity{
	private EditText et_input;
	
	private TextView tv_out;
	
	private String result;
	private TextView tv_after_content;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			parseData(result);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_out_money);
		
		setTitle("上缴款项");
		result = getIntent().getStringExtra("mData");
		et_input = (EditText) findViewById(R.id.et_input);
		CurrencyUtils.setPricePoint(et_input);//控制输入小数后两位
		
		tv_out = (TextView) findViewById(R.id.tv_out);
		tv_out.setOnClickListener(this);
		
		tv_after_content = (TextView) findViewById(R.id.tv_after_content);
		tv_after_content.setText(result);
	}
	
	private void parseData(String result) {
		try {
			JSONObject obj1 = new JSONObject(result);
			int resultCode = obj1.getInt("resultCode");
			if(resultCode==1){
				Toast.makeText(OutMoneyActivity.this, "上缴成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(OutMoneyActivity.this, MainActivity.class);
				startActivity(intent);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public Activity getActivity() {
		return this;
	}
	private boolean isFrist = true;
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_out:
			if(isFrist){
				
				sure();
			}
			break;

		default:
			break;
		}
	}

	private void sure() {
		String money =et_input.getText().toString();
		//先判断
		if(money==null||money.equals("")){
			T.showShort(OutMoneyActivity.this, "请输入要提交的金额");
		}else if(Double.parseDouble(money)>Double.parseDouble(tv_after_content.getText().toString())){
			T.showShort(OutMoneyActivity.this, "上缴金额大于余额");
		}else {
			isFrist = false;
			String shipper_id = (String) SPUtils.get(OutMoneyActivity.this, SPutilsKey.SHIP_ID, "error");
			String shop_id = (String) SPUtils.get(OutMoneyActivity.this, SPutilsKey.SHOP_ID, "error");
			RequestParams params = new RequestParams(RequestUrl.FORPAY);
			params.addBodyParameter("shipper_id", shipper_id);
			params.addBodyParameter("shop_id", shop_id);
			params.addBodyParameter("money", money);
			
			HttpUtil.PostHttp(params, handler, new ProgressDialog(OutMoneyActivity.this));
		}
				
	}

	@Override
	public Handler[] initHandler() {
		return null;
	}

}
