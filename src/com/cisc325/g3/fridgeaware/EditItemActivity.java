package com.cisc325.g3.fridgeaware;

import java.util.Date;

import com.cisc325.g3.fridgeaware.sql.FoodItemDataSource;
import com.cisc325.g3.fridgeaware.models.FoodItem;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

public class EditItemActivity extends FragmentActivity {
	
	private FoodItem foodItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Populate existing fields...
		FoodItemDataSource datasource = new FoodItemDataSource(EditItemActivity.this);
  		datasource.open();
  		
		foodItem = datasource.getFoodItem(getIntent().getLongExtra("ID",-1));
		
		((EditText) findViewById(R.id.edit_name)).setText((CharSequence)foodItem.getName());
		final DatePicker expiryPicker = ((DatePicker) findViewById(R.id.edit_picker_expiry));
		expiryPicker.updateDate(foodItem.getDate().getYear() + 1900,
				foodItem.getDate().getMonth(),
				foodItem.getDate().getDate());
		
		TextView mDateTextView = (TextView) findViewById(R.id.textView1234);
		
		mDateTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	DatePickerDialogFragment t = new DatePickerDialogFragment ();
            	t.show(getSupportFragmentManager(), "datePicker");
            }
        });  
		
		//Number Picker
		NumberPicker drawer = (NumberPicker) findViewById(R.id.edit_picker_categorie);
		drawer.setMinValue(1);
		drawer.setMaxValue(6);
		drawer.setValue(foodItem.getDrawer());

		EditText categoryEditText = (EditText) findViewById(R.id.edit_category);
 	   	categoryEditText.setText(foodItem.getCategory());
	   
	   	datasource.close();
	   
	   //Notification Settings Spinner...
		Spinner spinner = (Spinner) findViewById(R.id.edit_spinner_notifications);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.notifications_spinner_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		//Done Button...
		Button doneButton = (Button) findViewById(R.id.edit_button_done);
		
		doneButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				EditText nameEditText = (EditText) findViewById(R.id.edit_name);
				String name = nameEditText.getText().toString();
				
		        EditText categoryEditText = (EditText) findViewById(R.id.edit_category);
				String category = categoryEditText.getText().toString();
				
				int drawer = ((NumberPicker) findViewById(R.id.edit_picker_categorie)).getValue();
				
				Date expiryDate = new Date(expiryPicker.getYear()-1900,
						expiryPicker.getMonth(),
						expiryPicker.getDayOfMonth());
				expiryDate.setHours(12);
				
				FoodItemDataSource datasource = new FoodItemDataSource(EditItemActivity.this);
		        datasource.open();
		        
		        datasource.updateFoodItem(foodItem.getId(), name, expiryDate, 0, category, drawer);
		        
		        datasource.close();
				
				finish();
				
			}
		});
		
		//Cancel Button...
		Button cancelButton = (Button) findViewById(R.id.edit_button_cancel);
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
				
			}
		});
		
		//Remoce Button...
		Button removeButton = (Button) findViewById(R.id.edit_button_remove);
		
		removeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				FoodItemDataSource datasource = new FoodItemDataSource(EditItemActivity.this);
				datasource.open();
		        
		        datasource.deleteFoodItem(foodItem);
		        
		        datasource.close();
				
				finish();
				
			}
		});
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
