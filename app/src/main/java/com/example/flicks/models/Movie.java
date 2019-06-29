package com.example.flicks.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {

    // fields
    private String title;
    private String overview;
    private String poster_Path;

    // initialize data from JSON
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        poster_Path = object.getString("poster_path");

    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_Path() {
        return poster_Path;
    }
}
