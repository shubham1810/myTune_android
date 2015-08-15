package com.shubhamdokania.mytune.model;

import com.google.gson.annotations.Expose;

/**
 * Created by shubham on 16/8/15.
 */
public class EmptyData {
    @Expose
    String url = "";

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
