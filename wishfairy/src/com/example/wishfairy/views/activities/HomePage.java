
package com.example.wishfairy.views.activities;

import java.util.ArrayList;

import com.example.wishfairy.R;
import com.example.wishfairy.views.fragment.*;
import com.example.wishfairy.database.DatabaseAdapter;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

public class HomePage extends Activity {
	private DatabaseAdapter dbAdapter;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<String> categoryTitles;
	private ShareActionProvider mShareActionProvider;

    public static final String MY_PREFS = "loggedUser";
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        dbAdapter = new DatabaseAdapter(this);
        try {
        	dbAdapter.openDataBase();
		 }catch(SQLException sqle){
		 		throw sqle;
		 } 
		 
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        
		Cursor c = dbAdapter.fetchAllCategories();

		c.moveToFirst();
		categoryTitles = new ArrayList<String>();
		for(int j=0;j<c.getCount();j++)	
		{
			categoryTitles.add(c.getString(1));
			c.moveToNext();
		}
		
        //categoryTitles = getResources().getStringArray(R.array.Category_array);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, categoryTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

        if (savedInstanceState == null) {
            setFragment();
        }
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		dbAdapter.close();
		super.onDestroy();
	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
    	if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch(item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        case R.id.action_addwish1:
        	openWishDialog1(getCurrentFocus());
        	setFragment();
           return true;
        default:
            return super.onOptionsItemSelected(item);
        }
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	getMenuInflater().inflate(R.menu.home_page, menu);
		menu.findItem(R.id.logout).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				logout();
				// TODO Auto-generated method stub
				return false;
			}
		});
		menu.findItem(R.id.menu_item_refresh).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				setFragment();
				// TODO Auto-generated method stub
				return false;
			}
		});
		MenuItem item = menu.findItem(R.id.menu_item_share);
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		setShareIntent(createIntent());

    	return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean val = true;
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        boolean filterset = WishFragment.filterstate();
        val= !drawerOpen & filterset;
        menu.findItem(R.id.action_addwish1).setVisible(val);
        return super.onPrepareOptionsMenu(menu);
    }    

    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		Cursor c = dbAdapter.fetchCategory(position);
    		c.moveToFirst();
    		c.close();
    		Intent intent = new Intent(HomePage.this, Page.class);
    		intent.putExtra("actId", position);
    		startActivity(intent);
        }
    }

    private void setFragment() {
        // update selected item and title, then close the drawer
		Cursor wishes = dbAdapter.fetchAllWishes();
		if(wishes.getCount()>0){
			Fragment fragment = new WishFragment();
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.content_frame, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}else {
    	  Fragment fragment = new ImgFragment();
          FragmentManager fragmentManager = getFragmentManager();
          fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
		}
        
		wishes.close();
    }

	@Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void logout()
    {
    	SharedPreferences myPrefs = getSharedPreferences(MY_PREFS, 0);
    	Editor e= myPrefs.edit();
    	e.putString("username", "");
    	e.commit();
    	Intent i = new Intent(HomePage.this, LoginActivity.class);
    	startActivity(i);
    	finish();
    }
    
	private Intent createIntent()
	{
		Intent I = new Intent(Intent.ACTION_SEND);
		I.setType("text/plain");

		//Set Subject Here
		I.putExtra(Intent.EXTRA_SUBJECT,"WishFairy");
		//Set Value here
		I.putExtra(Intent.EXTRA_TEXT, "Make your wish come true!!");
		return I;
	}
	private void setShareIntent(Intent shareIntent) {
	    if (mShareActionProvider != null) {
	        mShareActionProvider.setShareIntent(shareIntent);
	    }
	}
	
	public void openWishDialog1(View v)
	{
    	int cat_id=1;
    	cat_id=WishFragment.catinfilter();
		Bundle args = new Bundle();
		args.putInt("subcatid", 0);
		args.putInt("catid", cat_id);
		if(cat_id>-1)
		{
			DialogFragment newFragment = new WishlistDialog();
			newFragment.setArguments(args);
			newFragment.show(getFragmentManager(), "Add wishlist");
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Select a category", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
    
}