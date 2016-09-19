package com.ruiqi.works;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.bean.Nopay;
import com.ruiqi.fragment.ArreasFragment;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 欠款客户界面
 * @author Administrator
 *
 */
public class ArrearsUserActivity extends BaseActivity{
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			System.out.println(result);
			parsData(result);
		};

	};

	private TextView tv_num,tv_money;
	private ImageView select;
	private EditText et_input;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_arrears_user);
		pd = new ProgressDialog(this);
		pd.setMessage("正在加载中.....");

		setTitle("欠款账户");
		tv_num=(TextView) findViewById(R.id.tv_num);
		tv_money=(TextView) findViewById(R.id.tv_money);
		select = (ImageView) findViewById(R.id.select);
		select.setOnClickListener(this);
		et_input = (EditText) findViewById(R.id.et_input);
		arrayList=new ArrayList<Nopay>();
		getData(null);
	}
	
	private float money;
	private ArrayList<Nopay> arrayList;
	private ArreasFragment arreasFragment;
	protected void parsData(String result) {
		try {
			money=0f;
			JSONObject jsob = new JSONObject(result);
			if (jsob.getInt("resultCode") == 1) {
				arrayList.clear();
				if("null".equals(jsob.getString("resultInfo"))){
					T.showShort(ArrearsUserActivity.this, "没有该客户的欠款记录");
				}else{
					
					JSONArray jsa = jsob.getJSONArray("resultInfo");
					tv_num.setText(""+jsa.length());
					for (int i = 0; i < jsa.length(); i++) {
						JSONObject jsb=jsa.getJSONObject(i);
						String str=jsb.getString("total");
						if(str.equals("null")){
							money=0f;
						}else{
							money+=Float.parseFloat(str);
						}
						String kehu_type=jsb.getString("kehu_type");
						String type_name = jsb.getString("type_name");
						String total=jsb.getString("total");
						String kid=jsb.getString("kid");
						String username=jsb.getString("username");
						arrayList.add(new Nopay(type_name,kid,total,username));
					}
					tv_money.setText("￥"+money);
					Bundle bundle=new Bundle();
					bundle.putSerializable("data", arrayList);
					arreasFragment=new ArreasFragment();
					arreasFragment.setArguments(bundle);
					getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, arreasFragment).commit();
				}

			} else {

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

	private ProgressDialog pd;

	public void getData(String str) {

		RequestParams params = new RequestParams(RequestUrl.PAYLIST);
		params.setConnectTimeout(6000);
		params.addBodyParameter("shipper_id",(String) SPUtils.get(this, SPutilsKey.SHIP_ID, "11"));
		if(str!=null){
			params.addBodyParameter("username", str);
		}
		System.out.println("shipper_id="+(String) SPUtils.get(this, SPutilsKey.SHIP_ID, "11"));
		System.out.println("str="+str);
		HttpUtil httpUtil = new HttpUtil();
		pd.show();
		httpUtil.PostHttp(params, handler, pd);
	}
	
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.select:
			//根据姓名请求网络
			String str = et_input.getText().toString().trim();
			if(str==null||str.equals("")){
				T.showShort(ArrearsUserActivity.this, "请输入客户姓名");
			}else{
				
				getData(str);
			}
			break;
			
		default:
			break;
		}
	}

}
