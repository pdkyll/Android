package com.taehyungkim.project_a;

import android.graphics.drawable.Drawable;

public class MovieContent {
    private String titleText;
    private String writingText;
    private String contentText;
    private String seeBtName;
    private String reservationBtName;

    private Drawable fbIcon;
    private Drawable kaIcon;
    private Drawable writingIcon;

    private String visible;

    public String getTitleText() {
        return titleText;
    }

    public String getWritingText() {
        return writingText;
    }

    public String getContentText() {
        return contentText;
    }

    public String getSeeBtName() {
        return seeBtName;
    }

    public String getReservationBtName() {
        return reservationBtName;
    }

    public Drawable getFbIcon() {
        return fbIcon;
    }

    public Drawable getKaIcon() {
        return kaIcon;
    }

    public Drawable getWritingIcon() {
        return writingIcon;
    }

    public MovieContent(String titleText, String contentText) {
        this.titleText = titleText;
        this.contentText = contentText;
    }

    public MovieContent(Drawable fbIcon, Drawable kaIcon, String reservationBtName) {
        this.fbIcon = fbIcon;
        this.kaIcon = kaIcon;
        this.reservationBtName = reservationBtName;
    }

    public MovieContent(String titleText, Drawable writingIcon, String writingText, String seeBtName) {
        this.titleText = titleText;
        this.writingIcon = writingIcon;
        this.writingText = writingText;
        this.seeBtName = seeBtName;
    }
}
