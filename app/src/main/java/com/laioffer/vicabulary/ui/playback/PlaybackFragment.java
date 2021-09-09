package com.laioffer.vicabulary.ui.playback;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.laioffer.vicabulary.R;
import com.laioffer.vicabulary.databinding.FragmentPlaybackBinding;
import com.laioffer.vicabulary.model.Movie;
import com.laioffer.vicabulary.model.Word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.google.android.exoplayer2.Player.STATE_ENDED;

public class PlaybackFragment extends Fragment implements ActionMode.Callback {

    private SimpleExoPlayerView simpleExoPlayerView;
    private FragmentPlaybackBinding binding;

    private TextView srtText;
    //create exo player
    SimpleExoPlayer player;
    private int resumeWindow;
    private long resumePosition;
    private String videoPath;
    private String srtPath;
    private TextView showDef;
    private long time = 0;
    String url;
    String TAG = "wordcapture";

    final Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                srtText.setText("" + msg.obj);
            }
        }
    };


    //public static String videoUrl = "https://vicabulary-video.s3.us-east-2.amazonaws.com/Avengers/Lifting+Thors+Hammer+-+Avengers+Age+of+Ultron+(2015)+Movie+CLIP+HD.mp4";

    //public static String srtUrl = "https://vicabulary-video.s3.us-east-2.amazonaws.com/Avengers/Lifting+Thors+Hammer+-+Avengers+Age+of+Ultron+(2015)+Movie+CLIP+HD.en.srt";

    //public static ArrayList<SubtitlesModel> list = new ArrayList<SubtitlesModel>();
    public ArrayList<SubtitlesModel> list = new ArrayList<SubtitlesModel>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        binding = FragmentPlaybackBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (getArguments() == null) {
            return;
        }


        Movie movie = PlaybackFragmentArgs.fromBundle(getArguments()).getMovie();
        if (movie == null) {
            time = getArguments().getLong("time");
            videoPath = getArguments().getString("videoPath");
            srtPath =  getArguments().getString("srtPath");
            Log.d("timeStamp",String.valueOf(time));
        }
        else {
            // display movie name and publisher
            binding.title1.setText(movie.getName());
            binding.publisher1.setText(movie.getPublisher());
            //srtText = view.findViewById(R.id.srt_txt);
            videoPath = movie.getClip();
            srtPath = movie.getSubtitle();
        }
        srtText = binding.srtTxt;
        simpleExoPlayerView = binding.simpleExoPlayerView;
        //simpleExoPlayerView = view.findViewById(R.id.simpleExoPlayerView);
        initExoplayer(videoPath);
        new Thread(new Runnable() {
            @Override
            public void run() {
                readFile(srtPath);
                Log.d("INFO", "Subtitle size: " + list.size() + list.get(0).context);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (player != null) {
                    for (SubtitlesModel subtitle : list) {
                        if (subtitle.end > player.getCurrentPosition() && resumePosition != player.getCurrentPosition()) {
                            resumePosition = player.getCurrentPosition();
                            Message message = new Message();
                            message.what = 1;
                            message.obj = subtitle.context;
                            mainHandler.sendMessage(message);
                            break;
                        }
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void initExoplayer(String videoUrl) {
        // create player
        player = ExoPlayerFactory.newSimpleInstance(this.getContext());
        // create data source
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this.getContext(),
                Util.getUserAgent(this.getContext(), "test"));
        // create media source
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(videoUrl));
//        player.prepare(videoSource);
//        simpleExoPlayerView.setPlayer(player);
        //player.seekTo(time);
        // player.setPlayWhenReady(true);
        Log.d("TIME",String.valueOf(time));
        //  boolean ready = player.getPlayWhenReady();
        player.seekTo(time);
        player.prepare(videoSource);
        simpleExoPlayerView.setPlayer(player);
        Log.d("Current",String.valueOf( player.getCurrentPosition()));
        player.setPlayWhenReady(true);


        initializeUI();


        // Log.d("TIME",String.valueOf(time));
        //  player.seekTo(time);

        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == STATE_ENDED) {
                    FinishDialog dialog = new FinishDialog();

                    dialog.setPlayListener(new FinishDialog.PlayListener() {
                        @Override
                        public void replay() {

                        }

                        @Override
                        public void seekTo(int position) {
                            player.seekTo(0);
                        }
                    });
                    dialog.show(getParentFragmentManager(), "");
                }
            }
        });

        //startPlayer();
    }

