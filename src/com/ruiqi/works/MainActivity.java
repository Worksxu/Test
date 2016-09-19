package com.ruiqi.works;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.InstrumentedActivity;

import com.google.gson.Gson;
import com.ruiqi.bean.GangPingList;
import com.ruiqi.chai.ChaiCreatOrderActivity;
import com.ruiqi.chai.ChaiSafeReport;
import com.ruiqi.chai.ChaiTuiPing;
import com.ruiqi.db.GpDao;
import com.ruiqi.utils.DensityUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.MyActivityManager;
import com.ruiqi.utils.PrefUtils;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.UpdateManager;
import com.ruiqi.view.RoundImageView;

public class MainActivity extends InstrumentedActivity implements OnClickListener,ParserData{
	
	private RelativeLayout rl_menu; //gridview的父布局
	private int width; //rl_menu的宽度
	private int height; //rl_menu的高度
	private int dip ; //间隙宽度
	
	private GridView gv_menu;
	
	//数据
	private int [] icons = {R.drawable.my_order,R.drawable.create,R.drawable.botter,
									R.drawable.stock,R.drawable.arrears_user,R.drawable.finance,R.drawable.tuiping,R.drawable.safe_report};
	private String [] types = {"我的订单","创建订单","商品领取","我的库存","欠款用户","我的财务","下单退瓶","安全报告"};
	private List<Map<String, Object>> list;
	
	//适配器
	private MySimAdapter adapter;
	
	private RoundImageView riv_head;//头像
	private RelativeLayout rl_account_magger;//个人资料
	
