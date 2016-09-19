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
import com.ruiqi.bean.TableInfo;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.works.R;

import android.app.ProgressDialog;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 配件库存的表格类fragment
 * @author Administrator
 *
 */
public class PeijianStockFragment extends BaseFragment{
	
	
	public ListView lv_unfinsh_order;
	
	public List<TabRow> table;
	
	public List<TableInfo> mDatas;
	
	public TabAdapter adapter;
	
	public ProgressDialog pd;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result);
		}
		
	};
	@Override
	public View initView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.fragmnet, null);
		lv_unfinsh_order = (ListView) view.findViewById(R.id.lv_unfinsh_order);
		pd = new ProgressDialog(getContext());
		pd.setMessage("正在加载");
		initData();
		return view;
	}
	

	public void initData() {
		mDatas = new ArrayList<TableInfo>();
		pd.show();
		String shipid = (String) SPUtils.get(getContext(), SPutilsKey.SHIP_ID, "error");
		RequestParams params = new RequestParams(RequestUrl.PJ_STOCK);
		params.addBodyParameter("shipper_id", shipid);
		HttpUtil.PostHttp(params, handler, pd);
	}
	
	private void paraseData(String result) {
		System.out.println("pjresult="+result);
		//解析数据
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				JSONArray array = obj.getJSONArray("resultInfo");
				for(int i=0;i<array.length();i++){
					JSONObject obj1 = array.getJSONObject(i);
					String id = obj1.getString("id");
					String shipper_id = obj1.getString("shipper_id");
					String goods_id = obj1.getString("goods_id");
					//String goods_name = obj1.getString("goods_name");
					String goods_kind = obj1.getString("goods_kind");
					String goods_num = obj1.getString("goods_num");
					String type = obj1.getString("goods_type");
					String name = obj1.getString("name");
					mDatas.add(new TableInfo(name, goods_num));
				}
				table = new OrderTable().addData( mDatas, initTitles());
				
				adapter = new TabAdapter(getContext(), table);
				
				lv_unfinsh_order.setAdapter(adapter);
				//解解决scroview和listview的冲突问题
				setListViewHeightBasedOnChildren(lv_unfinsh_order);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	public String[] initTitles() {
		String [] titles = {"配件名称","数量"};
		return titles;
	}


	private void setListViewHeightBasedOnChildren(ListView listView) {   
		// 获取ListView对应的Adapter   
		ListAdapter listAdapter = listView.getAdapter();   
		if (listAdapter == null) {
			return;   
		}   
		
		int totalHeight = 0;   
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   
			// listAdapter.getCount()返回数据项的数目   
			View listItem = listAdapter.getView(i, null, listView);   
			// 计算子项View 的宽高   
			listItem.measure(0, 0);    
			// 统计所有子项的总高度   
			totalHeight += listItem.getMeasuredHeight();    
		}   
		
		ViewGroup.LayoutParams params = listView.getLayoutParams();   
		params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
		// listView.getDividerHeight()获取子项间分隔符占用的高度   
		// params.height最后得到整个ListView完整显示需要的高度   
		listView.setLayoutParams(params);   
	}   
		

}
