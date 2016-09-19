package com.ruiqi.chai;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.chai.ChaiHttpBottleToShop.ParserBottleToShopData;
import com.ruiqi.utils.ChaiMyDialog;
import com.ruiqi.utils.ChaiMyDialog.ChaiSelectCallBack;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.works.R;

public class ChaiBottleToShopZheJiu extends Activity implements
		OnClickListener, ParserBottleToShopData,ChaiSelectCallBack {

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chai_bottle_to_shop_zhejiu);
		arrayLists = (ArrayList<ChaiTableInfo>) getIntent()
				.getSerializableExtra("data");
		initView();
	}

	private TextView tv_back, tvTitle, tv_summary_content, botttle_confirm;
	private ListView lv_bottle_detail;
	private ChaiListViewAdapterText3 chaiListViewAdapterText2;
	private ImageView ivBack;

	private ArrayList<ChaiTableInfo> arrayList = new ArrayList<ChaiTableInfo>();
	private ArrayList<ChaiTableInfo> arrayLists;
	private int total;
	private ProgressDialog pd;

	private void initView() {
		pd = new ProgressDialog(this);
		pd.setMessage("正在提交中...");
		tv_back = (TextView) findViewById(R.id.tv_back);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("折旧瓶信息");
		tv_summary_content = (TextView) findViewById(R.id.tv_summary_content);
		botttle_confirm = (TextView) findViewById(R.id.botttle_confirm);
		lv_bottle_detail = (ListView) findViewById(R.id.lv_bottle_detail);
		chaiListViewAdapterText2 = new ChaiListViewAdapterText3(this, arrayList);
		lv_bottle_detail.setAdapter(chaiListViewAdapterText2);
		getData();
		tv_back.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		botttle_confirm.setOnClickListener(this);
		tv_summary_content.setOnClickListener(this);
		ChaiHttpBottleToShop.getInstance().setOnParserBottleToShopData(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivBack:
		case R.id.tv_back:
			finish();
			break;
		case R.id.botttle_confirm:// 请求回瓶
			ChaiMyDialog.getInstance().setSelectCallBack(this);
			ChaiMyDialog.getInstance().showHintConfirm(this, "确认是否提交折旧瓶？","确定","取消");
			
			break;
		default:
			break;
		}
	}

	public void getData() {
		if (arrayLists != null) {
			for (int i = 0; i < arrayLists.size(); i++) {
				total += Integer.parseInt(arrayLists.get(i).getTitle2());
				arrayList.add(new ChaiTableInfo(i + 1 + "", arrayLists.get(i)
						.getTitle1(), arrayLists.get(i).getTitle2()));
			}
		}
		if (total != 0) {
			tv_summary_content.setText("共计" + total + "瓶");
		} else if (total == 0) {
			tv_summary_content.setText("暂时没有瓶");
		}
		chaiListViewAdapterText2.notifyDataSetChanged();
	}

	@Override
	public void parserBottleToShopResult(boolean webErrer, boolean loginErrer,
			UniversalData universalData) {
		if(webErrer){
			if(loginErrer){
				Toast.makeText(this, universalData.getResultInfo(), Toast.LENGTH_SHORT).show();
				finish();
			}else{
				
			}
		}else{}

	}

	/**
	 * 封装data串
	 * 
	 * @return
	 */
	public String getGsonString() {
		JSONArray jsoa = new JSONArray();
		for (int i = 0; i < arrayLists.size(); i++) {
			JSONObject jsob = new JSONObject();
			try {
				jsob.put("type_name", arrayLists.get(i).getTitle1());
				jsob.put("type_num", arrayLists.get(i).getTitle2());
				jsob.put("type_id", arrayLists.get(i).getTitle3());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			jsoa.put(jsob);
		}
		return jsoa.toString();

	}
	
	public void confirmReturn(){
		if (total == 0) {
			ChaiMyDialog.getInstance().showHint(this, "您没有折旧瓶需要送回门店！！！");
		} else {
			pd.show();
			ChaiHttpBottleToShop.getInstance().app2server(3,
					(String) SPUtils.get(this, SPutilsKey.SHIP_ID, "0"),
					(String) SPUtils.get(this, "shipper_name", ""),
					(String) SPUtils.get(this, SPutilsKey.SHOP_ID, "0"),
					(Integer) SPUtils.get(this, SPutilsKey.TOKEN, 0),
					getGsonString(), "", "");
		}
	}

	@Override
	public void chaiSelectCallBack(boolean isSelect) {
		if(isSelect){
			confirmReturn();
		}
		
	}

}
