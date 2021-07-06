package com.example.asms;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("animals")
    Call<List<Animal>> getAnimals();
}
