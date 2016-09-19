package com.ruiqi.works;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruiqi.db.OrderDao;
import com.ruiqi.utils.ChaiMyDialog;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.MyDialog;
import com.ruiqi.utils.ChaiMyDialog.ChaiCallBack;
import com.ruiqi.utils.MyDialog.StrCallBack;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.T;

//修改订单界面
public class ModifiyOrderActivity extends BaseActivity implements OnCheckedChangeListener,ChaiCallBack{
	
	private TextView tv_order_content,tv_name_content,tv_mobile_content,tv_address_content;
	
	private TextView tv_money_content,tv_yunfei_content,tv_total_content,tv_modify_status;
	
	private String ordersn,username,mobile,address,status,kid,total,yunfei,all;
	
	private Intent intent;
	
	private TextView motifiy_save,tv_card_sn;
	
	private ProgressDialog pd;
	
	private ImageView motifiy_name,motifiy_mobile,motifiy_address,iv_modify_status;
	
	private MyDialog myDialog;
	
	private OrderDao od;
	
	private LinearLayout rl_status_content,rl_card_sn;
	private CheckBox ck_content_one;
	private EditText et_content;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			parseData(result);
		}

		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_order);
		pd = new ProgressDialog(this);
		pd.setMessage("正在保存......");
		od = OrderDao.getInstances(this);
		init();
		
		initData();
		
		 myDialog= new MyDialog();
		//getSupportFragmentManager().beginTransaction().replace(R.id.rl_fragment, new OrderInfoFragment()).commit();
	}
	
	private void initData() {
		intent = getIntent();
		ordersn = intent.getStringExtra("ordersn");
		username = intent.getStringExtra("username");
		mobile = intent.getStringExtra("mobile");
		address = intent.getStringExtra("address");
		yunfei = intent.getStringExtra("yunfei");
		total = intent.getStringExtra("total");
		kid = intent.getStringExtra("kid");
		
		all = intent.getStringExtra("all");
		status = intent.getStringExtra("status");
		
		finalStatus = status;
		
		tv_order_content.setText(ordersn);
		tv_name_content.setText(username);
		tv_mobile_content.setText(mobile);
		tv_address_content.setText(address);
		tv_money_content.setText(total);
		tv_yunfei_content.setText(yunfei);
		tv_total_content.setText(all);
		
		System.out.println("status="+status);
		if("2".equals(status)){
			tv_modify_status.setText("正常订单");
		}else if("5".equals(status)){
			tv_modify_status.setText("问题订单");
		}else{
			tv_modify_status.setText("");
		}
	}

	private void init() {
		et_content = (EditText) findViewById(R.id.et_content);
		ck_content_one = (CheckBox) findViewById(R.id.ck_content_one);
		ck_content_one.setOnCheckedChangeListener(this);
		rl_status_content = (LinearLayout) findViewById(R.id.rl_status_content);
		rl_card_sn = (LinearLayout) findViewById(R.id.rl_card_sn);
		tv_order_content = (TextView) findViewById(R.id.tv_order_content);
		
		tv_name_content = (TextView) findViewById(R.id.tv_name_content);
		
		tv_mobile_content = (TextView) findViewById(R.id.tv_mobile_content);
		
		tv_address_content = (TextView) findViewById(R.id.tv_address_content);
		
		tv_money_content = (TextView) findViewById(R.id.tv_money_content);
		
		tv_yunfei_content = (TextView) findViewById(R.id.tv_yunfei_content);
		tv_card_sn = (TextView) findViewById(R.id.tv_card_sn);//用户卡号
		
		tv_total_content = (TextView) findViewById(R.id.tv_total_content);
		
		tv_modify_status = (TextView) findViewById(R.id.tv_modify_status);
		motifiy_save = (TextView) findViewById(R.id.motifiy_save);
		motifiy_save.setOnClickListener(this);
		
		motifiy_name = (ImageView) findViewById(R.id.motifiy_name);
		motifiy_name.setOnClickListener(this);
		rl_card_sn.setOnClickListener(this);
		motifiy_mobile = (ImageView) findViewById(R.id.motifiy_mobile);
		motifiy_mobile.setOnClickListener(this);
		motifiy_address = (ImageView) findViewById(R.id.motifiy_address);
		motifiy_address.setOnClickListener(this);
		iv_modify_status = (ImageView) findViewById(R.id.iv_modify_status);
		iv_modify_status.setOnClickListener(this);
	}
	
	private String finalStatus;
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.motifiy_save://保存修改
			requestData();
			break;
		case R.id.motifiy_name://修改名字
			myDialog.show(ModifiyOrderActivity.this,tv_name_content.getText().toString().trim(),2);
			myDialog.setCallBack(new StrCallBack() {
				@Override
				public void callBack(String str) {
					tv_name_content.setText(str);
				}
			});
			break;
		case R.id.motifiy_mobile://修改电话
			myDialog.show(ModifiyOrderActivity.this,tv_mobile_content.getText().toString().trim(),1);
			myDialog.setCallBack(new StrCallBack() {
				@Override
				public void callBack(String str) {
					tv_mobile_content.setText(str);
				}
			});
			break;
		case R.id.motifiy_address://修改地址
			myDialog.show(ModifiyOrderActivity.this,tv_address_content.getText().toString().trim(),2);
			myDialog.setCallBack(new StrCallBack() {
				@Override
				public void callBack(String str) {
					tv_address_content.setText(str);
				}
			});   
			break;
		case R.id.rl_card_sn://填写卡号
			ChaiMyDialog.getInstance().setCallBack(this);
			ChaiMyDialog.getInstance().show(ModifiyOrderActivity.this, "请输入用户卡号",6);
			break;
		case R.id.iv_modify_status://修改订单状态
			//弹出单线对话框
			myDialog.showRadio(ModifiyOrderActivity.this,finalStatus);
			myDialog.setCallBack(new StrCallBack() {
				@Override
				public void callBack(String str) {
					if("问题订单".equals(str)){
						finalStatus = "5";
						rl_status_content.setVisibility(View.VISIBLE);
					}else if("正常订单".equals(str)){
						finalStatus = "2";
						rl_status_content.setVisibility(View.GONE);
					}else{
						finalStatus = status;
						rl_status_content.setVisibility(View.GONE);
					}
					
					if("2".equals(finalStatus)){
						tv_modify_status.setText("正常订单");
					}else if("5".equals(finalStatus)){
						tv_modify_status.setText("问题订单");
					}
					
				}
			});
			break;
			
		default:
			break;
		}
	}
	
	
	//请求网络
	private void requestData() {
	
		
		RequestParams params = new RequestParams(RequestUrl.EDITOR_ORDER);
		params.addBodyParameter("ordersn", ordersn);
		if(!tv_name_content.getText().toString().equals(username)){
			params.addBodyParameter("username", tv_name_content.getText().toString());
		}
		if(!tv_mobile_content.getText().toString().equals(mobile)){
			params.addBodyParameter("mobile", tv_mobile_content.getText().toString());
		}
		if(!tv_address_content.getText().toString().equals(address)){
			params.addBodyParameter("address", tv_address_content.getText().toString());
		}
		if(!tv_card_sn.getText().toString().trim().equals("")){//不为空字符
			params.addBodyParameter("card_sn", tv_card_sn.getText().toString());//card_sn
		}
		if(finalStatus.equals("5")){//问题订单
			if(TextUtils.isEmpty(et_content.getText().toString().trim())){
				ChaiMyDialog.getInstance().showHint(this, "请填写问题备注");
				return ;
			}
			params.addBodyParameter("comment", et_content.getText().toString().trim());//card_sn
		}
		params.addBodyParameter("kid",kid);//card_sn
		if(finalStatus!=null){
			if(!finalStatus.equals(status)){
				params.addBodyParameter("status", finalStatus);
			}
		};
		Log.e("lll参数",params.getStringParams().toString());
		pd.show();
		HttpUtil.PostHttp(params, handler, pd);
	}
	
	private void parseData(String result) {
		System.out.println("result="+result);
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				//修改订单的本地数据库
				od.updateFromOrdersn(tv_name_content.getText().toString(), tv_mobile_content.getText().toString(),tv_address_content.getText().toString(), finalStatus, ordersn);
				T.showShort(ModifiyOrderActivity.this, "订单修改成功");
				Intent intent = new Intent(ModifiyOrderActivity.this,OrderInfoActivity.class);
				intent.putExtra("name",tv_name_content.getText().toString() );
				intent.putExtra("mobile",tv_mobile_content.getText().toString() );
				intent.putExtra("address",tv_address_content.getText().toString() );
				intent.putExtra("status",finalStatus );
				setResult(2,intent);
				finish();
			}else{
				T.showShort(ModifiyOrderActivity.this, "修改失败");
			}
		} catch (JSONException e) {
			e.printStackTrace();
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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(buttonView.getId()==R.id.ck_content_one){
			if(isChecked){
				et_content.setText(ck_content_one.getText().toString().trim());
			}else{
				et_content.setText("");
			}
		}
	}

	@Override
	public void chaiStringCallBack(String str) {
		tv_card_sn.setText(str);
	}

	@Override
	public void chaiIntCallBack(int in,int where) {
		// TODO Auto-generated method stub
		
	}

}


























