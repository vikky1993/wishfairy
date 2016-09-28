package com.example.samsung.wishfairy.home;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.samsung.wishfairy.R;
import com.example.samsung.wishfairy.fragments.FragmentCars;
import com.example.samsung.wishfairy.fragments.FragmentList;
import com.example.samsung.wishfairy.fragments.FragmentOne;
import com.example.samsung.wishfairy.fragments.FragmentThree;
import com.example.samsung.wishfairy.login.LoginProcess;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Created by SAMSUNG on 29-07-2015.
 */
 public class HomeActivity extends AppCompatActivity {
        private ListView mDrawerList;
        private ArrayAdapter<String> mAdapter;
        private ActionBarDrawerToggle mDrawerToggle;
        private DrawerLayout mDrawerLayout;
        private String mActivityTitle;
        static boolean isDraerClosed = true;
        View swapOne, swapTwo;
        public static int drawerListSelectedPosition = 1;
        private static ImageView head;
        private Vector arrayObjects;
        public static View firstItem;
        private Vector arrayObjects1;
        List<ArrayAdapter> dataList;
        private AlertDialog.Builder dialog;
        static Fragment fragment = null;
        static int i = 0;
        public static int RESULT_CODE = 1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.home_activity_layout);
            arrayObjects = new Vector();
            arrayObjects1 = new Vector();

            fragment = new FragmentList();
            selectFragment(fragment);
            mDrawerList = (ListView) findViewById(R.id.navList);
            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    switch (position) {
                        case 1:
                            mDrawerLayout.closeDrawers();
                            firstItem.setBackgroundColor(Color.WHITE);
                            firstItem = view;
                            firstItem.setBackgroundColor(Color.parseColor("#f1f1f1"));
                            fragment = new FragmentList();
                            drawerListSelectedPosition = 1;
                            getSupportActionBar().setTitle("Wish List");
                            i = i + 1;
                            break;
                        case 2:
                            mDrawerLayout.closeDrawers();
                            firstItem.setBackgroundColor(Color.WHITE);
                            firstItem = view;
                            firstItem.setBackgroundColor(Color.parseColor("#f1f1f1"));
                            fragment = new FragmentOne(0, 0, "Super Bikes");
                            drawerListSelectedPosition = 2;
                            getSupportActionBar().setTitle("Super Bikes");
                            i = i + 1;
                            break;
                        case 3:
                            mDrawerLayout.closeDrawers();
                            firstItem.setBackgroundColor(Color.WHITE);
                            firstItem = view;
                            firstItem.setBackgroundColor(Color.parseColor("#f1f1f1"));
                            fragment = new FragmentCars(0, 0);
                            drawerListSelectedPosition = 3;
                            getSupportActionBar().setTitle("Super Cars");
                            i = i + 1;
                            break;
                        case 4:
                            mDrawerLayout.closeDrawers();
                            firstItem.setBackgroundColor(Color.WHITE);
                            firstItem = view;
                            firstItem.setBackgroundColor(Color.parseColor("#f1f1f1"));
                            fragment = new FragmentThree(0, 0, "Dream Lands");
                            drawerListSelectedPosition = 4;
                            getSupportActionBar().setTitle("Tourist Locations");
                            i = i + 1;
                            break;
                    }
                    selectFragment(fragment);
                    mDrawerList.setItemChecked(position, true);
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
            });
            addDrawerItems();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mActivityTitle = getTitle().toString();
            setupDrawer();
            getSupportActionBar().setTitle("Wish List");

        }

        public void selectFragment(Fragment fragment) {
            FragmentManager frgManager = getFragmentManager();
            frgManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null)
                    .commit();
        }

        public void getImageUri() throws IOException {
            final SharedPreferences settings = getSharedPreferences("PREF", Context.MODE_PRIVATE);
            String imageUriString = settings.getString(LoginProcess.username, "false");
            Uri imageUri = Uri.parse(imageUriString);
            if (imageUri != null) {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                head.setImageBitmap(bitmap);
            } else {
                head.setImageResource(R.drawable.cbr);
            }
        }

        private void addDrawerItems() {
            String[] osArray = {"Wish List", "Super Bikes", "Super Cars", "Tourist Locations", "Luxury Gadgets"};

            mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
            View header = (View) getLayoutInflater().inflate(R.layout.android_header_view, null);
            TextView profileName = (TextView) header.findViewById(R.id.username);
            TextView email = (TextView) header.findViewById(R.id.email_id);
            TextView phone_num = (TextView) header.findViewById(R.id.ph_no);
            profileName.setText(LoginProcess.username);
            String emai = LoginProcess.userDetails.getString(LoginProcess.userDetails.getColumnIndex("phone"));
            if (emai.length() < 14) {
                email.setTextSize(15);
            } else if (emai.length() > 18) {
                email.setTextSize(13);

            }
            email.setText(emai);
            // phone_num.setText(LoginProcess.userDetails.getString(LoginProcess.userDetails.getColumnIndex("email")));
            head = (ImageView) header.findViewById(R.id.ivIcon);
            try {
                getImageUri();
            } catch (IOException e) {
                e.printStackTrace();
            }
            head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryIntent = new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), 1);
                }
            });
            mDrawerList.addHeaderView(header);
            mDrawerList.setAdapter(mAdapter);

        }

        @Override
        public void onBackPressed() {
            //super.onBackPressed();
            if (FragmentList.checkedMode) {
                FragmentList.modeGlobal.finish();
            } else if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawers();
            } else if (!(drawerListSelectedPosition == 1)) {
                if (!(drawerListSelectedPosition == 0)) {
                    firstItem.setBackgroundColor(Color.WHITE);
                }
                firstItem = mDrawerList.getChildAt(1);
                firstItem.setBackgroundColor(Color.parseColor("#f1f1f1"));
                fragment = new FragmentList();
                i = i + 1;
                drawerListSelectedPosition = 1;
                selectFragment(fragment);
            } else {
                dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Alert !");
                //LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = this.getLayoutInflater().inflate(R.layout.exit_dialog_layout_title, null);
                dialog.setCustomTitle(v);
                View view = this.getLayoutInflater().inflate(R.layout.exit_dialog_layout, null);
                dialog.setView(view);
                final CheckBox checkBox = (CheckBox) view.findViewById(R.id.logge_me_in);
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final SharedPreferences preferences = getApplicationContext().getSharedPreferences("LogInDetails", Context.MODE_PRIVATE);
                        if (checkBox.isChecked()) {

                            if ((!preferences.contains("UserName"))) {
                                preferences.edit().putString("UserName", LoginProcess.username).commit();
                                preferences.edit().putString("Password", LoginProcess.password).commit();
                                preferences.edit().putBoolean("Flag", true).commit();
                            } else {
                                preferences.edit().putString("UserName", LoginProcess.username).commit();
                                preferences.edit().putString("Password", LoginProcess.password).commit();
                                preferences.edit().putBoolean("Flag", true).commit();
                            }

                        } else {
                            if (preferences.contains("Flag")) {
                                preferences.edit().putBoolean("Flag", false).commit();
                                preferences.edit().putString("UserName", "").commit();
                                preferences.edit().putString("PassWord", "").commit();
                                Intent intent = new Intent("android.intent.action.LOGIN");
                                startActivity(intent);
                                finish();
                            }
                        }
                        dialog.dismiss();
                        finish();
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

                Uri uri = data.getData();
                SharedPreferences settings = getSharedPreferences("PREF", Context.MODE_PRIVATE);
                settings.edit().putString(LoginProcess.username, uri.toString()).commit();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    // Log.d(TAG, String.valueOf(bitmap));
                    head.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void setupDrawer() {
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                    R.string.drawer_open, R.string.drawer_close) {

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    //getSupportActionBar().setTitle("Wish Fairy");
                    invalidateOptionsMenu();
                    isDraerClosed = false;
                    if (i < 1) {
                        firstItem = mDrawerList.getChildAt(1);
                        firstItem.setBackgroundColor(Color.parseColor("#f1f1f1"));
                    } else {
                        firstItem.setBackgroundColor(Color.parseColor("#f1f1f1"));
                    }
                }

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    //getSupportActionBar().setTitle(mActivityTitle);
                    isDraerClosed = true;
                    invalidateOptionsMenu();
                }
            };
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.setDrawerListener(mDrawerToggle);

        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            // Activate the navigation drawer toggle
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        protected void onPostCreate(Bundle savedInstanceState) {
            super.onPostCreate(savedInstanceState);
            mDrawerToggle.syncState();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            mDrawerToggle.onConfigurationChanged(newConfig);
        }

    }