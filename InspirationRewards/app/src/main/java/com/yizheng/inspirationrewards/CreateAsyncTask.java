package com.yizheng.inspirationrewards;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

public class CreateAsyncTask extends AsyncTask<Profile, Void, String> {

    private final String studentid = "1427953";
    private final String baseUrl = "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com";
    private String OK = "OK";
    private CreateAccountActivity createAccountActivity;

    private boolean isCreation;

    public CreateAsyncTask(CreateAccountActivity createAccountActivity, boolean isCreation) {
        this.createAccountActivity = createAccountActivity;
        this.isCreation = isCreation;
    }


    @Override
    protected void onPostExecute(String s) {
        createAccountActivity.processResult(s);
    }

    @Override
    protected String doInBackground(Profile... profiles) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("studentId", studentid);
            jsonObject.put("username", profiles[0].getUsername());
            jsonObject.put("password", profiles[0].getPassword());
            jsonObject.put("firstName", profiles[0].getFirstname());
            jsonObject.put("lastName", profiles[0].getLastname());
//            if (profiles[0].getPointsToAward() == null) {
//                jsonObject.put("pointsToAward", 1000);
//            }
            jsonObject.put("pointsToAward", profiles[0].getPointsToAward());
            jsonObject.put("department", profiles[0].getDepartment());
            jsonObject.put("story", profiles[0].getStory());
            jsonObject.put("position", profiles[0].getPosition());
            jsonObject.put("admin", profiles[0].isAdministrator());
            jsonObject.put("location", profiles[0].getLocation());
            jsonObject.put("imageBytes", profiles[0].getPhoto());
            if (profiles[0].rewardsIsEmpty()) {
                jsonObject.put("rewardRecords", new JSONArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            String urlString = baseUrl + "/profiles";


            Uri.Builder buildURL = Uri.parse(urlString).buildUpon();
            String urlToUse = buildURL.build().toString();
            URL url = new URL(urlToUse);

            connection = (HttpURLConnection) url.openConnection();
            if (isCreation) {
                connection.setRequestMethod("POST");
            }
            else {
                connection.setRequestMethod("PUT");
            }
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
                //JSONObject jObj = new JSONObject(result.toString());
                return result.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
