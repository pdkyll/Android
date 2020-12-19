package com.taehyungkim.project_a.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kth on 2020-06-12.
 */
public class MovieCommentResponse {
    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("code")
    @Expose
    public int code;

    @SerializedName("resultType")
    @Expose
    public String resultType;

    @SerializedName("result")
    @Expose
    public ArrayList<MovieCommentList> result = new ArrayList<>();

    public ArrayList<MovieCommentList> getResult() {
        return result;
    }

    public void setResult(ArrayList<MovieCommentList> result) {
        this.result = result;
    }
}
