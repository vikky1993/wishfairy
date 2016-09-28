package com.example.wishfairy.views.helper;

import java.util.ArrayList;

import com.example.wishfairy.R;
import com.example.wishfairy.database.DatabaseAdapter;
import com.example.wishfairy.views.component.ExpandListChild;
import com.example.wishfairy.views.component.ExpandListGroup;
import com.example.wishfairy.views.fragment.WishFragment;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandListAdapter extends BaseExpandableListAdapter {

	private DatabaseAdapter dbAdapter;
	private Context context;
	private ArrayList<ExpandListGroup> groups;
	public ExpandListAdapter(Context context, ArrayList<ExpandListGroup> groups) {
		
		this.context = context;
		this.groups = groups;
        dbAdapter = new DatabaseAdapter(context);
        Log.i("main activity", "creating database");
	}
	
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		ArrayList<ExpandListChild> chList = groups.get(groupPosition).getItems();
		return chList.get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		ExpandListChild child = (ExpandListChild) getChild(groupPosition, childPosition);
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.expandlist_child_item, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.tvChild);
		tv.setText(child.getName().toString());
		tv.setTag(child.getTag());
		
		// TODO Auto-generated method stub
		return view;
	}

	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		ArrayList<ExpandListChild> chList = groups.get(groupPosition).getItems();

		return chList.size();

	}

	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groups.get(groupPosition);
	}

	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groups.size();
	}

	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		ExpandListGroup group = (ExpandListGroup) getGroup(groupPosition);
		if (view == null) {
			LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.expandlist_group_item, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.tvGroup);
		tv.setText(group.getName());
		TextView cat = (TextView) view.findViewById(R.id.subcat);
		dbAdapter.openDataBase();
		Cursor wish = dbAdapter.fetchWish(groupPosition);
		if(wish.getCount()>0)
		{
			int catid = wish.getInt(3);

	    	int catFilterId;
	    	catFilterId=WishFragment.catinfilter();
	    	if(catFilterId == -1)
	    		catid = wish.getInt(3);
	    	else
	    		catid = catFilterId;
	    	
			Cursor c = dbAdapter.fetchAllCategories();
			c.moveToFirst();
			int i=0;
			while(i<catid)
			{
				c.moveToNext();
				i++;
			}
			Log.i("catid", String.valueOf(catid));
			String cat_name = c.getString(1);
			cat.setText(cat_name);
		}
		wish.close();
		dbAdapter.close();

		// TODO Auto-generated method stub
		return view;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}
}
