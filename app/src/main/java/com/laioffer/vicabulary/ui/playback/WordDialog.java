package com.laioffer.vicabulary.ui.playback;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import com.laioffer.vicabulary.MainActivity;
import com.laioffer.vicabulary.R;
import com.laioffer.vicabulary.database.DatabaseHelper;
import com.laioffer.vicabulary.model.Word;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;

public class WordDialog extends DialogFragment {
    private SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer player;
    public DatabaseHelper db;
    int time;
    private Word word;
    String TAG = "createdialog";

    /*public static WordDialog newInstance(int videoViewId) {
        WordDialog wd = new WordDialog();
        Bundle args = new Bundle();
        args.putInt("video_view_id", videoViewId);
        wd.setArguments(args);
        return wd;
        //TODO : stop video when capture a word
    }*/

    public static WordDialog newInstance(int arg, Word word) {
        WordDialog wordDialog = new WordDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("count", arg);
        wordDialog.setArguments(bundle);
        wordDialog.setWord(word);
        return wordDialog;
    }

    private void setWord(Word word) {
        this.word = word;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //get word from playback

        super.onCreateDialog(savedInstanceState);
        setRetainInstance(true);
        int vwId = getArguments().getInt("video_view_id");
        simpleExoPlayerView = getActivity().findViewById(R.id.simpleExoPlayerView);
        db = MainActivity.vDb;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        builder.setIcon(R.mipmap.ic_launcher);
        //TODO : Capture word as a title
        builder.setTitle(word.getWord());

        //TODO : put your explanation in setMessage
        builder.setMessage(word.getExplanation());


        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //TODO : save to word database
                saveWord(word);

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
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
      //  dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_lightbulb_outline_black_24dp);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.BOTTOM;
       // wmlp.x = 100;   //x position
        wmlp.y = 120;   //y position

        return dialog;
    }

    private void saveWord(Word word){
        if(db.getWord(word.getWord()).getWord() == null) {
//            int time = simpleExoPlayerView.;
            db.insertWord(word.getWord(), word.getTime(), word.getExplanation(), word.getVideoPath(), word.getSrtPath());
            String res = db.getAllWord().get(0).toString();
            Log.d("saveWord", res);
//            Log.d("data",String.valueOf(time));
        }
    }

}
