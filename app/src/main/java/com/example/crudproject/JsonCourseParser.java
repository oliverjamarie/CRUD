package com.example.crudproject;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonCourseParser {
    private static List<Course> classes;
    private static final String TAG = "JSON-COURSE-PARSER";

    public static List<Course> parse (String jsonStr){
        classes = new ArrayList<>();

        try{
            JSONArray jsonArray = new JSONArray(jsonStr);
            JSONObject jsonObject;

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                Course course = new Course(jsonObject.getInt("id"),
                        jsonObject.getString("title"),
                        jsonObject.getString("description"));
                classes.add(course);
            }
        }
        catch (Exception e){
            Log.e(TAG, "parse: ERROR", e);
        }

        return classes;
    }
}
