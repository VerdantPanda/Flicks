package com.example.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {
    // base URL for loading images
    String imageBaseURL;

    // the poster size to use when fetching images
    String posterSize;

    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        this.imageBaseURL = images.getString("secure_base_url");
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        this.posterSize = posterSizeOptions.optString(3, "w342");
    }

    public String getImageBaseURL() {
        return imageBaseURL;
    }

    public String getPosterSize() {
        return posterSize;
    }


    public String getImageUrl(String size, String path){
        return String.format("%s%s%s", imageBaseURL, size, path);
    }
}

