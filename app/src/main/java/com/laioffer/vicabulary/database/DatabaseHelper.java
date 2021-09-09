package com.laioffer.vicabulary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.laioffer.vicabulary.model.Word;

import java.sql.Time;
import java.util.LinkedList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {



    public static final String DATABASE_NAME = "vocabulary.db";

    public static final String TABLE_NAME = "vocabulary_table";




    

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
   //    db.execSQL("drop table if exists " + TABLE_NAME);

       db.execSQL("create table if not exists " + TABLE_NAME + "(WORD TEXT PRIMARY KEY , TIME INTEGER, EXPLANATION TEXT, VIDEOPATH TEXT, SRTPATH TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }



    
    

    
    public void insertWord(String word, long time, String explanation, String videoPath, String srtPath){


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("WORD", word);
        contentValues.put("TIME", time);
        contentValues.put("EXPLANATION", explanation);
        contentValues.put("VIDEOPATH",videoPath);
        contentValues.put("SRTPATH", srtPath);

        db.insert(TABLE_NAME, null, contentValues);
        db.close();

    }

    public Word getWord(String word){
        SQLiteDatabase db = this.getWritableDatabase();
        Word w = new Word();
        String selectQuery = "select v.word, v.time, v.explanation, v.videopath, v.srtpath from " + TABLE_NAME +
                " v where v.word = " + "'"+ word + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            w.setWord(cursor.getString(0));
            w.setTime(cursor.getInt(1));
            w.setExplanation(cursor.getString(2));
            w.setVideoPath(cursor.getString(3));
            w.setSrtPath(cursor.getString(4));
        }
        db.close();
        return w;
    }

    public List<Word> getAllWord() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Word> wordList = new LinkedList<Word>();
        String selectQuery = "select * from " +  TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Word word = new Word();
                word.setWord(cursor.getString(0));
                word.setTime(cursor.getInt(1));
                word.setExplanation(cursor.getString(2));
                word.setVideoPath(cursor.getString(3));
                word.setSrtPath(cursor.getString(4));
                wordList.add(word);
            }while (cursor.moveToNext());
        }

        return wordList;
    }

    public void unSaveWord(Word word) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "delete from " +  TABLE_NAME + " where word = " + "'" + word.getWord() + "'";
        db.execSQL(Query);
        Log.d("delete", "de");

    }


}
