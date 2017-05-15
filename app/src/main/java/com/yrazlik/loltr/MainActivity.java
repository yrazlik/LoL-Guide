package com.yrazlik.loltr;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yrazlik.loltr.adapters.LeftMenuListAdapter;
import com.yrazlik.loltr.billing.PaymentSevice;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.LeftMenuItem;
import com.yrazlik.loltr.fragments.AboutFragment;
import com.yrazlik.loltr.fragments.AllChampionSkinsFragment;
import com.yrazlik.loltr.fragments.AllChampionsFragment;
import com.yrazlik.loltr.fragments.ContactFragment;
import com.yrazlik.loltr.fragments.DiscountsFragment;
import com.yrazlik.loltr.fragments.LiveChannelsFragment;
import com.yrazlik.loltr.fragments.MatchInfoFragment;
import com.yrazlik.loltr.fragments.NewItemsFragment;
import com.yrazlik.loltr.fragments.NewsFragment;
import com.yrazlik.loltr.fragments.OtherAppsFragment;
import com.yrazlik.loltr.fragments.RemoveAdsFragment;
import com.yrazlik.loltr.fragments.RunesFragment;
import com.yrazlik.loltr.fragments.SettingsFragment;
import com.yrazlik.loltr.fragments.SummonerSearchFragment;
import com.yrazlik.loltr.fragments.WeeklyFreeChampionsFragment;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.SummonerSpellsResponse;
import com.yrazlik.loltr.service.ServiceHelper;
import com.yrazlik.loltr.view.PushNotificationDialog;
import com.yrazlik.loltr.view.RobotoTextView;
import java.util.ArrayList;
import java.util.List;

import static com.yrazlik.loltr.LoLFirebaseMessagingService.FIREBASE_PUSH_TOPIC;
import static com.yrazlik.loltr.LolNotification.NOTIFICATION_ACTION.ACTION_DISCOUNTS;
import static com.yrazlik.loltr.LolNotification.NOTIFICATION_ACTION.ACTION_HOME;
import static com.yrazlik.loltr.LolNotification.NOTIFICATION_ACTION.ACTION_LIVE_CHANNELS;
import static com.yrazlik.loltr.LolNotification.NOTIFICATION_ACTION.ACTION_NEWS;
import static com.yrazlik.loltr.LolNotification.NOTIFICATION_ACTION.ACTION_REMOVE_ADS;

public class MainActivity extends ActionBarActivity implements ResponseListener, View.OnClickListener {

    private Intent deeplinkIntent;
    int mPosition = -1;
    String mTitle = "";

