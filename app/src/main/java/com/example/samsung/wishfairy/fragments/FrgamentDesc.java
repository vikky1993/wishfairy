package com.example.samsung.wishfairy.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samsung.wishfairy.R;
import com.example.samsung.wishfairy.database.DBHelper;
import com.example.samsung.wishfairy.handlewishes.AddWish;
import com.example.samsung.wishfairy.login.LoginProcess;

/**
 * Created by SAMSUNG on 10-09-2015.
 */
@SuppressLint("ValidFragment")
public class FrgamentDesc extends Fragment implements View.OnClickListener {
    TextView title,matter;
    Button addToWishList;
    int thumbNailId;
    TextView shareButton;
    String head;
    int resourceId;
    int position=0;
    String category;
    private AlertDialog.Builder dialog;
    @SuppressLint("ValidFragment")
    public FrgamentDesc(String category) {
        this.category=category;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentdesc_layout,container,false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            addToWishList=(Button)getActivity().findViewById(R.id.addWish);
            addToWishList.setOnClickListener(this);
            addToWishList.setOnClickListener(this);
            title= (TextView) getActivity().findViewById(R.id.head);
            matter= (TextView) getActivity().findViewById(R.id.matter);
            shareButton= (TextView) getActivity().findViewById(R.id.share);
            title.setText(head);
            setMatter();
            Typeface font = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "fonts/Roboto-Regular.ttf");
            matter.setTypeface(font,4);
            matter.clearComposingText();
            title.setTypeface(font,1);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle("Alert !");
                    //LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View share_layout_title = getActivity().getLayoutInflater().inflate(R.layout.share_dialoge_title, null);
                    dialog.setCustomTitle(share_layout_title);
                    View share_layout = getActivity().getLayoutInflater().inflate(R.layout.share_dialoge_layout, null);
                    dialog.setView(share_layout);
                    dialog.setPositiveButton("Share",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                            whatsappIntent.setType("text/plain");
                            whatsappIntent.setPackage("com.whatsapp");
                            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Yadhu has shared "+head+" with you from wishfairy.");
                            try {
                                startActivity(whatsappIntent);
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(getActivity(),"Application not found",Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });dialog.show();

                }
            });
    }
    public void setMatter(){
        switch (category){
            case "Super Bikes":
                String []array1=getResources().getStringArray(R.array.super_bikes);
                matter.setText("          "+array1[position]);

                break;
            case "Super Cars":
                String []array=getResources().getStringArray(R.array.super_cars);
                matter.setText("          "+array[position]);
                break;
            case "Dream Lands":
                String []array2=getResources().getStringArray(R.array.dream_lands);
                matter.setText("          "+array2[position]);
                break;
        }
    }
    public void changeContent(String head){
        title.setText(head);
    }
    public void selectedItem(int thumbNailId,int resourceId,String head,int position){
        this.thumbNailId=thumbNailId;
        this.resourceId=resourceId;
        this.head=head;
        this.position=position;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.addWish:
                AddWish add=new AddWish();
                add.setResourceId(resourceId);
                add.setHead(head);
                add.setThumbId(thumbNailId);
                add.setuID(LoginProcess.userDetails.getInt(LoginProcess.userDetails.getColumnIndex("id")));
                add.setMatterPosition(position);
                add.setCategory(category);
                DBHelper dbHelper=new DBHelper(getActivity());
                int id=dbHelper.getItemId(head);
                add.setItemID(id);
                if(!(dbHelper.checkItemIdInWishList(id,LoginProcess.userDetails.getInt(LoginProcess.userDetails.getColumnIndex("id"))))) {
                    dbHelper.addWish(add);
                    Toast.makeText(getActivity(), "Item added to your Wishlist", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Item already in your  Wishlist", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
