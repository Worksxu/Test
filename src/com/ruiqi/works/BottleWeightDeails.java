package com.ruiqi.works;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.adapter.CommonAdapter;
import com.ruiqi.adapter.ViewHolder;
import com.ruiqi.bean.ApplyWeightType;
import com.ruiqi.bean.Weight;
import com.ruiqi.db.GpDao;
import com.ruiqi.fragment.WeightBootleDeailsFragment;
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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 重瓶领取详情界面
 * @author Administrator
 *
 */
public class BottleWeightDeails extends BaseActivity{
	
	private ListView lv_table;
	
	private CommonAdapter<ApplyWeightType> adapter;
	
	private List<ApplyWeightType> mDatas;
	
	private List<Weight> list;
	
	private TextView tv_total;
	
	private TextView next;
	
	private GpDao gd;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result);
		}

	};
	private static final String TAG = "BottleWeightDeails";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.weight_bootle_deails);
		
		setTitle("重瓶领取详情");
		gd = GpDao.getInstances(this);
		
		init();
		initDatas();
		
		adapter = new CommonAdapter<ApplyWeightType>(BottleWeightDeails.this,mDatas,R.layout.lv_table_item) {
			
			@Override
			public void convert(ViewHolder helper, ApplyWeightType item,int position) {
				helper.setText(R.id.tv_type, item.getType());
				helper.setText(R.id.tv_num, item.getNum());
			}
		};
		
		lv_table.setAdapter(adapter);
	}

	private void initDatas() {
		int total = 0;
		list = (List<Weight>) getIntent().getSerializableExtra("mData");
		mDatas = new ArrayList<ApplyWeightType>();
		for(int i=0;i<list.size();i++){
			mDatas.add(new ApplyWeightType(list.get(i).getXinpian(), "1"));
		}
		for(int i =0;i<list.size();i++){
			total+=Double.parseDouble(list.get(i).getStatus());
		}
		tv_total.setText(total+"");
	}

	private void init() {
		lv_table = (ListView) findViewById(R.id.lv_table);
		tv_total= (TextView) findViewById(R.id.tv_total);
		next = (TextView) findViewById(R.id.next);
		next.setOnClickListener(this);
	}
	
	private boolean flag = true;
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.next:
			if(flag){
				sure();
				Log.d(TAG, "点击");
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public void jumpPage() {
		super.jumpPage();
	}
	
	private String shipper_id;
	private void sure() {
		//如果没有瓶，不请求服务器
		if(mDatas.size()==0){
			T.showShort(BottleWeightDeails.this, "还没扫描瓶");
		}else{
			flag = false;
			
			String shop_id = (String) SPUtils.get(BottleWeightDeails.this, SPutilsKey.SHOP_ID, "error");
			shipper_id = (String) SPUtils.get(BottleWeightDeails.this, SPutilsKey.SHIP_ID, "error");
			String name = (String) SPUtils.get(BottleWeightDeails.this, "shipper_name", "error");
			
			RequestParams params = new RequestParams(RequestUrl.IN_SHIPER);
			params.addBodyParameter("shop_id", shop_id);
			params.addBodyParameter("shipper_id", shipper_id);
			params.addBodyParameter("data", getJsonStr(mDatas));
			params.addBodyParameter("type_id", "1");
			params.addBodyParameter("username", name);
			HttpUtil.PostHttp(params, handler, new ProgressDialog(BottleWeightDeails.this));
			System.out.println("data="+ getJsonStr(mDatas));
			System.out.println("name="+name);
		}
	}
	
	private void paraseData(String result) {
		System.out.println("result="+result);
		//json解析
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
						
				//String resultInfo = obj.getString("resultInfo");
				Toast.makeText(BottleWeightDeails.this, "领瓶成功", Toast.LENGTH_SHORT).show();
				if(NfcActivity.mDataRecei!=null){
					NfcActivity.mDataRecei=null;
				}
				//将钢瓶芯片号存到本地数据库中
//				for(int i=0;i<mDatas.size();i++){
//					gd.saveOrder(mDatas.get(i).getType(),shipper_id);
//				}
				//返回到主界面
				Intent intent = new Intent(BottleWeightDeails.this, MainActivity.class);
				startActivity(intent);
				BottleWeightDeails.this.finish();
			}else{
				Toast.makeText(BottleWeightDeails.this, "领瓶失败", Toast.LENGTH_SHORT).show();
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
	
	 /**
     * 得到传出的data
     */
    private String  getJsonStr(List<ApplyWeightType > sList){
    	JSONArray array = new JSONArray();
    	for(int i=0;i<sList.size();i++){
    		array.put(sList.get(i).getType());
    	}
    	return array.toString();
    }

}
