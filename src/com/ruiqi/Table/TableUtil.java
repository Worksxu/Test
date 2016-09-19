package com.ruiqi.Table;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.widget.TableRow.LayoutParams;


/**
 * 表格的操作类(基类)
 * @author Administrator
 *
 */
public abstract class TableUtil <T>{
	
	public abstract String getStr (T contents,int j);
	public  List<TabRow> addData(List<T> tables,String[] columnsHead) {  
        List<TabRow> table = new ArrayList<TabRow>();     
        TableCell[] titles = new TableCell[columnsHead.length];// 每行4个单元   
        try{  
  
            // 定义标题      
            for (int i = 0; i < titles.length; i++) {     
                titles[i] = new TableCell(columnsHead[i],      
                                        LayoutParams.WRAP_CONTENT,      
                                        TableCell.STRING);     
            }  
            table.add(new TabRow(titles));   
            for (int i = 0; i < tables.size(); i++) {  
                TableCell[] cols = new TableCell[columnsHead.length];// 每行几个单元   
                T contents = tables.get(i);  
                for(int j = 0;j < columnsHead.length;j++) { 
                	String str = getStr(contents, j);
                    cols[j] = new TableCell(str, LayoutParams.WRAP_CONTENT, TableCell.STRING);  
                }  
                table.add(new TabRow(cols));  
            }  
        }catch(Exception e) {  
            Log.d("添加表格异常", "Error:", e);  
        }  
        return table;  
    }  
	
	/**
	 * 没有标题的表格
	 */
	
	public  List<TabRow> addDataNoTitle(List<T> tables) {  
        List<TabRow> table = new ArrayList<TabRow>();     
        try{  
  
            for (int i = 0; i < tables.size(); i++) {  
                TableCell[] cols = new TableCell[2];// 每行几个单元   
                T contents = tables.get(i);  
                for(int j = 0;j < 2;j++) { 
                	String str = getStr(contents, j);
                    cols[j] = new TableCell(str, LayoutParams.WRAP_CONTENT, TableCell.STRING);  
                }  
                table.add(new TabRow(cols));  
            }  
        }catch(Exception e) {  
            Log.d("添加表格异常", "Error:", e);  
        }  
        return table;  
    }  
	
	public  List<TabRow> addDataNoTitle(List<T> tables,int size) {  
        List<TabRow> table = new ArrayList<TabRow>();     
        try{  
  
            for (int i = 0; i < tables.size(); i++) {  
                TableCell[] cols = new TableCell[size];// 每行几个单元   
                T contents = tables.get(i);  
                for(int j = 0;j < size;j++) { 
                	String str = getStr(contents, j);
                    cols[j] = new TableCell(str, LayoutParams.WRAP_CONTENT, TableCell.STRING);  
                }  
                table.add(new TabRow(cols));  
            }  
        }catch(Exception e) {  
            Log.d("添加表格异常", "Error:", e);  
        }  
        return table;  
    }
}

























