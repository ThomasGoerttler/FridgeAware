package com.cisc325.g3.fridgeaware.sql;

import java.text.ParseException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.cisc325.g3.fridgeaware.FoodItemListActivity;
import com.cisc325.g3.fridgeaware.models.FoodItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;

public class FoodItemDataSource {
	private SQLiteDatabase database;
	private FridgeDBHelper dbHelper;
	private String[] allColumns = { 
			FridgeDBHelper.FOODITEM_COLUMN_ID,
			FridgeDBHelper.FOODITEM_COLUMN_NAME,
			FridgeDBHelper.FOODITEM_COLUMN_EXPIRY,
			FridgeDBHelper.FOODITEM_COLUMN_NOTIFICATION_SETTING,
			FridgeDBHelper.FOODITEM_COLUMN_CATEGORY,
			FridgeDBHelper.FOODITEM_COLUMN_DRAWER
	};
	
	public FoodItemDataSource(Context context) {
		dbHelper = new FridgeDBHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	
	

	public List<FoodItem> getAllFoodItems(int typeOfSort, int drawer) {
		
		List<FoodItem> items = new ArrayList<FoodItem>();

		Cursor cursor = null;
		String drawerString = null;
		if (drawer != 0)
			drawerString = FridgeDBHelper.FOODITEM_COLUMN_DRAWER + " = " + drawer;
			
		switch (typeOfSort) {
		case FoodItemListActivity.SORT_BY_ALPHABET:
			cursor = database.query(FridgeDBHelper.FOODITEM_TABLE_NAME,
		    		allColumns, drawerString, null, null, null, FridgeDBHelper.FOODITEM_COLUMN_NAME);
			break;
		case FoodItemListActivity.SORT_BY_EXPIRED_DATE:
			cursor = database.query(FridgeDBHelper.FOODITEM_TABLE_NAME,
		    		allColumns, drawerString, null, null, null, FridgeDBHelper.FOODITEM_COLUMN_EXPIRY);
			break;
		case FoodItemListActivity.SORT_BY_CATEGORY:
			cursor = database.query(FridgeDBHelper.FOODITEM_TABLE_NAME,
		    		allColumns, drawerString, null, null, null, FridgeDBHelper.FOODITEM_COLUMN_CATEGORY);
			break;
		case FoodItemListActivity.SORT_BY_DRAWER:
			cursor = database.query(FridgeDBHelper.FOODITEM_TABLE_NAME,
		    		allColumns, drawerString, null, null, null, FridgeDBHelper.FOODITEM_COLUMN_DRAWER);
			break;
			
		}
		
		 
		
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	FoodItem item = cursorToFoodItem(cursor);
	    	items.add(item);
	    	cursor.moveToNext();
	    }
	    
	    // Make sure to close the cursor
	    cursor.close();
	    
	    return items;
	}
	
	
	public FoodItem getFoodItem(long id) {
		
		FoodItem item = null;

	    Cursor cursor = database.query(FridgeDBHelper.FOODITEM_TABLE_NAME,
	    		allColumns, FridgeDBHelper.FOODITEM_COLUMN_ID + " = " + id, null, null, null, null);
	    
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	item = cursorToFoodItem(cursor);
	    	cursor.moveToNext();
	    }
	    
	    // Make sure to close the cursor
	    cursor.close();
		
	    return item;
		
	}
	
	public FoodItem createFoodItem(String name, Date expiryDate, int notificationSetting, String category, int drawer) {
	    ContentValues values = new ContentValues();
	    
	    values.put(FridgeDBHelper.FOODITEM_COLUMN_NAME, name);
	    
	    String date_string = FoodItem.database_format.format(expiryDate);
	    values.put(FridgeDBHelper.FOODITEM_COLUMN_EXPIRY, date_string);
	    
	    values.put(FridgeDBHelper.FOODITEM_COLUMN_NOTIFICATION_SETTING, notificationSetting);

	    values.put(FridgeDBHelper.FOODITEM_COLUMN_CATEGORY, category);

	    values.put(FridgeDBHelper.FOODITEM_COLUMN_DRAWER, drawer);
	    
	    long insertId = database.insertOrThrow(FridgeDBHelper.FOODITEM_TABLE_NAME, null,
	        values);
	    
	    // Get the item we just inserted so we can return it
	    Cursor cursor = database.query(FridgeDBHelper.FOODITEM_TABLE_NAME,
	        allColumns, FridgeDBHelper.FOODITEM_COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    
	    cursor.moveToFirst();
	    FoodItem newItem = cursorToFoodItem(cursor);
	    
	    cursor.close();
	    
	    return newItem;
	}
	
	public void updateFoodItem(long id, String name, Date expiryDate, int notificationSetting, String category, int drawer) {
		
		ContentValues values = new ContentValues();
	    
	    values.put(FridgeDBHelper.FOODITEM_COLUMN_NAME, name);
	    String date_string = FoodItem.database_format.format(expiryDate);
	    values.put(FridgeDBHelper.FOODITEM_COLUMN_EXPIRY, date_string);
	    values.put(FridgeDBHelper.FOODITEM_COLUMN_NOTIFICATION_SETTING, notificationSetting);
	    values.put(FridgeDBHelper.FOODITEM_COLUMN_CATEGORY, category);
	    values.put(FridgeDBHelper.FOODITEM_COLUMN_DRAWER, drawer);
	    
	    database.update(FridgeDBHelper.FOODITEM_TABLE_NAME, values, FridgeDBHelper.FOODITEM_COLUMN_ID + " = " + id, null);
		
	}

	public void deleteFoodItem(FoodItem item) {
		long id = item.getId();
		System.out.println("Food item deleted with id: " + id);
	    database.delete(FridgeDBHelper.FOODITEM_TABLE_NAME, FridgeDBHelper.FOODITEM_COLUMN_ID
	        + " = " + id, null);
	}
	
	private FoodItem cursorToFoodItem(Cursor cursor) {
		FoodItem item = new FoodItem();
		item.setId(cursor.getLong(0));
		item.setName(cursor.getString(1));
		
		
		try {
			String date_string = cursor.getString(2);
			Date date = FoodItem.database_format.parse(date_string);
			item.setDate(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		
		item.setNotificationSetting(cursor.getInt(3));
		item.setCategory(cursor.getString(4));
		item.setDrawer(cursor.getInt(5));
		
	    return item;
	}

	public List<FoodItem> searchFoodItems(String query) {

		List<FoodItem> items = new ArrayList<FoodItem>();
		
	    Cursor cursor = database.query(FridgeDBHelper.FOODITEM_TABLE_NAME,
	    		allColumns, "name like '%" + query + "%'", null, null, null, FridgeDBHelper.FOODITEM_COLUMN_NAME);
	    
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	FoodItem item = cursorToFoodItem(cursor);
	    	items.add(item);
	    	cursor.moveToNext();
	    }
	    
	    // Make sure to close the cursor
	    cursor.close();
	    
	    return items;
	}
}
