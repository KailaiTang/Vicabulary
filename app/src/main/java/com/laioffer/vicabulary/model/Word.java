package com.laioffer.vicabulary.model;

import androidx.annotation.NonNull;

public class Word {
    private String word;
    private String explanation;
//    private String movie_name;
//    private String publisher;



    //private String translation;
    private long time;
    private String videoPath;
    private String srtPath;



    public void setWord(String word) {
        this.word = word;
    }




    public void setExplanation(String explanation){
        this.explanation = explanation;
    }






    public String getWord() {

        return word;
    }

    public String getExplanation() {
        return explanation;
    }


    public String getVideoPath() {
        return videoPath;

    }

    public String getSrtPath() {
        return srtPath;

    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public void setVideoPath(String videoPath){
        this.videoPath = videoPath;

    }

    public void setSrtPath(String srtPath){
        this.srtPath = srtPath;

    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", explanation='" + explanation + '\'' +
                ", time=" + time +
                ", videoPath='" + videoPath + '\'' +
                ", srtPath='" + srtPath + '\'' +
                '}';
    }
}


