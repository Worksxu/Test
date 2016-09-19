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
import com.ruiqi.bean.BackBottle;
import com.ruiqi.bean.Bottle;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.db.BackBottleDao;
import com.ruiqi.db.OrderDao;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.view.AutoListView;
import com.ruiqi.view.AutoListView.OnLoadListener;
import com.ruiqi.view.AutoListView.OnRefreshListener;
import com.ruiqi.works.BackBottleActivity;
import com.ruiqi.works.BackBottleOrder;
import com.ruiqi.works.OrderInfoActivity;
import com.ruiqi.works.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TableRow;

public class FinshBottleFragment extends OrderFragment {
	
	private BackBottleDao od;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result);
		}

	};
	private int page =1;
	private int position;
	private boolean isRefush = true;
	private ProgressDialog pd;
	@Override
	public View initView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.unfinsh_fragment, null);
		lv_unfinsh_order = (AutoListView) view.findViewById(R.id.lv_unfinsh_order);
		pd = new ProgressDialog(getContext());
		pd.setMessage("正在加载");
		initData();
		
		lv_unfinsh_order.setOnItemClickListener(this);
		
		lv_unfinsh_order.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				isRefush = true;
				page =1;
				mDatas = new ArrayList<TableInfo>();
				BackBottleOrder.mData = new ArrayList<BackBottle>();
				String shop_id = (String) SPUtils.get(getContext(), SPutilsKey.SHOP_ID, "error");
				String ship_id = (String) SPUtils.get(getContext(), SPutilsKey.SHIP_ID, "error");
			
				RequestParams params = new RequestParams(RequestUrl.DESPOIT);
				params.addBodyParameter("shipper_id", ship_id);
				params.addBodyParameter("shop_id", shop_id);
				params.addBodyParameter("type", "2");
				params.addBodyParameter("page", "1");
				System.out.println("shop_id="+shop_id);
				
				HttpUtil.PostHttp(params, handler, new ProgressDialog(getContext()));
			}
		});
		
		lv_unfinsh_order.setOnLoadListener(new OnLoadListener() {
			
			@Override
			public void onLoad() {
				page++;
				position = lv_unfinsh_order.getFirstVisiableItem();
				String shop_id = (String) SPUtils.get(getContext(), SPutilsKey.SHOP_ID, "error");
				String ship_id = (String) SPUtils.get(getContext(), SPutilsKey.SHIP_ID, "error");
			
				RequestParams params = new RequestParams(RequestUrl.DESPOIT);
				params.addBodyParameter("shipper_id", ship_id);
				params.addBodyParameter("shop_id", shop_id);
				params.addBodyParameter("type", "2");
				params.addBodyParameter("page", page+"");
				System.out.println("shop_id="+shop_id);
				
				HttpUtil.PostHttp(params, handler, new ProgressDialog(getContext()));
			}
		});
		
		return view;
	}

	@Override
	public void initData() {
		pd.show();
		if(isRefush){
			mDatas = new ArrayList<TableInfo>();
		}
		od = BackBottleDao.getInstances(getContext());
		BackBottleOrder.mData = new ArrayList<BackBottle>();
		String shop_id = (String) SPUtils.get(getContext(), SPutilsKey.SHOP_ID, "error");
		String ship_id = (String) SPUtils.get(getContext(), SPutilsKey.SHIP_ID, "error");
	
		RequestParams params = new RequestParams(RequestUrl.DESPOIT);
		params.addBodyParameter("shipper_id", ship_id);
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("type", "2");
		params.addBodyParameter("page", "1");
		Log.e("lll", "退瓶订单的列表详情"+params.getStringParams().toString());
		HttpUtil.PostHttp(params, handler, pd);
		}
	
	private void paraseData(String result) {
		System.out.println("完成="+result);
		//json解析
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				//继续解析
				if(!obj.isNull("resultInfo")){
					JSONArray array = obj.getJSONArray("resultInfo");
					for(int i=0;i<array.length();i++){
						JSONObject object = array.getJSONObject(i);
						Log.e("lll","已完成tuiping"+object.toString() );
						String depositsn = object.getString("depositsn");
						String money = object.getString("money");//
						String doormoney = object.getString("doormoney");//上门费
						String productmoney = object.getString("productmoney");
						String shouldmoney = object.getString("shouldmoney");//残液
						String status = object.getString("status");
						String username = object.getString("username");
						String time = object.getString("time");
						String mobile = object.getString("mobile");
						String id = object.getString("id");
						String address = object.getString("address");
						String kid = object.getString("kid");
						String status_name = object.getString("status_name");	
						String boottle = object.getString("bottle");
						
						String datainfo = object.getString("datainfo");
						
						if(!"null".equals(datainfo)){
							JSONArray array1 = new JSONArray(datainfo);
							List<Bottle> list = new ArrayList<Bottle>();
							for(int j = 0;j<array1.length();j++){
								JSONObject json = array1.getJSONObject(j);
								String goods_num = json.getString("number");
								String goods_price = json.getString("typename");
								String title = json.getString("xinpian");
								list.add(new Bottle(title, goods_price, goods_num));
							}
							BackBottleOrder.mData.add(new BackBottle(depositsn, money, doormoney, productmoney, shouldmoney, username, 
									time, mobile, id, address,status,kid,status_name,list));
						}else{
							BackBottleOrder.mData.add(new BackBottle(depositsn, money, doormoney, productmoney, shouldmoney, username, 
									time, mobile, id, address,status,kid,status_name,new ArrayList<Bottle>()));
						}
						
						mDatas.add(new TableInfo(depositsn, money, "已完成", time));
						//保存到数据库
						Cursor cursor = od.getFromOrderSn(depositsn);
						if(cursor.getCount()==0){//没找到，插入到数据库
							od.saveOrder(depositsn, money, doormoney, productmoney, shouldmoney, username, 
									time, mobile, id, address,status,kid,status_name);
						}
						
					}
					table = new OrderTable().addData( mDatas, initTitles());
					
					adapter = new TabAdapter(getContext(), table);
					
					lv_unfinsh_order.setAdapter(adapter);
					if(isRefush){
						System.out.println("77777777777777777777");
						lv_unfinsh_order.setResultSize(array.length());
						lv_unfinsh_order.onRefreshComplete();
					}else{
						lv_unfinsh_order.onLoadComplete();
						lv_unfinsh_order.setSelection(position);
						lv_unfinsh_order.setResultSize(array.length());
					}
				}
				
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(position>1&&position<mDatas.size()+2){
			String depositsn = mDatas.get(position-2).getOrderNum();
			SPUtils.put(getContext(), "depositsn", depositsn);
			//跳转到订单详情界面
			Intent intent = new Intent(getContext(), BackBottleActivity.class);
			intent.putExtra("depositsn", depositsn);
			startActivity(intent);
		}
	}

	@Override
	public String[] initTitles() {
		String titles [] ={"订单号","订单金额","订单状态","订单时间"};
		return titles;
	}

	@Override
	public int isOrNoSet() {
		return 1;
	}

	@Override
	public boolean isRefush() {
		return true;
	}

	}
