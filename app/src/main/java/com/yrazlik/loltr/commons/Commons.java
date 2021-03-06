package com.yrazlik.loltr.commons;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.TextView;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.data.Item;
import com.yrazlik.loltr.data.Items;
import com.yrazlik.loltr.data.RecentSearchItem;
import com.yrazlik.loltr.data.SummonerSpell;
import com.yrazlik.loltr.responseclasses.AllChampionsResponse;
import com.yrazlik.loltr.responseclasses.SummonerInfo;
import com.yrazlik.loltr.view.RobotoTextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class Commons {

    public boolean ADS_ENABLED = true;
	
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

    public static final String FONT_NORMAL = "fonts/Roboto.ttf";
    public static final String FONT_MEDIUM = "fonts/Roboto-Medium.ttf";
    public static final String FONT_BOLD = "fonts/Roboto-Medium.ttf";
    public static final String FONT_ITALIC = "fonts/Roboto-Italic.ttf";

    public static String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnZHsXqXjxssy8qMs7Ekl/PjTQDSlT9AcjyFOpauiVnBCOVv81zvJODMOsgLY1A+kMRvbjkrD2I3Ey6FVFfpE5Rl6gLSs6fDv9iEm22iDASU/pKMsMG4keaNgKxw9+GP0anyzvSbhuYar6MZHefcr/3WuElakkxN4z/wOGXcaIRx8ZNqk4Fh4CL06xERnD+ZT6jBFztUM8Jpdf+DdVoxqFamh8Cu74H+/2x4OGgp4JAU+HKJXDHE51x15ezDBHGpSGNeuH57Ew4xvq27Ixvx+GbfJYioiJmAYpZk5OuvK6Zx6YzS8jPy7kCXZJJRFswcKjuGL286dDhE2J6zPL3pRQwIDAQAB";
    public static final int REMOVE_ADS_REQUEST_CODE = 1111;
    public static final String REMOVE_ADS_ID = "remove_ads";

    public static String TEXT = "AAA";

    public static final String PURCHASE_CLICK = "PURCHASE_CLICK";
    public static final String PURCHASE_SUCCESSFUL = "PURCHASE_SUCCESSFUL";
    public static final String PURCHASE_FAIL = "PURCHASE_FAIL";

    public static final String LOL_TR_SHARED_PREFS = "LOL_TR_SHARED_PREFS";
    public static final String LOL_TR_SHARED_PREF_LANGUAGE = "LOL_TR_SHARED_PREF_LANGUAGE";
    public static final String LOL_TR_SHARED_PREF_REGION = "LOL_TR_SHARED_PREF_REGION";
    public static final String LOL_TR_PURCHASED_AD_FREE = "LOL_TR_PURCHASED_AD_FREE";

    public static final String LOL_TR_SUMMONER_NAME = "LOL_TR_SUMMONER_NAME";
    public static final String LOL_TR_SUMMONER_ID = "LOL_TR_SUMMONER_ID";
    public static final String LOL_TR_PROFILE_ICON_ID = "LOL_TR_PROFILE_ICON_ID";
    public static final String LOL_TR_SUMMONER_LEVEL = "LOL_TR_SUMMONER_LEVEL";

    public static final String GOOGLE_ANALYTICS_TRACKING_ID = "UA-52774268-8";
	
	public static String LATEST_VERSION = "6.16.1";
    public static String RECOMMENDED_ITEMS_VERSION = "6.16.1";
	
	public static final String API_KEY = "adcd5cd2-a699-4e04-90e0-2e7b9e5d990a";
	
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
    public static final int RSS_NEWS_REQUEST = 25;

    public static String[] regions = {"TR", "EUW", "NA", "EUNE", "JP", "OCE", "BR", "LAN", "LAS", "RU", "KR"};

    public static String BASE_URL_BODY = ".api.riotgames.com";
    public static String YAHO_RSS_FEED_URL = "https://esports.yahoo.com/league-of-legends/rss";
    public static String SELECTED_REGION = "";
    public static String SELECTED_REGION_FOR_MATCH_INFO_PATH_PARAM = "tr";
    public static String SERVICE_BASE_URL_FOR_MATCH_INFO = "https://" + SELECTED_REGION.toLowerCase() + BASE_URL_BODY + "/api/lol";
    public static String SPECTATOR_SERVICE_BASE_URL_CURRENT_SELECTED = "https://" + SELECTED_REGION + BASE_URL_BODY + "/observer-mode/rest/consumer/getSpectatorGameInfo/TR1";
    public static String PROFILE_ICON_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + Commons.LATEST_VERSION + "/img/profileicon/";




	public static final String STATIC_DATA_BASE_URL = "https://global.api.riotgames.com/api/lol";
    public static String SUMMONER_SPELL_IMAGE_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/spell/";
	public static String CHAMPION_IMAGE_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/champion/";
	public static final String CHAMPION_SPLASH_IMAGE_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/img/champion/loading";
	public static String CHAMPION_SPELL_IMAGE_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/spell/";
	public static String CHAMPION_PASSIVE_IMAGE_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/passive/";
	public static final String CHAMPION_ABILITIES_VIDEOS_BASE_URL = "http://cdn.leagueoflegends.com/champion-abilities/videos/mp4/";
	public static String ITEM_IMAGES_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/item/";
	public static String RUNES_IMAGES_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/rune/";
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
    public static final String REMOVE_ADS_FRAGMENT = "com.yrazlik.loltr.fragments.removeadsfragment";
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

    public static String getSpectatorServiceBaseUrl(String selectedRegion) {
        return "https://" + selectedRegion.toLowerCase() + BASE_URL_BODY + "/api/lol";
    }

    public static String getSpectatorServiceBaseUrlCurrentSelected(String selectedRegion) {

        if(selectedRegion.equalsIgnoreCase("tr")) {
            return "https://" + selectedRegion + BASE_URL_BODY +"/observer-mode/rest/consumer/getSpectatorGameInfo/TR1";
        } else if(selectedRegion.equalsIgnoreCase("ru")) {
            return "https://" + selectedRegion + BASE_URL_BODY +"/observer-mode/rest/consumer/getSpectatorGameInfo/RU";
        } else if(selectedRegion.equalsIgnoreCase("euw")) {
            return "https://" + selectedRegion + BASE_URL_BODY +"/observer-mode/rest/consumer/getSpectatorGameInfo/EUW1";
        } else if(selectedRegion.equalsIgnoreCase("na")) {
            return "https://" + selectedRegion + BASE_URL_BODY +"/observer-mode/rest/consumer/getSpectatorGameInfo/NA1";
        } else if(selectedRegion.equalsIgnoreCase("eune")) {
            return "https://" + selectedRegion + BASE_URL_BODY +"/observer-mode/rest/consumer/getSpectatorGameInfo/EUN1";
        } else if(selectedRegion.equalsIgnoreCase("oce")) {
            return "https://" + selectedRegion + BASE_URL_BODY +"/observer-mode/rest/consumer/getSpectatorGameInfo/OC1";
        } else if(selectedRegion.equalsIgnoreCase("br")) {
            return "https://" + selectedRegion + BASE_URL_BODY +"/observer-mode/rest/consumer/getSpectatorGameInfo/BR1";
        } else if(selectedRegion.equalsIgnoreCase("lan")) {
            return "https://" + selectedRegion + BASE_URL_BODY +"/observer-mode/rest/consumer/getSpectatorGameInfo/LA1";
        } else if(selectedRegion.equalsIgnoreCase("las")) {
            return "https://" + selectedRegion + BASE_URL_BODY +"/observer-mode/rest/consumer/getSpectatorGameInfo/LA2";
        } else if(selectedRegion.equalsIgnoreCase("kr")) {
            return "https://" + selectedRegion + BASE_URL_BODY +"/observer-mode/rest/consumer/getSpectatorGameInfo/KR";
        } else if(selectedRegion.equalsIgnoreCase("jp")) {
            return "https://" + selectedRegion + BASE_URL_BODY +"/observer-mode/rest/consumer/getSpectatorGameInfo/JP1";
        }
        return "https://" + selectedRegion + BASE_URL_BODY +"/observer-mode/rest/consumer/getSpectatorGameInfo/TR1";
    }

    public static String getSpectatorServiceRegionPathParameter(String selectedRegion) {

        if(selectedRegion.equalsIgnoreCase("tr")) {
            return "TR1";
        } else if(selectedRegion.equalsIgnoreCase("ru")) {
            return "RU";
        } else if(selectedRegion.equalsIgnoreCase("euw")) {
            return "EUW1";
        } else if(selectedRegion.equalsIgnoreCase("na")) {
            return "NA1";
        } else if(selectedRegion.equalsIgnoreCase("eune")) {
            return "EUN1";
        } else if(selectedRegion.equalsIgnoreCase("oce")) {
            return "OC1";
        } else if(selectedRegion.equalsIgnoreCase("br")) {
            return "BR1";
        } else if(selectedRegion.equalsIgnoreCase("lan")) {
            return "LA1";
        } else if(selectedRegion.equalsIgnoreCase("las")) {
            return "LA2";
        } else if(selectedRegion.equalsIgnoreCase("kr")) {
            return "KR";
        } else if(selectedRegion.equalsIgnoreCase("jp")) {
            return "JP1";
        }
        return "TR1";
    }

    public static void updateLatestVersionVariables(){
        PROFILE_ICON_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/profileicon/";
        SUMMONER_SPELL_IMAGE_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/spell/";
        CHAMPION_IMAGE_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/champion/";
        CHAMPION_SPELL_IMAGE_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/spell/";
        CHAMPION_PASSIVE_IMAGE_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/passive/";
        ITEM_IMAGES_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/item/";
        RUNES_IMAGES_BASE_URL = "http://ddragon.leagueoflegends.com/cdn/" + LATEST_VERSION + "/img/rune/";

    }
	
	
	public static String getTuesday(){
		Calendar c = Calendar.getInstance();
		
		if(c.get(Calendar.DAY_OF_WEEK) < 3){
			c.add(Calendar.DATE, -7);
			c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", new Locale(getLocaleForMonthName()));
			Date d = c.getTime();
			String start = sdf.format(d);
			c.add(Calendar.DATE, 7);
			Date d2 = c.getTime();
			String end = sdf.format(d2);
			return start + " - " + end;
		}else{
			c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", new Locale(getLocaleForMonthName()));
			Date d = c.getTime();
			String start = sdf.format(d);
			c.add(Calendar.DATE, 7);
			Date d2 = c.getTime();
			String end = sdf.format(d2);
			return start + " - " + end;
		}
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

    public static String getRegion(){
        return SELECTED_REGION;
    }

    public static String getLocale(){
        String locale = mContext.getApplicationContext().getResources().getConfiguration().locale.toString();
        if(locale.toLowerCase().startsWith("en")) {
            return "en_US";
        } else if(locale.toLowerCase().startsWith("tr")) {
            return "tr_TR";
        } else if(locale.toLowerCase().startsWith("pt")) {
            return "pt_BR";
        }
        return "en_US";
    }

    public static String getLocaleForMonthName(){
        String locale = getLocale();
        if(locale.toLowerCase().startsWith("en")) {
            return "en";
        } else if(locale.toLowerCase().startsWith("tr")) {
            return "tr";
        } else if(locale.toLowerCase().startsWith("pt")) {
            return "pt";
        }
        return "en";
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

    public void savePurchaseData() {
        SharedPreferences prefs = mContext.getSharedPreferences(Commons.LOL_TR_SHARED_PREFS, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(Commons.LOL_TR_PURCHASED_AD_FREE, true).commit();
    }

    public boolean loadPurchaseData() {
        SharedPreferences prefs = mContext.getSharedPreferences(Commons.LOL_TR_SHARED_PREFS, Context.MODE_PRIVATE);
        boolean isPurchased = prefs.getBoolean(Commons.LOL_TR_PURCHASED_AD_FREE, false);
        return isPurchased;
    }

    public static void setAllChampions(AllChampionsResponse resp) {
        try {
            Map<String, Map<String, String>> data = resp.getData();
            if (Commons.allChampions != null) {
                Commons.allChampions.clear();
            } else {
                Commons.allChampions = new ArrayList<>();
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
        } catch (Exception ignored) {}
    }

    public static void openInBrowser(Context context, String url) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        }catch (Exception e) {
            Log.d("LoLApp", "Invalid url");
        }
    }

    public static void underline(RobotoTextView tv) {
        if(tv != null) {
            SpannableString content = new SpannableString(tv.getText().toString());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            tv.setText(content);
        }
    }

    public static boolean isValidString(String s) {
        return s != null && s.length() > 0;
    }

    public static void saveToSharedPrefs(String key, String value){
        try{
            SharedPreferences prefs = mContext.getApplicationContext().getSharedPreferences(Commons.LOL_TR_SHARED_PREFS, Context.MODE_PRIVATE);
            prefs.edit().putString(key, value).commit();
        }catch (Exception ignored){}
    }

    public static ArrayList<String> getSortedRegions() {
        try {
            ArrayList<String> allRegions = new ArrayList<>(Arrays.asList(regions));
            ArrayList<String> sortedRegions = new ArrayList<>();
            sortedRegions.add(Commons.getRegion().toUpperCase());
            for(int i = 0; i < allRegions.size(); i++) {
                if(!Commons.getRegion().equalsIgnoreCase(allRegions.get(i))) {
                    sortedRegions.add(allRegions.get(i).toUpperCase());
                }
            }
            return sortedRegions;
        } catch (Exception e) {
            return new ArrayList<>(Arrays.asList(regions));
        }
    }
}
