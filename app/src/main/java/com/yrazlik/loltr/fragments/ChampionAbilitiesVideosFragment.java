package com.yrazlik.loltr.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;

public class ChampionAbilitiesVideosFragment extends DialogFragment {

    public static String EXTRA_KEY_POSITION = "position";
    public static String EXTRA_CHAMP_ID = "champId";
    private int champId;
    private int abilityNumber;

    private VideoView videoView;
    private Button closeDialogButton;
    private ProgressBar progress;
    private View blackBg;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.DialogFullScreenThemeTransparentBg) {
            @Override
            public void onBackPressed() {
                if (videoView != null) {
                    videoView.setBackgroundColor(getResources().getColor(R.color.black));
                    try {
                        videoView.stopPlayback();
                        videoView = null;
                    } catch (Exception e) {
                        videoView = null;
                    }
                }

                if(blackBg != null) {
                    blackBg.setVisibility(View.VISIBLE);
                }
                dismiss();
            }
        };
        dialog.setContentView(R.layout.fragment_abilities_videos);

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialogAnimation;

        initUI(dialog);
        progress.setVisibility(View.VISIBLE);
        videoView.start();
        videoView.requestFocus();

        return dialog;
    }

    private void initUI(Dialog d) {
        blackBg = d.findViewById(R.id.blackBg);
        progress = (ProgressBar) d.findViewById(R.id.progress);
        videoView = (VideoView) d.findViewById(R.id.videoView);
        closeDialogButton = (Button) d.findViewById(R.id.closeDialogButton);
        Bundle args = getArguments();
        champId = args.getInt(EXTRA_CHAMP_ID);
        abilityNumber = args.getInt(EXTRA_KEY_POSITION);
        String sAbilityNumber = String.valueOf(abilityNumber + 1);
        sAbilityNumber = makeTwoDigit(sAbilityNumber);

        MediaController mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                progress.setVisibility(View.GONE);
                if (closeDialogButton != null) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            blackBg.setVisibility(View.GONE);
                        }
                    }, 500);
                    closeDialogButton.setVisibility(View.VISIBLE);
                }

            }
        });
        videoView.setOnErrorListener(new OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                progress.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getResources().getString(R.string.no_video_found), Toast.LENGTH_LONG).show();
                dismiss();
                return true;
            }
        });
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getDialog().onBackPressed();
            }
        });
        videoView.setVideoURI(Uri.parse(Commons.CHAMPION_ABILITIES_VIDEOS_BASE_URL
                + makeFourDigit(String.valueOf(champId)) + "_" + sAbilityNumber + ".mp4"));
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        progress.setVisibility(View.GONE);
        super.onDismiss(dialog);
    }

    @Override
    public void onResume() {
        super.onResume();
        reportGoogleAnalytics();
    }

    protected void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("ChampionAbilitiesVideosFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
