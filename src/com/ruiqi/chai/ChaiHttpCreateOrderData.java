package com.ruiqi.chai;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.ruiqi.chai.ChaiHttputils.Success;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.works.CreateOrderActivity;

/**
 * 创建订单
 * @author Administrator
 *
 */
public class ChaiHttpCreateOrderData implements Success {

	private static ChaiHttpCreateOrderData CreateOrder;

	private ChaiHttputils httputils;
	private int where;

	public static ChaiHttpCreateOrderData getInstance() {
		if (CreateOrder == null) {
			CreateOrder = new ChaiHttpCreateOrderData();
		}
		return CreateOrder;
	}
	
	private ChaiHttpCreateOrderData() {
		httputils = new ChaiHttputils();
		httputils.setOnSuccess(this);
	}

	/**
	 * 
	 * @param where 1 保存订单 2开始订单
	 * @param usermobile用户手机号
	 * @param username用户民称
	 * @param sheng 省
	 * @param shi市
	 * @param qu区
	 * @param cun村
	 * @param detail地址详情
	 * @param shipper_name送气工姓名
	 * @param mobile 送气工手机号
	 * @param shop_id 门店id
	 * @param datajson 订单json
	 * @param money 订单总价
	 * @param user_type 客户类型
	 * @param shipper_id 送气工id
	 * @param youhuijson 优惠id json
	 */
	public void app2server(int where,String usermobile, String username, String sheng,
			String shi, String qu, String cun, String detail,
			String shipper_name, String mobile, String shop_id,
			String datajson, String money, String user_type,String shipper_id,String youhuijson) {
		this.where=where;
		RequestParams params = new RequestParams(RequestUrl.CREATE_ORDER);
		params.addBodyParameter("mobile", usermobile);
		params.addBodyParameter("username", username);
		params.addBodyParameter("shipper_id", shipper_id);
		params.addBodyParameter("sheng", sheng);
		params.addBodyParameter("shi",shi);
		params.addBodyParameter("qu", qu);
		params.addBodyParameter("cun", cun);
		params.addBodyParameter("address", detail);
		params.addBodyParameter("shipper_name", shipper_name);
		params.addBodyParameter("shipper_mobile", mobile);
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("urgent", 0 + "");
		params.addBodyParameter("goodtime", CurrencyUtils.getNowTime());
		params.addBodyParameter("isold", "0");// 是否有折旧
		params.addBodyParameter("ismore", "0");// 是否有残页
		params.addBodyParameter("status", "1");// 1 客户已经存在
		params.addBodyParameter("data", datajson);
		params.addBodyParameter("tc_type", "0");//订单类型
		params.addBodyParameter("money", money);
		params.addBodyParameter("user_type", user_type);// 用户类型 1居民 2商业
		params.addBodyParameter("youhuijson", youhuijson);// 优惠json
		
		Log.e("lll", "创建订单参数"+params.getStringParams().toString());
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
					parserCreateOrderData.parserCreateOrderResult(true, true,
							 chaiCreateOrderData,universalData,where);
				} else {
					universalData = gson.fromJson(result, UniversalData.class);
					parserCreateOrderData.parserCreateOrderResult(true, false,chaiCreateOrderData,
							universalData,where);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			universalData=new UniversalData();
			parserCreateOrderData.parserCreateOrderResult(false, false,chaiCreateOrderData,
					universalData,where);
		}

	}

	private ParserCreateOrderData parserCreateOrderData;

	public interface ParserCreateOrderData {
		public abstract void parserCreateOrderResult(boolean webErrer,
				boolean loginErrer, ChaiCreateOrderData chaiCreateOrderData,UniversalData universalData,int where);
	}

	public void setOnParserCreateOrderData(
			ParserCreateOrderData parserCreateOrderData) {
		this.parserCreateOrderData = parserCreateOrderData;
	}

}
