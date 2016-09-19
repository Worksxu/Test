package com.ruiqi.Table;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 表格填充的适配器
 * @author Administrator
 *
 */
public class TabAdapterNoTitle extends BaseAdapter{
	private Context context;     
    private List<TabRow> table; //每一行的数据集合    
    private TextView tv;
    private TextView tv_1;
        
  
    public TabAdapterNoTitle(Context context, List<TabRow> table) {
		super();
		this.context = context;
		this.table = table;
	}
    
    

	public TabAdapterNoTitle(Context context, List<TabRow> table, TextView tv,TextView tv_1) {
		super();
		this.context = context;
		this.table = table;
		this.tv = tv;
		this.tv_1 = tv_1;
	}
	



	@Override  
    public int getCount() {  
        return table.size();  
    }  
  
    @Override  
    public long getItemId(int arg0) {  
        return arg0;  
    }  
  
    @Override  
    public View getView(int arg0, View arg1, ViewGroup arg2) {  
        TabRow tableRow = table.get(arg0); 
        if(tv_1==null){
        	return new TableRowViewNoTitle(this.context, tableRow,arg0,100,tv,null);   
        }else if(tv==null){
        	return new TableRowViewNoTitle(this.context, tableRow,arg0,100,null,tv_1);   
        }
        else{
        	return new TableRowViewNoTitle(this.context, tableRow,arg0,100,tv,tv_1);   
        }
    }  
  
    @Override  
    public Object getItem(int arg0) {  
        return arg0;  
    }  
}
