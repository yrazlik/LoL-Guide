package com.yrazlik.loltr.adapters;

import java.util.List;

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
import android.widget.TextView;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Spell;
import com.yrazlik.loltr.fragments.ChampionAbilitiesVideosFragment;
import com.yrazlik.loltr.view.RemoteImageView;

public class ChampionSpellsListAdapter extends ArrayAdapter<Spell> implements
		OnClickListener {

	private Context mContext;
	private Typeface typeFace;

	public ChampionSpellsListAdapter(Context context, int resource,
			List<Spell> objects) {
		super(context, resource, objects);
		this.mContext = context;
		typeFace = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/dinproregular.ttf");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(R.layout.list_row_abilities, parent,
					false);

			holder = new ViewHolder();
			holder.spellImage = (RemoteImageView) convertView
					.findViewById(R.id.imageViewSpellImage);
			holder.spellTitle = (TextView) convertView
					.findViewById(R.id.textViewSpellTitle);
			holder.spellBody = (TextView) convertView
					.findViewById(R.id.textViewSpellBody);
			holder.spellKey = (TextView) convertView
					.findViewById(R.id.textViewSpellKey);
			holder.playVideoButton = (ImageButton) convertView
					.findViewById(R.id.imageButtonPlayVideo);

            holder.textViewVideo = (TextView) convertView.findViewById(R.id.textViewVideo);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Spell championSpell = getItem(position);
		holder.spellTitle.setText(championSpell.getName());
		holder.spellBody.setText(championSpell.getSanitizedDescription());
		holder.spellKey.setText(championSpell.getSpellKey());
		holder.spellImage.setLocalURI(null);
		if (championSpell.getName().contains("Pasif")) {
			holder.spellImage
					.setRemoteURI(Commons.CHAMPION_PASSIVE_IMAGE_BASE_URL
							+ championSpell.getImage().getFull());
		} else {
			holder.spellImage
					.setRemoteURI(Commons.CHAMPION_SPELL_IMAGE_BASE_URL
							+ championSpell.getImage().getFull());
		}
		holder.spellImage.loadImage();
		holder.spellKey.setTypeface(typeFace);
		holder.spellBody.setTypeface(typeFace);
		holder.spellTitle.setTypeface(typeFace);
		holder.playVideoButton.setTag(position);
		holder.playVideoButton.setOnClickListener(this);
        holder.textViewVideo.setTag(position);
        holder.textViewVideo.setOnClickListener(this);

		return convertView;

	}

	static class ViewHolder {
		public RemoteImageView spellImage;
		public TextView spellTitle;
		public TextView spellBody;
		public TextView spellKey;
		public ImageButton playVideoButton;
        public TextView textViewVideo;
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.imageButtonPlayVideo || v.getId() == R.id.textViewVideo) {
			int key = (Integer) v.getTag();
			ChampionAbilitiesVideosFragment fragment = new ChampionAbilitiesVideosFragment();
			Bundle args = new Bundle();
			args.putInt(ChampionAbilitiesVideosFragment.EXTRA_PRESSED_KEY_POSITION, key);
			fragment.setArguments(args);
			fragment.show(((FragmentActivity)getContext()).getSupportFragmentManager(), "");
		}

	}

}