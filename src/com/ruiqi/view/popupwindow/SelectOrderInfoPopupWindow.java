package com.ruiqi.view.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ruiqi.works.R;
/**
 * popupwindow 装listview的类
 * @author Administrator
 *
 */
public class SelectOrderInfoPopupWindow extends PopupWindow{
	      private View mMenuView;  
	      private TextView tv_quxiao,tv_sure;
	      
	      
	      
	    
	      @Override
		public void dismiss() {
			super.dismiss();
			popDismiss.popDismissCalBack();
		}



		public SelectOrderInfoPopupWindow(Activity context,OnItemClickListener itemsOnClick,ListView lv,BaseAdapter adapter 
				,OnClickListener oncli) {  
	          super(context);  
	         LayoutInflater inflater = (LayoutInflater) context  
	                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	          mMenuView = inflater.inflate(R.layout.poputwindow_list, null);
	          lv = (ListView) mMenuView.findViewById(R.id.lv_select_address);
	          tv_quxiao = (TextView) mMenuView.findViewById(R.id.tv_quxiao);
	          tv_sure = (TextView) mMenuView.findViewById(R.id.tv_sure);
	          
	          tv_quxiao.setOnClickListener(oncli);
	          tv_sure.setOnClickListener(oncli);
	          lv.setAdapter(adapter);
	          lv.setOnItemClickListener(itemsOnClick);
	         //设置按钮监听  
	         //设置SelectPicPopupWindow的View  
	         this.setContentView(mMenuView);  
	         //设置SelectPicPopupWindow弹出窗体的宽  
	          this.setWidth(LayoutParams.MATCH_PARENT);  
	          //设置SelectPicPopupWindow弹出窗体的高  
	          this.setHeight(LayoutParams.WRAP_CONTENT);  
	          //设置SelectPicPopupWindow弹出窗体可点击  
	          this.setFocusable(true);  
	          //设置SelectPicPopupWindow弹出窗体动画效果  
	          this.setAnimationStyle(R.style.AnimBottom);  
	         //实例化一个ColorDrawable颜色为半透明  
	          ColorDrawable dw = new ColorDrawable(0xb0000000);  
	          //设置SelectPicPopupWindow弹出窗体的背景  
	          this.setBackgroundDrawable(dw);  
	        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框  
	          mMenuView.setOnTouchListener(new OnTouchListener() {  
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int height = mMenuView.findViewById(R.id.ll_menu).getTop();  
					int y=(int) event.getY();  
					if(event.getAction()==MotionEvent.ACTION_UP){  
						if(y<height){  
							dismiss();  
						}  
					}                 
					return true;  
				}  
	          });  
	   
	     }  
		public SelectOrderInfoPopupWindow(Activity context,OnItemClickListener itemsOnClick,BaseAdapter adapter 
				,OnClickListener oncli) {  
			super(context);  
			LayoutInflater inflater = (LayoutInflater) context  
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
			mMenuView = inflater.inflate(R.layout.poputwindow_list, null);
			tv_quxiao = (TextView) mMenuView.findViewById(R.id.tv_quxiao);
			tv_sure = (TextView) mMenuView.findViewById(R.id.tv_sure);
			
			tv_quxiao.setOnClickListener(oncli);
			tv_sure.setOnClickListener(oncli);
			//设置按钮监听  
			//设置SelectPicPopupWindow的View  
			this.setContentView(mMenuView);  
			//设置SelectPicPopupWindow弹出窗体的宽  
			this.setWidth(LayoutParams.MATCH_PARENT);  
			//设置SelectPicPopupWindow弹出窗体的高  
			this.setHeight(LayoutParams.WRAP_CONTENT);  
			//设置SelectPicPopupWindow弹出窗体可点击  
			this.setFocusable(true);  
			//设置SelectPicPopupWindow弹出窗体动画效果  
			this.setAnimationStyle(R.style.AnimBottom);  
			//实例化一个ColorDrawable颜色为半透明  
			ColorDrawable dw = new ColorDrawable(0xb0000000);  
			//设置SelectPicPopupWindow弹出窗体的背景  
			this.setBackgroundDrawable(dw);  
			//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框  
			mMenuView.setOnTouchListener(new OnTouchListener() {  
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int height = mMenuView.findViewById(R.id.ll_menu).getTop();  
					int y=(int) event.getY();  
					if(event.getAction()==MotionEvent.ACTION_UP){  
						if(y<height){  
							dismiss();  
						}  
					}                 
					return true;  
				}  
			});  
			
		}  
		private PopDismiss popDismiss;
		public interface PopDismiss{
			public void popDismissCalBack();
			
		}
		public void setPopDismiss(PopDismiss popDismiss){
			this.popDismiss=popDismiss;
			
		}
	    

}
