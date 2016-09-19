package com.ruiqi.works;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.bean.NopayDetail;
import com.ruiqi.fragment.ArrearsDeailFragment;
import com.ruiqi.fragment.ReceiDeailFragment;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 客户的欠款详情界面
 * @author Administrator
 *
 */
public class ArrearsDeails extends BaseActivity implements OnCheckedChangeListener{
	
private RadioGroup rg_select;
	
	private TextView next,tv_name_content,tv_money_content;
	private String kid,username,total;
	
	private ProgressDialog pd;
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			String result=(String) msg.obj;
			System.out.println("欠款记录"+result);
			parasData(result,msg.what);
		};
		
	};
	private ArrayList<NopayDetail> strRecei,strArrears;
	protected void parasData(String result,int what) {
		pd.dismiss();
		try {
			JSONObject jsob=new JSONObject(result);
			if(jsob.getInt("resultCode")==1){
				
				if(what==1){//欠款详情
					addData(strRecei,jsob.getJSONObject("resultInfo").getJSONArray("data"),jsob,what);
				}else if(what==2){//还款详情
					System.out.println("=====================");
					addData(strArrears,jsob.getJSONArray("resultInfo"),jsob,what);
					System.out.println("strarrears="+strArrears);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.arrears_deails);
		pd = new ProgressDialog(this);
		kid=getIntent().getStringExtra("kid");
		username=getIntent().getStringExtra("username");
		total=getIntent().getStringExtra("total");
		setTitle("欠款客户");
		strRecei=new ArrayList<NopayDetail>();
		strArrears=new ArrayList<NopayDetail>();
		getData();
		init();
	}
	
	
	
	public void addData(ArrayList<NopayDetail> arrayList,JSONArray jsa,JSONObject jsob,int what){
		
		for (int i = 0; i < jsa.length(); i++) {
			try {
				JSONObject jsb=jsa.getJSONObject(i);
				if(what==1){
					
					arrayList.add(new NopayDetail(jsb.getString("time_list"),jsb.getString("money"),jsb.getString("order_sn")));
				}else if(what==2){
					arrayList.add(new NopayDetail(jsb.getString("time_list"),jsb.getString("money"),jsb.getString("order_sn")));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		Bundle bundleArrears=new Bundle();
		bundleArrears.putSerializable("strArrears", strArrears);
		
		
		Bundle bundleRecei=new Bundle();
		bundleRecei.putSerializable("strRecei", strRecei);
		
		arrearsDeailFragment=new ArrearsDeailFragment();
		receiDeailFragment=new ReceiDeailFragment();
		arrearsDeailFragment.setArguments(bundleArrears);
		receiDeailFragment.setArguments(bundleRecei);
		getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, arrearsDeailFragment).commit();
	}

	private ArrearsDeailFragment arrearsDeailFragment;
	private ReceiDeailFragment receiDeailFragment;
	private void init() {
		rg_select = (RadioGroup) findViewById(R.id.rg_select);
		tv_name_content = (TextView) findViewById(R.id.tv_name_content);
		tv_money_content = (TextView) findViewById(R.id.tv_money_content);
		tv_name_content.setText(username);
		tv_money_content.setText("￥"+total);
		rg_select.setOnCheckedChangeListener(this);
		
		next = (TextView) findViewById(R.id.next);
		next.setOnClickListener(this);
		
		
		
		
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.next://跳转到收款界面
			Intent intent = new Intent(ArrearsDeails.this, ReceiActivity.class);
			intent.putExtra("username", username);
			intent.putExtra("kid", kid);
			intent.putExtra("total", total);
			startActivity(intent);
			break;

		default:
			break;
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
	public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.rb_qiankuan://欠款
				getSupportFragmentManager().beginTransaction().replace(R.id.rl_content,arrearsDeailFragment).commit();
				break;
			case R.id.rb_jiaokuan://缴款
				getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, receiDeailFragment).commit();
				break;

			default:
				break;
			}
	}

	public void getData(){
		pd.show();
		RequestParams params=new RequestParams(RequestUrl.NOORDERLIST);//欠款记录
		params.addBodyParameter("kid", kid);
		HttpUtil httpUtils=new HttpUtil();
		httpUtils.PostHttp(params, handler,1);
		RequestParams params1=new RequestParams(RequestUrl.REPAYMENTLIST);//还款记录
		params1.addBodyParameter("kid", kid);
		HttpUtil httpUtils1=new HttpUtil();
		httpUtils1.PostHttp(params1, handler,2);
	}

}
