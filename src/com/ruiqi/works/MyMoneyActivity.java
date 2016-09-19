package com.ruiqi.works;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.fragment.MoneyInFragment;
import com.ruiqi.fragment.MoneyInFragment.OrderInCallBack;
import com.ruiqi.fragment.MoneyOutFragment;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.AlteredCharSequence;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 我的财务界面
 * @author Administrator
 *
 */
public class MyMoneyActivity extends BaseActivity implements OnCheckedChangeListener,ParserData{
	
	private RadioGroup rg_select;
	private TextView tv_out;
	
	private TextView tv_home_hours_count,tv_home_orders_count,tv_home_paimings_count;
	
	private MoneyInFragment mif;
	
	private MoneyOutFragment mof;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mymoney);
		setTitle("我的财务");
		init();
	}
	private void init() {
		rg_select = (RadioGroup) findViewById(R.id.rg_select);
		rg_select.setOnCheckedChangeListener(this);
		
		tv_out = (TextView) findViewById(R.id.tv_out);
		tv_out.setOnClickListener(this);
		
		tv_home_hours_count = (TextView) findViewById(R.id.tv_home_hours_count);
		tv_home_orders_count = (TextView) findViewById(R.id.tv_home_orders_count);
		tv_home_paimings_count = (TextView) findViewById(R.id.tv_home_paimings_count);
		
		mif = new MoneyInFragment();
		//mof = new MoneyOutFragment();
		mif.setCallBack(new OrderInCallBack() {
			
			@Override
			public void callBack(String a, String b, String c) {
				tv_home_hours_count.setText(a);
				tv_home_orders_count.setText(b);
				tv_home_paimings_count.setText(c);
			}
		});
		getSupportFragmentManager().beginTransaction().replace(R.id.rl_content_fragment,mif ).commit();
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
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_out://跳转到上缴界面
//			Intent intent = new Intent(MyMoneyActivity.this, OutMoneyActivity.class);
//			intent.putExtra("mData", tv_home_paimings_count.getText().toString());
//			startActivity(intent);
			
			AlertDialog.Builder builder = new Builder(this);
			// builder.setMessage("网络连接失败");
			TextView tv = new TextView(this);
			tv.setText("确认是否上交余额");
			tv.setTextSize(20);
			tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(0, 20, 0, 0);
			builder.setView(tv);
			builder.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					sure();
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			builder.create().show();
			
			
			break;

		default:
			break;
		}
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_unfinsh://收入
			mif = new MoneyInFragment();
			
			mif.setCallBack(new OrderInCallBack() {
				
				@Override
				public void callBack(String a, String b, String c) {
					tv_home_hours_count.setText(a);
					tv_home_orders_count.setText(b);
					tv_home_paimings_count.setText(c);
				}
			});
			//切换到收入fragment
			getSupportFragmentManager().beginTransaction().replace(R.id.rl_content_fragment, mif).commit();
			
			break;
		case R.id.rb_finsh://支出
			//切换到支出界面
			mof = new MoneyOutFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.rl_content_fragment, mof).commit();
			break;

		default:
			break;
		}
	}
	
	private void sure() {
		//先判断
		String money=tv_home_paimings_count.getText().toString();
			if(Float.parseFloat(money)!=0){
				String shipper_id = (String) SPUtils.get(this, SPutilsKey.SHIP_ID, "error");
				String shop_id = (String) SPUtils.get(this, SPutilsKey.SHOP_ID, "error");
				RequestParams params = new RequestParams(RequestUrl.FORPAY);
				params.addBodyParameter("shipper_id", shipper_id);
				params.addBodyParameter("shop_id", shop_id);
				params.addBodyParameter("money", money);
				HttpUtil httpUtil=new HttpUtil();
				httpUtil.setParserData(this);
				httpUtil.PostHttp(params,0);
			}else{
				Toast.makeText(this, "当前余额为零", Toast.LENGTH_SHORT).show();
			}
	}
	@Override
	public void sendResult(String result, int what) {
		try {
			JSONObject obj1 = new JSONObject(result);
			int resultCode = obj1.getInt("resultCode");
			if(resultCode==1){
				Toast.makeText(this, "上缴成功", Toast.LENGTH_SHORT).show();
				finish();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
