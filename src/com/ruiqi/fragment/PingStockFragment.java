package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ruiqi.Table.OrderTable;
import com.ruiqi.Table.TabAdapterNoTitle;
import com.ruiqi.Table.TabRow;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.chai.ChaiBottleToShop;
import com.ruiqi.chai.ChaiBottleToShopZheJiu;
import com.ruiqi.chai.ChaiTableInfo;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.works.R;

/**
 * 钢瓶库存fragment
 * @author Administrator
 *
 */
public class PingStockFragment extends BaseFragment implements OnClickListener{
	
	private ListView lv_content,lv_content_null,lv_content_not;
	
	private List<TabRow> table;
	
	private List<TableInfo> mWeightDatas;//重瓶的数据
	private List<TableInfo> mNullDatas;//空瓶的数据
	private List<TableInfo> mDatas;//废瓶的数据
	private ArrayList<ChaiTableInfo> arrayList=new ArrayList<ChaiTableInfo>();//折旧瓶
	
	private TabAdapterNoTitle adapter;
	
	private TextView tv_weight;
	private TextView tv_null;
	private TextView tv_not;
	
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result);
		}

		
	};
	@Override
	public View initView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.ping_stock_fragment, null);
		
		tv_weight = (TextView) view.findViewById(R.id.tv_weight);
		tv_null = (TextView) view.findViewById(R.id.tv_null);
		tv_not = (TextView) view.findViewById(R.id.tv_not);
		
		tv_weight.setOnClickListener(this);
		tv_null.setOnClickListener(this);
		tv_not.setOnClickListener(this);
		
		
		lv_content = (ListView) view.findViewById(R.id.lv_content);
		lv_content_null = (ListView) view.findViewById(R.id.lv_content_null);
		lv_content_not = (ListView) view.findViewById(R.id.lv_content_not);
		
		initData();
		return view;
	}
	
	private void paraseData(String result) {
		Log.e("lll", "刚瓶库存"+result);
		
		//json解析
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				JSONObject obj1 = obj.getJSONObject("resultInfo");
				String total = obj1.getString("total");
				JSONArray array = obj1.getJSONArray("data");
				for(int i=0;i<array.length();i++){
					System.out.println("==================");
					JSONObject object = array.getJSONObject(i);
					String title = object.getString("title");
					String typename = object.getString("typename");
					String typeid = object.getString("typeid");
					int num = object.getInt("num");
					if("重瓶".equals(typename)){
						mWeightDatas.add(new TableInfo(title, num+""));
					}else if("空瓶".equals(typename)){
						mNullDatas.add(new TableInfo(title, num+""));
					}else {
						mDatas.add(new TableInfo(title, num+""));
						arrayList.add(new ChaiTableInfo(title, num+"",typeid));
					}
				}
				
				System.out.println("mWeightDatas="+mWeightDatas);
				System.out.println("mNullDatas="+mNullDatas);
				System.out.println("mDatas="+mDatas);
				
				setDataToListView(mWeightDatas, tv_weight, lv_content);
				setDataToListView(mNullDatas, tv_null, lv_content_null);
				setDataToListView(mDatas, tv_not, lv_content_not);
				
				CurrencyUtils.setListViewHeightBasedOnChildren(lv_content);
				CurrencyUtils.setListViewHeightBasedOnChildren(lv_content_null);
				CurrencyUtils.setListViewHeightBasedOnChildren(lv_content_not);
			}
		}catch (Exception e) {
			}
	}

	

	private void initData() {
		mWeightDatas = new ArrayList<TableInfo>();
		mNullDatas = new ArrayList<TableInfo>();
		mDatas = new ArrayList<TableInfo>();
		String shipid = (String) SPUtils.get(getContext(), SPutilsKey.SHIP_ID, "error");
		RequestParams params = new RequestParams(RequestUrl.STOCK_LIST);
		params.addBodyParameter("shipper_id", shipid);
		HttpUtil.PostHttp(params, handler, new ProgressDialog(getContext()));
	}
	
	private void setDataToListView(List<TableInfo> datas,TextView tv,ListView lv){
		table = new OrderTable().addDataNoTitle( datas);
		
		adapter = new TabAdapterNoTitle(getContext(), table,tv,null);
		
		lv.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {

		Intent it=new Intent(getContext(),ChaiBottleToShop.class);
		switch (v.getId()) {
		case R.id.tv_weight://重瓶
			it.putExtra("type", "1");
			startActivity(it);
			break;
		case R.id.tv_null://空瓶
			it.putExtra("type", "0");
			startActivity(it);
			break;
		case R.id.tv_not://折旧
			Intent itc=new Intent(getContext(),ChaiBottleToShopZheJiu.class);
			itc.putExtra("data", arrayList);
			startActivity(itc);
			break;
    
		default:
			break;
		}
	}

}

























