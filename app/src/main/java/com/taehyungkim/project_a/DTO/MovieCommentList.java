package com.taehyungkim.project_a.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kth on 2020-06-12.
 */
public class MovieCommentList {

    @SerializedName("total")
    @Expose
    public String total;
    @SerializedName("writer")
    @Expose
    public String writer;
    @SerializedName("review_id")
    @Expose
    public String review_id;
    @SerializedName("writer_image")
    @Expose
    public String writer_image;
    @SerializedName("time")
    @Expose
    public String time;
    @SerializedName("rating")
    @Expose
    public float rating;
    @SerializedName("contents")
    @Expose
    public String contents;
    @SerializedName("recommend")
    @Expose
    public int recommend;
}
