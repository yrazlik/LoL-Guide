package com.yrazlik.loltr.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.AllChampionsGridAdapter;
import com.yrazlik.loltr.api.ApiHelper;
import com.yrazlik.loltr.api.error.ApiResponseListener;
import com.yrazlik.loltr.api.error.RetrofitResponseHandler;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.db.DbHelper;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.model.ChampionDto;
import com.yrazlik.loltr.model.ChampionListDto;
import com.yrazlik.loltr.model.ImageDto;
import com.yrazlik.loltr.responseclasses.AllChampionsResponse;
import com.yrazlik.loltr.utils.CacheUtils;
import com.yrazlik.loltr.utils.Utils;
import com.yrazlik.loltr.view.RobotoEditText;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class AllChampionsFragment extends BaseFragment implements OnItemClickListener, TextWatcher {

    private GridView championsGrid;
    private RobotoEditText searchBar;
    private RobotoTextView noChampsFoundTV;

    private AllChampionsGridAdapter allChampionsGridAdapter;
    private List<ChampionDto> gridChampions; //current champions being shown on gridview
    private List<ChampionDto> allChampions; // all champions

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allChampions = getAllChampionsList();
        gridChampions = new ArrayList<>(allChampions);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_champions, container, false);
            showProgressWithWhiteBG();
            initUI(rootView);
        } else {
            dismissProgress();
        }

        if (gridChampions == null || gridChampions.size() == 0) {
            requestAllChampions();
        } else {
            setAdapter();
        }
        return rootView;
    }

    private void initUI(View v) {
        noChampsFoundTV = (RobotoTextView) v.findViewById(R.id.noChampsFoundTV);
        championsGrid = (GridView) v.findViewById(R.id.gridview_champions);
        searchBar = (RobotoEditText) v.findViewById(R.id.edittextSearchBar);
        searchBar.addTextChangedListener(this);
        championsGrid.setOnItemClickListener(this);
    }

    private void requestAllChampions() {
        ApiHelper.getInstance(getContext()).getAllChampions(new RetrofitResponseHandler(new ApiResponseListener() {
            @Override
            public void onResponseFromCache(Object response) {
                dismissProgress();
                copyAllChampionsToGridArray();
                setAdapter();
            }

            @Override
            public void onResponse(Call call, Response response) {
                dismissProgress();
                ChampionListDto resp = (ChampionListDto) response.body();
                DbHelper.getInstance().saveAllChampionsData(resp);
                updateGridChampions(resp);
                setAdapter();
            }

            @Override
            public void onUnknownError() {
                handleAllChampionsFailure(null);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                handleAllChampionsFailure(null);
            }

            @Override
            public void onFailure(String errorMessage) {
                handleAllChampionsFailure(errorMessage);
            }

            @Override
            public void onNetworkError() {
                handleAllChampionsFailure(null);
            }
        }));
    }

    private void copyAllChampionsToGridArray() {
        gridChampions.clear();
        if(allChampions != null) {
            for (int i = 0; i < allChampions.size(); i++) {
                gridChampions.add(allChampions.get(i));
            }
        }
    }

    private void setAdapter() {
        if (allChampionsGridAdapter == null) {
            allChampionsGridAdapter = new AllChampionsGridAdapter(getContext(), R.layout.row_grid, gridChampions);
            championsGrid.setAdapter(allChampionsGridAdapter);
        } else {
            allChampionsGridAdapter.notifyDataSetChanged();
        }
        dismissProgress();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        noChampsFoundTV.setVisibility(View.GONE);
        if (s.length() >= 2) {
            gridChampions.clear();
            for (ChampionDto c : allChampions) {
                if (Utils.containsIgnoreCase(c.getName(), String.valueOf(s))) {
                    gridChampions.add(c);
                }
            }
            setAdapter();
            if (gridChampions.size() <= 0) {
                noChampsFoundTV.setVisibility(View.VISIBLE);
            } else {
                noChampsFoundTV.setVisibility(View.GONE);
            }
        } else {
            copyAllChampionsToGridArray();
            setAdapter();
        }
    }

    private void hideKeyboard() {
        try {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        hideKeyboard();
        ChampionDto c = (ChampionDto) championsGrid.getItemAtPosition(position);
        int champId = c.getId();
        ChampionDetailFragment fragment = new ChampionDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ChampionDetailFragment.EXTRA_CHAMPION_ID, champId);
        args.putString(ChampionDetailFragment.EXTRA_CHAMPION_IMAGE_URL, c.getImage().getFull());
        args.putString(ChampionDetailFragment.EXTRA_CHAMPION_NAME, c.getKey());
        fragment.setArguments(args);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Commons.setAnimation(ft, Commons.ANIM_OPEN_FROM_RIGHT_WITH_POPSTACK);
        ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.CHAMPION_DETAILS_FRAGMENT).commit();
    }

    private void handleAllChampionsFailure(String errorMessage) {
        if (Utils.isValidString(errorMessage)) {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }
        showRetryView();
    }

    @Override
    protected void retry() {
        super.retry();
        setAdapter();
    }

    public void updateGridChampions(ChampionListDto championListDto) {
        if (championListDto != null && championListDto.getData() != null && championListDto.getData().size() > 0) {
            try {
                gridChampions.clear();
                Map<String, ChampionDto> data = championListDto.getData();

                for (Map.Entry<String, ChampionDto> entry : data.entrySet()) {
                    String key = entry.getKey();
                    String imageUrl = Commons.CHAMPION_IMAGE_BASE_URL + key + ".png";
                    ChampionDto c = new ChampionDto();
                    ImageDto imageDto = new ImageDto();
                    imageDto.setFull(imageUrl);
                    c.setImage(imageDto);
                    c.setName(entry.getValue().getName());
                    c.setId(entry.getValue().getId());
                    c.setKey(entry.getValue().getKey());
                    c.setTitle("\"" + entry.getValue().getTitle() + "\"");
                    gridChampions.add(c);
                }
                if (gridChampions != null) {
                    Collections.sort(gridChampions, new Comparator<ChampionDto>() {
                        @Override
                        public int compare(ChampionDto c1, ChampionDto c2) {
                            return c1.getName().compareTo(c2.getName());
                        }
                    });
                }
            } catch (Exception ignored) {
            }
        }
    }

    private List<ChampionDto> getAllChampionsList() {
        if(allChampions == null || allChampions.size() == 0) {
            allChampions = DbHelper.getInstance().getAllChampionsData();
            if(allChampions == null) {
                allChampions = new ArrayList<>();
            }
        }
        return allChampions;
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onResume() {
        super.onResume();
        reportGoogleAnalytics();
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("AllChampionsFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
