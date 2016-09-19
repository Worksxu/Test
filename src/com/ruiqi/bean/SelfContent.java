package com.ruiqi.bean;
/**
 * 安全报告内容类
 * @author Administrator
 *
 */
public class SelfContent {
	private String content;
	private int icon;
	private String id; //绑定有问题的内容
	public SelfContent(String content, int icon, String id) {
		super();
		this.content = content;
		this.icon = icon;
		this.id = id;
	}
	public SelfContent() {
		super();
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "SelfContent [content=" + content + ", icon=" + icon + ", id="
				+ id + "]";
	}
	
	
}
