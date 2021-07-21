package com.example.asms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//Activity dedicated to viewing all the animals
public class AnimalActivity extends AppCompatActivity{

    //Classwide variables
    String myUrl = "https://asms.herokuapp.com/animals/";
    RequestQueue queue;
    ArrayList<Animal> animalList = new ArrayList<>();
    CustomAdapter customAdapter;

    /*On create method that initializes activity and creates header
    along with setting up a recyclerview and sending data to
    individual custom adapters */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        BottomNavigationView navView = findViewById(R.id.navBar);
        navView.setSelectedItemId(R.id.animalList);
        navView.setOnNavigationItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case R.id.form:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.animalList:
                    return true;
            }
            return false;
        });


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        queue = Volley.newRequestQueue(this);
        customAdapter = new CustomAdapter(AnimalActivity.this, animalList, queue);
        recyclerView.setAdapter(customAdapter);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, myUrl, null,
                response -> {
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
                }, error -> Toast.makeText(AnimalActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show());
        queue.add(request);
    }

    //Refreshes the application
    public void update() {
        finish();
        overridePendingTransition(0,0);
        startActivity(getIntent());
        overridePendingTransition(0,0);
    }

}
