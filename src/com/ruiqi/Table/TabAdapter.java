package com.ruiqi.Table;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 表格填充的适配器
 * @author Administrator
 *
 */
public class TabAdapter extends BaseAdapter{
	private Context context;     
    private List<TabRow> table; //每一行的数据集合    
        
  
    public TabAdapter(Context context, List<TabRow> table) {
		super();
		this.context = context;
		this.table = table;
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
        return new TableRowView(this.context, tableRow,arg0,100);   
    }  
  
    @Override  
    public Object getItem(int arg0) {  
        return arg0;  
    }  
}
