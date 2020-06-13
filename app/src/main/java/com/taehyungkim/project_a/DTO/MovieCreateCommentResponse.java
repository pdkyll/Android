package com.taehyungkim.project_a.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kth on 2020-06-13.
 */
public class MovieCreateCommentResponse {

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("code")
    @Expose
    public int code;

}
