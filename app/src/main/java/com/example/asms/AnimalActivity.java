package com.example.asms;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnimalActivity extends AppCompatActivity{

    String myUrl = "https://asms.herokuapp.com/animals/";
    RequestQueue queue;
    ArrayList<Animal> animalList = new ArrayList<>();
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        queue = Volley.newRequestQueue(this);
        customAdapter = new CustomAdapter(AnimalActivity.this, animalList, queue);
        recyclerView.setAdapter(customAdapter);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, myUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject animal = response.getJSONObject(i);
                                    String animalId = animal.getString("_id");
                                    String animalname = animal.getString("Name");
                                    String animalimage = animal.getString("selectedFile");
                                    String animalbreed = animal.getString("Breed");
                                    String animalage = animal.getString("Age");
                                    String animalintakereason = animal.getString("IntakeReason");
                                    animalList.add(new Animal(animalId, animalname, animalimage, animalbreed, animalage,
                                            animalintakereason));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            customAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AnimalActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    public void update() {
        finish();
        overridePendingTransition(0,0);
        startActivity(getIntent());
        overridePendingTransition(0,0);
    }

}
