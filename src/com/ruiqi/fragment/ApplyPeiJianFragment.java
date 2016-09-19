package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ruiqi.bean.OutIn;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.chai.ListViewAdapterText3;
import com.ruiqi.chai.PartsDetail;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.SerializableMap;
import com.ruiqi.view.AutoListView;
import com.ruiqi.view.AutoListView.OnLoadListener;
import com.ruiqi.view.AutoListView.OnRefreshListener;
import com.ruiqi.works.R;

/**
 * 配件领取fragment
 * 
 * @author Administrator
 *
 */
public class ApplyPeiJianFragment extends BaseFragment implements OnItemClickListener,OnRefreshListener, OnLoadListener,ParserData{

	private AutoListView alv_content;
	private ListViewAdapterText3 adapter;
	private ArrayList<HashMap<String, String>> arrayList =new ArrayList<HashMap<String,String>>();
	private int page=1;
	
	private List<TableInfo> mDatas;//子项的表格类数据
	public List<OutIn> list; //listview的数据
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			
				List<HashMap<String, String>> result = (List<HashMap<String, String>>) msg.obj;
				switch (msg.what) {
				case AutoListView.REFRESH:
					alv_content.onRefreshComplete();
					arrayList.clear();
					HashMap<String, String> hm = new HashMap<String, String>();
					hm.put("title1", "订单号");
					hm.put("title2", "状态");
					hm.put("title3", "时间");
					arrayList.add(hm);
					arrayList.addAll(result);
					break;
				case AutoListView.LOAD:
					alv_content.onLoadComplete();
					arrayList.addAll(result);
					break;
				}
				alv_content.setResultSize(result.size());
				adapter.notifyDataSetChanged();
			}
			
	};
	
	
	@Override
	public View initView() {
		initData();
		View view = LayoutInflater.from(getContext()).inflate(R.layout.apply_peijian_fragment, null);
		alv_content = (AutoListView) view.findViewById(R.id.alv_content);
		alv_content.setOnItemClickListener(this);
		adapter = new ListViewAdapterText3(getContext(), arrayList);
		alv_content.setAdapter(adapter);
		alv_content.setOnRefreshListener(this);
		alv_content.setOnLoadListener(this);
		alv_content.setOnItemClickListener(this);
		return view;
	}
	private HttpUtil httpUtil;
	private ArrayList<HashMap<String, String>> orderList;
	private void initData() {
		orderList = new ArrayList<HashMap<String, String>>();
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
		loadData(AutoListView.REFRESH);
	}
	private void loadData(final int what) {
		// 这里模拟从服务器获取数据
		list = new ArrayList<OutIn>();
		String shipper_id = (String) SPUtils.get(getContext(), SPutilsKey.SHIP_ID, "error");
		String shop_id = (String) SPUtils.get(getContext(), SPutilsKey.SHOP_ID, "error");
		RequestParams params = new RequestParams(RequestUrl.SHIPPERPRODUCT);
		params.addBodyParameter("shipper_id", shipper_id);
		params.addBodyParameter("shop_id", shop_id);
		//params.addBodyParameter("page", page+"");
		httpUtil.PostHttp(params, what);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(position>1&& position<alv_content.getCount()-1){
			Intent intent = new Intent(getContext(), PartsDetail.class);
			
			SerializableMap sMap = new SerializableMap();
			HashMap<String, String> map = arrayList.get(position-1);
			sMap.setMap(map);// 将map数据添加到封装的myMap<span></span>中
			Bundle bundle = new Bundle();
			bundle.putSerializable("sMap", sMap);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}
	
	@Override
	public void onLoad() {
		page=1;
		loadData(AutoListView.LOAD);
	}
	@Override
	public void onRefresh() {
		page++;
		loadData(AutoListView.REFRESH);
	}

	@Override
	public void sendResult(String result,int what) {
		System.out.println("pjresult="+result);
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				orderList.clear();
				if(what==AutoListView.REFRESH){
					list.clear();
				}
				JSONArray array = obj.getJSONArray("resultInfo");
				for(int i=0;i<array.length();i++){
					JSONObject obj1 = array.getJSONObject(i);
					String product_no = obj1.getString("product_no");
				//	JSONArray array1 = obj1.getJSONArray("products");
					
					HashMap<String, String> hm=new HashMap<String, String>();
					
					hm.put("status", obj1.getString("status"));
					
					hm.put("title1", product_no);
					if(obj1.getString("status").equals("0")){
						hm.put("title2", "未确认");
					}else{
						hm.put("title2", "已确认");
					}
					hm.put("title3", obj1.getString("time"));
					hm.put("good_name", obj1.getString("good_name"));
					hm.put("good_num", obj1.getString("good_num"));
					hm.put("id", obj1.getString("id"));
					//hm.put("time", obj1.getString("time"));
					
					
					orderList.add(hm);
//					String good_name = obj1.getString("good_name");
//					String good_num = obj1.getString("good_num");
//					String good_typename = obj1.getString("good_name");
//					mDatas.add(new TableInfo(good_name,good_typename, good_num));
//					list.add(new OutIn(product_no, "", mDatas));
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Message msg = handler.obtainMessage();
		msg.obj = orderList;
		msg.what = what;
		handler.sendMessage(msg);
	}
}
