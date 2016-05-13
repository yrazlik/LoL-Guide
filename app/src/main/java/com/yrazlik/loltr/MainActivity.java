package com.yrazlik.loltr;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.yrazlik.loltr.billing.PaymentSevice;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.fragments.AboutFragment;
import com.yrazlik.loltr.fragments.AllChampionSkinsFragment;
import com.yrazlik.loltr.fragments.AllChampionsFragment;
import com.yrazlik.loltr.fragments.ContactFragment;
import com.yrazlik.loltr.fragments.CountryFragment;
import com.yrazlik.loltr.fragments.DiscountsFragment;
import com.yrazlik.loltr.fragments.LiveChannelsFragment;
import com.yrazlik.loltr.fragments.MatchInfoFragment;
import com.yrazlik.loltr.fragments.NewItemsFragment;
import com.yrazlik.loltr.fragments.NewsFragment;
import com.yrazlik.loltr.fragments.RemoveAdsFragment;
import com.yrazlik.loltr.fragments.RunesFragment;
import com.yrazlik.loltr.fragments.SettingsFragment;
import com.yrazlik.loltr.fragments.SummonerSearchFragment;
import com.yrazlik.loltr.fragments.WeeklyFreeChampionsFragment;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.AllChampionsResponse;
import com.yrazlik.loltr.responseclasses.SummonerSpellsResponse;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity implements ResponseListener {

    public interface IsAppPurchasedListener {
        void onAppPurchaseResultReceived();
    }

    int allchampionsRequestCount = 0, allSpellsRequestCount = 0;
    int mPosition = -1;
    String mTitle = "";

    // Array of strings storing country names
    String[] leftMenuItems;

    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] mFlags = new int[]{R.drawable.profile, R.drawable.coin, R.drawable.discount, R.drawable.news, R.drawable.champion,
            R.drawable.item, R.drawable.rune, R.drawable.costume, R.drawable.swords2, R.drawable.tv2, R.drawable.settings, R.drawable.block, R.drawable.contact,
            R.drawable.info};

    // Array of strings to initial counts
    String[] mCount = new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

    public static Fragment activeFragment;
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
    private Toolbar mToolBar;

    private IsAppPurchasedListener appPurchasedListener = new IsAppPurchasedListener() {
        @Override
        public void onAppPurchaseResultReceived() {
            IInAppBillingService mService = PaymentSevice.getInstance(MainActivity.this).getService();
            if(mService != null) {
                try {
                    Bundle ownedItems = mService.getPurchases(3, getPackageName(), "inapp", null);
                    if(ownedItems != null) {
                        int response = ownedItems.getInt("RESPONSE_CODE");
                        if (response == 0) {
                            ArrayList<String> ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                            if(ownedSkus != null && ownedSkus.size() > 0) {
                                for (int i = 0; i < ownedSkus.size(); ++i) {
                                    String sku = ownedSkus.get(i);
                                    if(sku.equalsIgnoreCase(Commons.REMOVE_ADS_ID)) {
                                        Commons.getInstance(getApplicationContext()).ADS_ENABLED = false;
                                    }
                                }
                            }
                        }
                    }
                } catch (RemoteException e) {
                    Commons.getInstance(getApplicationContext()).ADS_ENABLED = false;
                }
            } else {
                Commons.getInstance(getApplicationContext()).ADS_ENABLED = false;
            }
            continueSetup();
        }
    };

    private void continueSetup() {
        if(Commons.getInstance(getApplicationContext()).ADS_ENABLED) {
            setContentView(R.layout.activity_main);
            mFlags = new int[]{R.drawable.profile, R.drawable.coin, R.drawable.discount, R.drawable.news, R.drawable.champion,
                    R.drawable.item, R.drawable.rune, R.drawable.costume, R.drawable.swords2, R.drawable.tv2, R.drawable.settings, R.drawable.block, R.drawable.contact,
                    R.drawable.info};
            mCount = new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
        } else {
            setContentView(R.layout.activity_main_noad);
            mFlags = new int[]{R.drawable.profile, R.drawable.coin, R.drawable.discount, R.drawable.news, R.drawable.champion,
                    R.drawable.item, R.drawable.rune, R.drawable.costume, R.drawable.swords2, R.drawable.tv2, R.drawable.settings, R.drawable.contact,
                    R.drawable.info};
            mCount = new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", ""};
        }


        makeGetAllChampionsRequest();
        makeGetAllSpellsRequest();

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolBar != null) {
            setSupportActionBar(mToolBar);
        }

        adView = (AdView) findViewById(R.id.adView);
        if(adView != null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
        commons = Commons.getInstance(getApplicationContext());

        setDrawer();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerList.performItemClick(mDrawerList.getAdapter().getView(1, null, null),
                        1,
                        mDrawerList.getAdapter().getItemId(1));
            }
        }, 250);

        String cat = leftMenuItems[1];
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>" + cat + "</font>"));
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isPurchased = Commons.getInstance(getApplicationContext()).loadPurchaseData();
        if(isPurchased) {
            Commons.getInstance(getApplicationContext()).ADS_ENABLED = true;
            setupInAppPurchases(null);
            continueSetup();
        } else {
            setupInAppPurchases(appPurchasedListener);
        }
    }

    private void setupInAppPurchases(IsAppPurchasedListener listener) {
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        ServiceConnection mServiceConn = PaymentSevice.getInstance(this, listener).getServiceConnection();
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    private void makeGetAllChampionsRequest(){
        ArrayList<String> pathParams = new ArrayList<String>();
        pathParams.add("static-data");
        pathParams.add(Commons.getInstance(getContext().getApplicationContext()).getRegion());
        pathParams.add("v1.2");
        pathParams.add("champion");
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("locale", Commons.getInstance(getContext().getApplicationContext()).getLocale());
        queryParams.put("version", Commons.LATEST_VERSION);
        queryParams.put("champData", "altimages");
        queryParams.put("api_key", Commons.API_KEY);
        ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.ALL_CHAMPIONS_REQUEST, pathParams, queryParams, null, this);

    }

    private void makeGetAllSpellsRequest(){
        ArrayList<String> pathParams2 = new ArrayList<>();
        pathParams2.add("static-data");
        pathParams2.add(Commons.getInstance(getContext().getApplicationContext()).getRegion());
        pathParams2.add("v1.2");
        pathParams2.add("summoner-spell");
        HashMap<String, String> queryParams2 = new HashMap<String, String>();
        queryParams2.put("spellData", "image");
        queryParams2.put("api_key", Commons.API_KEY);
        ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.SUMMONER_SPELLS_REQUEST, pathParams2, queryParams2, null, this);

    }

    private void showInterstitial(){
        if(((LolApplication)getApplication()).shouldShowInterstitial()){
            ((LolApplication)getApplication()).showInterstitial();
        }
    }


    private void setDrawer() {
        if(Commons.getInstance(getApplicationContext()).ADS_ENABLED) {
            leftMenuItems = getResources().getStringArray(R.array.titles);
        } else {
            leftMenuItems = getResources().getStringArray(R.array.titles_noad);
        }

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
        String[] from = {FLAG, COUNTRY, COUNT};

        // Ids of views in listview_layout
        int[] to = {R.id.flag, R.id.country, R.id.count};

        // Instantiating an adapter to store each items
        // R.layout.drawer_layout defines the layout of each item
        mAdapter = new SimpleAdapter(this, mList, R.layout.drawer_layout, from,
                to);

        // Getting reference to DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Creating a ToggleButton for NavigationDrawer with drawer event
        // listener
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.drawer_open, R.string.drawer_close) {

            /** Called when drawer is closed */
            public void onDrawerClosed(View view) {
                highlightSelectedCountry();
                supportInvalidateOptionsMenu();
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                String selectCategory = getResources().getString(R.string.select_category);
                getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>" + selectCategory + "</font>"));
                supportInvalidateOptionsMenu();
            }
        };

        // Setting event listener for the drawer
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if(mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }

        // ItemClick event handler for the drawer items
        if(Commons.getInstance(getApplicationContext()).ADS_ENABLED) {
            mDrawerList.setOnItemClickListener(leftMenuWithAdsClickListener);
        } else {
            mDrawerList.setOnItemClickListener(leftMenuNoAdsClickListener);
        }

        // Enabling Up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Setting the adapter to the listView
        mDrawerList.setAdapter(mAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }

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


        if (mPosition != -1) {
            String cat = leftMenuItems[mPosition];
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>" + cat + "</font>"));
        }
    }

    @Override
    public void onSuccess(Object response) {
        if (response instanceof AllChampionsResponse) {
            try {
                AllChampionsResponse resp = (AllChampionsResponse) response;
                Map<String, Map<String, String>> data = resp.getData();
                if (Commons.allChampions != null) {
                    Commons.allChampions.clear();
                } else {
                    Commons.allChampions = new ArrayList<Champion>();
                }
                for (Map.Entry<String, Map<String, String>> entry : data.entrySet()) {
                    String key = entry.getKey();
                    String imageUrl = Commons.CHAMPION_IMAGE_BASE_URL + key + ".png";
                    Champion c = new Champion();
                    c.setChampionImageUrl(imageUrl);
                    c.setChampionName(entry.getValue().get("name"));
                    c.setId(Integer.parseInt(entry.getValue().get("id")));
                    c.setKey(entry.getValue().get("key"));
                    c.setTitle("\"" + entry.getValue().get("title") + "\"");
                    Commons.allChampions.add(c);
                }
                if (Commons.allChampions != null) {
                    Collections.sort(Commons.allChampions, new Comparator<Champion>() {
                        @Override
                        public int compare(Champion c1, Champion c2) {
                            return c1.getChampionName().compareTo(c2.getChampionName());
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (response instanceof SummonerSpellsResponse){
            if(response != null) {
                SummonerSpellsResponse summonerSpellsResponse = (SummonerSpellsResponse) response;
                if (summonerSpellsResponse != null) {
                    Commons.allSpells = summonerSpellsResponse.getSpells();
                }
            }
        }
    }

        @Override
        public void onFailure (Object response){
            if(response instanceof Integer){
                Integer requestID = (Integer) response;
                if(requestID == Commons.ALL_CHAMPIONS_REQUEST) {
                    if (allchampionsRequestCount < 3) {
                        allchampionsRequestCount++;
                        makeGetAllChampionsRequest();
                    }
                }else if(requestID == Commons.SUMMONER_SPELLS_REQUEST){
                    if(allSpellsRequestCount < 3){
                        allSpellsRequestCount++;
                        makeGetAllSpellsRequest();
                    }
                }
            }
        }

        @Override
        public Context getContext () {
            return this;
        }

        @Override
        public void onBackPressed () {
            if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
                mDrawerLayout.closeDrawer(Gravity.START);
            } else {
                int count = getSupportFragmentManager().getBackStackEntryCount();
                if (count <= 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(getResources().getString(R.string.areyousure)).setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                            .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }


        }

    public void updateDrawer() {
        if(Commons.getInstance(getApplicationContext()).ADS_ENABLED) {
            leftMenuItems = getResources().getStringArray(R.array.titles);
        } else {
            leftMenuItems = getResources().getStringArray(R.array.titles_noad);
        }

        setDrawer();


        String cat = leftMenuItems[7];
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>" + cat + "</font>"));
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //super.onSaveInstanceState(outState, outPersistentState);
    }

    private OnItemClickListener leftMenuWithAdsClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            final FragmentTransaction ft = fragmentManager.beginTransaction();
            mDrawerLayout.closeDrawer(mDrawer);
            if(fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

            if (position == 0) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        SummonerSearchFragment pFragment = new SummonerSearchFragment();
                        ft.replace(R.id.content_frame, pFragment).addToBackStack(Commons.PROFILE_FRAGMENT);
                        ft.commitAllowingStateLoss();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 1) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        WeeklyFreeChampionsFragment cFragment = new WeeklyFreeChampionsFragment();
                        ft.replace(R.id.content_frame, cFragment).addToBackStack(Commons.WEEKLY_FREE_CHAMPIONS_FRAGMENT);
                        ft.commitAllowingStateLoss();
                        showInterstitial();
                    }
                }, 350);

            } else if (position == 2) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        DiscountsFragment fragment = new DiscountsFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.DISCOUNTS_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 3) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        NewsFragment fragment = new NewsFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.NEWS_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 4) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        AllChampionsFragment fragment = new AllChampionsFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.ALL_CHAMPIONS_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 5) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        NewItemsFragment fragment = new NewItemsFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.ALL_ITEMS_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 6) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        RunesFragment fragment = new RunesFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.RUNES_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 7) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        AllChampionSkinsFragment fragment = new AllChampionSkinsFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.ALL_CHAMPIONS_SKINS_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 8) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        MatchInfoFragment fragment = new MatchInfoFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.MATCH_INFO_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 9) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        LiveChannelsFragment fragment = new LiveChannelsFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.LIVE_CHANNELS_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 10) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        SettingsFragment fragment = new SettingsFragment();
                        ft.replace(R.id.content_frame, fragment, Commons.TAG_SETTINGS_FRAGMENT).addToBackStack(Commons.SETTINGS_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            }  else if (position == 11) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        RemoveAdsFragment fragment = new RemoveAdsFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.REMOVE_ADS_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            }
            else if (position == 12) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        ContactFragment fragment = new ContactFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.CONTACT_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 13) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Log.d("TAGGG", "MainActivityOnClick");
                        AboutFragment fragment = new AboutFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.ABOUT_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            }

            // Closing the drawer
        }
    };

    private OnItemClickListener leftMenuNoAdsClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            final FragmentTransaction ft = fragmentManager.beginTransaction();
            mDrawerLayout.closeDrawer(mDrawer);
            if(fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

            if (position == 0) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        SummonerSearchFragment pFragment = new SummonerSearchFragment();
                        ft.replace(R.id.content_frame, pFragment).addToBackStack(Commons.PROFILE_FRAGMENT);
                        ft.commitAllowingStateLoss();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 1) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        WeeklyFreeChampionsFragment cFragment = new WeeklyFreeChampionsFragment();
                        ft.replace(R.id.content_frame, cFragment).addToBackStack(Commons.WEEKLY_FREE_CHAMPIONS_FRAGMENT);
                        ft.commitAllowingStateLoss();
                        showInterstitial();
                    }
                }, 350);

            } else if (position == 2) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        DiscountsFragment fragment = new DiscountsFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.DISCOUNTS_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 3) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        NewsFragment fragment = new NewsFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.NEWS_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 4) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        AllChampionsFragment fragment = new AllChampionsFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.ALL_CHAMPIONS_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 5) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        NewItemsFragment fragment = new NewItemsFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.ALL_ITEMS_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 6) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        RunesFragment fragment = new RunesFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.RUNES_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 7) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        AllChampionSkinsFragment fragment = new AllChampionSkinsFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.ALL_CHAMPIONS_SKINS_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 8) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        MatchInfoFragment fragment = new MatchInfoFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.MATCH_INFO_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 9) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        LiveChannelsFragment fragment = new LiveChannelsFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.LIVE_CHANNELS_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 10) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        SettingsFragment fragment = new SettingsFragment();
                        ft.replace(R.id.content_frame, fragment, Commons.TAG_SETTINGS_FRAGMENT).addToBackStack(Commons.SETTINGS_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 11) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        ContactFragment fragment = new ContactFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.CONTACT_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            } else if (position == 12) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Log.d("TAGGG", "MainActivityOnClick");
                        AboutFragment fragment = new AboutFragment();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.ABOUT_FRAGMENT);
                        ft.commit();
                        showInterstitial();
                    }
                }, 350);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (PaymentSevice.getInstance(this).getServiceConnection() != null) {
            unbindService(PaymentSevice.getInstance(this).getServiceConnection());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==  Commons.REMOVE_ADS_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Commons.getInstance(getApplicationContext()).ADS_ENABLED = false;
                Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
