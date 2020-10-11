package com.yizheng.inspirationrewards;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.yizheng.inspirationrewards.CommonInfo.*;
import static java.net.HttpURLConnection.HTTP_OK;

public class LeaderboardAsyncTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "LeaderboardAsyncTask";
    private LeaderboardActivity leaderboardActivity;

    public LeaderboardAsyncTask(LeaderboardActivity leaderboardActivity) {
        this.leaderboardActivity = leaderboardActivity;
    }

    protected void onPostExecute(String s){

        if (s.startsWith("Error")) {

            return;
        }
        if (s == null){
            Log.d(TAG, "onPostExecute: doInBackground returned null.");
            return;
        }

        try {
            JSONArray jsonArray = new JSONArray(s);
            ArrayList<Profile> profiles = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonP = jsonArray.getJSONObject(i);
                String username = jsonP.getString("username");
                String fname = jsonP.getString("firstName");
                String lname = jsonP.getString("lastName");
                String dept = jsonP.getString("department");
                String story = jsonP.getString("story");
                String pos = jsonP.getString("position");
                String img = jsonP.getString("imageBytes");
                JSONArray jRewards = null;
                if (!jsonP.isNull("rewards")) {
                    jRewards = jsonP.getJSONArray("rewards");
                }
                ArrayList<Reward> rewards = new ArrayList<>();
                if (jRewards != null) {
                    for (int j = 0; j < jRewards.length(); j++) {
                        JSONObject jR = jRewards.getJSONObject(j);
                        Reward r = new Reward();
                        r.setValue(jR.getInt("value"));
                        rewards.add(r);
                    }
                }
                Profile p = new Profile();
                p.setUsername(username);
                p.setFirstname(fname);
                p.setLastname(lname);
                p.setDepartment(dept);
                p.setStory(story);
                p.setPosition(pos);
                p.setRewards(rewards);
                p.setPhoto(img);
                profiles.add(p);
            }
            leaderboardActivity.processResult(profiles);
        } catch (Exception e){
            Log.d(TAG, "onPostExecute: "+e);
            return;
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("studentId", studentid);
            jsonObject.put("username", strings[0]);
            jsonObject.put("password", strings[1]);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            String urlString = baseUrl + "/allprofiles";


            Uri.Builder buildURL = Uri.parse(urlString).buildUpon();
            String urlToUse = buildURL.build().toString();
            URL url = new URL(urlToUse);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(jsonObject.toString());
            out.close();

            int responseCode = connection.getResponseCode();

            StringBuilder result = new StringBuilder();
            if (responseCode != HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }
                JSONObject errorObj = new JSONObject(result.toString());
                return "Error: " + errorObj.getJSONObject("errordetails").getString("message");
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }

                return result.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
