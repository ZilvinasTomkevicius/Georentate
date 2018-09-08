package com.example.zilvinastomkevicius.georentate.APIClients;

import android.os.AsyncTask;
import android.view.View;

import com.example.zilvinastomkevicius.georentate.Entities.AdditionalCheckInfo;
import com.example.zilvinastomkevicius.georentate.Entities.Checkpoint;
import com.example.zilvinastomkevicius.georentate.Fragments.MapFragment;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.CheckpointObj;
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

//Async task for getting info about nearest checkpoints

public class ClosestCheckpointFinderTask extends AsyncTask<String, String, ArrayList<String>> {

    HttpURLConnection httpURLConnection = null;
    InputStream inputStream = null;
    ArrayList<String> data = new ArrayList<>();

    private MapFragment mMapFragment;

    public ClosestCheckpointFinderTask(MapFragment mapFragment) {

        mMapFragment = mapFragment;
    }

    @Override
    protected ArrayList<String> doInBackground(String... uri) {

        try {

            for(String u : uri) {

                URL url = new URL(u);

                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.connect();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {

                    stringBuffer.append(line);
                }

                data.add(stringBuffer.toString());
                bufferedReader.close();
            }
        }
        catch (IOException e) {

            e.printStackTrace();
        }

        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<String> s) {

        parseRouteInfo(s);
    }

    //Parsing gotten data
    public void parseRouteInfo(ArrayList<String> result) {

        try {

            CheckpointObj.CLOSEST_CHECKPOINTS.clear();
            CheckpointObj.CLOSEST_CHECKPOINTS = new ArrayList<>();

            for(String r : result) {

                JSONObject jsonObject;
                JSONObject jsonObject2 = new JSONObject(r);
                AdditionalCheckInfo additionalCheckInfo = new AdditionalCheckInfo();

                //get approximate distance
                jsonObject = jsonObject2.getJSONArray("routes").getJSONObject(0)
                        .getJSONArray("legs").getJSONObject(0).getJSONObject("distance");
                additionalCheckInfo.ApproximateDistance =  jsonObject.getString("text");

                //get exact distance
                jsonObject = jsonObject2.getJSONArray("routes").getJSONObject(0)
                        .getJSONArray("legs").getJSONObject(0).getJSONObject("distance");
                additionalCheckInfo.ExactDistance = jsonObject.getInt("value");

                //get approximate travel time
                jsonObject = jsonObject2.getJSONArray("routes").getJSONObject(0)
                        .getJSONArray("legs").getJSONObject(0).getJSONObject("duration");
                additionalCheckInfo.TravelTime = jsonObject.getString("text");

                CheckpointObj.CLOSEST_CHECKPOINTS.add(additionalCheckInfo);
            }

            mMapFragment.setClosestCheckpointInfo();
        }
        catch (JSONException e) {

            e.printStackTrace();
        }
    }
}
