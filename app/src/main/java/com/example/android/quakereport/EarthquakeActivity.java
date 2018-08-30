/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//import android.app.LoaderManager;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private ListView earthquakeListView;
    private EarthquakeAdapter adapter;
    private EarthquakeAdapter emptyAdapter;
    private Context appContext;
    /**
     * URL for earthquake data from the USGS dataset
     */


    private String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        appContext = this;

        //ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();
        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = findViewById(R.id.list);
        emptyAdapter = new EarthquakeAdapter(appContext, new ArrayList<Earthquake>(), 5);
        earthquakeListView.setAdapter(emptyAdapter);
        getSupportLoaderManager().initLoader(1, null, this).forceLoad();
    }

    @Override
    public EarthquakeLoader onCreateLoader(int id, Bundle bundle) {
        Log.i("Earthqueake ACtivity", "onCreateLoeader running...");
        return new EarthquakeLoader(appContext, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        Log.i("Earthqueake ACtivity", "onLoadFinished running...");
        if (earthquakes != null && !earthquakes.isEmpty()) {
            adapter = new EarthquakeAdapter(appContext, earthquakes, 5);
            earthquakeListView.setAdapter(adapter);
            Log.i("Earthqueake ACtivity", "Adapter Set.");
        } else {
            Log.i("Earthqueake ACtivity", "Loader Error.");
            Toast.makeText(appContext, "HTTP Data Request Failed!", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<Earthquake>> loader) {
        Log.i("Earthqueake ACtivity", "Loader Reset.");
        adapter.clear();
    }


}
