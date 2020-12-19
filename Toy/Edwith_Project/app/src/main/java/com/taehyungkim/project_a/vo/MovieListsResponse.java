package com.taehyungkim.project_a.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kth on 2020-06-10.
 */
public class MovieListsResponse {

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
    public ArrayList<MovieListInfo> result = new ArrayList<>();

    public ArrayList<MovieListInfo> getResult() {
        return result;
    }

    public void setResult(ArrayList<MovieListInfo> result) {
        this.result = result;
    }
}
