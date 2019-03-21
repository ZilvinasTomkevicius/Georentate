package com.example.zilvinastomkevicius.georentate.APIClients;

import android.os.AsyncTask;

import com.example.zilvinastomkevicius.georentate.Fragments.MapFragment;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.RouteObj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Async task for route search
 */

public class RoutFinderTask extends AsyncTask<String, String, String> {

    private MapFragment mMapFragment;

    public RoutFinderTask(MapFragment mapFragment) {
        mMapFragment = mapFragment;
    }

    HttpURLConnection httpURLConnection = null;
    InputStream inputStream = null;
    String data = "";

    @Override
    protected String doInBackground(String... uri) {
        try {
            URL url = new URL(uri[0]);

            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            data = stringBuffer.toString();
            bufferedReader.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        parseRouteInfo(s);
    }

    /**
     * Parsing gotten data
     * @param result
     */
    public void parseRouteInfo(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0)
                    .getJSONArray("legs").getJSONObject(0).getJSONArray("steps");

            JSONObject jsonObject3;
            JSONObject jsonObject2 = new JSONObject(result);

            jsonObject3 = jsonObject2.getJSONArray("routes").getJSONObject(0)
                    .getJSONArray("legs").getJSONObject(0).getJSONObject("distance");
            RouteObj.ROUTE_DISTANCE = jsonObject3.getString("text");

            jsonObject3 = jsonObject2.getJSONArray("routes").getJSONObject(0)
                    .getJSONArray("legs").getJSONObject(0).getJSONObject("duration");
            RouteObj.ROUTE_DURATION = jsonObject3.getString("text");

            ArrayList<String> polylineArrayList = new ArrayList<>();
            int count = jsonArray.length();

            for(int i = 0; i < count; i++) {

                jsonObject2 = jsonArray.getJSONObject(i);

                String point = jsonObject2.getJSONObject("polyline").getString("points");

                polylineArrayList.add(point);
            }
            mMapFragment.drawRoute(polylineArrayList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
