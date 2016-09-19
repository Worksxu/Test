package com.ruiqi.works;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.IsPhone;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FindActivity extends BaseActivity implements BDLocationListener{
	public LocationClient mLocationClient = null;
//	public BDLocationListener myListener = new MyLocationListener();
	private TextView tv_get_verf_code;//获取验证码
	private EditText et_verf_code;//请输入验证码
	private EditText et_find_password_ID;//请输入手机号
	private EditText et_entry_password;//请输入密码
	private EditText et_confirm_password ;//确认输入密码\
	
	private int a;//用于接收产生的随机数
	private int counter; //用于倒计时
	private int code;
	
	private ProgressDialog pd;
	
	private String mobile;
	private String flag;
	
	//控制60s倒计时
		Handler mHandler = new Handler();
		Runnable mRunnable = new Runnable() {
			@Override
			public void run() {
				counter--;
				tv_get_verf_code.setText(counter+"");
				tv_get_verf_code.setClickable(false);
				if(counter<0){
					tv_get_verf_code.setText("获取验证码");
					tv_get_verf_code.setClickable(true);
					return;
				}
				mHandler.postDelayed(this, 1000);
			}
		};
	//找回密码网络请求的handler
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			String strSu = (String) msg.obj;
			parseData(2,strSu);
		};
	};
	//得到验证码handler
	private Handler codeHandler = new Handler(){
		public void handleMessage(Message msg) {
			String strSu = (String) msg.obj;
			//进行json解析
			parseData(1,strSu);
		}

	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find);
		
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(this); // 注册监听函数
		mLocationClient.start();
		setTitle("找回密码");
		mobile = getIntent().getStringExtra("mobile");
		flag = getIntent().getStringExtra("flag");
		a=(int)(Math.random()*10);
		init();
		pd = new ProgressDialog(this);
		pd.setMessage("正在请求修改密码，请稍后......");
	}
	private void init() {
		tv_get_verf_code = (TextView) findViewById(R.id.tv_get_verf_code);
		et_find_password_ID = (EditText) findViewById(R.id.et_find_password_ID);
		et_verf_code = (EditText) findViewById(R.id.et_verf_code);
		et_entry_password=(EditText) findViewById(R.id.et_entry_password);
		et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
		
		et_find_password_ID.setText(mobile);
		//获取验证码的点击事件
		tv_get_verf_code.setClickable(true);//将textview设置成可点击属性
		tv_get_verf_code.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View arg0) {
				counter = 60;
				//获取输入的手机号
				String mobile = et_find_password_ID.getText().toString().trim();
				if(TextUtils.isEmpty(mobile)){
					Toast.makeText(FindActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
				}else{
					if(!IsPhone.isOrNotPhone(mobile)){
						Toast.makeText(FindActivity.this, "输入的手机号码不合法", Toast.LENGTH_SHORT).show();
					}else{
						//请求参数
						RequestParams params = new RequestParams(RequestUrl.GET_CODE);
						params.addBodyParameter("mobile", mobile);
						params.addBodyParameter("type",  1+"");
						//发送网络请求
						HttpUtil.PostHttp(params, codeHandler, pd);
					}
				}
						
			}
		});
	}
	
	/**
	 * 完成密码修改的点击事件
	 */
	public void bt_finish(View view){
		//获取各个输入框的内容
		String mobile = et_find_password_ID.getText().toString().trim();
		String password = et_entry_password.getText().toString().trim();
		String confirm_password = et_confirm_password.getText().toString().trim();
		String captcha = et_verf_code.getText().toString().trim();
		String passWord64 = Base64.encodeToString(password.getBytes(), Base64.DEFAULT);
		String confirm_password64 = Base64.encodeToString(confirm_password.getBytes(), Base64.DEFAULT);
		if(TextUtils.isEmpty(mobile)||TextUtils.isEmpty(password)||TextUtils.isEmpty(confirm_password)||TextUtils.isEmpty(captcha)){
			Toast.makeText(FindActivity.this, "每一项都为必填项", Toast.LENGTH_SHORT).show();
		}else if(!captcha.equals(code+"")){
			Toast.makeText(FindActivity.this, "验证码输入错误", Toast.LENGTH_SHORT).show();
		}else if(!password.equals(confirm_password)){
			Toast.makeText(FindActivity.this, "两次输入的密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
		}else{
			pd.show();
			//封装
			RequestParams params = new RequestParams(RequestUrl.PASS_RESET);
			params.addBodyParameter("mobile", mobile);
			params.addBodyParameter("password",  passWord64+a);
			params.addBodyParameter("confirm_password", confirm_password64+a);
			params.addBodyParameter("captcha", captcha);
			//发送网络请求
			HttpUtil.PostHttp(params, handler, pd);
		}
	}
	
	private void parseData(int i, String strSu) {
		System.out.println("strsu="+strSu);
		if(i==1){//是发送验证码的数据
			//json解析
			try {
				JSONObject object = new JSONObject(strSu);
				int resultCode = object.getInt("resultCode");
				String resultInfo = object.getString("resultInfo");
				if(resultCode<0){
					Toast.makeText(FindActivity.this, resultInfo, Toast.LENGTH_SHORT).show();
				}else if(resultCode==1){
					//发送成功，继续解析
					tv_get_verf_code.setText(counter+"");
					mHandler.postDelayed(mRunnable, 1000);
					JSONObject object1 = object.getJSONObject("resultInfo");
					int  mobile = object1.getInt("mobile");
					code = object1.getInt("code");
					Toast.makeText(FindActivity.this, "已将验证码发送至手机", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else if(i==2){
			try {
				JSONObject object = new JSONObject(strSu);
				int resultCode = object.getInt("resultCode");
				if(resultCode<0){
					Toast.makeText(FindActivity.this, "密码修改失败", Toast.LENGTH_SHORT).show();
				}else if(resultCode==1){
					Toast.makeText(FindActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
					
					//解析
					SPUtils.put(FindActivity.this, SPutilsKey.FLAG, false);
					SPUtils.put(FindActivity.this, SPutilsKey.MOBILLE,  et_find_password_ID.getText().toString().trim());
					SPUtils.put(FindActivity.this, SPutilsKey.PWD,  et_entry_password.getText().toString().trim());
					String resultInfo = object.getString("resultInfo");
					if("login".equals(flag)){
						Intent intent = getIntent();
						//intent.putExtra("mobile", et_find_password_ID.getText().toString().trim());
						
						setResult(2, intent);
						this.finish();
					}else if("personal".equals(flag)){
						Intent intent = new Intent(FindActivity.this, LoginActivity.class);
						//intent.putExtra("mobile", et_find_password_ID.getText().toString().trim());
						startActivity(intent);
					}
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
	@Override
	public Activity getActivity() {
		return FindActivity.this;
	}
	@Override
	public Handler[] initHandler() {
		Handler [] mHandler = {this.mHandler,handler,codeHandler};
		return mHandler;
	}
//	public class MyLocationListener implements BDLocationListener {
//
//		@Override
//		public void onReceiveLocation(BDLocation location) {
//			// Receive Location
//		
//		}
//
//	}
	@Override
	public void onReceiveLocation(BDLocation arg0) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer(256);
		sb.append("time : ");
		sb.append(arg0.getTime());
		sb.append("\nerror code : ");
		sb.append(arg0.getLocType());
		sb.append("\nlatitude : ");
		sb.append(arg0.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(arg0.getLongitude());
		sb.append("\nradius : ");
		sb.append(arg0.getRadius());

		Log.e("BaiduLocationApiDem", sb.toString());
	}
}









