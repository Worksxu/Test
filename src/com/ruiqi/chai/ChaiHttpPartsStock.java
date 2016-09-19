package com.ruiqi.chai;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.google.gson.Gson;
import com.ruiqi.chai.ChaiHttputils.Success;
import com.ruiqi.utils.RequestUrl;
/**
 * 配件库存
 * @author Administrator
 *
 */
public class ChaiHttpPartsStock implements Success{
	
	private static ChaiHttpPartsStock PartsStock;

	private ChaiHttputils httputils;

	public static ChaiHttpPartsStock getInstance() {
		if (PartsStock == null) {
			PartsStock = new ChaiHttpPartsStock();
		}
		return PartsStock;
	}

	private ChaiHttpPartsStock() {
		httputils = new ChaiHttputils();
		httputils.setOnSuccess(this);
	}
/**
 * 
 * @param shipper_id 送气工id
 */
	public void app2server(String shipper_id) {
		RequestParams params = new RequestParams(RequestUrl.PJ_STOCK);
		params.addBodyParameter("shipper_id", shipper_id);
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
					parserPartsStockData.parserPartsStockResult(true,true,universalData);
				}else{
					parserPartsStockData.parserPartsStockResult(true,false,universalData);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			parserPartsStockData.parserPartsStockResult(false,false,universalData);
		}

	}

	private ParserPartsStockData parserPartsStockData;

	public interface ParserPartsStockData {
		public abstract void parserPartsStockResult(boolean webErrer,boolean loginErrer,ChaiSafeData universalData);
	}

	public void setOnParserPartsStockData(ParserPartsStockData parserPartsStockData) {
		this.parserPartsStockData = parserPartsStockData;
	}

}
