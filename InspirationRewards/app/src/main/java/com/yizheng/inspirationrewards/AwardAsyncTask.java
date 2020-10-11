package com.yizheng.inspirationrewards;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.yizheng.inspirationrewards.CommonInfo.baseUrl;
import static com.yizheng.inspirationrewards.CommonInfo.studentid;
import static java.net.HttpURLConnection.HTTP_OK;

public class AwardAsyncTask extends AsyncTask<Profile, Void, String> {

    private AwardActivity awardActivity;
    private String notes;
    private int value;

    public AwardAsyncTask(AwardActivity awardActivity, String notes, int value) {
        this.awardActivity = awardActivity;
        this.notes = notes;
        this.value = value;
    }

    @Override
    protected void onPostExecute(String s) {
        awardActivity.processResult(s);
    }

    @Override
    protected String doInBackground(Profile... profiles) {
        Profile source = profiles[0];
        Profile target = profiles[1];
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        String dateString = dateFormat.format(date);
        JSONObject jTarget = new JSONObject();
        JSONObject jSource = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jTarget.put("studentId", studentid);
            jTarget.put("username", target.getUsername());
            jTarget.put("name", source.getFirstname()+" "+source.getLastname());
            jTarget.put("date", dateString);
            //jTarget.put("date", date);
            jTarget.put("notes", notes);
            jTarget.put("value", value);

            jSource.put("studentId", studentid);
            jSource.put("username", source.getUsername());
            jSource.put("password", source.getPassword());

            jsonObject.put("target", jTarget);
            jsonObject.put("source", jSource);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            String urlString = baseUrl + "/rewards";
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
                //JSONObject jObj = new JSONObject(result.toString());
                return result.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
