package com.ruiqi.chai;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.google.gson.Gson;
import com.ruiqi.chai.ChaiHttputils.Success;
import com.ruiqi.utils.RequestUrl;

/**
 * 搜索客户
 * @author Administrator
 *
 */
public class ChaiHttpCustomerData implements Success{
	
	private static ChaiHttpCustomerData CustomerDefault;

	private ChaiHttputils httputils;

	public static ChaiHttpCustomerData getInstance() {
		if (CustomerDefault == null) {
			CustomerDefault = new ChaiHttpCustomerData();
		}
		return CustomerDefault;
	}

	private ChaiHttpCustomerData() {
		httputils = new ChaiHttputils();
		httputils.setOnSuccess(this);
	}
/**
 * 
 * @param kid
 * @param mobile
 */
	public void app2server(String kid,String mobile) {
		RequestParams params = new RequestParams(RequestUrl.USER_INFO);
		params.addBodyParameter("mobile", mobile);
		httputils.getDataFromServer(params);
	}

	private ChaiCustomerTuiPingData universalData;
	private UniversalData universalDatas;
	@Override
	public void getSuccessData(boolean bool,String result) {
		Gson gson=new Gson();
		if(bool){
			try {
				JSONObject jsob=new JSONObject(result);
				if(jsob.getInt("resultCode")==1){
					universalData=gson.fromJson(result, ChaiCustomerTuiPingData.class);
					parserCustomerDefaultData.parserCustomerDefaultResult(true,true,universalData,universalDatas);
				}else{
					universalDatas=gson.fromJson(result, UniversalData.class);
					parserCustomerDefaultData.parserCustomerDefaultResult(true,false,universalData,universalDatas);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			parserCustomerDefaultData.parserCustomerDefaultResult(false,false,universalData,universalDatas);
		}

	}

	private ParserCustomerDefaultData parserCustomerDefaultData;

	public interface ParserCustomerDefaultData {
		public abstract void parserCustomerDefaultResult(boolean webErrer,boolean loginErrer,ChaiCustomerTuiPingData universalData, UniversalData universalDatas);
	}

	public void setOnParserCustomerDefaultData(ParserCustomerDefaultData parserCustomerDefaultData) {
		this.parserCustomerDefaultData = parserCustomerDefaultData;
	}

}
