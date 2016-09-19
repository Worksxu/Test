package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.Table.OrderTable;
import com.ruiqi.Table.TabAdapter;
import com.ruiqi.Table.TabRow;
import com.ruiqi.bean.OutToShop;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.works.R;

import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

/**
 * 支出的fragment
 * @author Administrator
 *
 */
public class MoneyOutFragment extends BaseFragment implements OnCheckedChangeListener{
	
	private ListView lv_content;
	
	public List<TabRow> table;
	
	public List<TableInfo> mDatas;
	
	public TabAdapter adapter;
	
	private RadioGroup rg_select;
	private RelativeLayout rl_content;

	private String shipper_id;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result);
		}
	};
	
	private Handler orderhandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			parseData(result);
		}
	};
	@Override
	public View initView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.money_out_fragment, null);
		
		lv_content = (ListView) view.findViewById(R.id.lv_content);
		rg_select = (RadioGroup) view.findViewById(R.id.rg_select);
		rg_select.setOnCheckedChangeListener(this);
		rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
		
		shipper_id = (String) SPUtils.get(getContext(), SPutilsKey.SHIP_ID, "error");
		
		initDataOrder();
		return view;
	}
	private List<OutToShop> list;
	private void paraseData(String result) {
		list = new ArrayList<OutToShop>();
		System.out.println("result="+result);
		//解析
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				System.out.println("mDatas="+mDatas);
				JSONArray array = obj.getJSONArray("resultInfo");
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj1 = array.getJSONObject(i);
					String money = obj1.getString("money");
					String shop_id = obj1.getString("shop_id");
					String shop_name = obj1.getString("shop_name");
					String time = obj1.getString("time");
					
					String str="";
					String status=obj1.getString("status");
					if(status.equals("0")){
						str="未审核";
					}else if(status.equals("1")){
						str="已审核";
					}
					mDatas.add(new TableInfo(money, shop_name, time,str));
				}
				setAdapterToListView(mDatas, initTitles());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}
	//上缴给门店的支出
	
	private void initData() {
		mDatas = new ArrayList<TableInfo>();
		
		RequestParams params = new RequestParams(RequestUrl.OUT);
		params.addBodyParameter("shipper_id", shipper_id);
		params.addBodyParameter("goodtime", CurrencyUtils.getNowTime());
		HttpUtil.PostHttp(params, handler, new ProgressDialog(getContext()));
	}
	//退瓶订单的支出
	private List<TableInfo> mList;
	private void initDataOrder(){
		mList = new ArrayList<TableInfo>();
		RequestParams params = new RequestParams(RequestUrl.DEPOSIT_ORDER);
		params.addBodyParameter("shipper_id", shipper_id);
		params.addBodyParameter("goodtime", CurrencyUtils.getNowTime());
		HttpUtil.PostHttp(params, orderhandler, new ProgressDialog(getContext()));
		
	}
	private void parseData(String result) {
		Log.e("lll", "上交记录"+result);
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				JSONArray array = obj.getJSONArray("resultInfo");
				for(int i=0;i<array.length();i++){
					JSONObject obj1 = array.getJSONObject(i);
					String depositsn = obj1.getString("depositsn");
					String money = obj1.getString("money");
					String time = obj1.getString("time");
						mList.add(new TableInfo(depositsn, money, time));
				}
				setAdapterToListView(mList,initTitlesOrder());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	private void setAdapterToListView(List<TableInfo> mData,String [] titles){
		table = new OrderTable().addData( mData, titles);
		
		adapter = new TabAdapter(getContext(), table);
		
		lv_content.setAdapter(adapter);
		
	}
	//订单支出的表头
	private String[] initTitlesOrder() {
		String [] titles = {"单号","金额","日期"};
		return titles;
	}
	//上缴给门店的表头
	private String[] initTitles() {
		String [] titles = {"金额","收款人","日期","状态"};
		return titles;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_mendian_out://上缴支出
			initData();
			//rl_content.removeAllViews();
			//rl_content.addView(lv_content);
			setAdapterToListView(mDatas,initTitles());
			break;
		case R.id.rb_order_out://订单支出
			initDataOrder();
			//rl_content.removeAllViews();
			//rl_content.addView(lv_content);
			setAdapterToListView(mList,initTitlesOrder());
			break;

		default:
			break;
		}
	}

}
