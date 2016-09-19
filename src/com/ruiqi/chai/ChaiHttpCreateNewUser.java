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
 * 创建新客户
 * @author Administrator
 *
 */
public class ChaiHttpCreateNewUser implements Success {

	private static ChaiHttpCreateNewUser CreateNewUser;

	private ChaiHttputils httputils;

	public static ChaiHttpCreateNewUser getInstance() {
		if (CreateNewUser == null) {
			CreateNewUser = new ChaiHttpCreateNewUser();
		}
		return CreateNewUser;
	}

	private ChaiHttpCreateNewUser() {
		httputils = new ChaiHttputils();
		httputils.setOnSuccess(this);
	}
/**
 * 
 * @param mobile
 * @param username
 * @param sheng
 * @param shi
 * @param qu
 * @param cun
 * @param address
 * @param card_sn
 * @param shop_id
 * @param user_type
 * @param recommended
 */
	public void app2server(String mobile, String username, String sheng,String shi, String qu, String cun, String address,String card_sn,String shop_id,String user_type,String recommended) {
		RequestParams params = new RequestParams(RequestUrl.CREATEKEHU);
		params.addBodyParameter("mobile", mobile);
		params.addBodyParameter("username", username);
		params.addBodyParameter("sheng", sheng);
		params.addBodyParameter("shi", shi );
		params.addBodyParameter("qu", qu);
		params.addBodyParameter("cun", cun);
		params.addBodyParameter("address", address);
		params.addBodyParameter("card_sn", card_sn);
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("user_type", user_type);
		if(!TextUtils.isEmpty(recommended)){
			params.addBodyParameter("recommended", recommended);
		}
		Log.e("lll", "创建新用户参数" + params.getStringParams());
		httputils.getDataFromServer(params);
	}

	private UniversalData universalData;
	private ChaiCreateOrderData chaiCreateOrderData;

	@Override
	public void getSuccessData(boolean bool, String result) {
		Gson gson = new Gson();
		if (bool) {
			try {
				JSONObject jsob = new JSONObject(result);
				if (jsob.getInt("resultCode") == 1) {
					chaiCreateOrderData = gson.fromJson(result, ChaiCreateOrderData.class);
					parserCreateNewUserData.parserCreateNewUserResult(true,true, chaiCreateOrderData,universalData);
				} else {
					universalData =gson.fromJson(result, UniversalData.class);
					parserCreateNewUserData.parserCreateNewUserResult(true,false, chaiCreateOrderData,universalData);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			universalData = new UniversalData();
			parserCreateNewUserData.parserCreateNewUserResult(false, false,
					chaiCreateOrderData,universalData);
		}
	}

	private ParserCreateNewUserData parserCreateNewUserData;

	public interface ParserCreateNewUserData {
		public abstract void parserCreateNewUserResult(boolean webErrer,
				boolean loginErrer,  ChaiCreateOrderData chaiCreateOrderData,UniversalData universalData);
	}

	public void setOnParserCreateNewUserData(
			ParserCreateNewUserData parserCreateNewUserData) {
		this.parserCreateNewUserData = parserCreateNewUserData;
	}

}
