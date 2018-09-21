package com.example.android.quakereport;

//import android.content.AsyncTaskLoader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private String url;

    public EarthquakeLoader (Context context, String url){
        super (context);
        this.url = url;
    }

    @Override
    protected void onStartLoading(){
        Log.i ("EarthqualeLoader","onStartingLoading Called ...");
        forceLoad();
    }

    //TODO: url is malformed,items are repeated twice
    public List<Earthquake> loadInBackground (){
        if (url==null ) return null;
        HttpHandler httpHandler = new HttpHandler();
        // Making a request to url and getting response
        Log.i("EarthqualeLoader", "url: " + url);
        String jsonStr = httpHandler.makeServiceCall(url);
        Log.i("EarthqualeLoader", "jsonStr: " + jsonStr);
        return QueryUtils.extractEarthquakes(jsonStr);
    }

}
