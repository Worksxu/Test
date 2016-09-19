package com.ruiqi.Table;
/**
 * 表格中的行
 * @author Administrator
 *
 */
public class TabRow {
	 private TableCell[] cell;     
	        
	    public TabRow(TableCell[] cell) {
		this.cell = cell;
	}
		public int getSize() {     
	        return cell.length;     
	    }     
	    public TableCell getCellValue(int index) {     
	        if (index >= cell.length)     
	            return null;     
	        return cell[index];     
	    }     
}
