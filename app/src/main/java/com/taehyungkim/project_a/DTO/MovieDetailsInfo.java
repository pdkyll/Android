package com.taehyungkim.project_a.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kth on 2020-06-10.
 */
public class MovieDetailsInfo {

    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("user_rating")
    @Expose
    public float user_rating;
    @SerializedName("audience_rating")
    @Expose
    public float audience_rating;
    @SerializedName("reviewer_rating")
    @Expose
    public float reviewer_rating;
    @SerializedName("reservation_rate")
    @Expose
    public float reservation_rate;
    @SerializedName("reservation_grade")
    @Expose
    public int reservation_grade;
    @SerializedName("grade")
    @Expose
    public int grade;
    @SerializedName("thumb")
    @Expose
    public String thumb;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("photos")
    @Expose
    public String photos;
    @SerializedName("videos")
    @Expose
    public String videos;
    @SerializedName("outlinks")
    @Expose
    public String outlinks;
    @SerializedName("genre")
    @Expose
    public String genre;
    @SerializedName("duration")
    @Expose
    public int duration;
    @SerializedName("audience")
    @Expose
    public int audience;
    @SerializedName("synopsis")
    @Expose
    public String synopsis;
    @SerializedName("director")
    @Expose
    public String director;
    @SerializedName("actor")
    @Expose
    public String actor;
    @SerializedName("like")
    @Expose
    public int like;
    @SerializedName("dislike")
    @Expose
    public int dislike;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(float user_rating) {
        this.user_rating = user_rating;
    }

    public float getAudience_rating() {
        return audience_rating;
    }

    public void setAudience_rating(float audience_rating) {
        this.audience_rating = audience_rating;
    }

    public float getReviewer_rating() {
        return reviewer_rating;
    }

    public void setReviewer_rating(float reviewer_rating) {
        this.reviewer_rating = reviewer_rating;
    }

    public float getReservation_rate() {
        return reservation_rate;
    }

    public void setReservation_rate(float reservation_rate) {
        this.reservation_rate = reservation_rate;
    }

    public int getReservation_grade() {
        return reservation_grade;
    }

    public void setReservation_grade(int reservation_grade) {
        this.reservation_grade = reservation_grade;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    public String getOutlinks() {
        return outlinks;
    }

    public void setOutlinks(String outlinks) {
        this.outlinks = outlinks;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAudience() {
        return audience;
    }

    public void setAudience(int audience) {
        this.audience = audience;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }
}