//    private void startPlayer() {
//        simpleExoPlayerView.setPlayer(player);
//       //player.seekTo(time);
//       // player.setPlayWhenReady(true);
//        Log.d("TIME",String.valueOf(time));
//
//        player.seekTo(time);
//
//        initializeUI();
//
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // release source
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (player != null && player.getCurrentPosition() > 0) {

            player.setPlayWhenReady(true);
            player.seekTo(resumePosition);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null && player.getPlayWhenReady()) {
            resumeWindow = player.getCurrentWindowIndex();
            resumePosition = Math.max(0, player.getContentPosition());
            player.setPlayWhenReady(false);
        }
    }


    /**
     * read file
     *
     * @param path
     */
    public void readFile(String srtUrl) {
        Log.d("INFO", "Start download");
        String line;
        Pattern pattern = Pattern.compile("[0-9]+");
        /**
         * read file
         */
        try {
            URL url = new URL(srtUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            line = reader.readLine();
            while (line != null) {
                Log.d("INFO", "read" + line);
                SubtitlesModel sm = new SubtitlesModel();
                // find node number
                if (pattern.matcher(line).matches()) {
                    // get node
                    sm.node = Integer.parseInt(line);
                    line = reader.readLine();
                    // get start time
                    sm.star = getTime(line.substring(0, 12));
                    // get end time
                    sm.end = getTime(line.substring(17, 29));

                    if (sm.end < sm.star) {
                        // last subtitle
                        sm.end = Integer.MAX_VALUE;
                    }
                    // get subtitle
                    sm.context = reader.readLine();
                    list.add(sm);
                }
                line = reader.readLine();
                while (reader.ready() && !pattern.matcher(line).matches()) {
                    if (line.isEmpty()) break;
                    sm.context += " " + line;
                    line = reader.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("INFO", "ENd download");
    }


    private final static int oneSecond = 1000;

    private final static int oneMinute = 60 * oneSecond;

    private final static int oneHour = 60 * oneMinute;

    /**
     * @param line
     * @return time
     * @descraption parse time to integer
     */
    private static int getTime(String line) {
        try {
            return Integer.parseInt(line.substring(0, 2)) * oneHour// 时
                    + Integer.parseInt(line.substring(3, 5)) * oneMinute// 分
                    + Integer.parseInt(line.substring(6, 8)) * oneSecond// 秒
                    + Integer.parseInt(line.substring(9, line.length()));// 毫秒
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return -1;
    }


    private void initializeUI() {
        if (srtText != null) {
            Log.d("show srt", "setText is not null");
            srtText.setTextIsSelectable(true);
            //srtText.setCustomSelectionActionModeCallback (new SelectWordCallback());
            srtText.setCustomSelectionActionModeCallback(this);
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        Log.d(TAG, "onCreateActionMode");
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.lookup, menu);
        menu.removeItem(android.R.id.selectAll);
        return true;

    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        Log.d(TAG, String.format("onActionItemClicked item=%s/%d", item.toString(), item.getItemId()));
        CharacterStyle cs;
        int start = srtText.getSelectionStart();
        int end = srtText.getSelectionEnd();
        SpannableStringBuilder ssb = new SpannableStringBuilder(srtText.getText());

        switch (item.getItemId()) {

            case R.id.search_dic:
                CharSequence selectWord = ssb.subSequence(start, end);
                String wordString = selectWord.toString();
                Log.d("Capture words", wordString);


                //Todo: pass selectString to call the movie API, selectString is the word
                Word word = new Word();
                word.setWord(wordString);
                word.setTime(player.getCurrentPosition());
                word.setVideoPath(videoPath);
                word.setSrtPath(srtPath);

                // Look up dictionary, prompt word dialog
                // and save word to database per user input
                getDefinition(word);

                return true;
        }
        return false;
    }

    private void getDefinition(Word word) {
        DictionaryRequest dR = new DictionaryRequest(word, this);
        url = dictionaryEntries(word.getWord());
        dR.execute(url);
    }

    private String dictionaryEntries(String capturedWord) {
        final String language = "en-gb";
        final String word = capturedWord;
        final String fields = "definitions";
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/"
                + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}
