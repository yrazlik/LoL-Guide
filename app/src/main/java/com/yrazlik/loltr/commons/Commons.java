package com.yrazlik.loltr.commons;

import android.content.Context;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.data.Items;

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
	
	public static final String LATEST_VERSION = "5.4.1";
	
	public static final String API_KEY = "2f29b61a-a187-49ab-a000-f5a4abc17f04";
	
	public static final String TAG = "com.yrazlik.leagueoflegends";
	
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

    public static final String SPECTATOR_SERVICE_BASE_URL_TR = "https://tr.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/TR1";
	public static final String SERVICE_BASE_URL = "https://tr.api.pvp.net/api/lol";
	public static final String STATIC_DATA_BASE_URL = "https://global.api.pvp.net/api/lol";
	public static final String CHAMPION_IMAGE_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/champion/";
	public static final String CHAMPION_SPLASH_IMAGE_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/img/champion/loading";
	public static final String CHAMPION_SPELL_IMAGE_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/spell/";
	public static String CHAMPION_PASSIVE_IMAGE_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/passive/";
	public static final String CHAMPION_ABILITIES_VIDEOS_BASE_URL = "http://cdn.leagueoflegends.com/champion-abilities/videos/mp4/";
	public static final String ITEM_IMAGES_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/item/";
	public static final String RUNES_IMAGES_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/rune/";
    public static final String LIVE_CHANNELS_URL = "https://api.twitch.tv/kraken/streams?game=League%20of%20Legends";
    public static final String URL_CHAMPION_PRICES = "https://gist.githubusercontent.com/yrazlik/d6b1c6644c7d40019063/raw/6563465cbcfc8effd1e082f7b47aa0e719bc99a0/championcosts";
	public static final String URL_CHAMPION_SKIN_BASE = "http://ddragon.leagueoflegends.com/cdn/img/champion/splash/";

	public static ArrayList<Champion> weeklyFreeChampions;
	public static ArrayList<Champion> allChampions;
	public static ArrayList<Items> allItems;
	
	
	
	public static String getTuesday(){
		Calendar c = Calendar.getInstance();
		
		if(c.get(Calendar.DAY_OF_WEEK) < 3){
			c.add(Calendar.DATE, -7);
			c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", new Locale("tr"));
			Date d = c.getTime();
			String start = sdf.format(d);
			c.add(Calendar.DATE, 7);
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMMM", new Locale("tr"));
			Date d2 = c.getTime();
			String end = sdf.format(d2);
			return start + " - " + end;
		}else{
			c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", new Locale("tr"));
			Date d = c.getTime();
			String start = sdf.format(d);
			c.add(Calendar.DATE, 7);
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMMM", new Locale("tr"));
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
	
}
