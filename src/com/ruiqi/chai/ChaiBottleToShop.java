package com.ruiqi.chai;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.chai.ChaiHttpBottleToShop.ParserBottleToShopData;
import com.ruiqi.db.GpDao;
import com.ruiqi.utils.ChaiMyDialog;
import com.ruiqi.utils.ChaiMyDialog.ChaiSelectCallBack;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.works.R;

public class ChaiBottleToShop extends Activity implements OnClickListener,
		ParserBottleToShopData, OnItemClickListener,ChaiSelectCallBack{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chai_bottle_to_shop);
		type = getIntent().getStringExtra("type");
		initView();
	}

	private String type = "0";
	private int types;
	private TextView tv_back, tvTitle, tv_summary_content, botttle_confirm;
	private ListView lv_bottle_detail;
	private GpDao gd;
	private ChaiListViewAdapterText4 chaiListViewAdapterText2;
	private ImageView ivBack;
	private ArrayList<ChaiTableInfo> arrayList = new ArrayList<ChaiTableInfo>();// 数据库查询出来钢瓶的容器
	private ArrayList<ChaiTableInfo> arrayListSelect = new ArrayList<ChaiTableInfo>();// 被选中钢瓶容器
	private ArrayList<ChaiTableInfo> arrayLists = new ArrayList<ChaiTableInfo>();// 钢瓶汇总的容器

	private ProgressDialog pd;

	private void initView() {
		pd = new ProgressDialog(this);
		pd.setMessage("正在提交中...");
		ivBack = (ImageView) findViewById(R.id.ivBack);
		tv_back = (TextView) findViewById(R.id.tv_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		if (type.equals("0")) {
			types = 2;
			tvTitle.setText("空瓶信息");
		} else {
			types = 1;
			tvTitle.setText("重瓶信息");
		}
		tv_summary_content = (TextView) findViewById(R.id.tv_summary_content);
		botttle_confirm = (TextView) findViewById(R.id.botttle_confirm);
		lv_bottle_detail = (ListView) findViewById(R.id.lv_bottle_detail);
		chaiListViewAdapterText2 = new ChaiListViewAdapterText4(this, arrayList);
		lv_bottle_detail.setAdapter(chaiListViewAdapterText2);
		lv_bottle_detail.setOnItemClickListener(this);
		gd = GpDao.getInstances(this);
		getData(type);
		ivBack.setOnClickListener(this);
		tv_back.setOnClickListener(this);
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
		case R.id.botttle_confirm:// 请求退还钢瓶
			ChaiMyDialog.getInstance().setSelectCallBack(this);
			ChaiMyDialog.getInstance().showHintConfirm(this, "是否退还钢瓶？","确定","取消");
			break;
		default:
			break;
		}
	}

	public void getData(String str) {
		Cursor c = gd.readDb.query("gangpingxinxi", new String[] { "number",
				"xinpin", "type_name", "type" }, "is_open=?",
				new String[] { str }, null, null, null);
		if (c.getCount() != 0) {
			tv_summary_content.setText("共计" + c.getCount() + "瓶");
		} else {
			tv_summary_content.setText("暂时没有瓶");
		}
		if (c.getCount() != 0) {
			c.moveToFirst();
			do {
				arrayList.add(new ChaiTableInfo(c.getPosition() + 1 + "", c
						.getString(c.getColumnIndex("number")), c.getString(c
						.getColumnIndex("xinpin")), c.getString(c
						.getColumnIndex("type_name")), c.getString(c
						.getColumnIndex("type"))));
			} while (c.moveToNext());
		}
		chaiListViewAdapterText2.notifyDataSetChanged();
	}

	@Override
	public void parserBottleToShopResult(boolean webErrer, boolean loginErrer,
			UniversalData universalData) {
		pd.dismiss();
		if (webErrer) {
			if (loginErrer) {
				Toast.makeText(this, universalData.getResultInfo(), Toast.LENGTH_SHORT).show();
				finish();
			} else {
			}
		} else {
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (arrayListSelect.contains(arrayList.get(position))) {
			view.setBackgroundResource(R.drawable.rectangle_white);
			arrayListSelect.remove(arrayList.get(position));
		} else {
			view.setBackgroundResource(R.drawable.rectangle_2269d4);
			arrayListSelect.add(arrayList.get(position));
		}

	}

	/**
	 * 封装data串 钢瓶信息 ,计算每一种类型的钢瓶数保存到arrayLists
	 * 
	 * @return
	 */
	public String getGsonString(ArrayList<ChaiTableInfo> arrayListSelec) {
		JSONArray jsoa = new JSONArray();
		for (int i = 0; i < arrayListSelec.size(); i++) {
			JSONObject jsob = new JSONObject();
			try {
				jsob.put("number", arrayListSelec.get(i).getTitle2());
				jsob.put("xinpian", arrayListSelec.get(i).getTitle3());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			jsoa.put(jsob);
			// 计算每一种类型的钢瓶数
			boolean flag = true;
			for (int j = 0; j < arrayLists.size(); j++) {
				if (arrayLists.get(j).getTitle1().equals(arrayListSelec.get(i).getTitle5())) {
					arrayLists.get(j).setNumber(arrayLists.get(j).getNumber() + 1);
					flag = false;
					break;
				}
			}
			if (flag) {
				arrayLists.add(new ChaiTableInfo(arrayListSelec.get(i).getTitle5(), arrayListSelec.get(i).getTitle4(), 1));
			}
		}
		return jsoa.toString();
	}

	/**
	 * 封装data串 钢瓶汇总
	 * 
	 * @return
	 */
	public String getGsonStringInfo() {
		JSONArray jsoa = new JSONArray();
		for (int i = 0; i < arrayLists.size(); i++) {
			JSONObject jsob = new JSONObject();
			try {
				jsob.put("type_id", arrayLists.get(i).getTitle1());
				jsob.put("type_name", arrayLists.get(i).getTitle2());
				jsob.put("type_num", arrayLists.get(i).getNumber());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			jsoa.put(jsob);
		}
		return jsoa.toString();
	}
	/**
	 * 确认退瓶
	 */
	public void confirmReturn(){
		if(arrayList.size()==0){
			ChaiMyDialog.getInstance().showHint(this, "您没有要退还的瓶！");
		}else{
			if (arrayListSelect.size() == 0) {//如果没有选中钢瓶  默认退还全部钢瓶
				pd.show();
				ChaiHttpBottleToShop.getInstance().app2server(types,
						(String) SPUtils.get(this, SPutilsKey.SHIP_ID, "0"),
						(String) SPUtils.get(this, "shipper_name", ""),
						(String) SPUtils.get(this, SPutilsKey.SHOP_ID, "0"),
						(Integer) SPUtils.get(this, SPutilsKey.TOKEN, 0),
						getGsonString(arrayList), getGsonStringInfo(), "");
			} else {
				pd.show();
				ChaiHttpBottleToShop.getInstance().app2server(types,
						(String) SPUtils.get(this, SPutilsKey.SHIP_ID, "0"),
						(String) SPUtils.get(this, "shipper_name", ""),
						(String) SPUtils.get(this, SPutilsKey.SHOP_ID, "0"),
						(Integer) SPUtils.get(this, SPutilsKey.TOKEN, 0),
						getGsonString(arrayListSelect), getGsonStringInfo(), "");
			}
		}
	}

	@Override
	public void chaiSelectCallBack(boolean isSelect) {
		if(isSelect){
			confirmReturn();
		}
	}

}
