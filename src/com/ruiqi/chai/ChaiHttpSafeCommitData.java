package com.ruiqi.chai;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.util.Log;

import com.google.gson.Gson;
import com.ruiqi.chai.ChaiHttputils.Success;
import com.ruiqi.utils.RequestUrl;
/**
 * 提交安全报告
 * @author Administrator
 *
 */
public class ChaiHttpSafeCommitData implements Success{
	
	private static ChaiHttpSafeCommitData SafeCommit;

	private ChaiHttputils httputils;

	public static ChaiHttpSafeCommitData getInstance() {
		if (SafeCommit == null) {
			SafeCommit = new ChaiHttpSafeCommitData();
		}
		return SafeCommit;
	}

	private ChaiHttpSafeCommitData() {
		httputils = new ChaiHttputils();
		httputils.setOnSuccess(this);
	}
/**
 * 
 * @param kid
 * @param arrayList
 * @param json
 * @param shipper
 * @param shipper_name
 * @param token
 */
	public void app2server(String kid,ArrayList<HashMap<String, String>> arrayList,String json,String shipper,String shipper_name,Integer token) {
		RequestParams params = new RequestParams(RequestUrl.SAFEREPORT);
		params.addBodyParameter("shipper_name", shipper_name);
		params.addBodyParameter("shipper_id", shipper);
		params.addBodyParameter("kid", kid);
		params.addBodyParameter("num", arrayList.size() + "");
		params.addBodyParameter("selfjson",json);
		params.addBodyParameter("token",token+"");
		for (int i = 0, j = 1; i < arrayList.size(); i++, j++) {
			params.addBodyParameter("file" + j, new File(arrayList.get(i).get("path")));
		}
		params.setMultipart(true);
		Log.e("lll","安全报告提交参数"+params.getStringParams().toString());
		httputils.getDataFromServer(params);
	}

	private ChaiSafeCommitData universalData;
	@Override
	public void getSuccessData(boolean bool,String result) {
		if(bool){
			try {
				JSONObject jsob=new JSONObject(result);
				if(jsob.getInt("resultCode")==1){
					Gson gson=new Gson();
					universalData=gson.fromJson(result, ChaiSafeCommitData.class);
					parserSafeCommitData.parserSafeCommitResult(true,true,universalData);
				}else{
					parserSafeCommitData.parserSafeCommitResult(true,false,universalData);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			parserSafeCommitData.parserSafeCommitResult(false,false,universalData);
		}

	}

	private ParserSafeCommitData parserSafeCommitData;

	public interface ParserSafeCommitData {
		public abstract void parserSafeCommitResult(boolean webErrer,boolean loginErrer,ChaiSafeCommitData universalData);
	}

	public void setOnParserSafeCommitData(ParserSafeCommitData parserSafeCommitData) {
		this.parserSafeCommitData = parserSafeCommitData;
	}

}
