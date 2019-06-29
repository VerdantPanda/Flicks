package com.example.flicks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.flicks.models.Config;
import com.example.flicks.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {


    // constants
    // Base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";

    // parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";

    // tag for logging activity
    public final static String TAG = "MovieListActivity";


    // instance fields
    AsyncHttpClient client;



    List<Movie> movies;

    RecyclerView rvMovies;

    MovieAdapter adapter;

    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        // instantiate the client
        client = new AsyncHttpClient();

        // instantiate the movie list
        movies = new ArrayList<>();

        //instantiate the adapter
        adapter = new MovieAdapter(movies);

        //
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);



        // call get configuration on app creation
        getConfiguration();


    }





    // get the list of currently playing movies
    private void getNowPlaying() {
        // create the url
        String url = API_BASE_URL + "/movie/now_playing";

        //set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); // API key, always required

        //execute a GET request, expect JSON object response
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // load the results into the movie list
                try {
                    JSONArray results = response.getJSONArray("results");

                    // iterate through the results set
                    for (int i = 0; i < results.length(); i++){
                        movies.add(new Movie(results.getJSONObject(i)));
                        //TODO: Stopped at this line on {Wed June 26, 2019}

                        adapter.notifyItemInserted(movies.size() - 1 );
                    }
                    Log.i(TAG, "Loaded "+results.length()+" movies");
                } catch (JSONException e){
                    logError("Failed to parse now_playing information.", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                logError("Failed get data from now_playing endpoint", throwable, true );
            }
        });

    }










    // get the config from the API
    private void getConfiguration() {
        // create the url
        String url = API_BASE_URL + "/configuration";

        //set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); // API key, always required

        //execute a GET request, expect JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);

                try {
                    config = new Config(response);
                    //
                    Log.i(TAG, String.format("Loaded configuration with imageBaseURL: %s and posterSize: %s", config.getImageBaseURL(), config.getPosterSize()));
                    adapter.setConfig(config);
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                logError("Failed getting configuration.", throwable, true);
            }
        });
    }

    // handles errors, alerts log and user
    private void logError(String message, Throwable error, boolean alertUser) {
        // always log the error
        Log.e(TAG, message, error);

        // alert user if necessary
        if (alertUser) {
            // Display long toast
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

}
