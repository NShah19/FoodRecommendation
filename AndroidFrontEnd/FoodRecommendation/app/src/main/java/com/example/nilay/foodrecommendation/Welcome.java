package com.example.nilay.foodrecommendation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nilay.foodrecommendation.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        final EditText m_firstName =(EditText)findViewById(R.id.editFirstName);
        final EditText m_lastName =(EditText)findViewById(R.id.editLastName);

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String base_url = "https://food-rec-staging.herokuapp.com";
        final String user_url = base_url + "/api/v1/user";

        Button btn = (Button)findViewById(R.id.continueButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = m_firstName.getText().toString();
                String lastName = m_lastName.getText().toString();

                JSONObject user_Name = new JSONObject();
                try {
                    user_Name = user_Name.put("firstname", firstName);
                    user_Name = user_Name.put("lastname", lastName);
                    Log.i("Object", firstName);
                    Log.d("Object", lastName);
                    Log.d("Object", user_Name.toString());
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.d("Object", "failure");
                }

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, user_url, user_Name,

                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Activity", response.toString());

                                // We need an Editor object to make preference changes.
                                // All objects are from android.context.Context
                                SharedPreferences userID = getPreferences(0);
                                SharedPreferences.Editor editor = userID.edit();
                                int i;
                                try {
                                    i = response.getInt("user_id");
                                } catch (JSONException e){
                                    i = -1;
                                }
                                editor.putInt("userID", i);

                                // Commit the edits!
                                editor.commit();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Activity", "Error: " + error.getMessage());
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }
                };
                queue.add(jsonObjReq);
                startActivity(new Intent(Welcome.this, Profile.class));
            }
        });
    }
}
