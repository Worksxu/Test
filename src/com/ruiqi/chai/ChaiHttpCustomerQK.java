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
 * 客户欠款合同
 * @author Administrator
 *
 */
public class ChaiHttpCustomerQK implements Success{
	
	private static ChaiHttpCustomerQK CustomerQK;

	private ChaiHttputils httputils;

	public static ChaiHttpCustomerQK getInstance() {
		if (CustomerQK == null) {
			CustomerQK = new ChaiHttpCustomerQK();
		}
		return CustomerQK;
	}

	private ChaiHttpCustomerQK() {
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
	public void app2server(String kid,String token) {
		RequestParams params = new RequestParams(RequestUrl.USERQKORDER);
		params.addBodyParameter("kid", kid);
		params.addBodyParameter("token", token);
		Log.e("lll", "退还钢瓶参数"+params.getStringParams());
		httputils.getDataFromServer(params);
	}
	
	

	private UniversalData universalData;
	private ChaiHeTongData chaiHeTongData;
	@Override
	public void getSuccessData(boolean bool,String result) {
		Log.e("lllUSERQKORDER", result);
		Gson gson=new Gson();
		if(bool){
			try {
				JSONObject jsob=new JSONObject(result);
				if(jsob.getInt("resultCode")==1){
					chaiHeTongData=gson.fromJson(result, ChaiHeTongData.class);
					parserCustomerQKData.parserCustomerQKResult(true,true,chaiHeTongData,universalData);
				}else{
					universalData=gson.fromJson(result, UniversalData.class);
					parserCustomerQKData.parserCustomerQKResult(true,false,chaiHeTongData,universalData);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			universalData=new UniversalData();
			parserCustomerQKData.parserCustomerQKResult(false,false,chaiHeTongData,universalData);
		}
	}

	private ParserCustomerQKData parserCustomerQKData;

	public interface ParserCustomerQKData {
		public abstract void parserCustomerQKResult(boolean webErrer,boolean loginErrer,ChaiHeTongData chaiHeTongData,UniversalData universalData);
	}

	public void setOnParserCustomerQKData(ParserCustomerQKData parserCustomerQKData) {
		this.parserCustomerQKData = parserCustomerQKData;
	}

}
