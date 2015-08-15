package com.shubhamdokania.mytune.API;

import com.shubhamdokania.mytune.model.SearchResults;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;


public interface gitapi {
    @GET("/users/{user}")      //here is the other url part.best way is to start using /
    public void getFeed2(@Path("user") String user, Callback<SearchResults> response);     //string user is for passing values from edittext for eg: user=basil2style,google
    //response is the response from the server which is now in the POJO

    @GET("/api/search")
    public void getFeed(@Query("query") String q, Callback<SearchResults> response);
}