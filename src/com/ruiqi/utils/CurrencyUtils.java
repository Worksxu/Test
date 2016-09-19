package com.ruiqi.utils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ruiqi.adapter.CommonAdapter;
import com.ruiqi.bean.Weight;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 通用方法的基础类
 * 
 * @author Administrator
 *
 */
public class CurrencyUtils {
	// 回收handler的方法,防止内存泄漏
	public static void recoveryHandler(Handler mHandler) {
		mHandler.removeCallbacksAndMessages(null);
	}

	/**
	 * 消失软键盘
	 */
	public static void dissMiss(Context ctx, View view) {
		InputMethodManager imm = (InputMethodManager) ctx
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * 日期选择器
	 */
	public static void showDataSelector(Context context, final TextView tv) {
		Calendar calendar = Calendar.getInstance();
		DatePickerDialog dialog = new DatePickerDialog(context,
				new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						tv.setText(year + "年" + (monthOfYear + 1) + "月"+ dayOfMonth + "日");
					}
				}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));

		dialog.show();
	}

	/**
	 * 读取图片
	 * 
	 * @param spath
	 */
	public static void setImageFromSdCard(String spath, ImageView mImageView) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeFile(spath, options);

		mImageView.setImageBitmap(bitmap);
	}

	/**
	 * 判断Sd卡中某图片是否存在
	 */
	public static Boolean isExit(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * listview与scrollview发生冲突时，重绘listview的高度，避免显示不完全
	 */

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public static boolean getStrFlag(String result, List<Weight> list) {
		boolean flag = false;
		if (list != null && result != null) {
			for (int i = 0; i < list.size(); i++) {
				if (result.equals(list.get(i).getXinpian())) {
					flag = true;
					break;
				} else {
					flag = false;
					// break;
				}
			}
		}

		return flag;
	}

	public static void setPricePoint(final EditText editText) {
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						editText.setText(s);
						editText.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					editText.setText(s);
					editText.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						editText.setText(s.subSequence(0, 1));
						editText.setSelection(1);
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}

		});

	}

	public static String getNowTime() {
		Date nowDate = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String now = df.format(nowDate);
		return now;
	}

	/**
	 * 扫瓶页面的长按删除操作
	 */
	public static void onLongClickDelete(final int position, Context context,
			final CommonAdapter<Weight> adapter, final List<Weight> mData,
			final List<Weight> mList, final List<Weight> list) {
		new AlertDialog.Builder(context).setMessage("确认删除？")
				.setPositiveButton("确认", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 删除
						if (mData != null) {

							mData.remove(position);
						}
						if (mList != null) {

							mList.remove(position);
						}

						if (list != null) {
							list.remove(position);
						}

						adapter.notifyDataSetChanged();
					}
				}).setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).create().show();
	}
}
