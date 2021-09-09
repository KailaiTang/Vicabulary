package com.laioffer.vicabulary.ui.playback;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.laioffer.vicabulary.model.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class DictionaryRequest extends AsyncTask<String, Integer, String> {
//    Context context;
    private Word word;
    private PlaybackFragment playbackFragment;


//    DictionaryRequest(Context context, TextView tV) {
//        this.context = context;
//    }
    DictionaryRequest(Word word, PlaybackFragment playbackFragment) {
        this.word = word;
        this.playbackFragment = playbackFragment;
    }


    @Override
    protected String doInBackground(String... params) {

        //TODO: replace with your own app id and app key
        final String app_id = "d7d9834f";
        final String app_key = "281c59791f31282e514cc60c2d88d781";
        try {
            URL url = new URL(params[0]);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("app_id",app_id);
            urlConnection.setRequestProperty("app_key",app_key);

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            return stringBuilder.toString();

        }
        catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.v("Result of Dictionary", "onPostExecute" + result);

        try {
            JSONObject js = new JSONObject(result);
            JSONArray results = js.getJSONArray("results");

            JSONObject lEntries = results.getJSONObject(0);
            JSONArray laArray = lEntries.getJSONArray("lexicalEntries");

            JSONObject entries = laArray.getJSONObject(0);
            JSONArray e = entries.getJSONArray("entries");

            JSONObject jsonObject = e.getJSONObject(0);
            JSONArray sensesArray = jsonObject.getJSONArray("senses");

            JSONObject de = sensesArray.getJSONObject(0);
            JSONArray d = de.getJSONArray("definitions");

            String def = d.getString(0);

            // Bind dictionary explanation to the word object
            word.setExplanation(def);

            // Setup transaction
            FragmentManager fragmentManager = playbackFragment.getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment prev = fragmentManager.findFragmentByTag("word");
            if (prev != null) {
                fragmentTransaction.remove(prev);
            }

            // Display captured word and its definition in word dialog
            WordDialog wordDialog = WordDialog.newInstance(1, word);
            wordDialog.show(fragmentTransaction, "word");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

