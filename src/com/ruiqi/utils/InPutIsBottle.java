package com.ruiqi.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;
import org.xutils.http.RequestParams;

import com.ruiqi.adapter.CommonAdapter;
import com.ruiqi.bean.Weight;
import com.ruiqi.db.GpDao;
import com.ruiqi.works.NfcActivity;
import com.ruiqi.works.OrderConfirmActivity;
import com.ruiqi.works.SaoMiaoActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

/**
 * 手动输入，判定是否是本公司瓶
 * @author Administrator
 *
 */
public class InPutIsBottle {
	
	private String str;
	private Context context;
	private List<Weight> list;
	private CommonAdapter<Weight> adapter;
	private ProgressDialog pd;
	private GpDao gd;
	private String ship_id;
	private List<Weight> mData;
	
	private int a;
	
	public InPutIsBottle(String str, Context context, List<Weight> list,
			CommonAdapter<Weight> adapter, ProgressDialog pd,List<Weight> mData) {
		super();
		this.str = str;
		this.context = context;
		this.list = list;
		this.adapter = adapter;
		this.pd = pd;
		this.mData = mData;
	}

	public InPutIsBottle(String str, Context context, List<Weight> list,
			CommonAdapter<Weight> adapter, ProgressDialog pd, GpDao gd,List<Weight> mData,int a) {
		super();
		this.str = str;
		this.context = context;
		this.list = list;
		this.adapter = adapter;
		this.pd = pd;
		this.gd = gd;
		this.mData = mData;
		this.a = a;//标记2重瓶入库，1重瓶出库
		ship_id = (String) SPUtils.get(context, SPutilsKey.SHIP_ID, "error");
	}

	public  void addDataToList() {
		if(str.trim()==null||str.trim().equals("")){
			T.showShort(context, "请输入钢瓶号");
		}else{
			RequestParams params = new RequestParams(RequestUrl.IS_BOTTLE);
			params.addBodyParameter("xinpian", str.trim());
			if(a==2){
				String shop_id = (String) SPUtils.get(context, SPutilsKey.SHOP_ID, "");
				params.addBodyParameter("shop_id",shop_id);
				params.addBodyParameter("is_open","1");
			}
			Log.e("lll","领瓶请求"+ params.getStringParams().toString());
			HttpUtil.PostHttp(params, handler, pd);
		}
		
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			parseData(result.trim());
		};
	};
	
	private void parseData(String result) {
		Log.e("lll钢瓶内容", result);
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				//加到集合中，通知更新
				JSONObject jsob=obj.getJSONObject("resultInfo");
				String type=jsob.getString("type");
				String number=jsob.getString("number");
				String xinpian=jsob.getString("xinpian");
				String type_name="";
				if(jsob.has("type_name")){
					 type_name=jsob.getString("type_name");
				}
				boolean flag = CurrencyUtils.getStrFlag(str, list);
				//判断是否是库存里的
				if(flag){
					T.showShort(context, "该瓶已经添加过");
				}else{
					if(gd!=null){
						if(a==1){
							Log.e("lll", "走a==1");
							//Cursor c = gd.getFromXinpianAndShip(str,ship_id);
							Cursor c=gd.readDb.query("gangpingxinxi", null,"number=?" , new String[] {str}, null, null, null);
							if(c.getCount()!=0){
								c.moveToFirst();
								if(list!=null){
									list.add(new Weight(str, c.getString(c.getColumnIndex("type")), "1"));
								}
								adapter.notifyDataSetChanged();
								if(mData!=null){
									mData.add(new Weight(str,c.getString(c.getColumnIndex("type")), "1"));
								}
								if(NfcActivity.mData==null){
									NfcActivity.mData = new ArrayList<Weight>();
								}
								NfcActivity.mData.add(new Weight(str, c.getString(c.getColumnIndex("type")), "1"));
							}else{
								T.showShort(context, "该瓶不在库存中");
							}
						}else if(a==2){
							Log.e("lll", "走a==2");
							Cursor c=gd.readDb.query("gangpingxinxi", null,"number=?" , new String[] {str}, null, null, null);
							if(c.getCount()!=0){
								T.showShort(context, "该瓶已在库存中");
							}else{
								c.moveToFirst();
								if(list!=null){
									list.add(new Weight(str, type, "1"));
								}
								adapter.notifyDataSetChanged();
								if(mData!=null){
									mData.add(new Weight(str,type, "1"));
								}
								if(NfcActivity.mData==null){
									NfcActivity.mData = new ArrayList<Weight>();
								}
								NfcActivity.mData.add(new Weight(str, type, "1"));
							}
						}
					}else{
						Log.e("lll","退瓶手动输入走的路径");
						if(list!=null){
							list.add(new Weight(str, "1", "1"));
						}
						adapter.notifyDataSetChanged();
						if(mData!=null){
							mData.add(new Weight(xinpian,type, "1",number,type_name));
						}
						if(NfcActivity.mData==null){
							NfcActivity.mData = new ArrayList<Weight>();
						}
						NfcActivity.mData.add(new Weight(xinpian,type, "1",number,type_name));
					}
				}
			}else{
				ChaiMyDialog.getInstance().showHint(context, "此钢瓶不属于本门店");
				//T.showShort(context, "此钢瓶不属于本门店");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
			
		}
				
	
}
