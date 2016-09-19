package com.ruiqi.chai;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.util.Log;

import com.google.gson.Gson;
import com.ruiqi.chai.ChaiHttputils.Success;
import com.ruiqi.utils.RequestUrl;
/**
 * 选择钢瓶类型
 * @author Administrator
 *
 */
public class ChaiHttpSelectBottletype implements Success {

	private static ChaiHttpSelectBottletype SelectBottletype;

	private ChaiHttputils httputils;

	public static ChaiHttpSelectBottletype getInstance() {
		if (SelectBottletype == null) {
			SelectBottletype = new ChaiHttpSelectBottletype();
		}
		return SelectBottletype;
	}

	private ChaiHttpSelectBottletype() {
		httputils = new ChaiHttputils();
		httputils.setOnSuccess(this);
	}

	/**
	 * 
	 * @param user_type
	 *            客户类型
	 * @param shop_id
	 * @param token
	 */
	public void app2server(String user_type, String shop_id, Integer token) {
		RequestParams params = new RequestParams(RequestUrl.BOTTLE_TYPE);
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("user_type", user_type);
		params.addBodyParameter("token", token + "");
		Log.e("lll","商品类型参数"+params.getStringParams().toString());
		httputils.getDataFromServer(params);
	}

	
	private ChaiBottles chaiBottles;

	private UniversalData universalDatas;
	@Override
	public void getSuccessData(boolean bool, String result) {
		Gson gson = new Gson();
		if (bool) {
			try {
				JSONObject jsob = new JSONObject(result);
				if (jsob.getInt("resultCode") == 1) {
					chaiBottles = gson.fromJson(result,ChaiBottles.class);
					parserSelectBottletypeData.parserSelectBottlestypeResult(true, true, chaiBottles, universalDatas);
				} else {
					universalDatas = gson.fromJson(result, UniversalData.class);
					parserSelectBottletypeData.parserSelectBottlestypeResult(true, false, chaiBottles, universalDatas);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			chaiBottles = new ChaiBottles();
			parserSelectBottletypeData.parserSelectBottlestypeResult(false,false, chaiBottles, universalDatas);
		}
	}

	private ParserSelectBottlestypeData parserSelectBottletypeData;

	public interface ParserSelectBottlestypeData {
		/**
		 * 钢瓶类型
		 * 
		 * @param webErrer
		 * @param loginErrer
		 * @param universalData
		 * @param universalDatas
		 */
		public abstract void parserSelectBottlestypeResult(boolean webErrer,boolean loginErrer, ChaiBottles universalData,UniversalData universalDatas);
	}

	public void setOnParserSelectBottlestypeData(
			ParserSelectBottlestypeData parserSelectBottletypeData) {
		this.parserSelectBottletypeData = parserSelectBottletypeData;
	}

}
