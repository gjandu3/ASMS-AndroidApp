package com.example.asms;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Validator.ValidationListener,
        AdapterView.OnItemSelectedListener{

    private Button uploadButton;
    private Button submit;
    private Validator validator;
    String[] Species = {"Species", "Dog", "Cat", "Other"};
    String[] intakeReason = {"Intake Reason", "Stray", "Seized", "Surrendered"};
    private ImageView previewImage;
    Spinner spin;
    Spinner spinner;
    Bitmap bitmap;
    String encodeImage;

    @NotEmpty
    private EditText editTextName;

    @NotEmpty
    private EditText editTextBreed;

    @NotEmpty
    private EditText editTextAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadButton = findViewById(R.id.UploadButton);
        submit = findViewById(R.id.Submit);
        previewImage = findViewById(R.id.ImagePreview);
        validator = new Validator(this);
        validator.setValidationListener((Validator.ValidationListener) this);

        spin = findViewById(R.id.Species);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Species);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(ad);

        spinner = findViewById(R.id.intakeReason);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter ad2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, intakeReason);
        ad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad2);

        initView();
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

        BottomNavigationView navView = findViewById(R.id.navBar);
        navView.setSelectedItemId(R.id.form);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.animalList:
                        startActivity(new Intent(getApplicationContext(), AnimalActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.form:
                        return true;
                }
                return false;
            }
        });

    }

    public void initView() {
        editTextName = findViewById(R.id.Name);
        editTextBreed = findViewById(R.id.Breed);
        editTextAge = findViewById(R.id.Age);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previewImage.getDrawable() == null) {
                    Toast.makeText(MainActivity.this, "Please upload a photo", Toast.LENGTH_SHORT).show();
                }
                else if ((spin.getSelectedItem().toString().trim().equals("Species"))) {
                    Toast.makeText(MainActivity.this, "Please select a Species", Toast.LENGTH_SHORT).show();
                }
                else if ((spinner.getSelectedItem().toString().trim().equals("Intake Reason"))) {
                    Toast.makeText(MainActivity.this, "Please select an Intake Reason", Toast.LENGTH_SHORT).show();
                } else {
                    validator.validate();
                }
            }
        });
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(result);
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            previewImage.setImageBitmap(bitmap);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] bytes = byteArrayOutputStream.toByteArray();
                            encodeImage = Base64.encodeToString(bytes, Base64.DEFAULT);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    public void onValidationSucceeded() {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url =  "https://asms.herokuapp.com/animals";
        String textName = editTextName.getText().toString();
        String textBreed = editTextBreed.getText().toString();
        String textAge = editTextAge.getText().toString();
        String textSpecies = spin.getSelectedItem().toString();
        String textIntakeReason = spinner.getSelectedItem().toString();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("true")){
                            Toast.makeText(MainActivity.this, "Uploaded Successful", Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(0,0);
                            startActivity(getIntent());
                            overridePendingTransition(0,0);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(0,0);
                            startActivity(getIntent());
                            overridePendingTransition(0,0);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Some error occurred -> "+error, Toast.LENGTH_SHORT).show();
                }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("selectedFile", encodeImage);
                parameters.put("Name", textName);
                parameters.put("Species", textSpecies);
                parameters.put("Breed", textBreed);
                parameters.put("Age", textAge);
                parameters.put("IntakeReason", textIntakeReason);
                parameters.put("Status", "Under Evaluation");
                return parameters;
            }
        };
        queue.add(postRequest);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error: errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}