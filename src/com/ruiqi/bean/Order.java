package com.ruiqi.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ruiqi.chai.ArrearsList;

/**
 * 订单信息
 * @author Administrator
 *
 */
public class Order implements Serializable{
	private String ordersn ;
	private String time ;
	private String delivery ; //完成时间
	private String total; //
	private String pay_money ;//实付款项
	private String status ;
	private String kid ;
	private String mobile ;
	private String username ;
	private String address ;
	private String shop_name ;
	private String worksname ;
	private String worksmobile;
	//是否当场需要收款字段，2表示不收款，其他则是当场收款
	private String is_settlement ;
	
	private List<Orderdeail> od;
	private String deposit;
	private String depreciation;
	private String raffinat;
	private String ispayment;
	private String order_tc_type;
	
	private ArrayList<ArrearsList> arrayListArrears;
private String yh_money;//优惠价格
	private String peijianprice;//配件价格

	private String comment;//备注
	
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Order(String ordersn, String time, String delivery, String total,
			String pay_money, String status, String kid, String mobile,
			String username, String address, String shop_name,
			String worksname, String worksmobile, String is_settlement,
			List<Orderdeail> od, String deposit, String depreciation,
			String raffinat, String ispayment,String order_tc_type,String comment,String peijianprice) {
		super();
		this.ordersn = ordersn;
		this.time = time;
		this.delivery = delivery;
		this.total = total;
		this.pay_money = pay_money;
		this.status = status;
		this.kid = kid;
		this.mobile = mobile;
		this.username = username;
		this.address = address;
		this.shop_name = shop_name;
		this.worksname = worksname;
		this.worksmobile = worksmobile;
		this.is_settlement = is_settlement;
		this.od = od;
		this.deposit = deposit;
		this.depreciation = depreciation;
		this.raffinat = raffinat;
		this.ispayment = ispayment;
		this.order_tc_type=order_tc_type;
		this.comment=comment;
		this.peijianprice=peijianprice;
	}
	public Order(String ordersn, String time, String delivery, String total,
			String pay_money, String status, String kid, String mobile,
			String username, String address, String shop_name,
			String worksname, String worksmobile, String is_settlement,
			List<Orderdeail> od, String deposit, String depreciation,
			String raffinat, String ispayment,String order_tc_type,String comment,String peijianprice,String yh_money) {
		super();
		this.ordersn = ordersn;
		this.time = time;
		this.delivery = delivery;
		this.total = total;
		this.pay_money = pay_money;
		this.status = status;
		this.kid = kid;
		this.mobile = mobile;
		this.username = username;
		this.address = address;
		this.shop_name = shop_name;
		this.worksname = worksname;
		this.worksmobile = worksmobile;
		this.is_settlement = is_settlement;
		this.od = od;
		this.deposit = deposit;
		this.depreciation = depreciation;
		this.raffinat = raffinat;
		this.ispayment = ispayment;
		this.order_tc_type=order_tc_type;
		this.comment=comment;
		this.peijianprice=peijianprice;
		this.yh_money=yh_money;
	}
	public Order(String ordersn, String time, String delivery, String total,
			String pay_money, String status, String kid, String mobile,
			String username, String address, String shop_name,
			String worksname, String worksmobile, String is_settlement,
			List<Orderdeail> od, String deposit, String depreciation,
			String raffinat, String ispayment,String order_tc_type,String comment,String peijianprice,String yh_money,ArrayList<ArrearsList> arrayListArrears) {
		super();
		this.ordersn = ordersn;
		this.time = time;
		this.delivery = delivery;
		this.total = total;
		this.pay_money = pay_money;
		this.status = status;
		this.kid = kid;
		this.mobile = mobile;
		this.username = username;
		this.address = address;
		this.shop_name = shop_name;
		this.worksname = worksname;
		this.worksmobile = worksmobile;
		this.is_settlement = is_settlement;
		this.od = od;
		this.deposit = deposit;
		this.depreciation = depreciation;
		this.raffinat = raffinat;
		this.ispayment = ispayment;
		this.order_tc_type=order_tc_type;
		this.comment=comment;
		this.peijianprice=peijianprice;
		this.yh_money=yh_money;
		this.arrayListArrears=arrayListArrears;
	}
	
	
	public ArrayList<ArrearsList> getArrayListArrears() {
		return arrayListArrears;
	}

