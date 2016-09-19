package com.ruiqi.Table;
/**
 * 表格中的单元格
 * @author Administrator
 *
 */
public class TableCell {
	public static final int STRING = 0;     
	public static final int IMAGE = 1;  
	
	    public Object value;     
	    public int width;     
	    public int height;     
	    public int type;     
	    public TableCell(Object value,  int height, int type) {     
	        this.value = value;     
	           
	        this.height = height;     
	        this.type = type;     
	    }   
	    
	    
	    
}
