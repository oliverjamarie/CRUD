package com.example.crudproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText title, id, description;
    RadioGroup radioGroup;
    public static final int CODE_POST_REQUEST = 1024;
    public static final int CODE_GET_REQUEST = 1025;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String TAG = "MAIN_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.Title_Input);
        description = findViewById(R.id.Desc_Input);
        id = findViewById(R.id.ID_Input);

        disableEditText(title);
        disableEditText(description);
        disableEditText(id);

        radioGroup = findViewById(R.id.radioGroup);

    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.Create_RadioBtn:
                if (checked){
                    enableEditText(title);
                    enableEditText(description);
                    disableEditText(id);
                }
                break;
            case R.id.Read_RadioBtn:
                if (checked){

                }
                break;
            case R.id.Update_RadioBtn:
                if (checked){
                    enableEditText(title);
                    enableEditText(description);
                    enableEditText(id);
                }
                break;
            case R.id.Delete_RadioBtn:
                if (checked){
                    enableEditText(id);
                    disableEditText(title);
                    disableEditText(description);
                }
                break;
        }
    }

    public void onQueryButtonClicked(View view){
        int id = radioGroup.getCheckedRadioButtonId();

        switch (id) {
            case R.id.Create_RadioBtn:
                createClass();
                break;
            case R.id.Read_RadioBtn:
                read();
                break;
            case R.id.Update_RadioBtn:
                updateClass();
                break;
            case R.id.Delete_RadioBtn:
                deleteClass();
                break;
        }
    }

    private void enableEditText(EditText editText){
        editText.setAlpha(1f);
        editText.setEnabled(true);
    }

    private void disableEditText(EditText editText){
        editText.setAlpha(0f);
        editText.setEnabled(false);
    }

    private class PerformNetworkRequest extends AsyncTask<Void,Void,String>{
        String url;
        private String result;
        HashMap<String, String> params;

        int requestCode;

        public PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            result = null;
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (!jsonObject.getBoolean("error")){
                    Toast toast = Toast.makeText(getApplicationContext(),"COMPLETED " + jsonObject.getString("message"),Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            result = null;
            try{
                Log.d("MainActivity", url + requestHandler.getPostDataString(params));
            }catch (Exception e){
                e.printStackTrace();
            }
            if (requestCode == CODE_POST_REQUEST){
                String respStr = requestHandler.sendPostRequest(url,params);
                result = respStr;
                return respStr;
            }

            if (requestCode == CODE_GET_REQUEST){
                String respStr =  requestHandler.sendGetRequest(url);
                result = respStr;
                return respStr;
            }

            return null;
        }

        // waits for request to finish
        public String getResult() {
            while(result == null){
                continue;
            }

            return result;
        }
    }

    private void createClass(){
        Toast toast = Toast.makeText(getApplicationContext(),"Creating Class", Toast.LENGTH_SHORT);
        toast.show();

        HashMap<String, String> parameters = extractParameters();

        if (parameters == null){
            return;
        }

        PerformNetworkRequest request = new PerformNetworkRequest(API.CREATE, parameters, CODE_POST_REQUEST);
        request.execute();

        String result = request.getResult();
        toast = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG);
        toast.show();
    }

    private void read(){
        Toast toast = Toast.makeText(getApplicationContext(),"Reading DB", Toast.LENGTH_SHORT);
        toast.show();

        extractParameters();

        PerformNetworkRequest request = new PerformNetworkRequest(API.READ, null, CODE_GET_REQUEST);
        request.execute();

        String result = request.getResult();

        Intent intent = new Intent(this, Read.class);
        intent.putExtra(EXTRA_MESSAGE, result);
        startActivity(intent);
    }

    private void updateClass(){
        Toast toast = Toast.makeText(getApplicationContext(), "Updating Class", Toast.LENGTH_SHORT);
        toast.show();

        HashMap<String, String> parameters = extractParameters();

        if (parameters == null){
            return;
        }

        PerformNetworkRequest request = new PerformNetworkRequest(API.UPDATE,parameters, CODE_POST_REQUEST);
        request.execute();
        String result = request.getResult();

        if (result.equalsIgnoreCase("true")){
            toast = Toast.makeText(getApplicationContext(), "Succesfully Updated Class", Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            toast = Toast.makeText(getApplicationContext(), "Failed To Update Class", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void deleteClass(){
        Toast toast = Toast.makeText(getApplicationContext(), "Deleting Class", Toast.LENGTH_SHORT);
        toast.show();

        HashMap<String, String> parameters = extractParameters();

        if (parameters == null){
            return;
        }

        PerformNetworkRequest request = new PerformNetworkRequest(API.DELETE,parameters, CODE_GET_REQUEST);
        request.execute();

        String result = request.getResult();
        if (result.equalsIgnoreCase("-1")){
            toast = Toast.makeText(getApplicationContext(), "Succesfully Deleted Class", Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            toast = Toast.makeText(getApplicationContext(), "Failed To Delete Class", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private HashMap<String,String> extractParameters(){
        HashMap<String, String> parameters = new HashMap<>();

        String titleStr = title.getText().toString().trim();
        String descStr = description.getText().toString().trim();
        String idStr = id.getText().toString().trim();

        if (title.isEnabled()){
            if (TextUtils.isEmpty(titleStr)){
                title.setError("Please enter class title");
                title.requestFocus();
                return null;
            }

            parameters.put("title", titleStr);
        }

        if (description.isEnabled()){
            if (TextUtils.isEmpty(descStr)){
                description.setError("Please enter class description");
                description.requestFocus();
                return null;
            }
            parameters.put("description", descStr);
        }


        if (id.isEnabled()){
            if (TextUtils.isEmpty(idStr)){
                id.setError("Please enter ID");
                id.requestFocus();
                return null;
            }
            parameters.put("id",idStr);
        }
        return parameters;
    }
}