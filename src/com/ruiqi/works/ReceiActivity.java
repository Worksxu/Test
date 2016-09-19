package com.ruiqi.works;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.a.a.p;

import com.ruiqi.bean.TableInfo;
import com.ruiqi.chai.ChaiHeTongData;
import com.ruiqi.chai.ChaiHttpCustomerQK;
import com.ruiqi.chai.ChaiHeTongData.QianKuanDetail;
import com.ruiqi.chai.ChaiHttpCustomerQK.ParserCustomerQKData;
import com.ruiqi.chai.ListViewAdapterText4;
import com.ruiqi.chai.ListViewAdapterText4NoTitle;
import com.ruiqi.chai.UniversalData;
import com.ruiqi.utils.ChaiMyDialog;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;

/**
 * 收款界面
 * @author Administrator
 *
 */
public class ReceiActivity extends BaseActivity implements ParserCustomerQKData,OnItemClickListener{
	

	private TextView tv_money_content,tv_name,tv_out;
	private TextView et_input;
	private RelativeLayout rl_btn;
	private ProgressDialog pd,pds;
	private ListView lv_hetonghao;
	private ListViewAdapterText4NoTitle listViewAdapterText4;
	private ArrayList<TableInfo> arrayList=new ArrayList<TableInfo>();
	private ArrayList<String> arrayList2=new ArrayList<String>();
	private String kid,username,total,hetongmoney="";
	private float selecMoney;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result=(String) msg.obj;
			paraData(result);
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_recei);
		setTitle("收缴欠款");
		kid=getIntent().getStringExtra("kid");
		username=getIntent().getStringExtra("username");
		total=getIntent().getStringExtra("total");
		
		ChaiHttpCustomerQK.getInstance().setOnParserCustomerQKData(this);
		ChaiHttpCustomerQK.getInstance().app2server(kid,(Integer)SPUtils.get(this,SPutilsKey.TOKEN, 0)+"");
		init();
	}

	protected void paraData(String result) {
		try {
			JSONObject jsob=new JSONObject(result);
			if(jsob.getInt("resultCode")==1){
				Toast.makeText(this, "缴费成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(ReceiActivity.this, MainActivity.class);
				startActivity(intent);
			}else{
				Toast.makeText(this, "请求失败", Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	private void init() {
		pds=new ProgressDialog(this);
		pds.setMessage("搜索中...");
		pd=new ProgressDialog(this);
		pd.setMessage("正在加载中.....");
		tv_out=(TextView) findViewById(R.id.tv_out);
		tv_name=(TextView) findViewById(R.id.tv_name);
		lv_hetonghao=(ListView) findViewById(R.id.lv_hetonghao);
		listViewAdapterText4=new ListViewAdapterText4NoTitle(this, arrayList);
		lv_hetonghao.setAdapter(listViewAdapterText4);
		lv_hetonghao.setOnItemClickListener(this);
		tv_money_content=(TextView) findViewById(R.id.tv_money_content);
		tv_name.setText(username);
		tv_money_content.setText("￥"+total);
		et_input= (TextView) findViewById(R.id.et_input);
		et_input.setText("￥0");
		rl_btn=(RelativeLayout) findViewById(R.id.rl_btn);
		tv_out.setOnClickListener(this);		
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public Handler[] initHandler() {  
		return null;
	}
	
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_out:
			if(selecMoney!=0){
				requestRecei(selecMoney+"",getJson());
			}else{
				ChaiMyDialog.getInstance().showHint(this, "请选择欠款合同！");
			}
			
			break;
		default:
			break;
		}
	}
	/**
	 * 
	 * @param money
	 * @param contractno合同
	 */
	private void requestRecei(String money,String contractno) {
		RequestParams params=new RequestParams(RequestUrl.ADDREPAYMENT);
		params.addBodyParameter("shipper_id", (String)SPUtils.get(this,SPutilsKey.SHIP_ID, "11"));
		params.addBodyParameter("money", money);
		params.addBodyParameter("kid", kid);
		params.addBodyParameter("type", "4");//现金支付
		params.addBodyParameter("contractno",contractno);//合同编号
		HttpUtil httpUtil=new HttpUtil();
		pd.show();
		Log.e("lll", "收欠款参数"+params.getStringParams().toString());
		httpUtil.PostHttp(params, handler, pd);
	}

	private ArrayList<QianKuanDetail> alQianKuanDetail;
	private ArrayList<QianKuanDetail> selectQianKuanDetail=new ArrayList<ChaiHeTongData.QianKuanDetail>();
	@Override
	public void parserCustomerQKResult(boolean webErrer, boolean loginErrer,ChaiHeTongData chaiHeTongData, UniversalData universalData) {
		
		
		if(webErrer){
			if(loginErrer){
				arrayList.clear();
				alQianKuanDetail=chaiHeTongData.getResultInfo();
				for (int i = 0; i < alQianKuanDetail.size(); i++) {
					arrayList.add(new TableInfo(alQianKuanDetail.get(i).getContractno(), alQianKuanDetail.get(i).getMoney(),alQianKuanDetail.get(i).getTypename(),alQianKuanDetail.get(i).getTime_list()));
				}
				listViewAdapterText4.notifyDataSetChanged();
			}else{
				Toast.makeText(this, universalData.getResultInfo(), Toast.LENGTH_SHORT).show();
			}
		}else{}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		
		if(arrayList2.contains(position+"")){
			
			selectQianKuanDetail.remove(alQianKuanDetail.get(position));
			arrayList2.remove(position+"");
			listViewAdapterText4.setSelectItemIdArrayList(arrayList2);
			sumMoney();
		}else if(!arrayList2.contains(position+"")){
			selectQianKuanDetail.add(alQianKuanDetail.get(position));
			arrayList2.add(position+"");
			listViewAdapterText4.setSelectItemIdArrayList(arrayList2);
			sumMoney();
		}
		listViewAdapterText4.notifyDataSetChanged();
	}
	
	public void sumMoney(){
		selecMoney=0f;
		for (int i = 0; i < selectQianKuanDetail.size(); i++) {
			selecMoney+=Float.parseFloat(selectQianKuanDetail.get(i).getMoney());
		}
		et_input.setText("￥"+selecMoney);
	}
	
	public String getJson(){
		JSONArray jsa=new JSONArray();
		for (int i = 0; i <  selectQianKuanDetail.size(); i++) {
			JSONObject jsob=new JSONObject();
			try {
				jsob.put("arrears_type", alQianKuanDetail.get(i).getArrears_type());
				jsob.put("money", alQianKuanDetail.get(i).getMoney());
				jsob.put("contractno", alQianKuanDetail.get(i).getContractno());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			jsa.put(jsob);
		}
		return jsa.toString();
	}
}
