package com.yrazlik.loltr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.data.Game;
import com.yrazlik.loltr.data.Stats;
import com.yrazlik.loltr.data.SummonerSpell;
import com.yrazlik.loltr.service.ServiceRequest;
import com.yrazlik.loltr.view.FadeInNetworkImageView;
import com.yrazlik.loltr.view.RobotoTextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by yrazlik on 22/04/16.
 */
public class MatchHistoryRVAdapter extends RecyclerView.Adapter<MatchHistoryRVAdapter.ViewHolder>{

    private long summonerId;
    private List<Game> games;
    private int rowLayoutId;
    private Context mContext;
    private RecyclerViewClickListener recyclerViewClickListener;
    private DisplayImageOptions mOptions;

    public interface RecyclerViewClickListener{
        void onRecyclerViewItemClicked(Game clickedItem, int position);
    }

    public MatchHistoryRVAdapter(Context context, List<Game> games,  int rowLayoutId, RecyclerViewClickListener listener){
        this.mContext = context;
        this.games = games;
        this.rowLayoutId = rowLayoutId;
        this.recyclerViewClickListener = listener;
        mOptions = new DisplayImageOptions.Builder().resetViewBeforeLoading(true).showImageOnLoading(R.drawable.nothing).showImageForEmptyUri(R.drawable.nothing)
                .showImageOnFail(R.drawable.nothing).cacheOnDisk(true).cacheInMemory(true)
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayoutId, parent, false);
        ViewHolder holder = new ViewHolder(v, new ViewHolder.ViewHolderClickListener() {
            @Override
            public void onItemClick(View caller, int position) {
                if(position >= 0 && position < games.size()) {
                    recyclerViewClickListener.onRecyclerViewItemClicked(games.get(position), position);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position < games.size()) {
            Game game = games.get(position);
            if (game != null) {

                String gameMode = game.getGameMode();
                String gameType = game.getGameType();
                String subType = game.getSubType();

                String gameModeText = getGameModeText(gameMode);
                String gameTypeText = getGameTypeText(gameType, subType);

                holder.matchModeTV.setText(gameModeText);
                holder.matchTypeTV.setText(gameTypeText);

                int spell1 = game.getSpell1();
                int spell2 = game.getSpell2();

                if (Commons.allSpells != null) {
                    String spell1Name = null, spell2Name = null;
                    for (SummonerSpell sp : Commons.allSpells) {
                        if (sp.getId() == spell1) {
                            if (sp.getImage() != null) {
                                spell1Name = sp.getImage().getFull();
                            }
                        }

                        if (sp.getId() == spell2) {
                            if (sp.getImage() != null) {
                                spell2Name = sp.getImage().getFull();
                            }
                        }
                    }

                    LolImageLoader.getInstance().loadImage(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell1Name, holder.spell1);
                    LolImageLoader.getInstance().loadImage(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell2Name, holder.spell2);

                }

                long createDate = game.getCreateDate();

                Calendar c = Calendar.getInstance();
                c.setTime(new Date(createDate));
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                holder.matchDateTV.setText(dateFormat.format(c.getTime()));

                int championID = game.getChampionId();
                String champImageUrl = null;
                if (Commons.allChampions != null && Commons.allChampions.size() > 0) {
                    for (Champion champ : Commons.allChampions) {
                        if (champ.getId() == championID) {
                            champImageUrl = Commons.CHAMPION_IMAGE_BASE_URL + champ.getKey() + ".png";
                            break;
                        }
                    }
                }

                LolImageLoader.getInstance().loadImage(champImageUrl, holder.champIV);

                Stats stats = game.getStats();
                if (stats != null) {
                    if (stats.isWin()) {
                        holder.winLoseLabel.setBackgroundColor(mContext.getResources().getColor(R.color.discount_green));
                    } else {
                        holder.winLoseLabel.setBackgroundColor(mContext.getResources().getColor(R.color.material_dark_red));
                    }

                    holder.levelTV.setText(stats.getLevel() + "");
                    holder.kdaTV.setText(stats.getChampionsKilled() + " / " + stats.getNumDeaths() + " / " + stats.getAssists());
                    holder.goldTV.setText(format(stats.getGoldEarned()));
                    String timeString = convertSecondsToHoursMinutes(stats.getTimePlayed());
                    if (timeString != null) {
                        holder.matchTimeTV.setText(timeString);
                    }

                    int item0 = stats.getItem0();
                    int item1 = stats.getItem1();
                    int item2 = stats.getItem2();
                    int item3 = stats.getItem3();
                    int item4 = stats.getItem4();
                    int item5 = stats.getItem5();
                    int item6 = stats.getItem6();

                    if(item0 != 0) {
                        LolImageLoader.getInstance().loadImage(Commons.ITEM_IMAGES_BASE_URL + item0 + ".png", holder.item0, mOptions);
                    } else {
                        LolImageLoader.getInstance().loadImage("", holder.item0, mOptions);
                    }

                    if (item1 != 0) {
                        LolImageLoader.getInstance().loadImage(Commons.ITEM_IMAGES_BASE_URL + item1 + ".png", holder.item1, mOptions);
                    } else {
                        LolImageLoader.getInstance().loadImage("", holder.item1, mOptions);
                    }

                    if (item2 != 0) {
                        LolImageLoader.getInstance().loadImage(Commons.ITEM_IMAGES_BASE_URL + item2 + ".png", holder.item2, mOptions);
                    } else {
                        LolImageLoader.getInstance().loadImage("", holder.item2, mOptions);
                    }
                    if (item3 != 0) {
                        LolImageLoader.getInstance().loadImage(Commons.ITEM_IMAGES_BASE_URL + item3 + ".png", holder.item3, mOptions);
                    } else {
                        LolImageLoader.getInstance().loadImage("", holder.item3, mOptions);
                    }
                    if (item4 != 0) {
                        LolImageLoader.getInstance().loadImage(Commons.ITEM_IMAGES_BASE_URL + item4 + ".png", holder.item4, mOptions);
                    } else {
                        LolImageLoader.getInstance().loadImage("", holder.item4, mOptions);
                    }
                    if (item5 != 0) {
                        LolImageLoader.getInstance().loadImage(Commons.ITEM_IMAGES_BASE_URL + item5 + ".png", holder.item5, mOptions);
                    } else {
                        LolImageLoader.getInstance().loadImage("", holder.item5, mOptions);
                    }
                    if (item6 != 0) {
                        LolImageLoader.getInstance().loadImage(Commons.ITEM_IMAGES_BASE_URL + item6 + ".png", holder.item6, mOptions);
                    } else {
                        LolImageLoader.getInstance().loadImage("", holder.item6, mOptions);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return games == null ? 0 : games.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RelativeLayout winLoseLabel;
        private ImageView champIV;
        private RobotoTextView levelTV;
        private RobotoTextView matchTypeTV;
        private RobotoTextView matchModeTV;
        private RobotoTextView kdaTV;
        private RobotoTextView goldTV;
        private RobotoTextView matchTimeTV;
        private RobotoTextView matchDateTV;
        private ImageView spell1;
        private ImageView spell2;
        private ImageView item0;
        private ImageView item1;
        private ImageView item2;
        private ImageView item3;
        private ImageView item4;
        private ImageView item5;
        private ImageView item6;
        public ViewHolderClickListener mItemClickListener;

        public ViewHolder(View itemView, ViewHolderClickListener clickListener) {
            super(itemView);
            this.winLoseLabel = (RelativeLayout) itemView.findViewById(R.id.winLoseLabel);
            this.champIV = (ImageView) itemView.findViewById(R.id.champIV);
            this.levelTV = (RobotoTextView) itemView.findViewById(R.id.levelTV);
            this.matchTypeTV = (RobotoTextView) itemView.findViewById(R.id.matchTypeTV);
            this.matchModeTV = (RobotoTextView) itemView.findViewById(R.id.matchModeTV);
            this.kdaTV = (RobotoTextView) itemView.findViewById(R.id.kdaTV);
            this.goldTV = (RobotoTextView) itemView.findViewById(R.id.goldTV);
            this.matchTimeTV = (RobotoTextView) itemView.findViewById(R.id.matchTimeTV);
            this.matchDateTV = (RobotoTextView) itemView.findViewById(R.id.matchDateTV);
            this.spell1 = (ImageView) itemView.findViewById(R.id.spell1);
            this.spell2 = (ImageView) itemView.findViewById(R.id.spell2);
            this.item0 = (ImageView) itemView.findViewById(R.id.item0);
            this.item1 = (ImageView) itemView.findViewById(R.id.item1);
            this.item2 = (ImageView) itemView.findViewById(R.id.item2);
            this.item3 = (ImageView) itemView.findViewById(R.id.item3);
            this.item4 = (ImageView) itemView.findViewById(R.id.item4);
            this.item5 = (ImageView) itemView.findViewById(R.id.item5);
            this.item6 = (ImageView) itemView.findViewById(R.id.item6);
            this.mItemClickListener = clickListener;
            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getAdapterPosition());
        }

        public interface ViewHolderClickListener {
            public void onItemClick(View caller, int position);
        }
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = ((truncated / 10d) != (truncated / 10));
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public String convertSecondsToHoursMinutes(int totalSecs){
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        String timeString = null;
        try {
            if(hours == 0) {
                timeString = String.format("%02d:%02d", minutes, seconds);
            }else{
                timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            }
        }catch (Exception e){
            timeString = null;
        }
        return timeString;
    }

    private String getGameModeText(String gameMode) {
        String gameTypeText = "";
        if (gameMode != null && (gameMode.length() > 0)) {
            if (gameMode.equalsIgnoreCase("classic")) {
                gameTypeText += mContext.getResources().getString(R.string.summoners_rift);
            } else if (gameMode.equalsIgnoreCase("odin")) {
                gameTypeText += mContext.getResources().getString(R.string.dominion);
            } else if (gameMode.equalsIgnoreCase("aram")) {
                gameTypeText += mContext.getResources().getString(R.string.aram);
            } else if (gameMode.equalsIgnoreCase("tutorial")) {
                gameTypeText += mContext.getResources().getString(R.string.tutorial);
            } else if (gameMode.equalsIgnoreCase("oneforall")) {
                gameTypeText += mContext.getResources().getString(R.string.oneforall);
            } else if (gameMode.equalsIgnoreCase("ascension")) {
                gameTypeText += mContext.getResources().getString(R.string.ascension);
            } else if (gameMode.equalsIgnoreCase("firstblood")) {
                gameTypeText += mContext.getResources().getString(R.string.firstblood);
            } else if (gameMode.equalsIgnoreCase("kingporo")) {
                gameTypeText += mContext.getResources().getString(R.string.kingporo);
            }
        }
        return gameTypeText;
    }

    private String getGameTypeText(String gameType, String subType){
        String gameTypeText = "";
        if(gameType != null && gameType.length() > 0){
            if(gameType.equalsIgnoreCase("custom_game")){
                gameTypeText += mContext.getResources().getString(R.string.custom_game);
            }else if(gameType.equalsIgnoreCase("matched_game")){
                if(subType != null) {
                    if (subType.equalsIgnoreCase("none")) {
                        gameTypeText += mContext.getResources().getString(R.string.none);
                    }else if (subType.contains("normal") || subType.contains("NORMAL")) {
                        gameTypeText += mContext.getResources().getString(R.string.normal);
                    }else if(subType.contains("odin") || subType.contains("ODIN")){
                        gameTypeText += mContext.getResources().getString(R.string.odin);
                    }else if(subType.contains("aram") || subType.contains("ARAM")){
                        gameTypeText += mContext.getResources().getString(R.string.aram_aram);
                    }else if(subType.equalsIgnoreCase("bot")){
                        gameTypeText += mContext.getResources().getString(R.string.bot);
                    }else if(subType.contains("oneforall") || subType.contains("ONEFORALL")){
                        gameTypeText += mContext.getResources().getString(R.string.oneforall);
                    }else if(subType.contains("firstblood") || subType.contains("FIRSTBLOOD")){
                        gameTypeText += mContext.getResources().getString(R.string.firstblood);
                    }else if(subType.equalsIgnoreCase("SR_6x6")){
                        gameTypeText += mContext.getResources().getString(R.string.sr6);
                    }else if(subType.equalsIgnoreCase("CAP_5x5")){
                        gameTypeText += mContext.getResources().getString(R.string.cap5);
                    }else if(subType.equalsIgnoreCase("urf")){
                        gameTypeText += mContext.getResources().getString(R.string.urf);
                    }else if(subType.equalsIgnoreCase("nightmare_bot")){
                        gameTypeText += mContext.getResources().getString(R.string.nightmare);
                    }else if(subType.equalsIgnoreCase("ascension")){
                        gameTypeText += mContext.getResources().getString(R.string.ascension);
                    }else if(subType.equalsIgnoreCase("hexakill")){
                        gameTypeText += mContext.getResources().getString(R.string.sr6);
                    }else if(subType.equalsIgnoreCase("king_poro")){
                        gameTypeText += mContext.getResources().getString(R.string.kingporo);
                    }else if(subType.equalsIgnoreCase("counter_pick")){
                        gameTypeText += mContext.getResources().getString(R.string.counter_pick);
                    }else if(subType.equalsIgnoreCase("bilgewater")){
                        gameTypeText += mContext.getResources().getString(R.string.bilgewater);
                    }else if (subType.contains("ranked") || subType.contains("RANKED")) {
                        gameTypeText += mContext.getResources().getString(R.string.ranked);
                    }else{
                        gameTypeText += mContext.getResources().getString(R.string.unknown);
                    }
                }
            }else if(gameType.equalsIgnoreCase("tutorial_game")){
                gameTypeText += mContext.getResources().getString(R.string.tutorial);
            }
        }
        return gameTypeText;
    }
}
