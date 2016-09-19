package com.ruiqi.utils;

/**
 * 网络请求的接口
 * 
 * @author Administrator
 *
 */
public interface RequestUrl {
	// String IP = "192.168.0.29";//本地服务器
	// String IP = "192.168.0.20";//郑拓测试服务器
	String IP = "cztest.ruiqi100.com";// 线上测试服务器
	String test = "测试app";
	// String test="";
	// String IP = "182.92.97.39";//线上服务器
	String LOGIN_PATH = "http://" + IP + "/appworks/login";// 登录
	String LOGIN_BOTTLELIST = "http://" + IP + "/appworks/bottlelist";// 送气工钢瓶库存信息
	String PASS_RESET = "http://" + IP + "/appworks/passreset";// 找回密码
	String GET_CODE = "http://" + IP + "/appworks/sendcode";// 发送验证码
	String PWD = "http://" + IP + "/appworks/logout";// 退出账号

	String SELF_REPORT = "http://" + IP + "/apptype/report";// 安全报告

	String USER_INFO = "http://" + IP + "/appworks/userinfo";// 获取老用户信息

	String BOTTLE_TYPE = "http://" + IP + "/apptype/bottletype"; // 押金

	String TAOCAN_TYPE = "http://" + IP + "/apptype/commditylist"; // 套餐类型

	String ORDET_LIST = "http://" + IP + "/appworks/orderlist";// 全部订单

	String ORDER_INFO = "http://" + IP + "/appworks/orderinfo";// 订单详情

	String DESPOIT = "http://" + IP + "/appworks/deposit";// 退押金详情

	String CONFIRMORDER = "http://" + IP + "/appworks/confirmorder";// 确认收款

	String SHIPPERPRODUCT = "http://" + IP + "/appworks/shipperproduct";// 配件入库记录

	String CONFIRM_DEPOSIT = "http://" + IP + "/appworks/confirmdeposit";// 退押金提交

	String STOCK_LIST = "http://" + IP + "/appworks/kucunlist";// 钢瓶库存

	String PJ_STOCK = "http://" + IP + "/appworks/kcproduct";// 配件库存

	String IN_SHIPER = "http://" + IP + "/appworks/inshipper";// 领瓶

	String PEIJIAN_LIST = "http://" + IP + "/appworks/shipperproduct";//

	String ORDER_IN = "http://" + IP + "/appworks/orderpayment";// 订单收入

	String CREATE_ORDER = "http://" + IP + "/appworks/createorder";// 创建订单

	String OUT = "http://" + IP + "/appworks/shipperpayment";

	String PAYLIST = "http://" + IP + "/appworks/nopaylist";// 欠款用户

	String REPAYMENTLIST = "http://" + IP + "/appworks/repaymentlist";// 还款记录

	String NOORDERLIST = "http://" + IP + "/appworks/noorderlist";// 欠款记录

	String ADDREPAYMENT = "http://" + IP + "/appworks/addrepayment";// 收缴欠款

	String IS_BOTTLE = "http://" + IP + "/appworks/isbottle";// 扫描判定钢瓶

	String PJ = "http://" + IP + "/apptype/producttype";// 配件列表

	String FORPAY = "http://" + IP + "/appworks/shipperforpay";// 上交钱

	String APPLY_PJ = "http://" + IP + "/appworks/inproduct";// 配件申请

	String EDITOR_ORDER = "http://" + IP + "/appworks/editorder";// 修改订单

	String SHIPPER_ARREARS = "http://" + IP + "/appworks/shipperarrears";// 收缴欠款记录

	String OUT_PING = "http://" + IP + "/appworks/cklist";// 钢瓶出库记录

	String IN_PING = "http://" + IP + "/appworks/rklist";// 钢瓶入库记录

	String DEPOSIT_ORDER = "http://" + IP + "/appworks/depositorder";// 退押金支出记录

	String BOOTLE_LIST = "http://" + IP + "/apptype/bottlelist";// 折旧瓶列表

	String UP_LOAD_DATA = "http://" + IP + "/appworks/safeimage";// 上传图片

	String PEIJIANLIEBIAO = "";// 配件列表

	String ADDRESSLIST = "http://" + IP + "/apptype/useraddress";// 地址库
	String YOUHUI = "http://" + IP + "/appworks/promotions";// 优惠折扣

	String SEARCHUSERSAFE = "http://" + IP + "/appworks/reportuserinfo";// 客户安全报告信息
	
	String REPORTLIST = "http://" + IP + "/apptype/report";// 获取安全报告信息列表
	
	String SAFEREPORT = "http://" + IP + "/appworks/safereport";// 提交安全报告信息
	
	String BACKSHOP = "http://" + IP + "/appworks/backshop";// 送气工送回门店
	
	String BACKORDER = "http://" + IP + "/appworks/backorder";// 送气工创建退瓶订单
	
	String GETRECEIPT = "http://" + IP + "/appworks/getreceipt";// 根据合同号获取当前押金欠款额度
	
	String REPORTUSERINFO = "http://" + IP + "/appworks/reportuserinfo";// 根据用户手机号获取最近一次安检时间
	
	String CREATEKEHU = "http://" + IP + "/appworks/createkehu";// 创建新客户
	
	String USERQKORDER = "http://" + IP + "/appworks/userqkorder";//客户合同列表
	String LOCATION = "http://" + IP + "/appworks/shipperposition";//送气工坐标
	String GENGXIN = "http://" + IP + "//appworks/appversion";//更新
//	getreceipt

}
