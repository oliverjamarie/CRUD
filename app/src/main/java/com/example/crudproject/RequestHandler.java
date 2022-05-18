package com.example.crudproject;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class RequestHandler {
    private final String TAG = "RequestHandler";
    public String sendPostRequest(String requestURL, HashMap<String,String> postDataParams){
        URL url;
        StringBuilder stringBuilder = new StringBuilder();
        try{
            url = new URL(requestURL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            bufferedWriter.write(getPostDataString(postDataParams));
            bufferedWriter.flush();
            bufferedWriter.close();

            outputStream.close();
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "Response Code " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                stringBuilder = new StringBuilder();
                String response;
                response = bufferedReader.readLine();

                while(response != null){
                    stringBuilder.append(response);
                    response = bufferedReader.readLine();
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public String sendGetRequest(String requestURL){
        StringBuilder stringBuilder = new StringBuilder();

        try{
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String s;
            s = bufferedReader.readLine();

            while (s != null){
                stringBuilder.append(s + "\n");
                s = bufferedReader.readLine();
            }

        }
        catch (Exception e){
            Log.d(TAG, "Error", e);
            e.printStackTrace();
        }

        Log.d(TAG, "DONE");
        return stringBuilder.toString();
    }

    public String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Log.d(TAG, "Start of post param");
        for(Map.Entry<String,String> entry : params.entrySet()){
            if (first){
                first = false;
                result.append("?");
            }
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        Log.d(TAG,result.toString());
        Log.d(TAG,"end of post param");
        return result.toString();
    }
}
