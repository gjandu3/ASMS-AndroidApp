package com.example.asms;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//Main Activity Class
public class MainActivity extends AppCompatActivity implements Validator.ValidationListener,
        AdapterView.OnItemSelectedListener{

    // Class Wide variables
    private Button submit;
    private Validator validator;
    private final String[] Species = {"Species", "Dog", "Cat", "Other"};
    private final String[] intakeReason = {"Intake Reason", "Stray", "Seized", "Surrendered"};
    private ImageView previewImage;
    private Spinner spin;
    private Spinner spinner;
    private String encodeImage;


    @NotEmpty
    private EditText editTextName;

    @NotEmpty
    private EditText editTextBreed;

    @NotEmpty
    private EditText editTextAge;

    /*Initial onCreate method that initializes fields and sets the header along with listeners for
    each field*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button uploadButton = findViewById(R.id.UploadButton);
        submit = findViewById(R.id.Submit);
        previewImage = findViewById(R.id.ImagePreview);
        validator = new Validator(this);
        validator.setValidationListener(this);

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
        uploadButton.setOnClickListener(v -> mGetContent.launch("image/*"));

        BottomNavigationView navView = findViewById(R.id.navBar);
        navView.setSelectedItemId(R.id.form);
        navView.setOnNavigationItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case R.id.animalList:
                    startActivity(new Intent(getApplicationContext(), AnimalActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.form:
                    return true;
            }
            return false;
        });

    }

    //Validates field entries from user
    public void initView() {
        editTextName = findViewById(R.id.Name);
        editTextBreed = findViewById(R.id.Breed);
        editTextAge = findViewById(R.id.Age);
        submit.setOnClickListener(v -> {
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
        });
    }

    //Captures the photo that the user uploads
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        try {

                            InputStream inputStream = getContentResolver().openInputStream(result);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            previewImage.setImageBitmap(bitmap);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            byte[] bytes = byteArrayOutputStream.toByteArray();
                            encodeImage = Base64.encodeToString(bytes, Base64.DEFAULT);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    //Sends data to interface and api if data passes validation
    @Override
    public void onValidationSucceeded() {
        String namePart = editTextName.getText().toString();
        String breedPart = editTextBreed.getText().toString();
        String agePart = editTextAge.getText().toString();
        String statusPart = "Under Evaluation";
        String speciesPart = spin.getSelectedItem().toString();
        String intakeReasonPart = spinner.getSelectedItem().toString();
        String selectedFile = "data:image/jpeg;base64," + encodeImage;

        PostAnimal newAnimal = new PostAnimal(namePart, speciesPart, breedPart, agePart, statusPart, intakeReasonPart,
                selectedFile);


        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://asms.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        AnimalClient client = retrofit.create(AnimalClient.class);

        Call<PostAnimal> call = client.createAnimal(newAnimal);
        call.enqueue(new Callback<PostAnimal>() {
            @Override
            public void onResponse(Call<PostAnimal> call, Response<PostAnimal> response) {
                Toast.makeText(MainActivity.this, "It worked", Toast.LENGTH_SHORT).show();
                System.out.println(response);
            }

            @Override
            public void onFailure(Call<PostAnimal> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    //Case for when user enters data that does not pass validation
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