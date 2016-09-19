package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import com.ruiqi.adapter.CommonAdapter;
import com.ruiqi.adapter.ViewHolder;
import com.ruiqi.bean.Weight;
import com.ruiqi.db.GpDao;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.InPutIsBottle;
import com.ruiqi.works.GoodsReceiActivity;
import com.ruiqi.works.NfcActivity;
import com.ruiqi.works.R;
import com.ruiqi.works.WeightActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 重瓶领取碎片
 * @author Administrator
 *
 */
public class BootleWeightFragment extends BaseFragment implements OnClickListener,OnItemLongClickListener{
	
	private ListView lv_content;
	
	private CommonAdapter<Weight> adapter;
	
	private List<Weight> mData;
	
	private EditText et_input;
	private TextView tv_input;
	
	private ProgressDialog pd;
	
	@Override
	public View initView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.bootle_weight_fragment, null);
		
		//initData();
		lv_content = (ListView) view.findViewById(R.id.lv_content);
		tv_input = (TextView) view.findViewById(R.id.tv_input);
		et_input = (EditText) view.findViewById(R.id.et_input);
		tv_input.setOnClickListener(this);
		
		pd = new ProgressDialog(getContext());
		pd.setMessage("正在加载......");
		
		initData();
		
		if(mData!=null){
			
			adapter = new CommonAdapter<Weight>(getContext(),mData,R.layout.ping_list_item) {
				
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
		
		
		return view;
	}

	private void initData() {
		Bundle bundle = getArguments();
		mData = (List<Weight>) bundle.getSerializable("mData");
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_input://手动添加
			if(NfcActivity.mDataRecei==null){
				NfcActivity.mDataRecei = new ArrayList<Weight>();
			}
			String str = et_input.getText().toString();
			new InPutIsBottle(str, getContext(), mData, adapter, pd, GpDao.getInstances(getActivity()),NfcActivity.mDataRecei,2).addDataToList();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		CurrencyUtils.onLongClickDelete(position, getContext(), adapter, mData, NfcActivity.mDataRecei,NfcActivity.mData);
		return false;
	}

}
