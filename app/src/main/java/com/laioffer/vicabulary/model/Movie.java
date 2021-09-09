package com.laioffer.vicabulary.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class Movie implements Serializable {
    @NonNull
    private final String id;
    private final String name;
    private final String publisher;
    private final String duration;
    private final String clip;
    private final String subtitle;
    private final String cover;

    public Movie(@NonNull String id, String name, String publisher, String duration, String clip, String subtitle, String cover) {
        this.id = id;
        this.name = name;
        this.publisher = publisher;
        this.duration = duration;
        this.clip = clip;
        this.subtitle = subtitle;
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getDuration() {
        return duration;
    }

    public String getClip() {
        return clip;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getCover() {
        return cover;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return id.equals(movie.id) &&
                Objects.equals(name, movie.name) &&
                Objects.equals(publisher, movie.publisher) &&
                Objects.equals(duration, movie.duration) &&
                Objects.equals(clip, movie.clip) &&
                Objects.equals(subtitle, movie.subtitle) &&
                Objects.equals(cover, movie.cover);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, publisher, duration, clip, subtitle, cover);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", publisher='" + publisher + '\'' +
                ", duration='" + duration + '\'' +
                ", clip='" + clip + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }
}
