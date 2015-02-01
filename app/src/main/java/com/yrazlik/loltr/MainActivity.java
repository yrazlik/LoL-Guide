package com.yrazlik.loltr;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.fragments.AboutFragment;
import com.yrazlik.loltr.fragments.AllChampionsFragment;
import com.yrazlik.loltr.fragments.AllItemsFragment;
import com.yrazlik.loltr.fragments.ContactFragment;
import com.yrazlik.loltr.fragments.CountryFragment;
import com.yrazlik.loltr.fragments.LiveChannelsFragment;
import com.yrazlik.loltr.fragments.RunesFragment;
import com.yrazlik.loltr.fragments.WeeklyFreeChampionsFragment;
import com.yrazlik.loltr.listener.ResponseListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends ActionBarActivity implements ResponseListener {

	int mPosition = -1;
	String mTitle = "";

	// Array of strings storing country names
	String[] leftMenuItems;

	// Array of integers points to images stored in /res/drawable-ldpi/
	int[] mFlags = new int[] { R.drawable.coin, R.drawable.champion,
			R.drawable.item, R.drawable.rune, R.drawable.tv2, R.drawable.contact,
			R.drawable.info};

	// Array of strings to initial counts
	String[] mCount = new String[] { "", "", "", "", "", "", "", "", "", "" };

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private LinearLayout mDrawer;
	private List<HashMap<String, String>> mList;
	private SimpleAdapter mAdapter;
	final private String COUNTRY = "country";
	final private String FLAG = "flag";
	final private String COUNT = "count";
	private Commons commons;
    private AdView adView;

    @SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);// Getting an array of country

        String s = "yasinrazlik";

        for(int i = 0; i<s.length(); i++){
            s = s.substring(1,s.length()-i) + s.substring(0,1) + s.substring(s.length()-i);
        }

        Log.d("INPLACE", s);

		adView = (AdView)findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		commons = Commons.getInstance(getApplicationContext());

		leftMenuItems = getResources().getStringArray(R.array.titles);

		// Title of the activity
		mTitle = (String) getTitle();

		// Getting a reference to the drawer listview
		mDrawerList = (ListView) findViewById(R.id.drawer_list);

		// Getting a reference to the sidebar drawer ( Title + ListView )
		mDrawer = (LinearLayout) findViewById(R.id.drawer);

		// Each row in the list stores country name, count and flag
		mList = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < leftMenuItems.length; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put(COUNTRY, leftMenuItems[i]);
			hm.put(COUNT, mCount[i]);
			hm.put(FLAG, Integer.toString(mFlags[i]));
			mList.add(hm);
		}

		// Keys used in Hashmap
		String[] from = { FLAG, COUNTRY, COUNT };

		// Ids of views in listview_layout
		int[] to = { R.id.flag, R.id.country, R.id.count };

		// Instantiating an adapter to store each items
		// R.layout.drawer_layout defines the layout of each item
		mAdapter = new SimpleAdapter(this, mList, R.layout.drawer_layout, from,
				to);

		// Getting reference to DrawerLayout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Creating a ToggleButton for NavigationDrawer with drawer event
		// listener
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when drawer is closed */
			public void onDrawerClosed(View view) {
				highlightSelectedCountry();
				supportInvalidateOptionsMenu();
			}

			/** Called when a drawer is opened */
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(getResources().getString(R.string.select_category));
				supportInvalidateOptionsMenu();
			}
		};

		// Setting event listener for the drawer
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// ItemClick event handler for the drawer items
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				FragmentManager fragmentManager = getSupportFragmentManager();
				final FragmentTransaction ft = fragmentManager.beginTransaction();
				mDrawerLayout.closeDrawer(mDrawer);
				
				if(position == 0){
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							WeeklyFreeChampionsFragment cFragment = new WeeklyFreeChampionsFragment();
							ft.replace(R.id.content_frame, cFragment);
							ft.commit();
							
						}
					}, 250);
					
				}else if(position == 1){
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							AllChampionsFragment fragment = new AllChampionsFragment();
							ft.replace(R.id.content_frame, fragment);
							ft.commit();
						}
					}, 250);
				}else if(position == 2){
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							AllItemsFragment fragment = new AllItemsFragment();
							ft.replace(R.id.content_frame, fragment);
							ft.commit();
						}
					}, 250);
				}else if(position == 3){
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							RunesFragment fragment = new RunesFragment();
							ft.replace(R.id.content_frame, fragment);
							ft.commit();
						}
					}, 250);
				}else if(position == 4){
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            LiveChannelsFragment fragment = new LiveChannelsFragment();
                            ft.replace(R.id.content_frame, fragment);
                            ft.commit();
                        }
                    }, 250);
                }else if(position == 5){
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
                            ContactFragment fragment = new ContactFragment();
                            ft.replace(R.id.content_frame, fragment);
                            ft.commit();
						}
					}, 250);
				}else if(position == 6){
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            Log.d("TAGGG", "MainActivityOnClick");
                            AboutFragment fragment = new AboutFragment();
                            ft.replace(R.id.content_frame, fragment);
                            ft.commit();
                        }
                    }, 250);
                }
				// Closing the drawer
				
			}
		});

		// Enabling Up navigation
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportActionBar().setDisplayShowHomeEnabled(true);

		// Setting the adapter to the listView
		mDrawerList.setAdapter(mAdapter);
        mDrawerList.performItemClick( mDrawerList.getAdapter().getView(0, null, null),
                0,
                mDrawerList.getAdapter().getItemId(0));
        getSupportActionBar().setTitle(leftMenuItems[0]);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	public void incrementHitCount(int position) {
		HashMap<String, String> item = mList.get(position);
		String count = item.get(COUNT);
		item.remove(COUNT);
		if (count.equals("")) {
			count = "  1  ";
		} else {
			int cnt = Integer.parseInt(count.trim());
			cnt++;
			count = "  " + cnt + "  ";
		}
		item.put(COUNT, count);
		mAdapter.notifyDataSetChanged();
	}

	public void showFragment(int position) {

		// Currently selected country
		mTitle = leftMenuItems[position];

		// Creating a fragment object
		CountryFragment cFragment = new CountryFragment();

		// Creating a Bundle object
		Bundle data = new Bundle();

		// Setting the index of the currently selected item of mDrawerList
		data.putInt("position", position);

		// Setting the position to the fragment
		cFragment.setArguments(data);

		// Getting reference to the FragmentManager
		FragmentManager fragmentManager = getSupportFragmentManager();

		// Creating a fragment transaction
		FragmentTransaction ft = fragmentManager.beginTransaction();

		// Adding a fragment to the fragment transaction
		ft.replace(R.id.content_frame, cFragment);

		// Committing the transaction
		ft.commit();
	}

	// Highlight the selected country : 0 to 4
	public void highlightSelectedCountry() {
		int selectedItem = mDrawerList.getCheckedItemPosition();

            mPosition = selectedItem;


		if (mPosition != -1)
			getSupportActionBar().setTitle(leftMenuItems[mPosition]);
	}

	@Override
	public void onSuccess(Object response) {
		System.out.println("");

	}

	@Override
	public void onFailure(Object response) {
		// TODO Auto-generated method stub

	}

	@Override
	public Context getContext() {
		return this;
	}

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(R.string.areyousure)).setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        })
                .setNegativeButton("Hayir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }



}
