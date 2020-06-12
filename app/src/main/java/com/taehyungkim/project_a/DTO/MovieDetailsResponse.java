package com.taehyungkim.project_a.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kth on 2020-06-10.
 */
public class MovieDetailsResponse {
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
    public ArrayList<MovieDetailsInfo> result = new ArrayList<>();

    public ArrayList<MovieDetailsInfo> getResult() {
        return result;
    }

    public void setResult(ArrayList<MovieDetailsInfo> result) {
        this.result = result;
    }
}
