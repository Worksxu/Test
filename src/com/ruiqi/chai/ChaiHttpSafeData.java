package com.ruiqi.chai;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.google.gson.Gson;
import com.ruiqi.chai.ChaiHttputils.Success;
import com.ruiqi.utils.RequestUrl;
/**
 * 安全报告内容列表
 * @author Administrator
 *
 */
public class ChaiHttpSafeData implements Success{
	
	private static ChaiHttpSafeData SafeDefault;

	private ChaiHttputils httputils;

	public static ChaiHttpSafeData getInstance() {
		if (SafeDefault == null) {
			SafeDefault = new ChaiHttpSafeData();
		}
		return SafeDefault;
	}

	private ChaiHttpSafeData() {
		httputils = new ChaiHttputils();
		httputils.setOnSuccess(this);
	}
/**
 * 
 * @param type
 */
	public void app2server(String type) {
		RequestParams params = new RequestParams(RequestUrl.REPORTLIST);
		params.addBodyParameter("type", type);
		httputils.getDataFromServer(params);
	}

	private ChaiSafeData universalData;
	@Override
	public void getSuccessData(boolean bool,String result) {
		if(bool){
			try {
				JSONObject jsob=new JSONObject(result);
				if(jsob.getInt("resultCode")==1){
					Gson gson=new Gson();
					universalData=gson.fromJson(result, ChaiSafeData.class);
					parserSafeDefaultData.parserSafeDefaultResult(true,true,universalData);
				}else{
					parserSafeDefaultData.parserSafeDefaultResult(true,false,universalData);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			parserSafeDefaultData.parserSafeDefaultResult(false,false,universalData);
		}

	}

	private ParserSafeDefaultData parserSafeDefaultData;

	public interface ParserSafeDefaultData {
		public abstract void parserSafeDefaultResult(boolean webErrer,boolean loginErrer,ChaiSafeData universalData);
	}

	public void setOnParserSafeDefaultData(ParserSafeDefaultData parserSafeDefaultData) {
		this.parserSafeDefaultData = parserSafeDefaultData;
	}

}