	public void setArrayListArrears(ArrayList<ArrearsList> arrayListArrears) {
		this.arrayListArrears = arrayListArrears;
	}

	public String getYh_money() {
		return yh_money;
	}

	public void setYh_money(String yh_money) {
		this.yh_money = yh_money;
	}

	public String getPeijianprice() {
		return peijianprice;
	}

	public void setPeijianprice(String peijianprice) {
		this.peijianprice = peijianprice;
	}

	public Order(String ordersn, String time, String delivery, String total,
			String pay_money, String status, String kid, String mobile,
			String username, String address, String shop_name,
			String worksname, String worksmobile, String is_settlement,
			List<Orderdeail> od, String deposit, String depreciation,
			String raffinat, String ispayment,String order_tc_type,String comment) {
		super();
		this.ordersn = ordersn;
		this.time = time;
		this.delivery = delivery;
		this.total = total;
		this.pay_money = pay_money;
		this.status = status;
		this.kid = kid;
		this.mobile = mobile;
		this.username = username;
		this.address = address;
		this.shop_name = shop_name;
		this.worksname = worksname;
		this.worksmobile = worksmobile;
		this.is_settlement = is_settlement;
		this.od = od;
		this.deposit = deposit;
		this.depreciation = depreciation;
		this.raffinat = raffinat;
		this.ispayment = ispayment;
		this.order_tc_type=order_tc_type;
		this.comment=comment;
	}

	public String getOrder_tc_type() {
		return order_tc_type;
	}

	public void setOrder_tc_type(String order_tc_type) {
		this.order_tc_type = order_tc_type;
	}
	public Order(String ordersn, String time, String delivery, String total,
			String pay_money, String status, String kid, String mobile,
			String username, String address, String shop_name,
			String worksname, String worksmobile, String is_settlement,
			List<Orderdeail> od, String deposit, String depreciation,
			String raffinat, String ispayment,String order_tc_type) {
		super();
		this.ordersn = ordersn;
		this.time = time;
		this.delivery = delivery;
		this.total = total;
		this.pay_money = pay_money;
		this.status = status;
		this.kid = kid;
		this.mobile = mobile;
		this.username = username;
		this.address = address;
		this.shop_name = shop_name;
		this.worksname = worksname;
		this.worksmobile = worksmobile;
		this.is_settlement = is_settlement;
		this.od = od;
		this.deposit = deposit;
		this.depreciation = depreciation;
		this.raffinat = raffinat;
		this.ispayment = ispayment;
		this.order_tc_type=order_tc_type;
	}

	public String getIspayment() {
		return ispayment;
	}

	public void setIspayment(String ispayment) {
		this.ispayment = ispayment;
	}

	public Order(String ordersn, String time, String delivery, String total,
			String pay_money, String status, String kid, String mobile,
			String username, String address, String shop_name,
			String worksname, String worksmobile, String is_settlement,
			List<Orderdeail> od, String deposit, String depreciation,
			String raffinat, String ispayment) {
		super();
		this.ordersn = ordersn;
		this.time = time;
		this.delivery = delivery;
		this.total = total;
		this.pay_money = pay_money;
		this.status = status;
		this.kid = kid;
		this.mobile = mobile;
		this.username = username;
		this.address = address;
		this.shop_name = shop_name;
		this.worksname = worksname;
		this.worksmobile = worksmobile;
		this.is_settlement = is_settlement;
		this.od = od;
		this.deposit = deposit;
		this.depreciation = depreciation;
		this.raffinat = raffinat;
		this.ispayment = ispayment;
	}

	public String getDepreciation() {
		return depreciation;
	}

