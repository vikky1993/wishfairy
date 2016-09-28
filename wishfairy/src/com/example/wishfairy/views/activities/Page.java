package com.example.wishfairy.views.activities;

import com.example.wishfairy.R;
import com.example.wishfairy.database.DatabaseAdapter;
import com.example.wishfairy.views.fragment.WishlistDialog;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.res.Resources;

public class Page extends Activity {

int prevSelect=0;
int imgres;
int catid=0;

private DatabaseAdapter dbAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//create a database
		setContentView(R.layout.activity_page );

		final LinearLayout ll = (LinearLayout)findViewById(R.id.linear1);
		Bundle extras = getIntent().getExtras();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Create a database adapter object
		dbAdapter = new DatabaseAdapter(this);

		//open the dbhelper in onCreate and close it in onDestroy
	 	try {
	 		dbAdapter.openDataBase();
	 	}catch(SQLException sqle){
	 		throw sqle;
	 	}
		final int actId = extras.getInt("actId");
		this.catid = actId;

		
		/**
		 * adding the horizontal scroll view elements for activity actId
		 */
		final Resources res = getResources();

		final Cursor c = dbAdapter.fetchAllSubCategories(actId);
		c.moveToFirst();
		int count = c.getCount();		//number of subcategories in the main category
		c.close();
		
		/**
		 * set the default page view
		 */
		Cursor c_default = dbAdapter.fetchSubCategory(0, actId);
		prevSelect = 0;
		((TextView)findViewById(R.id.act_title)).setText(c_default.getString(1));
		((TextView)findViewById(R.id.act_desc)).setText(c_default.getString(2));
		imgres = res.getIdentifier(c_default.getString(4), "drawable", "com.example.wishfairy");
		((ImageView) findViewById(R.id.largeImage)).setImageResource(imgres);
		c_default.close();
		
		/**
		 * set the image views for the horizontal scroll view
		 */
		for(int i=0;i<count;i++)
		 {		final int x=i;
		        final ImageView ii= new ImageView(this);
		        ii.setPadding(5, 5, 5, 5);
				final Cursor c1 = dbAdapter.fetchSubCategory(x, actId);
				c1.moveToFirst();
				final int imgId = res.getIdentifier(c1.getString(4), "drawable", "com.example.wishfairy");
				ii.setScaleType(ScaleType.FIT_XY);
		        ii.setImageResource(imgId);
				ii.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						final ImageView largeView = (ImageView) findViewById(R.id.largeImage);
						imgres = imgId;
						ll.getChildAt(prevSelect).setBackgroundColor(Color.parseColor("#00000000"));
						prevSelect = x;
						ii.setBackgroundColor(Color.parseColor("#0099FF"));
						TextView title = (TextView)findViewById(R.id.act_title);
						TextView desc = (TextView)findViewById(R.id.act_desc);
				        largeView.setImageResource(imgId);
				        Animation myFadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
				        largeView.startAnimation(myFadeInAnimation);
						title.setText(c1.getString(1));
						desc.setText(c1.getString(2));
					}
					
				});
		        ll.addView(ii,300,200);
		 }
		ll.getChildAt(prevSelect).setBackgroundColor(Color.parseColor("#00000000"));
	}

	@Override
		protected void onDestroy() {
//			dbAdapter.close();
			dbAdapter.close();
			super.onDestroy();
		}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.page, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        case R.id.action_addwish:
        	openWishDialog(getCurrentFocus());
           return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		imgres = savedInstanceState.getInt("img");
		final ImageView largeView = (ImageView) findViewById(R.id.largeImage);
		largeView.setImageResource(imgres);
		prevSelect = savedInstanceState.getInt("prevselect");
		((LinearLayout)findViewById(R.id.linear1)).getChildAt(prevSelect).setBackgroundColor(Color.parseColor("#00000000"));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("img",imgres);
		outState.putInt("prevselect", prevSelect);
	}
	
	public void openWishDialog(View v)
	{
		Bundle args = new Bundle();
		args.putInt("subcatid", this.prevSelect);
		args.putInt("catid", this.catid);
		DialogFragment newFragment = new WishlistDialog();
		newFragment.setArguments(args);
		newFragment.show(getFragmentManager(), "Add wishlist");
	}
}
