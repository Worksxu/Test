package com.ruiqi.works;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.ruiqi.bean.PeiJian;
import com.ruiqi.bean.PeiJianTypeMoney;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;

public class UpdatePeiJianActivity extends ApplyActivity{
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setTitle("选择配件");
	}

	@Override
	public boolean setIsVisbily() {
		return false;
	}
	
	private List<PeiJianTypeMoney> finalList;
	public static List<PeiJian> finalData;
	private List<PeiJianTypeMoney> list;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result);
		}
	};
	
	private void paraseData(String result) {
		System.out.println("pjresult="+result);
		//解析数据
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				if("null".equals(obj.getString("resultInfo"))){
					T.showShort(UpdatePeiJianActivity.this, "配件库存为空");
				}else{
					
					JSONArray array = obj.getJSONArray("resultInfo");
					for(int i=0;i<array.length();i++){
						JSONObject obj1 = array.getJSONObject(i);
						String id = obj1.getString("id");
						String shipper_id = obj1.getString("shipper_id");
						String goods_id = obj1.getString("goods_id");
					//	String goods_name = obj1.getString("goods_name");
						String goods_kind = obj1.getString("goods_kind");
						String goods_num = obj1.getString("goods_num");
						String goods_type = obj1.getString("goods_type");
						String type = obj1.getString("type");
						String name = obj1.getString("name");
						list.add(new PeiJianTypeMoney(goods_kind, goods_num));
					}
					//将list和finallist作对比,符合条件才跳转
					if(compareList()){
						Intent intent = new Intent(UpdatePeiJianActivity.this, TakePhotoActivity.class);
						startActivity(intent);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	private boolean compareList(){
		System.out.println("finalList="+finalList);
		System.out.println("list="+list);
		for(int a = 0;a<finalData.size();a++){
			List<PeiJianTypeMoney> finalList = finalData.get(a).getmList();
			for(int i = 0;i<finalList.size();i++){//选择之后的集合
				String id = finalList.get(i).getId();
				String num = finalList.get(i).getNum();
				List<String> sList = new ArrayList<String>();
				for(int j = 0;j<list.size();j++){
					String goods_id = list.get(j).getId();//配件库存的集合
					sList.add(goods_id);
				}
				if(!sList.contains(id)){
					T.showShort(UpdatePeiJianActivity.this, "选中的有些配件库存里没有");
					return false;
				}else{//比较数目
					for(int j = 0;j<list.size();j++){
						String goods_id = list.get(j).getId();//配件库存的集合
						if(id.equals(goods_id)){//两id相同的情况下
							String goods_num = list.get(j).getNum();
							if(Integer.parseInt(num)>Integer.parseInt(goods_num)){
								T.showShort(UpdatePeiJianActivity.this, "配件库存数目不够");
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_next:
			finalData = new ArrayList<PeiJian>();
			list = new ArrayList<PeiJianTypeMoney>();
			for(int i=0;i<mData.size();i++){
				List<PeiJianTypeMoney> list = new ArrayList<PeiJianTypeMoney>();
				list = mData.get(i).getmList();
				for(int j=0;j<list.size();j++){
					if(Integer.parseInt(list.get(j).getNum())>0){
						finalList = new ArrayList<PeiJianTypeMoney>();
						PeiJianTypeMoney p = list.get(j);
						finalList.add(new PeiJianTypeMoney(p.getId(), p.getName(), p.getType(), p.getNorm_id(), p.getTypename(), p.getPrice(),p.getNum()));
						finalData.add(new PeiJian(mData.get(i).getName(), finalList));
					}
				}
				//保存胶管对象
			}
			//根据库存判断是否可以跳转
			//请求配件库存接口
			String shipid = (String) SPUtils.get(UpdatePeiJianActivity.this, SPutilsKey.SHIP_ID, "error");
			RequestParams params = new RequestParams(RequestUrl.PJ_STOCK);
			params.addBodyParameter("shipper_id", shipid);
			HttpUtil.PostHttp(params, handler, new ProgressDialog(UpdatePeiJianActivity.this));
			break;

		default:
			break;
		}
	}

}
