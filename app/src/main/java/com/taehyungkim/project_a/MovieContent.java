package com.taehyungkim.project_a;

public class MovieContent {
    private String titleText;
    private String writingText;
    private String contentText;

    public String getTitleText() {
        return titleText;
    }

    public String getWritingText() {
        return writingText;
    }

    public String getContentText() {
        return contentText;
    }

    public MovieContent(String titleText, String writingText, String contentText) {
        this.contentText = contentText;
        this.writingText = writingText;
        this.contentText = contentText;
    }
}
