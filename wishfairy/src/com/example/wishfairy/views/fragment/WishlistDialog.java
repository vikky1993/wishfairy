package com.example.wishfairy.views.fragment;

import com.example.wishfairy.R;
import com.example.wishfairy.database.DatabaseAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class WishlistDialog extends DialogFragment{
	
	DatabaseAdapter dbAdapter;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		dbAdapter = new DatabaseAdapter(getActivity());
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyTheme);
	    // Get the layout inflater
	    final LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    final View v = inflater.inflate(R.layout.wishlist_dialog, null);
	    builder.setTitle("Add your wish");
	    builder.setIcon(R.drawable.plus);
	    builder.setView(v)
	    // Add action buttons
	           .setPositiveButton("Add", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	           	    TextView title_v = (TextView) v.findViewById(R.id.title);	
	           	    TextView desc_v = (TextView) v.findViewById(R.id.desc);
	           	    String title = title_v.getText().toString();
	           	    String desc = desc_v.getText().toString();
	           	    int catid = getArguments().getInt("catid");
	           	    int subcatid = getArguments().getInt("subcatid");
	           	    if(title.compareTo("")!=0 && desc.compareTo("")!=0)
	           	    {
		           	    dbAdapter.openDataBase();
		           	    dbAdapter.insertWish(title, desc, catid, subcatid);
		           	    dbAdapter.close();
	           	    }
	           	    else
	           	    {
	           	    	Toast.makeText(getActivity(), "Please enter the values", Toast.LENGTH_SHORT).show();
	           	    }
	               }
	           })
	           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   WishlistDialog.this.getDialog().cancel();
	               }
	           });      
	    return builder.create();	
	    }
}
