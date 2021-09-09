package com.laioffer.vicabulary.database;

import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;


import com.laioffer.vicabulary.R;
import com.laioffer.vicabulary.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONResourceReader {

    // === [ Private Data Members ] ============================================

    // Our JSON, in string form.
    private String jsonString;
    private static final String LOGTAG = JSONResourceReader.class.getSimpleName();

    // === [ Public API ] ======================================================

    /**
     * Read from a resources file and create a {@link JSONResourceReader} object that will allow the creation of other
     * objects from this resource.
     *
     * @param resources An application {@link Resources} object.
     * @param id The id for the resource to load, typically held in the raw/ folder.
     */
    public JSONResourceReader(Resources resources) {
        InputStream resourceReader = resources.openRawResource(R.raw.video_metadata);
        Writer writer = new StringWriter();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceReader, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            Log.e(LOGTAG, "Unhandled exception while using JSONResourceReader", e);
        } finally {
            try {
                resourceReader.close();
            } catch (Exception e) {
                Log.e(LOGTAG, "Unhandled exception while using JSONResourceReader", e);
            }
        }

        jsonString = writer.toString();
        Log.d("json", jsonString);
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray metadata = jsonObject.getJSONArray("metadata");
            for (int i = 0; i < metadata.length(); i++) {
                JSONObject jsonMovie = metadata.getJSONObject(i);
                String id = "" + i;
                String name = jsonMovie.getString("name");
                String publisher = jsonMovie.getString("publisher");
                String duration = jsonMovie.getString("duration");
                String clip = jsonMovie.getString("clip");
                String subtitle = jsonMovie.getString("subtitle");
                String cover = jsonMovie.getString("cover");
                movies.add(new Movie(id, name, publisher, duration, clip, subtitle, cover));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
