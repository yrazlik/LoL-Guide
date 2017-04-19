package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.pkmmte.view.CircularImageView;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Spell;
import com.yrazlik.loltr.fragments.ChampionAbilitiesVideosFragment;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.List;

public class ChampionSpellsListAdapter extends ArrayAdapter<Spell> implements
		OnClickListener {

	private Context mContext;
    private int champId;

	public ChampionSpellsListAdapter(Context context, int resource, List<Spell> objects, int champId) {
		super(context, resource, objects);
		this.mContext = context;
        this.champId = champId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(R.layout.list_row_abilities, parent,
					false);

			holder = new ViewHolder();
			holder.spellImage = (CircularImageView) convertView
					.findViewById(R.id.imageViewSpellImage);
			holder.spellTitle = (RobotoTextView) convertView
					.findViewById(R.id.textViewSpellTitle);
			holder.spellBody = (RobotoTextView) convertView
					.findViewById(R.id.textViewSpellBody);

            holder.textViewVideo = (RobotoTextView) convertView.findViewById(R.id.textViewVideo);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.imageProgress);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Spell championSpell = getItem(position);
        String spellKey = championSpell.getSpellKey();
        if(spellKey != null && spellKey.trim().toString().length() > 0) {
            spellKey = spellKey + " : ";
        } else {
            spellKey = "";
        }
		holder.spellTitle.setText(spellKey + championSpell.getName());
		holder.spellBody.setText(championSpell.getSanitizedDescription());

		if (championSpell.getName().contains(getContext().getResources().getString(R.string.passive))) {
            LolImageLoader.getInstance().loadImage(Commons.CHAMPION_PASSIVE_IMAGE_BASE_URL
                    + championSpell.getImage().getFull(), holder.spellImage);
		} else {
            LolImageLoader.getInstance().loadImage(Commons.CHAMPION_SPELL_IMAGE_BASE_URL
                    + championSpell.getImage().getFull(), holder.spellImage);
		}

        holder.textViewVideo.setTag(position);
        holder.textViewVideo.setOnClickListener(this);

		return convertView;

	}

	static class ViewHolder {
		public CircularImageView spellImage;
		public RobotoTextView spellTitle;
		public RobotoTextView spellBody;
        public RobotoTextView textViewVideo;
        public ProgressBar progress;
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.textViewVideo) {
			int key = (Integer) v.getTag();
			ChampionAbilitiesVideosFragment fragment = new ChampionAbilitiesVideosFragment();
			Bundle args = new Bundle();
            args.putInt(ChampionAbilitiesVideosFragment.EXTRA_CHAMP_ID, champId);
			args.putInt(ChampionAbilitiesVideosFragment.EXTRA_KEY_POSITION, key);
			fragment.setArguments(args);
			fragment.show(((FragmentActivity)getContext()).getSupportFragmentManager(), "");
		}
	}
}