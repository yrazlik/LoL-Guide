package com.yrazlik.loltr.fragments;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.service.ServiceRequest;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class ChampionAbilitiesVideosFragment extends DialogFragment {

	private VideoView videoView;
	public static String EXTRA_PRESSED_KEY_POSITION = "com.yrazlik.leagueoflegends.fragments.extrapressedkeyposition";
	int abilityNumber;
	Dialog progresDialog;

	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_abilities_videos,
				container, false);
		initUI(v);
		progresDialog.show();
		videoView.start();
		videoView.requestFocus();

		return v;
	}
	
	private void initUI(View v){
		progresDialog = ServiceRequest.showLoading(getActivity(), null);
		videoView = (VideoView)v.findViewById(R.id.videoView);
		Bundle args = getArguments();
		abilityNumber = args.getInt(EXTRA_PRESSED_KEY_POSITION);
		String sAbilityNumber = String.valueOf(abilityNumber + 1);
		sAbilityNumber = makeTwoDigit(sAbilityNumber);

		MediaController mediaController = new MediaController(getActivity());
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);
		videoView.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				progresDialog.dismiss();
				
			}
		});
		videoView.setOnErrorListener(new OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
            	if(progresDialog != null){
            		progresDialog.dismiss();
            	}
                Toast.makeText(getActivity(), "Video yuklenirken bir sorun olustu. Lutfen daha sonra tekrar deneyin", Toast.LENGTH_LONG).show();
                dismiss();
                return true;
            }
        });
		videoView
				.setVideoURI(Uri.parse(Commons.CHAMPION_ABILITIES_VIDEOS_BASE_URL
						+ makeFourDigit(String
								.valueOf(ChampionOverviewFragment.lastSelectedChampionId))
						+ "_" + sAbilityNumber + ".mp4"));
	}

	private String makeTwoDigit(String s) {
		if (s.length() == 1) {
			s = "0" + s;
		}

		return s;
	}

	private String makeFourDigit(String s) {
		if (s.length() == 1) {
			s = "000" + s;
		} else if (s.length() == 2) {
			s = "00" + s;
		} else if (s.length() == 3) {
			s = "0" + s;
		}

		return s;
	}

}
