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
 * 退押金给客户提交
 * @author Administrator
 *
 */
public class ChaiHttpTuiYaJinToCustomer implements Success{
	
	private static ChaiHttpTuiYaJinToCustomer TuiYaJinToCustomer;

	private ChaiHttputils httputils;

	public static ChaiHttpTuiYaJinToCustomer getInstance() {
		if (TuiYaJinToCustomer == null) {
			TuiYaJinToCustomer = new ChaiHttpTuiYaJinToCustomer();
		}
		return TuiYaJinToCustomer;
	}

	private ChaiHttpTuiYaJinToCustomer() {
		httputils = new ChaiHttputils();
		httputils.setOnSuccess(this);
	}
/**
 * 
 * @param id 订单id
 * @param receiptno 收据号
 * @param money 付给客户的钱
 * @param kid 客户id
 * @param bottle 扫描钢瓶
 * @param canye_money 余气的钱
 * @param canye_weight 余气的重量
 * @param shop_id 送气工id
 * @param shipper_id 门店id
 */
	public void app2server(String id,String money,String kid,String bottle,String canye_money,String canye_weight,String shop_id,String shipper_id) {
		RequestParams params = new RequestParams(RequestUrl.CONFIRM_DEPOSIT);
		params.addBodyParameter("depositsn", id);
		params.addBodyParameter("money", money);
		params.addBodyParameter("kid", kid);
		params.addBodyParameter("bottle", bottle);// 扫的瓶
		params.addBodyParameter("canye_money", canye_money);
		params.addBodyParameter("canye_weight", canye_weight);
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("shipper_id", shipper_id);
		Log.e("lll", "退押金给客户参数"+params.getStringParams());
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
					parserTuiYaJinToCustomerData.parserTuiYaJinToCustomerResult(true,true,universalData);
				}else{
					parserTuiYaJinToCustomerData.parserTuiYaJinToCustomerResult(true,false,universalData);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			parserTuiYaJinToCustomerData.parserTuiYaJinToCustomerResult(false,false,universalData);
		}
	}

	private ParserTuiYaJinToCustomerData parserTuiYaJinToCustomerData;

	public interface ParserTuiYaJinToCustomerData {
		public abstract void parserTuiYaJinToCustomerResult(boolean webErrer,boolean loginErrer,UniversalData universalData);
	}

	public void setOnParserTuiYaJinToCustomerData(ParserTuiYaJinToCustomerData parserTuiYaJinToCustomerData) {
		this.parserTuiYaJinToCustomerData = parserTuiYaJinToCustomerData;
	}

}