	private TextView tv_home_name,tv_home_mobile,tv_home_address;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		com.ruiqi.view.UpdateManager up = new com.ruiqi.view.UpdateManager(MainActivity.this);
		up.checkUpdate();
		MyActivityManager.getInstance().pushOneActivity(this);
		init();
		//initData();
		//用于手动计算view的宽度和高度,直接计算时获得的高度为0
		rl_menu.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			boolean isFirst = true;//默认调用两次，手动让其调用一次
			@Override
			public void onGlobalLayout() {
				if(isFirst){
					isFirst = false;//
					initData();
					System.out.println("height="+height);
					adapter = new MySimAdapter(MainActivity.this, list, R.layout.main_gridview_item, 
							new String [] {"icon","type"}, 
							new int [] {R.id.iv_item_menu,R.id.tv_item_menu});
					gv_menu.setAdapter(adapter);
				}
				
			}
		});
	}
	/**
	 * 数据的初始化
	 */
	private void initData() {
		width = rl_menu.getWidth();
		height = rl_menu.getHeight();
		dip = DensityUtils.dp2px(MainActivity.this,10);
		
		list = new ArrayList<Map<String,Object>>();
    	for(int i = 0;i<icons.length;i++){
    		Map<String , Object> map = new HashMap<String, Object>();
    		map.put("icon", icons[i]);
    		map.put("type", types[i]);
    		list.add(map);
    	}
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		getWorkerBottle();
	}
	/**
	 * 组件的初始化
	 */
	
	private String name,mobile;
	private void init() {
		//地址
		name = (String) SPUtils.get(MainActivity.this, "shipper_name", "error");
		mobile = (String) SPUtils.get(MainActivity.this, SPutilsKey.MOBILLE, "error");
		String shop_name = (String) SPUtils.get(MainActivity.this, "shop_name", "");
		
		rl_menu = (RelativeLayout) findViewById(R.id.rl_menu);
		gv_menu = (GridView) findViewById(R.id.gv_menu);
		
		rl_account_magger = (RelativeLayout) findViewById(R.id.rl_account_magger);
		rl_account_magger.setOnClickListener(this);
		
		riv_head = (RoundImageView) findViewById(R.id.riv_head);
		riv_head.setOnClickListener(this);
		
		tv_home_name = (TextView) findViewById(R.id.tv_home_name);
		tv_home_name.setText(name);
		tv_home_mobile = (TextView) findViewById(R.id.tv_home_mobile);
		tv_home_mobile.setText(RequestUrl.test+mobile);
		tv_home_address = (TextView) findViewById(R.id.tv_home_address);
		tv_home_address.setText(shop_name);
		
		//gridview设置点击事件
		gv_menu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				pageJump(position);
			}

		});
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_account_magger:
			pageJump();
			break;
		case R.id.riv_head:
			pageJump();
			break;
		default:
			break;
		}
	}
	/**
	 * gridview的点击跳转事件
	 * @param position
	 */
	private void pageJump(int position) {
		Intent intent = null;
		switch (position) {
		case 0://我的订单
			intent = new Intent(MainActivity.this, MyOrderActivity.class);
			break;
		case 1://创建订单
			//intent = new Intent(MainActivity.this, CreateOrderActivity.class);
			intent = new Intent(MainActivity.this, ChaiCreatOrderActivity.class);
			break;
		case 2://商品领取
			intent = new Intent(MainActivity.this, GoodsReceiActivity.class);
			break;
		case 3://我的库存
			intent = new Intent(MainActivity.this, StockActivity.class);
			break;
		case 4://欠款账户
			intent = new Intent(MainActivity.this, ArrearsUserActivity.class);
			break;
		case 5://我的财务
			intent = new Intent(MainActivity.this, MyMoneyActivity.class);
			break;
		case 6://下退瓶订单
			intent = new Intent(MainActivity.this, ChaiTuiPing.class);
			break;
		case 7://安全报告
			intent = new Intent(MainActivity.this, ChaiSafeReport.class);
			break;
		default:
			break;
		}
		startActivity(intent);
	}
	/**
	 * 跳转到个人资料界面
	 */
	private void pageJump(){
		Intent intent = new Intent(MainActivity.this, PersonalActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 自定义simpleadapter,用于适配
	 */
	class MySimAdapter extends SimpleAdapter{
		private Context context;
		public MySimAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			this.context = context;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			convertView = LayoutInflater.from(context).inflate(R.layout.main_gridview_item, null);
			@SuppressWarnings("unchecked")
			HashMap<String, Object> theMap = (HashMap<String,Object>)getItem(position);
			TextView tvShow = (TextView)convertView.findViewById(R.id.tv_item_menu);
			ImageView ivgType = (ImageView) convertView.findViewById(R.id.iv_item_menu);
			tvShow.setText(theMap.get("type").toString());
			ivgType.setImageResource((Integer) theMap.get("icon"));
 
			// 根据列数计算项目宽度，以使总宽度尽量填充屏幕
			int itemWidth = (width-dip)/2;
			// 下面根据比例计算您的item的高度
			int itemHeight = (height-2*dip)/4;
		    AbsListView.LayoutParams param = new AbsListView.LayoutParams(itemWidth,itemHeight);
		    convertView.setLayoutParams(param);
		    return convertView;
			
		}
		
	}

	/**
	 * 按两次返回键退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			//调用双击退出程序
			exit();
		}
		return false;
	}    
	/**
	 * 双击退出程序功能
	 */
	private static boolean isExit = false; //判定是否退出
	private void exit() {
		Timer tExit = null;
		if(isExit==false){
			isExit = true; //准备退出
			Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; //取消退出
				}
			}, 2000);//如果两秒中没有按下，启动定时器，取消上次执行的操作
		}else{//退出系统
			MyActivityManager.getInstance().finishAllActivity();
			System.exit(0);
			Process.killProcess(0);
		}
	}
	
	
	public void getWorkerBottle(){
		
		String shop_id = (String) SPUtils.get(this, SPutilsKey.SHOP_ID, "error");
		String shiper_id = (String) SPUtils.get(this, SPutilsKey.SHIP_ID, "error");
		RequestParams params = new RequestParams(RequestUrl.LOGIN_BOTTLELIST);
		
		SPUtils.get(this, SPutilsKey.SHIP_ID, "");
		
		params.addBodyParameter("shop_id", shop_id);
		
		params.addBodyParameter("shipper_id", shiper_id);
		HttpUtil httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
		httpUtil.PostHttp(params, 1);
	}
	@Override
	public void sendResult(final String result, int what) {
		Log.e("lll", "钢瓶库存"+result);
		 final GpDao gd=GpDao.getInstances(this);
		 Thread t = new Thread(new Runnable(){  
	            public void run(){  
	            	try {
	        			JSONObject jsob=new JSONObject(result);
	        			Gson gson=new Gson();
	        			if(jsob.getInt("resultCode")==1){
	        				GangPingList gangPingList=gson.fromJson(result,  GangPingList.class);
	        				gd.writeDb.delete("gangpingxinxi", null, null);
	        				for (int i = 0; i < gangPingList.getResultInfo().size(); i++) {
	        					gd.insertGangPingXinXi(gangPingList.getResultInfo().get(i).getXinpian(), gangPingList.getResultInfo().get(i).getNumber(),gangPingList.getResultInfo().get(i).getType(),gangPingList.getResultInfo().get(i).getIs_open(),gangPingList.getResultInfo().get(i).getType_name());
							}
	        			}
	        		} catch (JSONException e) {
	        			e.printStackTrace();
	        		}
	            }});  
	        t.start();  
	}
	
}
















