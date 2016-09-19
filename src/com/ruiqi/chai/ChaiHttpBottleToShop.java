package com.ruiqi.chai;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.ruiqi.chai.ChaiHttputils.Success;
import com.ruiqi.utils.RequestUrl;
/**
 * 送气工退瓶、配件回门店
 * @author Administrator
 *
 */
public class ChaiHttpBottleToShop implements Success{
	
	private static ChaiHttpBottleToShop BottleToShop;

	private ChaiHttputils httputils;

	public static ChaiHttpBottleToShop getInstance() {
		if (BottleToShop == null) {
			BottleToShop = new ChaiHttpBottleToShop();
		}
		return BottleToShop;
	}

	private ChaiHttpBottleToShop() {
		httputils = new ChaiHttputils();
		httputils.setOnSuccess(this);
	}
/**
 * 
 * @param type 1 退重瓶 2退空瓶 3退折旧 4退配件
 * @param shipper_id
 * @param shop_id
 * @param bottle 钢瓶汇总 
 * @param bottle_data 钢瓶信息 
 * @param comment 备注
 * 
 */
	public void app2server(int type,String shipper_id,String shipper_name,String shop_id,Integer token,String bottle_data,String bottle,String comment) {
		RequestParams params = new RequestParams(RequestUrl.BACKSHOP);
		params.addBodyParameter("shipper_id", shipper_id);
		params.addBodyParameter("shipper_name", shipper_name);
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("token", token+"");
		if(type==1){
			params.addBodyParameter("bottle", bottle);
			params.addBodyParameter("bottle_data", bottle_data);
		}else if(type==2){
			params.addBodyParameter("kbottle", bottle);
			params.addBodyParameter("kbottle_data", bottle_data);
		}else if(type==3){
			params.addBodyParameter("qbottle", bottle);
		}else if(type==4){
			params.addBodyParameter("pbottle", bottle);
		}
		if(!TextUtils.isEmpty(comment)){
			params.addBodyParameter("comment", comment);
		}
		Log.e("lll", "退还钢瓶参数"+params.getStringParams());
		httputils.getDataFromServer(params);
	}

	private UniversalData universalData;
	@Override
	public void getSuccessData(boolean bool,String result) {
		if(bool){
			try {
				JSONObject jsob=new JSONObject(result);
				if(jsob.getInt("resultCode")==1){
					Gson gson=new Gson();
					universalData=gson.fromJson(result, UniversalData.class);
					parserBottleToShopData.parserBottleToShopResult(true,true,universalData);
				}else{
					universalData=new UniversalData();
					parserBottleToShopData.parserBottleToShopResult(true,false,universalData);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			universalData=new UniversalData();
			parserBottleToShopData.parserBottleToShopResult(false,false,universalData);
		}
	}

	private ParserBottleToShopData parserBottleToShopData;

	public interface ParserBottleToShopData {
		public abstract void parserBottleToShopResult(boolean webErrer,boolean loginErrer,UniversalData universalData);
	}

	public void setOnParserBottleToShopData(ParserBottleToShopData parserBottleToShopData) {
		this.parserBottleToShopData = parserBottleToShopData;
	}

}
