package com.ruiqi.chai;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;

import com.ruiqi.bean.Orderdeail;
import com.ruiqi.bean.PeiJianTypeMoney;
import com.ruiqi.bean.Type;
import com.ruiqi.bean.Weight;
import com.ruiqi.db.GpDao;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.InPutIsBottle;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;
import com.ruiqi.works.CreateOrderActivity;
import com.ruiqi.works.NfcActivity;
import com.ruiqi.works.NullActivity;
import com.ruiqi.works.OrderInfoActivity;
import com.ruiqi.works.R;
import com.ruiqi.works.SaoMiaoActivity;
import com.ruiqi.works.SubsidiaryActivity;

//扫描重瓶界面
public class ChaiWeightActivity extends SaoMiaoActivity {

	private String show;

	private List<String> mList;

	public static List<Weight> list;

	private ChaiWeightActivity wa;

	private List<Weight> mData;

	private int num;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("扫描重瓶");
	}

	@Override
	protected void onResume() {
		super.onResume();
		SPUtils.put(ChaiWeightActivity.this, "UI", "chaiweight");
		show = getIntent().getStringExtra("show");
		num = setNum();
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	private int setNum() {
		int num =0;
		if("ChaiCreateOrderActivity".equals(show)){
			for(int i=0;i<SPutilsKey.list.size();i++){
				Type t = SPutilsKey.list.get(i);
				if(t.getType().equals("1")){
					num+=Integer.parseInt(t.getNum());
				}
			}
		}else if("OrderInfoActivity".equals(show)){
			for(int i=0;i<ChaiOrderInfoActivity.list.size();i++){
				Orderdeail o = ChaiOrderInfoActivity.list.get(i);
				Log.e("lll", "商品类型"+o.getGoods_type());
				if(o.getGoods_type().equals("1")){
					num+=Integer.parseInt(o.getNum());
				}
			}
		}
		return num;
	}

	@Override
	public int initFlag() {
		show = getIntent().getStringExtra("show");
		if (show.equals("ChaiCreateOrderActivity")) {
			SPUtils.put(ChaiWeightActivity.this, "NFC", "chaicreateOrder");
			return 2;// 隐藏
		} else if (show.equals("OrderInfoActivity")) {
			SPUtils.put(ChaiWeightActivity.this, "NFC", "orderInfo");
			return 2;
		} else {
			return 1;// 显示
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = null;
		switch (v.getId()) {
		case R.id.next:// 下一步
			intent = initIntent(intent);
			break;
		case R.id.tv_input:// 手动输入
			if (NfcActivity.mDataWeight == null) {
				NfcActivity.mDataWeight = new ArrayList<Weight>();
			}
			str = et_input.getText().toString().trim();
			// 柴钢瓶判断
			new InPutIsBottle(str, ChaiWeightActivity.this, mData, adapter, pd,
					GpDao.getInstances(ChaiWeightActivity.this),
					NfcActivity.mDataWeight, 1).addDataToList();
			break;

		default:
			break;
		}
		if (intent != null) {
			if (mData.size() > 0 || (num==0&&mData.size()==0)) {
				if (NfcActivity.mData != null) {
					NfcActivity.mData = null;
				}
				SPUtils.remove(ChaiWeightActivity.this, "UI");
				startActivity(intent);
				finish();
			} else if (mData.size() == 0) {
				T.showShort(ChaiWeightActivity.this, "请扫描钢瓶");
			} else if (mData.size() > num) {
				T.showShort(ChaiWeightActivity.this, "瓶的数量大于用户实际的需求");
			}
		}
	}

	@Override
	public void jumpPage() {
		super.jumpPage();

		SPutilsKey.type = 0;
		if (NfcActivity.mData != null) {
			NfcActivity.mData = null;
		}
		if (NfcActivity.mDataWeight != null) {
			NfcActivity.mDataWeight = null;
		}
		if (list != null) {
			list = null;
		}
		SPUtils.remove(ChaiWeightActivity.this, "UI");

		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == event.KEYCODE_BACK) {
			SPutilsKey.type = 0;
			if (NfcActivity.mData != null) {
				NfcActivity.mData = null;
			}
			if (NfcActivity.mDataWeight != null) {
				NfcActivity.mDataWeight = null;
			}
			if (list != null) {
				list = null;
			}
			SPUtils.remove(ChaiWeightActivity.this, "UI");
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private Intent initIntent(Intent intent) {
		
		intent = new Intent(ChaiWeightActivity.this, ChaiNullActivity.class);// 空瓶
		intent.putExtra("show", show);

		return intent;
	}

	@Override
	public Handler[] initHandler() {
		return null;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	public List<Weight> initList() {
		mData = new ArrayList<Weight>();
		if (NfcActivity.mDataWeight != null) {
			for (int i = 0; i < NfcActivity.mDataWeight.size(); i++) {
				Weight w = NfcActivity.mDataWeight.get(i);
				mData.add(new Weight(w.getXinpian(), w.getType(), w.getStatus()));
			}
		}
		System.out.println("mDataWeight====" + NfcActivity.mDataWeight);
		return mData;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		System.out.println("NfcActivity.mData=" + NfcActivity.mData);
		CurrencyUtils.onLongClickDelete(position, ChaiWeightActivity.this,
				adapter, mData, NfcActivity.mDataWeight, NfcActivity.mData);
		return false;
	}

}
