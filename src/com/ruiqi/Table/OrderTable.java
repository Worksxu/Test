package com.ruiqi.Table;

import com.ruiqi.bean.TableInfo;

/**
 * 订单的表格类
 * @author Administrator
 *
 */
public class OrderTable extends TableUtil<TableInfo>{

	@Override
	public String getStr(TableInfo contents, int j) {
		String result = null;
		switch (j) {
		case 0:
			result = contents.getOrderNum();//第一列
			break;
		case 1:
			result = contents.getOrderMoney();//第二列
			break;
		case 2:
			result = contents.getOrderStatus();//第三列
			break;
		case 3:
			result = contents.getOrderTime();//第四列
			break;
		case 4:
			result = contents.getBeizhu();//第五列
			break;

		default:
			break;
		}
		return result;
	}

}