    // Array of strings storing country names
    String[] leftMenuItems;

    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] icons = new int[]{R.drawable.ic_account_circle_black_24dp, R.drawable.coin, R.drawable.discount, R.drawable.news, R.drawable.champion,
            R.drawable.item, R.drawable.rune, R.drawable.costume, R.drawable.swords2, R.drawable.tv2, R.drawable.settings, R.drawable.block, R.drawable.contact,
            R.drawable.info};

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mDrawer;
    private List<LeftMenuItem> mList;

    private FloatingActionMenu fabMenu;
    private FloatingActionButton fabShare;
    private FloatingActionButton fabFacebook;
    private FloatingActionButton fabTwitter;
    private FloatingActionButton fabOtherApps;

    private LeftMenuListAdapter mAdapter;
    private AdView adView;
    private Toolbar mToolBar;

    private void continueSetup() {
        if(Commons.getInstance(getApplicationContext()).ADS_ENABLED) {
            setContentView(R.layout.activity_main);
            icons = new int[]{R.drawable.ic_profile, R.drawable.dollar, R.drawable.ic_discount, R.drawable.ic_newspaper, R.drawable.ic_face,
                    R.drawable.ic_hourglass, R.drawable.rune, R.drawable.ic_tshirt, R.drawable.ic_shield, R.drawable.ic_camera, R.drawable.ic_settings, R.drawable.ic_block, R.drawable.ic_mail,
                    R.drawable.ic_info};
        } else {
            setContentView(R.layout.activity_main_noad);
            icons = new int[]{R.drawable.ic_profile, R.drawable.dollar, R.drawable.ic_discount, R.drawable.ic_newspaper, R.drawable.ic_face,
                    R.drawable.ic_hourglass, R.drawable.rune, R.drawable.ic_tshirt, R.drawable.ic_shield, R.drawable.ic_camera, R.drawable.ic_settings, R.drawable.ic_mail,
                    R.drawable.ic_info};
        }

        fabMenu = (FloatingActionMenu) findViewById(R.id.menu_like);
        fabShare = (FloatingActionButton) findViewById(R.id.fabShare);
        fabTwitter = (FloatingActionButton) findViewById(R.id.fabTwitter);
        fabFacebook = (FloatingActionButton) findViewById(R.id.fabFacebook);
        fabOtherApps = (FloatingActionButton) findViewById(R.id.fabOtherApps);
        fabMenu.setOnClickListener(this);
        fabShare.setOnClickListener(this);
        fabTwitter.setOnClickListener(this);
        fabFacebook.setOnClickListener(this);
        fabOtherApps.setOnClickListener(this);

        ServiceHelper.getInstance(getContext()).makeGetAllSpellsRequest(this);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolBar != null) {
            setSupportActionBar(mToolBar);
        }

        adView = (AdView) findViewById(R.id.adView);
        if(adView != null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        setDrawer();
        performDrawerMenuItemClick(1);


        String cat = leftMenuItems[1];
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((RobotoTextView)toolbar.findViewById(R.id.toolbarTitle)).setText(cat);
        highlightSelectedMenuRow(2);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupInAppPurchases();
        continueSetup();
        deeplinkIntent = getIntent();
        subscribeToTopic();
    }

    private void subscribeToTopic() {
        try {
            FirebaseMessaging.getInstance().subscribeToTopic(FIREBASE_PUSH_TOPIC);
        } catch (Exception e) {
            Log.d("LolApplication", "Cannot subscribe to topic.");
        }
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
        if(mDrawerList.getHeaderViewsCount() == 0) {
            View headerView = getLayoutInflater().inflate(R.layout.menu_header_view, null, false);
            mDrawerList.addHeaderView(headerView, null, false);
        }

        // Getting a reference to the sidebar drawer ( Title + ListView )
        mDrawer = (NavigationView) findViewById(R.id.drawer);

        // Each row in the list stores country name, count and flag
        mList = new ArrayList<>();

        for (int i = 0; i < leftMenuItems.length; i++) {
            LeftMenuItem item = new LeftMenuItem(icons[i], leftMenuItems[i]);
            mList.add(item);
        }

        // Instantiating an adapter to store each items
        // R.layout.drawer_listrow defines the layout of each item
        mAdapter = new LeftMenuListAdapter(this, R.layout.drawer_listrow, mList);

        // Getting reference to DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Creating a ToggleButton for NavigationDrawer with drawer event
        // listener
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.drawer_open, R.string.drawer_close) {

            /** Called when drawer is closed */
            public void onDrawerClosed(View view) {
                highlightSelectedMenuRow();
                supportInvalidateOptionsMenu();
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                String selectCategory = getResources().getString(R.string.select_category);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                ((RobotoTextView)toolbar.findViewById(R.id.toolbarTitle)).setText(selectCategory);
                //getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>" + selectCategory + "</font>"));
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

    // Highlight the selected country : 0 to 4
    public void highlightSelectedMenuRow() {
        int selectedItem = mDrawerList.getCheckedItemPosition() - mDrawerList.getHeaderViewsCount();

        mPosition = selectedItem;


        if (mPosition > -1) {
            String cat = leftMenuItems[mPosition];
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            ((RobotoTextView)toolbar.findViewById(R.id.toolbarTitle)).setText(cat);
            //getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>" + cat + "</font>"));
            mAdapter.setSelectedItem(mPosition);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void highlightSelectedMenuRow(int position) {
        int selectedItem = position - mDrawerList.getHeaderViewsCount();

        mPosition = selectedItem;

        if (mPosition > -1) {
            String cat = leftMenuItems[mPosition];
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            ((RobotoTextView)toolbar.findViewById(R.id.toolbarTitle)).setText(cat);
            //getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>" + cat + "</font>"));
            mAdapter.setSelectedItem(mPosition);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSuccess(Object response) {
        if (response instanceof SummonerSpellsResponse){
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
        }

        @Override
        public Context getContext () {
            return this;
        }

        @Override
        public void onBackPressed () {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                int count = getSupportFragmentManager().getBackStackEntryCount();
                if (count <= 1) {
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences(AppRater.SHARED_PREFS_APPRATER, 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    if (AppRater.showRateDialog(this, editor, new AppRater.DialogDismissedListener() {
                        @Override
                        public void onDialogDismissed() {
                            showAlertDialog();
                        }
                    })) {
                    } else {
                        showAlertDialog();
                    }
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((RobotoTextView)toolbar.findViewById(R.id.toolbarTitle)).setText(cat);
        //getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>" + cat + "</font>"));
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //super.onSaveInstanceState(outState, outPersistentState);
    }

    private OnItemClickListener leftMenuWithAdsClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            position = position - mDrawerList.getHeaderViewsCount();
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
            position = position - mDrawerList.getHeaderViewsCount();
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
    protected void onResume() {
        super.onResume();
        if(deeplinkIntent != null) {
            if(deeplinkIntent.getExtras() != null) {
                if(Commons.isValidString(getIntentMessageBody())) {
                    LolNotification notification = new LolNotification(deeplinkIntent);
                    handleDeeplinkIntent(notification);
                }
            }
        }
        deeplinkIntent = null;
    }

    private String getIntentMessageBody() {
        try {
            return (String) deeplinkIntent.getExtras().get(LolNotification.PUSH_NOTIFICATION_BODY);
        } catch (Exception e) {
            return null;
        }
    }

    private void handleDeeplinkIntent(LolNotification notification) {
        if(notification != null) {
           if(notification.getNotificationAction() == LolNotification.NOTIFICATION_ACTION.ACTION_PLAY_STORE) {
               try {
                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(notification.getDeeplink())));
               } catch (Exception ignored) {}
           } else if(notification.getNotificationAction() == LolNotification.NOTIFICATION_ACTION.ACTION_WEB_URL) {
               try {
                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(notification.getDeeplink())));
               } catch (Exception ignored) {}
           } else {
               performDrawerMenuItemClick(getDeeplinkActionPositionOnDrawerMenu(notification.getNotificationAction()));
               new PushNotificationDialog(MainActivity.this, getBody(notification)).show();
           }
        }
    }

    private String getBody(LolNotification notification) {
        if (Commons.isValidString(notification.getBody())) {
            return notification.getBody();
        }
        return getString(R.string.app_name);
    }

    private void setupInAppPurchases() {
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        ServiceConnection mServiceConn = PaymentSevice.getInstance(this, null, true).getServiceConnection();
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (PaymentSevice.getInstance(this).getServiceConnection() != null) {
            try {
                unbindService(PaymentSevice.getInstance(this).getServiceConnection());
            }catch (IllegalArgumentException e) {

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==  Commons.REMOVE_ADS_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Commons.getInstance(getApplicationContext()).ADS_ENABLED = false;
                Commons.getInstance(getApplicationContext()).savePurchaseData();
                sendPurchaseSuccessEvent();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(getResources().getString(R.string.purchase_successful)).setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent restartIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                        PendingIntent intent = PendingIntent.getActivity(
                                MainActivity.this, 0,
                                restartIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        manager.set(AlarmManager.RTC, System.currentTimeMillis() + 1, intent);
                        System.exit(0);
                    }
                }).setCancelable(false).show();
            }
        }
    }

    public void sendPurchaseSuccessEvent() {
        try {
            Tracker t = ((LolApplication) getApplication()).getTracker();
            t.send(new HitBuilders.EventBuilder().setCategory(Commons.PURCHASE_SUCCESSFUL)
                    .setAction(Commons.PURCHASE_SUCCESSFUL)
                    .setLabel(Commons.PURCHASE_SUCCESSFUL)
                    .build());
        } catch (Exception e) {
        }
    }

    private void showAlertDialog() {
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
    }

    private void performDrawerMenuItemClick(final int position) {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("LolApplication", "Perform click position: " + position);
                mDrawerList.performItemClick(mDrawerList.getAdapter().getView(position, null, null), position + 1,
                        mDrawerList.getAdapter().getItemId(position + 1));
                highlightSelectedMenuRow();
            }
        }, 1000);
    }

    private int getDeeplinkActionPositionOnDrawerMenu(LolNotification.NOTIFICATION_ACTION notificationAction) {
        try {
            if(notificationAction == ACTION_HOME) {
               return 1;
            } else if(notificationAction == ACTION_REMOVE_ADS) {
                if(Commons.getInstance(getApplicationContext()).ADS_ENABLED) {
                    return 11;
                }
            } else if(notificationAction == ACTION_DISCOUNTS) {
                return 2;
            } else if(notificationAction == ACTION_NEWS) {
                return 3;
            } else if(notificationAction == ACTION_LIVE_CHANNELS) {
                return 9;
            }
        } catch (Exception e) {
            return 1;
        }
        return 1;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        deeplinkIntent = intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v == fabShare) {
            closeFabMenu();
            Commons.showAppPickerShareDialog(getContext(), "https://play.google.com/store/apps/details?id=com.yrazlik.loltr");
        } else if(v == fabFacebook) {
            closeFabMenu();
            Commons.openFacebookPage(this);
        } else if(v == fabTwitter) {
            closeFabMenu();
            Commons.openTwitterPage(this);
        } else if(v == fabOtherApps) {
            closeFabMenu();
            FragmentManager fragmentManager = getSupportFragmentManager();
            final FragmentTransaction ft = fragmentManager.beginTransaction();
            try {
                mDrawerLayout.closeDrawer(mDrawer);
            } catch (Exception e) {}

            Fragment currentFragment = fragmentManager.findFragmentById(R.id.content_frame);
            if(!(currentFragment instanceof  OtherAppsFragment)) {
                OtherAppsFragment otherAppsFragment = new OtherAppsFragment();
                Commons.setAnimation(ft, Commons.ANIM_OPEN_FROM_RIGHT_WITH_POPSTACK);
                ft.replace(R.id.content_frame, otherAppsFragment).addToBackStack(Commons.OTHERAPPS_FRAGMENT);
                ft.commit();
                showInterstitial();
            }
        }
    }

    private void closeFabMenu() {
        if(fabMenu.isOpened()) {
            fabMenu.close(true);
        }
    }
}
