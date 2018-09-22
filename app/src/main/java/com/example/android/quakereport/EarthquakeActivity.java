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
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//import android.app.LoaderManager;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private ListView earthquakeListView;
    private EarthquakeAdapter adapter;
    private EarthquakeAdapter emptyAdapter;
    private Context appContext;
    View loadingIndicator;
    private TextView tvEmptyView;
    private boolean isWiFi = false;
    /**
     * URL for earthquake data from the USGS dataset
     */
    private String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        appContext = this;

        //ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();
        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = findViewById(R.id.list);
        tvEmptyView = findViewById(R.id.tvEmptyState);
        loadingIndicator = findViewById(R.id.loading_spinner);
        earthquakeListView.setEmptyView(tvEmptyView);
        emptyAdapter = new EarthquakeAdapter(appContext, new ArrayList<Earthquake>(), 5);
        earthquakeListView.setAdapter(emptyAdapter);

        //Determine and monitor the connectivity status
        //https://developer.android.com/training/monitoring-device-state/connectivity-monitoring?utm_source=udacity&utm_medium=course&utm_campaign=android_basics#java
        ConnectivityManager cm =
                (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        //Determine the type of your internet connection
        if (isConnected) isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;

        if (isConnected) {
            getSupportLoaderManager().initLoader(1, null, this).forceLoad();
        } else {
            loadingIndicator.setVisibility(View.GONE);
            tvEmptyView.setText(R.string.noInternetConnection);
        }
    }

    @Override
    // This method initialize the contents of the Activity's options main.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        To determine which item was selected and what action to take, call getItemId,
        which returns the unique ID for the menu item (defined by the android:id attribute
        in the menu resource).
         */
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public EarthquakeLoader onCreateLoader(int id, Bundle bundle) {
        Log.i("MyApp", "[EarthquakeActivity.onCreateLoader] running...");

        //get the settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String minMagnitude = sharedPreferences.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `format=geojson`
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(appContext, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {

        loadingIndicator.setVisibility(View.GONE);
        // Set empty state text to display "No earthquakes found."
        tvEmptyView.setText("No earthquakes found.");

        Log.i("MyApp", "[EarthquakeActivity.onLoadFinished] running...");
        if (earthquakes != null && !earthquakes.isEmpty()) {
            adapter = new EarthquakeAdapter(appContext, earthquakes, 5);
            earthquakeListView.setAdapter(adapter);
            Log.i("MyApp", "[EarthquakeActivity.onLoadFinished] Adapter Set.");
        } else {
            tvEmptyView.setVisibility(TextView.VISIBLE);
            Log.i("MyApp", "[EarthquakeActivity.onLoadFinished] Loader Error.");
            Toast.makeText(appContext, "No Earthquakes Found!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<Earthquake>> loader) {
        Log.i("MyApp", "[EarthquakeActivity.onLoaderReset] running...");
        if (adapter != null) adapter.clear();
    }


}