	public void setDepreciation(String depreciation) {
		this.depreciation = depreciation;
	}

	public String getRaffinat() {
		return raffinat;
	}

	public void setRaffinat(String raffinat) {
		this.raffinat = raffinat;
	}

	public Order(String ordersn, String time, String delivery, String total,
			String pay_money, String status, String kid, String mobile,
			String username, String address, String shop_name,
			String worksname, String worksmobile, String is_settlement,
			List<Orderdeail> od, String deposit, String depreciation,
			String raffinat) {
		super();
		this.ordersn = ordersn;
		this.time = time;
		this.delivery = delivery;
		this.total = total;
		this.pay_money = pay_money;
		this.status = status;
		this.kid = kid;
		this.mobile = mobile;
		this.username = username;
		this.address = address;
		this.shop_name = shop_name;
		this.worksname = worksname;
		this.worksmobile = worksmobile;
		this.is_settlement = is_settlement;
		this.od = od;
		this.deposit = deposit;
		this.depreciation = depreciation;
		this.raffinat = raffinat;
	}

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}

	public Order(String ordersn, String time, String delivery, String total,
			String pay_money, String status, String kid, String mobile,
			String username, String address, String shop_name,
			String worksname, String worksmobile, String is_settlement,
			List<Orderdeail> od, String deposit) {
		super();
		this.ordersn = ordersn;
		this.time = time;
		this.delivery = delivery;
		this.total = total;
		this.pay_money = pay_money;
		this.status = status;
		this.kid = kid;
		this.mobile = mobile;
		this.username = username;
		this.address = address;
		this.shop_name = shop_name;
		this.worksname = worksname;
		this.worksmobile = worksmobile;
		this.is_settlement = is_settlement;
		this.od = od;
		this.deposit = deposit;
	}

	@Override
	public String toString() {
		return "Order [ordersn=" + ordersn + ", time=" + time + ", delivery="
				+ delivery + ", total=" + total + ", pay_money=" + pay_money
				+ ", status=" + status + ", kid=" + kid + ", mobile=" + mobile
				+ ", username=" + username + ", address=" + address
				+ ", shop_name=" + shop_name + ", worksname=" + worksname
				+ ", worksmobile=" + worksmobile + ", is_settlement="
				+ is_settlement + ", od=" + od + ", deposit=" + deposit
				+ ", depreciation=" + depreciation + ", raffinat=" + raffinat
				+ ", ispayment=" + ispayment + "]";
	}

	public String getOrdersn() {
		return ordersn;
	}

	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getPay_money() {
		return pay_money;
	}

	public void setPay_money(String pay_money) {
		this.pay_money = pay_money;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getWorksname() {
		return worksname;
	}

	public void setWorksname(String worksname) {
		this.worksname = worksname;
	}

	public String getWorksmobile() {
		return worksmobile;
	}

	public void setWorksmobile(String worksmobile) {
		this.worksmobile = worksmobile;
	}

	public String getIs_settlement() {
		return is_settlement;
	}

	public void setIs_settlement(String is_settlement) {
		this.is_settlement = is_settlement;
	}

	public  List<Orderdeail> getOd() {
		return od;
	}

	public void setOd( List<Orderdeail> od) {
		this.od = od;
	}

	public Order() {
		super();
	}

	public Order(String ordersn, String time, String delivery, String total,
			String pay_money, String status, String kid, String mobile,
			String username, String address, String shop_name,
			String worksname, String worksmobile, String is_settlement,
			 List<Orderdeail> od) {
		super();
		this.ordersn = ordersn;
		this.time = time;
		this.delivery = delivery;
		this.total = total;
		this.pay_money = pay_money;
		this.status = status;
		this.kid = kid;
		this.mobile = mobile;
		this.username = username;
		this.address = address;
		this.shop_name = shop_name;
		this.worksname = worksname;
		this.worksmobile = worksmobile;
		this.is_settlement = is_settlement;
		this.od = od;
	}
}
