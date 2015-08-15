package com.shubhamdokania.mytune.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class SearchResults {

    @Expose
    List<Datum> data = new ArrayList<Datum>();

    /**
     *
     * @return
     * The data
     */
    public List<Datum> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<Datum> data) {
        this.data = data;
        //Log.d("Data-------1231231312---------", data.toString());
    }

}