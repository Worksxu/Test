package com.ruiqi.chai;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.google.gson.Gson;
import com.ruiqi.chai.ChaiHttputils.Success;
import com.ruiqi.utils.RequestUrl;
/**
 * 下退瓶订单
 * @author Administrator
 *
 */
public class ChaiHttpTuiPingData implements Success {

	private static ChaiHttpTuiPingData TuiPing;

	private ChaiHttputils httputils;

	public static ChaiHttpTuiPingData getInstance() {
		if (TuiPing == null) {
			TuiPing = new ChaiHttpTuiPingData();
		}
		return TuiPing;
	}

	private ChaiHttpTuiPingData() {
		httputils = new ChaiHttputils();
		httputils.setOnSuccess(this);
	}

	/**
	 * 
	 * @param kid 客户id
	 * @param mobile 客户手机号
	 * @param username 客户姓名
	 * @param address 客户地址
	 * @param shop_id 门店id
	 * @param shipper_id 送气工id
	 * @param shipper_name 送气工姓名
	 * @param shipper_mobile 送气工手机号
	 * @param comment 备注
	 * @param token
	 */
	public void app2server(String kid, String mobile, String username,
			String address, String shop_id, String shipper_id,
			String shipper_name, String shipper_mobile, String comment,
			Integer token) {
		RequestParams params = new RequestParams(RequestUrl.BACKORDER);
		params.addBodyParameter("kid", kid);
		params.addBodyParameter("mobile", mobile);
		params.addBodyParameter("username", username);
		params.addBodyParameter("address", address);
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("shipper_id", shipper_id);
		params.addBodyParameter("shipper_name", shipper_name);
		params.addBodyParameter("shipper_mobile", shipper_mobile);
		params.addBodyParameter("comment", comment);
		params.addBodyParameter("token", token+"");
		httputils.getDataFromServer(params);
	}

	private UniversalData universalData;

	@Override
	public void getSuccessData(boolean bool, String result) {
		if (bool) {
			try {
				JSONObject jsob = new JSONObject(result);
				if (jsob.getInt("resultCode") == 1) {
					Gson gson = new Gson();
					universalData = gson.fromJson(result,UniversalData.class);
					parserTuiPingData.parserTuiPingResult(true, true,universalData);
				} else {
					parserTuiPingData.parserTuiPingResult(true, false,universalData);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			parserTuiPingData.parserTuiPingResult(false, false, universalData);
		}

	}

	private ParserTuiPingData parserTuiPingData;

	public interface ParserTuiPingData {
		public abstract void parserTuiPingResult(boolean webErrer,boolean loginErrer, UniversalData universalData);
	}

	public void setOnParserTuiPingData(ParserTuiPingData parserTuiPingData) {
		this.parserTuiPingData = parserTuiPingData;
	}

}
