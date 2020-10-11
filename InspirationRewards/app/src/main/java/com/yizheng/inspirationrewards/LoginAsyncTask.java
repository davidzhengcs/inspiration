package com.yizheng.inspirationrewards;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.yizheng.inspirationrewards.CommonInfo.studentid;
import static java.net.HttpURLConnection.HTTP_OK;


public class LoginAsyncTask extends AsyncTask<String, Void, String> {
    private MainActivity mainActivity;

    public LoginAsyncTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPostExecute(String s) {
        mainActivity.processResult(s);
    }

    @Override
    protected String doInBackground(String... strings) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("studentId", studentid);
            jsonObject.put("username", strings[0]);
            jsonObject.put("password", strings[1]);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            String urlString = CommonInfo.baseUrl + "/login";

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
                JSONObject jObj = new JSONObject(result.toString());
                return result.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
