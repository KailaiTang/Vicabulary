package com.laioffer.vicabulary.ui.playback;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.laioffer.vicabulary.R;

public class FinishDialog extends DialogFragment {
    private SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer player;
    PlayListener playListener;

    interface PlayListener {
        void replay();
        void seekTo(int position);
    }

    public void setPlayListener(PlayListener playListener) {
        this.playListener = playListener;
    }

    public  FinishDialog newInstance() {
        FinishDialog fd = new FinishDialog();
        Bundle args = new Bundle();
        //args.putInt("video_view_id", videoViewId);
        fd.setArguments(args);
        return fd;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //int vwId = getArguments().getInt("video_view_id");
        //simpleExoPlayerView = getActivity().findViewById(R.id.simpleExoPlayerView);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        builder.setTitle("Message");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Watch again?")
                .setTitle("Let's review!");
        builder.setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (playListener != null) {
                    playListener.seekTo(0);
                }

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(id == -1) {
                    getDialog().dismiss();
                }
            }
        });
        final AlertDialog dialog = builder.create();
        return dialog;
    }


}