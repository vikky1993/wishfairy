package com.example.wishfairy.views.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.wishfairy.database.DatabaseAdapter;
import com.example.wishfairy.views.helper.*;
import com.example.wishfairy.views.component.*;
import com.example.wishfairy.R;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.Spinner;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class WishFragment extends Fragment implements OnItemSelectedListener{
	private ExpandListAdapter ExpAdapter;
	private ArrayList<ExpandListGroup> ExpListItems;
	private ExpandableListView ExpandList;
	private DatabaseAdapter dbAdapter;
	private Spinner spinner;
    private List<String> categoryTitles;
    View rootView;

	public WishFragment(){
	}

    public static int cat_filter=1;
    public static boolean filter_bool=false;
    public static int catinfilter(){
    	return cat_filter;
    }
    public static boolean filterstate(){
    	return filter_bool;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	
     	dbAdapter = new DatabaseAdapter(getActivity().getApplicationContext());
     	try {
	 		dbAdapter.openDataBase();
	 	}catch(SQLException sqle){
	 		throw sqle;
	 	}
        rootView = inflater.inflate(R.layout.fragment_wish, container, false);
        spinner = (Spinner) rootView.findViewById(R.id.spinner);
		Cursor c = dbAdapter.fetchAllCategories();
		c.moveToFirst();
		categoryTitles = new ArrayList<String>();
		categoryTitles.add("All categories");
		for(int j=0;j<c.getCount();j++)	
		{
			categoryTitles.add(c.getString(1));
			c.moveToNext();
		}
		c.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryTitles);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		
        ExpandList = (ExpandableListView) rootView.findViewById(R.id.expandableListView1);
//        ImageView addwish = (ImageView) rootView.findViewById(R.id.addwish);
        ExpListItems = SetStandardGroups();
        if(ExpListItems.size()==0)
        {
//    		addwish.setImageResource(R.drawable.genie);
        }

        ExpAdapter = new ExpandListAdapter(rootView.getContext(), ExpListItems);
        ExpandList.setAdapter(ExpAdapter);
        return rootView;
    }
    
    public ArrayList<ExpandListGroup> SetStandardGroups() {
    	
    	ArrayList<ExpandListGroup> list = new ArrayList<ExpandListGroup>();
    	Cursor wishes = dbAdapter.fetchAllWishes();
    	if(wishes.getCount()>0)
    	{
	    	do{
	    		Log.i("totalwishcount", String.valueOf(wishes.getCount()));
	            ExpandListGroup group = new ExpandListGroup();
	        	ArrayList<ExpandListChild> list2 = new ArrayList<ExpandListChild>();
	            group.setName(wishes.getString(1));

	            ExpandListChild subcat = new ExpandListChild();
				int catid = wishes.getInt(3);
				int subcatid = wishes.getInt(4);
				subcat.setName(dbAdapter.fetchSubCategory(subcatid, catid).getString(1));
	        	list2.add(subcat);

	            ExpandListChild ch = new ExpandListChild();
	            ch.setName(wishes.getString(2));
	            list2.add(ch);
	            group.setItems(list2);
	            list.add(group);
	    	}
	    	while(wishes.moveToNext());
    	}
        return list;
    }
    
    public ArrayList<ExpandListGroup> SetStandardGroups(int position) {
    	
    	ArrayList<ExpandListGroup> list = new ArrayList<ExpandListGroup>();
    	Cursor wishes = dbAdapter.fetchWishesByCategoryID(position);
    	if(wishes.getCount()>0)
    	{
	    	do{
	    		Log.i("wishcount", String.valueOf(wishes.getCount()));
	            ExpandListGroup group = new ExpandListGroup();
	        	ArrayList<ExpandListChild> list2 = new ArrayList<ExpandListChild>();
	            group.setName(wishes.getString(1));

	            ExpandListChild subcat = new ExpandListChild();
				int catid = wishes.getInt(3);
				int subcatid = wishes.getInt(4);
				subcat.setName(dbAdapter.fetchSubCategory(subcatid, catid).getString(1));
	        	list2.add(subcat);

	            ExpandListChild ch = new ExpandListChild();
	            ch.setName(wishes.getString(2));
	            list2.add(ch);
	            
	            group.setItems(list2);
	            list.add(group);
	    	}
	    	while(wishes.moveToNext());
    	}

        return list;
    }

    @Override
    public void onDestroyView() {
    	// TODO Auto-generated method stub
    	dbAdapter.close();
    	super.onDestroyView();
    }

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long arg3) {
		// TODO Auto-generated method stub
				
		cat_filter=position-1;
		filter_bool=true;
		
		if(position==0)
		{
	        ExpListItems = SetStandardGroups();
		}
		else
		{
			 ExpListItems = SetStandardGroups(position-2);
		}
       ExpandList = (ExpandableListView) rootView.findViewById(R.id.expandableListView1);
//	        ImageView addwish = (ImageView) rootView.findViewById(R.id.addwish);
        if(ExpListItems.size()==0)
        {
//	    		addwish.setImageResource(R.drawable.genie);
        }
        ExpAdapter = new ExpandListAdapter(rootView.getContext(), ExpListItems);
        ExpandList.setAdapter(ExpAdapter);        
	}

	public void refresh()
	{
          ExpandList = (ExpandableListView) rootView.findViewById(R.id.expandableListView1);
	      ExpListItems = SetStandardGroups();
	      if(ExpListItems.size()==0)
	      {
	//  		addwish.setImageResource(R.drawable.genie);
	      }
	      ExpAdapter = new ExpandListAdapter(rootView.getContext(), ExpListItems);
	      ExpandList.setAdapter(ExpAdapter);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(ExpandList);
	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		//Toast.makeText(getActivity(), String.valueOf(ExpandList.getId()), Toast.LENGTH_SHORT).show();
		//Toast.makeText(getActivity(), "id of the item selected is "+String.valueOf(v.getId()), Toast.LENGTH_SHORT).show();
		menu.setHeaderTitle("Update");
		menu.add(0, v.getId(),0,"Delete");
		menu.add(0, v.getId(),0,"Edit");
		menu.add(0, v.getId(),0,"Share");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();
		//Toast.makeText(getActivity(), String.valueOf(ExpandableListView.getPackedPositionGroup(info.packedPosition)), Toast.LENGTH_SHORT).show();
		Cursor wish = dbAdapter.fetchWish(ExpandableListView.getPackedPositionGroup(info.packedPosition));

		if((item.getTitle()).equals("Delete"))
		{
			dbAdapter.deleteWish(ExpandableListView.getPackedPositionGroup(info.packedPosition));
			dbAdapter.updateWishIds();
		}

		else if((item.getTitle().equals("Edit")))
		{
			Bundle args = new Bundle();
			args.putCharSequence("title", wish.getString(1));
			args.putCharSequence("description", wish.getString(2));
			args.putInt("wishId", wish.getInt(0));
			wish.close();
			DialogFragment newFragment = new UpdateWishlistDialog();
			newFragment.setArguments(args);
			newFragment.show(getFragmentManager(), "Update wishlist");
		}
		else if((item.getTitle().equals("Share")))
		{
			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("text/plain");
			String s ="I added a wish on WishFairy\n"+ wish.getString(1) + " - " + wish.getString(2);
			wish.close();
			s = s+"\nhttp://www.atimi.com";
			Toast.makeText(getActivity(), s , Toast.LENGTH_SHORT).show();
			share.putExtra(Intent.EXTRA_TEXT, s);
			startActivity(Intent.createChooser(share, "Share your Wish"));
		}
		refresh();
		return super.onContextItemSelected(item);
	}
}
