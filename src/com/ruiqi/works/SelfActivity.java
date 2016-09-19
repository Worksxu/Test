package com.ruiqi.works;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.adapter.CommonAdapter;
import com.ruiqi.adapter.ViewHolder;
import com.ruiqi.bean.PeiJian;
import com.ruiqi.bean.SelfContent;
import com.ruiqi.bean.Weight;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//安全报告界面
public class SelfActivity extends BaseActivity implements OnCheckedChangeListener,OnItemClickListener{
	
	private ListView lv_self;
	
	private  List<SelfContent> mDatas;
	
	private CommonAdapter<SelfContent> adapter;
	
	private ProgressDialog pd;
	
	private CheckBox iv_peijian;//是否选择配件
	private boolean flag = false; //用于判断选没选择配件
	
	private TextView tv_next;
	
	private RelativeLayout rl;
	private int i = 1;
	
	private List<String> numbers = new ArrayList<String>(); //可变字符数组，用来存储重点标注编号
	
	private TextView tv_last_content;
	
	public static List<String> selfList;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			praseData(result);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_self);
		
		pd = new ProgressDialog(this);
		pd.setMessage("正在加载......");
		pd.show();
		setTitle("安全检查");
		init();
		//请求网络，得到数据
		RequestParams params = new RequestParams(RequestUrl.SELF_REPORT);
		HttpUtil.PostHttp(params, handler, pd);
	}
	
	private void init() {
		lv_self = (ListView) findViewById(R.id.lv_self);
		lv_self.setOnItemClickListener(this);
		
		iv_peijian = (CheckBox) findViewById(R.id.iv_peijian);
		iv_peijian.setOnCheckedChangeListener(this);
		
		
		tv_next = (TextView) findViewById(R.id.tv_next);
		tv_next.setOnClickListener(this);
		
		tv_last_content = (TextView) findViewById(R.id.tv_last_content);
		
		String num = (String) SPUtils.get(SelfActivity.this, "numbers", "error");
		
		if(num!=null&&num.length()>0&&!num.equals("error")){
			tv_last_content.setText(num);
		}else{
			tv_last_content.setText("之前没有重点标注");
		}
	}
	//数据解析
	private void praseData(String result) {
		Log.e("lll安全报告", result);
		mDatas = new ArrayList<SelfContent>();
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){//返回成功,继续解析
				JSONArray array = obj.getJSONArray("resultInfo");
				for(int i=0;i<array.length();i++){
					JSONObject object = array.getJSONObject(i);
					String title = object.getString("title");
					String id = object.getString("id");
					mDatas.add(new SelfContent(title, R.drawable.xiaoqi_hui,id));
					//将数据存入数据库中
					//先根据id查询，如果没有，则插入到数据库中
				}
				//创建适配器z
				adapter = new CommonAdapter<SelfContent>(SelfActivity.this,mDatas,R.layout.self_list_item) {
					@Override
					public void convert(ViewHolder helper, SelfContent item,int position) {
						helper.setImageResource(R.id.iv_myself_pic, item.getIcon());
						helper.setText(R.id.tv_myself_content, (position+1)+","+item.getContent());
					}
				};
				//填充适配器
				lv_self.setAdapter(adapter);
			}else{
				Toast.makeText(SelfActivity.this, "无法连接网络", Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	};
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = null;
		switch (v.getId()) {
		case R.id.tv_next://下一步
			UpdatePeiJianActivity.finalData = new ArrayList<PeiJian>();
			selfList = new ArrayList<String>();
			for(int i=0;i<mDatas.size();i++){
				if(mDatas.get(i).getIcon()==R.drawable.xiaoqi_lan){
					selfList.add(mDatas.get(i).getId());
				}
			}
			//将重点标注编号存起来
			SPUtils.put(SelfActivity.this, "numbers", numbers.toString());
			if(flag){//跳转到配件界面
				intent = new Intent(SelfActivity.this, UpdatePeiJianActivity.class);
			}else{//跳转到拍照留底界面(调用系统相册)
				intent = new Intent(SelfActivity.this, TakePhotoActivity.class);
			}
			break;
		default:
			break;
		}
		if(intent!=null){
			startActivity(intent);
		}
	}
	@Override
	public Activity getActivity() {
		return this;
	}
	@Override
	public Handler[] initHandler() {
		Handler [] handler = {this.handler};
		return handler;
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){//通知跳转到配件界面
			flag = true;
		}else{//通知跳转到拍照留底界面
			flag = false;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		i=1;
		//改变小旗的状态
		if(mDatas.get(position).getIcon()==R.drawable.xiaoqi_hui){
			mDatas.get(position).setIcon(R.drawable.xiaoqi_lan);
			adapter.notifyDataSetChanged();
			//将编号存入数组中
			numbers.add(position+1+"");
		}else{
			mDatas.get(position).setIcon(R.drawable.xiaoqi_hui);
			adapter.notifyDataSetChanged();
			//删除对应的编号
			numbers.remove(position+1+"");
		}
		
	}

}
