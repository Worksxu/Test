package com.ruiqi.works;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.adapter.CommonAdapter;
import com.ruiqi.adapter.ViewHolder;
import com.ruiqi.bean.Weight;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.InPutIsBottle;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class SaoMiaoActivity extends BaseActivity implements OnItemLongClickListener{
	
	public RadioGroup rg_usertype;
	
	public TextView next;
	
	public List<Weight> mData;
	
	public ListView lv_content;
	
	public RelativeLayout rl_saomiao_fragment,rl_saomiao_fragment_content;
	
	public CommonAdapter<Weight> adapter;
	
	public EditText et_input;
	
	public TextView tv_input;
	
	public ProgressDialog pd;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.saomiao);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initA();
		lv_content = (ListView) findViewById(R.id.lv_content);
		rl_saomiao_fragment = (RelativeLayout) findViewById(R.id.rl_saomiao_fragment);
		rl_saomiao_fragment_content = (RelativeLayout) findViewById(R.id.rl_saomiao_fragment_content);
		mData = initList();
		if(mData!=null){
			
			adapter = new CommonAdapter<Weight>(SaoMiaoActivity.this,mData,R.layout.ping_list_item) {
				
				@Override
				public void convert(ViewHolder helper, Weight item,int position) {
					helper.setText(R.id.tv_xinpian, item.getXinpian());
					helper.setText(R.id.tv_type, "1");
					helper.setText(R.id.tv_status, (position+1)+"");
				}
			};
			lv_content.setAdapter(adapter);
			lv_content.setOnItemLongClickListener(this);
		}
		
	}
	
	public abstract List<Weight> initList();
	/**
	 * 初始化组件
	 */
	private RadioButton rb_newuser,rb_olduser;
	private void initA() {
		
		pd = new ProgressDialog(SaoMiaoActivity.this);     
		pd.setMessage("正在加载......");
		rg_usertype = (RadioGroup) findViewById(R.id.rg_usertype);
		rb_newuser = (RadioButton) findViewById(R.id.rb_newuser);
		rb_olduser = (RadioButton) findViewById(R.id.rb_olduser);
		if(SPutilsKey.type==1){
			rb_newuser.setChecked(true);
		}else if(SPutilsKey.type==2){
			rb_olduser.setChecked(true);
		}
		if(initFlag()==1){//扫重瓶显示
			//点击
			rg_usertype.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					switch (checkedId) {
					case R.id.rb_newuser://新用户
						SPutilsKey.type=1;
						break;
					case R.id.rb_olduser://老用户
						SPutilsKey.type=2;
						break;

					default:
						break;
					}
				}
			});
			
		}
		else if(initFlag()==2){//扫空瓶 隐藏
			rg_usertype.setVisibility(View.GONE);
		}
		
		next = (TextView) findViewById(R.id.next);
		next.setOnClickListener(this);
		
		et_input = (EditText) findViewById(R.id.et_input);
		tv_input = (TextView) findViewById(R.id.tv_input);
		tv_input.setOnClickListener(this);
		
		
	}
	
	public String str;
	public abstract int initFlag();//子类实现，判定rediogroup的显示隐藏
	

}





















