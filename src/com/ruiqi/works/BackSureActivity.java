package com.ruiqi.works;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.bean.BackBottle;
import com.ruiqi.bean.Bottle;
import com.ruiqi.bean.Weight;
import com.ruiqi.db.BackBottleDao;
import com.ruiqi.fragment.BackSureFragment;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;

/**
 * 退瓶的确定界面
 * @author Administrator
 *
 */
public class BackSureActivity extends BaseActivity{
	
	private TextView tv_yunfei_title,tv_songqi_title;
	
	
	private String depositsn;
	
	private BackBottleDao od;
	
	private String username,mobile,address,time,doormoney,money,productmoney,id,kid;
	
	private TextView tv_name,tv_phone,tv_address,tv_time,tv_money,tv_yunfei_money,tv_pay,tv_songqi_money;
	
	private String canye,canye_weight;
	
	private List<Weight> list;
	
	private BackBottleDao bbd;
	
	private RelativeLayout rl_out_money,rl_youhui;
	
	private TextView tv_should_pay;
	private TextView tv_order_commit;
	private RadioGroup rg_type;
	
	private TextView tv_back_modify;
	private RelativeLayout rl_pj;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			
			paraseData(result);
		}

	};
	
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backsure1);
		setTitle("退瓶确定");
		bbd = BackBottleDao.getInstances(this);
		pd = new ProgressDialog(this);
		pd.setMessage("正在提交......");
		init();
		initData();
		BackSureFragment bsf = new BackSureFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("mData", (Serializable)list);
		bsf.setArguments(bundle);
		getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, bsf).commit();
		
	}
	private void initData() {
		//list = (List<Weight>) getIntent().getSerializableExtra("mData");
		list = new ArrayList<Weight>();
		canye = getIntent().getStringExtra("money");
		canye_weight = getIntent().getStringExtra("canye_weight");
		od = BackBottleDao.getInstances(this);
		depositsn = (String) SPUtils.get(BackSureActivity.this, "depositsn", "error");
		Cursor c = od.getFromOrderSn(depositsn);
		while (c.moveToNext()) {
			username = c.getString(c.getColumnIndex("username"));
			mobile = c.getString(c.getColumnIndex("mobile"));
			address = c.getString(c.getColumnIndex("address"));
			time = c.getString(c.getColumnIndex("time"));
			doormoney = c.getString(c.getColumnIndex("doormoney"));
			money = c.getString(c.getColumnIndex("money"));
			productmoney = c.getString(c.getColumnIndex("productmoney"));
			id = c.getString(c.getColumnIndex("id"));
			kid = c.getString(c.getColumnIndex("kid"));
		}
		
		if(BackBottleOrder.mData!=null){
			for(int i=0;i<BackBottleOrder.mData.size();i++){
				BackBottle b = BackBottleOrder.mData.get(i);
				if(b.getDepositsn().equals(depositsn)){
					
					List<Bottle> mlist = b.getList();
					for(int j=0;j<mlist.size();j++){
						list.add(new Weight(mlist.get(j).getXinpian(), mlist.get(j).getWeight(), mlist.get(j).getType()));
					}
				}
			}
		}
		
		tv_name.setText(username);
		tv_phone.setText(mobile);
		tv_address.setText(address);
		tv_time.setText(time);
		tv_money.setText(productmoney);
		tv_yunfei_money.setText(doormoney);
		//tv_pay.setText(productmoney);
		
		tv_songqi_money.setText(canye);
		
		tv_should_pay.setText( Double.parseDouble(productmoney)-Double.parseDouble(doormoney)+Double.parseDouble(canye)+"");
		
		tv_order_commit.setText("确定付款");
		tv_order_commit.setOnClickListener(this);
	}
	private void init() {
		tv_yunfei_title = (TextView) findViewById(R.id.tv_yunfei_title);
		tv_songqi_title = (TextView) findViewById(R.id.tv_songqi_title);
		
		tv_yunfei_title.setText("上门费");
		tv_songqi_title.setText("残液");
		
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_money = (TextView) findViewById(R.id.tv_money);
		
		tv_money.setOnClickListener(this);
		tv_yunfei_money = (TextView) findViewById(R.id.tv_yunfei_money);
		//tv_pay = (TextView) findViewById(R.id.tv_pay);
		tv_songqi_money = (TextView) findViewById(R.id.tv_songqi_money);
		
		rl_out_money = (RelativeLayout) findViewById(R.id.rl_out_money);
		rl_out_money.setVisibility(View.GONE);
		
		tv_should_pay = (TextView) findViewById(R.id.tv_pay);
		tv_order_commit = (TextView) findViewById(R.id.tv_order_commit);
		
		rl_youhui = (RelativeLayout) findViewById(R.id.rl_youhui);
		rl_youhui.setVisibility(View.GONE);
		
		rg_type = (RadioGroup) findViewById(R.id.rg_type);
		rg_type.setVisibility(View.GONE);
		
		tv_back_modify = (TextView) findViewById(R.id.tv_back_modify);
		tv_back_modify.setVisibility(View.GONE);
		
		rl_pj = (RelativeLayout) findViewById(R.id.rl_pj);
		rl_pj.setVisibility(View.GONE);
		
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_order_commit://确定付款
			surePay();
			break;
		case R.id.tv_money://确定付款
			changeYaJIn();
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			Intent intent =  new Intent(BackSureActivity.this, BackSaoMiaoActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void jumpPage() {
		super.jumpPage();
		Intent intent =  new Intent(BackSureActivity.this, BackSaoMiaoActivity.class);
		startActivity(intent);
		finish();
	}
	/**
	 * 确定付款
	 */
	private void surePay() {
		pd.show();
		System.out.println("mDataBottle="+NfcActivity.mDataBottle);
		String shop_id = (String) SPUtils.get(BackSureActivity.this, SPutilsKey.SHOP_ID, "error");
		String shipper_id = (String) SPUtils.get(BackSureActivity.this, SPutilsKey.SHIP_ID, "error");
		RequestParams params = new RequestParams(RequestUrl.CONFIRM_DEPOSIT);
		params.addBodyParameter("deposit_id", id);
		params.addBodyParameter("money",tv_should_pay.getText().toString());
		params.addBodyParameter("kid", kid);
		params.addBodyParameter("bottle", getJsonStr(NfcActivity.mDataBottle));//扫的瓶
		params.addBodyParameter("canye_money", canye);
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("shipper_id", shipper_id);
		
		System.out.println("bottle="+getJsonStr(NfcActivity.mDataBottle));
		System.out.println("kid="+kid);
		System.out.println("canye="+canye);
		System.out.println("shop_id="+shop_id);
		System.out.println("shipper_id="+shipper_id);
		
		HttpUtil.PostHttp(params, handler, pd);
		
	}
	
	private void paraseData(String result) {
		System.out.println("result="+result);
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				bbd.updateStatus("2", depositsn);
				//修改总额
				bbd.updatePayMoney(tv_should_pay.getText().toString(), depositsn);
				Toast.makeText(BackSureActivity.this, "退押金成功", Toast.LENGTH_SHORT).show();
				if(NfcActivity.mDataBottle!=null){
					NfcActivity.mDataBottle=null;
				}
				//退押金成功
				//跳转到主页
				Intent intent = new Intent(BackSureActivity.this, MainActivity.class);
				startActivity(intent);
				this.finish();
				
			}else{
				Toast.makeText(BackSureActivity.this, "退押金失败", Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 拼接json串
	 */
    private String  getJsonStr(List<Weight > sList){
    	JSONArray array = new JSONArray();
    	for(int i=0;i<sList.size();i++){
    		array.put(sList.get(i).getXinpian());
    	}
    
    	return array.toString();
    }
	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public Handler[] initHandler() {
		return null;
	}
	
public  void changeYaJIn(){
		
		AlertDialog.Builder builder = new Builder(this);
		 builder.setTitle("请输入押金");
		final EditText tv = new EditText(this);
		tv.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
		tv.setTextSize(20);
		tv.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		tv.setGravity(Gravity.CENTER);
		tv.setPadding(0, 20, 0, 0);
		builder.setView(tv);
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(!TextUtils.isEmpty(tv.getText())){
					
					tv_should_pay.setText( Double.parseDouble(tv.getText().toString().trim())-Double.parseDouble(doormoney)+Double.parseDouble(canye)+"");
					tv_money.setText(""+Double.parseDouble(tv.getText().toString().trim()));
					productmoney=""+Double.parseDouble(tv.getText().toString().trim());
				}
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
	}

	
}
