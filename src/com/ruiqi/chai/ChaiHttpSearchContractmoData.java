package com.ruiqi.chai;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.util.Log;

import com.google.gson.Gson;
import com.ruiqi.chai.ChaiHttputils.Success;
import com.ruiqi.utils.RequestUrl;
/**
 * 搜索合同编号
 * @author Administrator
 *
 */
public class ChaiHttpSearchContractmoData implements Success{
	
	private static ChaiHttpSearchContractmoData SearchContractmo;

	private ChaiHttputils httputils;

	public static ChaiHttpSearchContractmoData getInstance() {
		if (SearchContractmo == null) {
			SearchContractmo = new ChaiHttpSearchContractmoData();
		}
		return SearchContractmo;
	}

	private ChaiHttpSearchContractmoData() {
		httputils = new ChaiHttputils();
		httputils.setOnSuccess(this);
	}
/**
 * 
 * @param receiptno 合同编号
 * @param type 1欠款 2押金
 * @param token
 */
	public void app2server(String receiptno,String type,Integer token) {
		RequestParams params = new RequestParams(RequestUrl.GETRECEIPT);
		params.addBodyParameter("receiptno", receiptno);
		params.addBodyParameter("type", type);
		params.addBodyParameter("token", token+"");
		Log.e("lll", "搜索合同号参数"+params.getStringParams().toString());
		httputils.getDataFromServer(params);
	}

	private ChaiHeTongData universalData;
	private UniversalData universalDatas;
	@Override
	public void getSuccessData(boolean bool,String result) {
		Gson gson=new Gson();
		if(bool){
			try {
				JSONObject jsob=new JSONObject(result);
				Log.e("lll", result);
				if(jsob.getInt("resultCode")==1){
					universalData=gson.fromJson(result, ChaiHeTongData.class);
					parserSearchContractmoData.parserSearchContractmoResult(true,true,universalData,universalDatas);
				}else{
					universalDatas=gson.fromJson(result, UniversalData.class);
					parserSearchContractmoData.parserSearchContractmoResult(true,false,universalData,universalDatas);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			universalData=new ChaiHeTongData();
			parserSearchContractmoData.parserSearchContractmoResult(false,false,universalData,universalDatas);
		}

	}

	private ParserSearchContractmoData parserSearchContractmoData;

	public interface ParserSearchContractmoData {
		public abstract void parserSearchContractmoResult(boolean webErrer,boolean loginErrer,ChaiHeTongData universalData,UniversalData universalDatas);
	}

	public void setOnParserSearchContractmoData(ParserSearchContractmoData parserSearchContractmoData) {
		this.parserSearchContractmoData = parserSearchContractmoData;
	}

}
