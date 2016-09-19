package com.ruiqi.chai;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.util.Log;

import com.google.gson.Gson;
import com.ruiqi.chai.ChaiHttputils.Success;
import com.ruiqi.utils.RequestUrl;

/**
 * 客户上次安全报告详情
 * @author Administrator
 *
 */
public class ChaiHttpCustomerReportData implements Success{
	
	private static ChaiHttpCustomerReportData CustomerReport;

	private ChaiHttputils httputils;

	public static ChaiHttpCustomerReportData getInstance() {
		if (CustomerReport == null) {
			CustomerReport = new ChaiHttpCustomerReportData();
		}
		return CustomerReport;
	}

	private ChaiHttpCustomerReportData() {
		httputils = new ChaiHttputils();
		httputils.setOnSuccess(this);
	}
/**
 * 
 * @param mobile
 * @param token
 */
	public void app2server(String mobile,Integer token) {
		RequestParams params = new RequestParams(RequestUrl.REPORTUSERINFO);
		params.addBodyParameter("mobile", mobile);
		params.addBodyParameter("token", token+"");
		Log.e("lll","安全报告上次检查信息"+params.getStringParams().toString());
		httputils.getDataFromServer(params);
	}

	private ChaiCustomerData universalData;
	@Override
	public void getSuccessData(boolean bool,String result) {
		if(bool){
			try {
				JSONObject jsob=new JSONObject(result);
				if(jsob.getInt("resultCode")==1){
					Gson gson=new Gson();
					universalData=gson.fromJson(result, ChaiCustomerData.class);
					parserCustomerReportData.parserCustomerReportResult(true,true,universalData);
				}else{
					parserCustomerReportData.parserCustomerReportResult(true,false,universalData);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			parserCustomerReportData.parserCustomerReportResult(false,false,universalData);
		}

	}

	private ParserCustomerReportData parserCustomerReportData;

	public interface ParserCustomerReportData {
		public abstract void parserCustomerReportResult(boolean webErrer,boolean loginErrer,ChaiCustomerData universalData);
	}

	public void setOnParserCustomerReportData(ParserCustomerReportData parserCustomerReportData) {
		this.parserCustomerReportData = parserCustomerReportData;
	}

}
