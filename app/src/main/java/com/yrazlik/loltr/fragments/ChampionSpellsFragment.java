package com.yrazlik.loltr.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.ChampionSpellsListAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Passive;
import com.yrazlik.loltr.data.Spell;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.ChampionSpellsResponse;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class ChampionSpellsFragment extends BaseFragment implements ResponseListener{
	
	private int champId;
	private ListView list;
	private ChampionSpellsListAdapter adapter;
	private ArrayList<Spell> championSpells;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_champion_spells, container, false);
		initUI(v);
		getExtras();
		
		ArrayList<String> pathParams = new ArrayList<String>();
		pathParams.add("static-data");
		pathParams.add(Commons.getInstance(getContext().getApplicationContext()).getRegion());
		pathParams.add("v1.2");
		pathParams.add("champion");
		pathParams.add(String.valueOf(champId));
		HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("locale", Commons.getInstance(getContext().getApplicationContext()).getLocale());
		queryParams.put("version", Commons.LATEST_VERSION);
		queryParams.put("champData", "passive,spells");
		queryParams.put("api_key", Commons.API_KEY);
		ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.CHAMPION_SPELLS_REQUEST, pathParams, queryParams, null, this);
		return v;

	}
	
	private void getExtras(){
		Bundle args = getArguments();
		if(args != null){
			champId = args.getInt(ChampionDetailFragment.EXTRA_CHAMPION_ID);
		}
	}
	
	private void initUI(View v){
		list = (ListView) v.findViewById(R.id.listviewChampionSpells);
		championSpells = new ArrayList<Spell>();
		adapter = new ChampionSpellsListAdapter(getContext(), R.layout.list_row_abilities, championSpells);
		list.setAdapter(adapter);
	}
	
	private String getSpellKey(int i) {
		switch (i) {
		case 0:
			return "Q";
		case 1:
			return "W";
		case 2:
			return "E";
		case 3:
			return "R";

		default:
			break;
		}
		return "";
	}

	@Override
	public void onSuccess(Object response) {
		if(response instanceof ChampionSpellsResponse){
            try {
                ChampionSpellsResponse resp = (ChampionSpellsResponse) response;
                ArrayList<Spell> spells = resp.getSpells();
                Passive passive = resp.getPassive();
                Spell passiveSpell = new Spell(passive.getName() + " (" + getResources().getString(R.string.passive) + ")", passive.getSanitizedDescription(), passive.getImage(), " ");
                championSpells.add(passiveSpell);
                for (int i = 0; i < spells.size(); i++) {
                    Spell spell = spells.get(i);
                    spell.setSpellKey(getSpellKey(i));
                    championSpells.add(spell);
                }
                adapter.notifyDataSetChanged();
            }catch (Exception ignored){}
			
		}
		
	}

	@Override
	public void onFailure(Object response) {
		String errorMessage = (String)response;
		Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
	}

	@Override
	public Context getContext() {
		return getActivity();
	}

    @Override
    public void onResume() {
        super.onResume();
        reportGoogleAnalytics();
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("ChampionSpellsFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
