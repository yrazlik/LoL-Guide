package com.yrazlik.loltr.commons;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.data.Item;
import com.yrazlik.loltr.data.Items;
import com.yrazlik.loltr.data.RecentSearchItem;
import com.yrazlik.loltr.data.SummonerSpell;
import com.yrazlik.loltr.responseclasses.SummonerInfo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Commons {
	
	private static Context mContext;
	private static Commons mCommons;
	
	public static Commons getInstance(Context context){
		if(mCommons == null){
			mCommons = new Commons();
			mContext = context;
		}
		return mCommons;
	}
	
	public enum FontType{
		default_font,
		bold,
		light,
		medium,
		regular
	}

    public static String TEXT = "AAA";

    public static final String LOL_TR_SHARED_PREFS = "LOL_TR_SHARED_PREFS";
    public static final String LOL_TR_SHARED_PREF_LANGUAGE = "LOL_TR_SHARED_PREF_LANGUAGE";
    public static final String LOL_TR_SHARED_PREF_REGION = "LOL_TR_SHARED_PREF_REGION";

    public static final String LOL_TR_SUMMONER_NAME = "LOL_TR_SUMMONER_NAME";
    public static final String LOL_TR_SUMMONER_ID = "LOL_TR_SUMMONER_ID";
    public static final String LOL_TR_PROFILE_ICON_ID = "LOL_TR_PROFILE_ICON_ID";
    public static final String LOL_TR_SUMMONER_LEVEL = "LOL_TR_SUMMONER_LEVEL";

    public static final String GOOGLE_ANALYTICS_TRACKING_ID = "UA-52774268-8";
	
	public static String LATEST_VERSION = "5.24.2";
    public static String RECOMMENDED_ITEMS_VERSION = "5.24.2";
	
	public static final String API_KEY = "2f29b61a-a187-49ab-a000-f5a4abc17f04";
	
	public static final String TAG = "com.yrazlik.loltr";
	
	public static final int WEEKLY_FREE_CHAMPIONS_REQUEST = 1;
	public static final int STATIC_DATA_WITH_ALT_IMAGES_REQUEST = 2;
	public static final int CHAMPION_RP_IP_COSTS_REQUEST = 3;
	public static final int CHAMPION_OVERVIEW_REQUEST = 4;
	public static final int CHAMPION_SPLASH_IMAGE_REQUEST = 5;
	public static final int CHAMPION_SPELLS_REQUEST = 6;
	public static final int ALL_CHAMPIONS_REQUEST = 7;
	public static final int CHAMPION_LEGEND_REQUEST = 8;
	public static final int CHAMPION_STRATEGY_REQUEST = 9;
	public static final int RECOMMENDED_ITEMS_REQUEST = 10;
	public static final int ALL_ITEMS_REQUEST = 11;
	public static final int ITEM_DETAIL_REQUEST = 12;
	public static final int ALL_RUNES_REQUEST = 13;
    public static final int LIVE_CHANNELS_REQUEST = 14;
    public static final int CHAMPION_SKINS_REQUEST = 15;
    public static final int SUMMONER_INFO_REQUEST = 16;
    public static final int MATCH_INFO_REQUEST = 17;
    public static final int LEAGUE_INFO_REQUEST = 18;
    public static final int STATS_REQUEST = 19;
    public static final int SUMMONER_BY_NAME_REQUEST = 20;
    public static final int RECENT_MATCHES_REQUEST = 21;
    public static final int SUMMONER_SPELLS_REQUEST = 22;
    public static final int SUMMONER_NAMES_REQUEST = 23;
    public static final int RANKED_STATS_REQUEST = 24;




    public static boolean REGION_SELECTED = false;
    public static String SELECTED_REGION = "tr";
    public static String SELECTED_LANGUAGE = "tr";
    public static String SPECTATOR_SERVICE_BASE_URL_CURRENT_SELECTED = "https://" + SELECTED_REGION + ".api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/TR1";
    public static final String SPECTATOR_SERVICE_BASE_URL_OC = "https://oce.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/OC1";
    public static final String SPECTATOR_SERVICE_BASE_URL_EUNE = "https://eune.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/EUN1";
    public static final String SPECTATOR_SERVICE_BASE_URL_EUW = "https://euw.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/EUW1";
    public static final String SPECTATOR_SERVICE_BASE_URL_NA = "https://na.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/NA1";
    public static final String SPECTATOR_SERVICE_BASE_URL_TR = "https://tr.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/TR1";
	public static final String SERVICE_BASE_URL = "https://" + SELECTED_REGION +".api.pvp.net/api/lol";
    public static final String SERVICE_BASE_URL_TR = "https://tr.api.pvp.net/api/lol";
    public static final String SERVICE_BASE_URL_EUNE = "https://eune.api.pvp.net/api/lol";
    public static final String SERVICE_BASE_URL_EUW = "https://euw.api.pvp.net/api/lol";
    public static final String SERVICE_BASE_URL_OCE = "https://oce.api.pvp.net/api/lol";
    public static final String SERVICE_BASE_URL_NA = "https://na.api.pvp.net/api/lol";
    public static String SERVICE_BASE_URL_FOR_MATCH_INFO = "https://tr.api.pvp.net/api/lol";
    public static String SERVICE_BASE_URL_SELECTED = "https://" + SELECTED_REGION +".api.pvp.net/api/lol";
    public static String PROFILE_ICON_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + Commons.LATEST_VERSION + "/img/profileicon/";




	public static final String STATIC_DATA_BASE_URL = "https://global.api.pvp.net/api/lol";
    public static final String SUMMONER_SPELL_IMAGE_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/spell/";
	public static final String CHAMPION_IMAGE_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/champion/";
	public static final String CHAMPION_SPLASH_IMAGE_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/img/champion/loading";
	public static final String CHAMPION_SPELL_IMAGE_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/spell/";
	public static String CHAMPION_PASSIVE_IMAGE_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/passive/";
	public static final String CHAMPION_ABILITIES_VIDEOS_BASE_URL = "http://cdn.leagueoflegends.com/champion-abilities/videos/mp4/";
	public static final String ITEM_IMAGES_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/item/";
	public static final String RUNES_IMAGES_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/rune/";
    public static final String LIVE_CHANNELS_URL = "https://api.twitch.tv/kraken/streams?game=League%20of%20Legends";
    public static final String URL_CHAMPION_PRICES = "https://gist.githubusercontent.com/yrazlik/d6b1c6644c7d40019063/raw/4affa8a1f849a587ce5277de61922e04b871737a/championcosts";
	public static final String URL_CHAMPION_SKIN_BASE = "http://ddragon.leagueoflegends.com/cdn/img/champion/splash/";

    public static final String PROFILE_FRAGMENT = "com.yrazlik.loltr.fragments.profilefragment";
    public static final String WEEKLY_FREE_CHAMPIONS_FRAGMENT = "com.yrazlik.loltr.fragments.weeklyfreechampionsfragment";
    public static final String ALL_CHAMPIONS_FRAGMENT = "com.yrazlik.loltr.fragments.allchampionsfragment";
    public static final String DISCOUNTS_FRAGMENT = "com.yrazlik.loltr.fragments.discountsfragment";
    public static final String DISCOUNT_COSTUMES_FRAGMENT = "com.yrazlik.loltr.fragments.discountcostumesfragment";
    public static final String NEWS_FRAGMENT = "com.yrazlik.loltr.fragments.newsfragment";
    public static final String NEWS_DETAIL_FRAGMENT = "com.yrazlik.loltr.fragments.newsdetailfragment";
    public static final String ALL_ITEMS_FRAGMENT = "com.yrazlik.loltr.fragments.allitemsfragment";
    public static final String RUNES_FRAGMENT = "com.yrazlik.loltr.fragments.runesfragment";
    public static final String ALL_CHAMPIONS_SKINS_FRAGMENT = "com.yrazlik.loltr.fragments.allchampionsskinsfragment";
    public static final String MATCH_INFO_FRAGMENT = "com.yrazlik.loltr.fragments.matchinfofragment";
    public static final String LIVE_CHANNELS_FRAGMENT = "com.yrazlik.loltr.fragments.livechannelsfragment";
    public static final String SETTINGS_FRAGMENT = "com.yrazlik.loltr.fragments.settingsfragment";
    public static final String CONTACT_FRAGMENT = "com.yrazlik.loltr.fragments.contactfragment";
    public static final String ABOUT_FRAGMENT = "com.yrazlik.loltr.fragments.aboutfragment";
    public static final String CHAMPION_DETAILS_FRAGMENT = "com.yrazlik.loltr.fragments.championdetailsfragment";
    public static final String ITEM_DETAIL_FRAGMENT = "com.yrazlik.loltr.fragments.itemdetailfragment";
    public static final String CHAMPION_SKINS_FRAGMENT = "com.yrazlik.loltr.fragments.championskinsfragment";
    public static final String MATCH_DETAIL_FRAGMENT = "com.yrazlik.loltr.fragments.matchdetailfragment";
    public static final String MATCH_HISTORY_FRAGMENT = "com.yrazlik.loltr.fragments.matchhistoryfragment";
    public static final String SUMMONER_CONTAINER_FRAGMENT = "com.yrazlik.loltr.fragments.summonercontainerfragment";

    public static final String TAG_SETTINGS_FRAGMENT = "com.yrazlik.loltr.fragments.settingsfragmenttag";

    public static final int ANIM_UNDEFINED = -1;
    public static final int ANIM_OPEN_FROM_LEFT = 1;
    public static final int ANIM_OPEN_FROM_RIGHT = 2;
    public static final int ANIM_OPEN_FROM_BOTTOM = 3;
    public static final int ANIM_OPEN_FROM_TOP = 4;
    public static final int ANIM_FLIP_PAGE = 5;
    public static final int ANIM_CLOSE_TO_TOP = 6;
    public static final int ANIM_CLOSE_TO_BOTTOM = 7;
    public static final int ANIM_OPEN_FROM_RIGHT_WITH_POPSTACK = 8;
    public static final int ANIM_OPEN_FROM_BOTTOM_WITH_POPSTACK = 9;
    public static final int ANIM_SLIDE_BOTTOM_IN_TOP_OUT = 10;
    public static final int ANIM_SLIDE_TOP_OUT_FADE_OUT = 11;
    public static final int ANIM_FLIP_WITH_POPSTACK = 12;


	public static ArrayList<Champion> weeklyFreeChampions;
	public static ArrayList<Champion> allChampions;
    public static ArrayList<SummonerSpell> allSpells;
	public static ArrayList<Items> allItems;
    public static ArrayList<Item> allItemsNew;
    public static SummonerInfo summonerInfo;
	
	
	
	public static String getTuesday(){
		Calendar c = Calendar.getInstance();
		
		if(c.get(Calendar.DAY_OF_WEEK) < 3){
			c.add(Calendar.DATE, -7);
			c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", new Locale(getLanguage()));
			Date d = c.getTime();
			String start = sdf.format(d);
			c.add(Calendar.DATE, 7);
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMMM", new Locale(getLanguage()));
			Date d2 = c.getTime();
			String end = sdf.format(d2);
			return start + " - " + end;
		}else{
			c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", new Locale(getLanguage()));
			Date d = c.getTime();
			String start = sdf.format(d);
			c.add(Calendar.DATE, 7);
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMMM", new Locale(getLanguage()));
			Date d2 = c.getTime();
			String end = sdf.format(d2);
			return start + " - " + end;
		}
	}
	
	public static String getTurkishTag(String tag){
		if(tag.equals(mContext.getResources().getString(R.string.mage))){
			return mContext.getResources().getString(R.string.buyucu);
		}else if(tag.equals(mContext.getResources().getString(R.string.fighter))){
			return mContext.getResources().getString(R.string.dovuscu);
		}else if(tag.equals(mContext.getResources().getString(R.string.assasin))){
			return mContext.getResources().getString(R.string.suikastci);
		}else if(tag.equals(mContext.getResources().getString(R.string.tank))){
			return mContext.getResources().getString(R.string.tank);
		}else if(tag.equals(mContext.getResources().getString(R.string.marksman))){
			return mContext.getResources().getString(R.string.nisanci);
		}else if(tag.equals(mContext.getResources().getString(R.string.support))){
			return mContext.getResources().getString(R.string.destek);
		}
		return "";
	}

    public static void setAnimation(FragmentTransaction ft, int animationDirection) {
        switch (animationDirection) {
            case ANIM_OPEN_FROM_LEFT:
                ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case ANIM_OPEN_FROM_RIGHT:
                ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case ANIM_OPEN_FROM_RIGHT_WITH_POPSTACK:
                ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out, R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case ANIM_OPEN_FROM_BOTTOM_WITH_POPSTACK:
                ft.setCustomAnimations(R.anim.slide_bottom_in, android.R.anim.fade_out, android.R.anim.fade_in, R.anim.slide_top_out);
                break;
            case ANIM_OPEN_FROM_BOTTOM:
                ft.setCustomAnimations(R.anim.slide_bottom_in, android.R.anim.fade_out);
                break;
            case ANIM_OPEN_FROM_TOP:
                ft.setCustomAnimations(R.anim.slide_top_in, android.R.anim.fade_out);
                break;
            case ANIM_CLOSE_TO_TOP:
                ft.setCustomAnimations(android.R.anim.fade_in, R.anim.slide_bottom_out);
                break;
            case ANIM_CLOSE_TO_BOTTOM:
                ft.setCustomAnimations(android.R.anim.fade_in, R.anim.slide_top_out);
                break;
            case ANIM_SLIDE_BOTTOM_IN_TOP_OUT:
                ft.setCustomAnimations(R.anim.slide_bottom_in, android.R.anim.fade_out);
                break;
            case ANIM_SLIDE_TOP_OUT_FADE_OUT:
                ft.setCustomAnimations(R.anim.slide_top_out, android.R.anim.fade_out);
                break;
            default:
                break;
        }
    }

    public static String getLanguage(){
        return SELECTED_LANGUAGE;
    }

    public static String getRegion(){
        return SELECTED_REGION;
    }

    public static String getLocale(){
        if(SELECTED_LANGUAGE.equalsIgnoreCase("tr")){
            return "tr_TR";
        }
        return "en_US";
    }

    public static void saveRecentSearchesArray(ArrayList<RecentSearchItem> obj, Context context) {

        String fileName = "com.yrazlik.loltr.recentsearches";
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);

            ObjectOutputStream out = new ObjectOutputStream(fos);

            out.writeObject(obj);

            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<RecentSearchItem> loadRecentSearchesArrayList(Context context) {
        try {
            String fileName = "com.yrazlik.loltr.recentsearches";
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream in = new ObjectInputStream(fis);

            ArrayList<RecentSearchItem> obj = (ArrayList<RecentSearchItem>) in.readObject();

            in.close();
            return obj;

        }  catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
