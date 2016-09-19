package com.ruiqi.chai;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruiqi.adapter.CommonAdapter;
import com.ruiqi.bean.SelfContent;
import com.ruiqi.chai.ChaiHttpSafeData.ParserSafeDefaultData;
import com.ruiqi.works.R;

public class ChaiSelfActivity extends Activity implements OnClickListener,ParserSafeDefaultData,OnItemClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chai_self_activity);
		chaiCustomerDataInfo = (ChaiCustomerDataInfo) getIntent()
				.getSerializableExtra("data");
		initView();
	}

	private ChaiCustomerDataInfo chaiCustomerDataInfo;
	private TextView tv_anjian, tv_back, tv_title;
	private ImageView ivBack;
	private RelativeLayout rl_button_paizhao;
	private ListView lv_self;
	private ChaiSelfAdapter commonAdapter;
	private ArrayList<SelfContent> mDatas=new ArrayList<SelfContent>();
	public static List<String> numbers = new ArrayList<String>();
	private ChaiSafeData universalData;

	private void initView() {
		numbers.clear();
		tv_title = (TextView) findViewById(R.id.tvTitle);
		tv_back = (TextView) findViewById(R.id.tv_back);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		tv_anjian = (TextView) findViewById(R.id.tv_anjian);
		lv_self = (ListView) findViewById(R.id.lv_self);
		rl_button_paizhao = (RelativeLayout) findViewById(R.id.rl_button_paizhao);

		tv_title.setText("安检选项");
		tv_anjian.setText(TextUtils.isEmpty(chaiCustomerDataInfo.getOrderlist())?"暂无":chaiCustomerDataInfo.getOrderlist());
		rl_button_paizhao.setOnClickListener(this);
		tv_back.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		commonAdapter=new ChaiSelfAdapter(this, mDatas);
		lv_self.setAdapter(commonAdapter);
		lv_self.setOnItemClickListener(this);
		
		ChaiHttpSafeData.getInstance().setOnParserSafeDefaultData(this);
		ChaiHttpSafeData.getInstance().app2server(chaiCustomerDataInfo.getKtype());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
		case R.id.tv_title:
			finish();
			break;
		case R.id.rl_button_paizhao:
			Intent it = new Intent(this, ChaiTakePhotoActivity.class);
			startActivity(it);
			break;
		default:
			break;
		}
	}

	@Override
	public void parserSafeDefaultResult(boolean webErrer, boolean loginErrer,ChaiSafeData universalData) {

		if (webErrer) {
			if (loginErrer) {
				this.universalData=universalData;
				for (int i = 0; i < universalData.getResultInfo().size(); i++) {
					mDatas.add(new SelfContent(universalData.getResultInfo().get(i).getListorder()+"、"+universalData.getResultInfo().get(i).getTitle(), R.drawable.xiaoqi_hui,universalData.getResultInfo().get(i).getId()));
				}
			} else {
			}
		} else {
		}
		commonAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		//改变小旗的状态
				if(mDatas.get(position).getIcon()==R.drawable.xiaoqi_hui){
					mDatas.get(position).setIcon(R.drawable.xiaoqi_lan);
					commonAdapter.notifyDataSetChanged();
					//将编号存入数组中
					numbers.add(universalData.getResultInfo().get(position).getId());
				}else{
					mDatas.get(position).setIcon(R.drawable.xiaoqi_hui);
					commonAdapter.notifyDataSetChanged();
					//删除对应的编号
					numbers.remove(universalData.getResultInfo().get(position).getId());
				}
		
	}

}
