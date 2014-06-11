package com.cisc325.g3.fridgeaware.models;

import java.text.SimpleDateFormat;
import java.util.*;

public class FoodItem {

	private long id;
	private String name;
	private Date expiry;
	private int notificationSetting;
	private String category;
	private int drawer;
	
	
	private static SimpleDateFormat date_format = new SimpleDateFormat("d MMMM yyyy");
	
	public static final SimpleDateFormat database_format = new SimpleDateFormat("yyyy/MM/dd");
	
	public FoodItem() {
	}
	
	public FoodItem(String name) {
		this.name = name;
		this.expiry = new Date();
	}
	
	public FoodItem(String name, Date expiry) {
		this.name = name;
		this.expiry = expiry;
	}
	
	public FoodItem(String name, Date expiry, String category, int drawer) {
		this.name = name;
		this.expiry = expiry;
		this.category = category;
		this.drawer = drawer;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getDate() {
		return expiry;
	}
	
	public String getDateString() {
		return date_format.format(expiry);
	}
	
	public void setDate(Date expiry) {
		this.expiry = expiry;
	}
	
	public String toString() {
		return new String(name);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getNotificationSetting() {
		return notificationSetting;
	}

	public void setNotificationSetting(int notificationSetting) {
		this.notificationSetting = notificationSetting;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public int getDrawer() {
		return drawer;
	}

	public void setDrawer(int drawer) {
		this.drawer = drawer;
	}
}
