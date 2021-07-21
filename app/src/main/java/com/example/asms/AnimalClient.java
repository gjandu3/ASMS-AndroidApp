package com.example.asms;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

// The interface that the api works with via Retrofit
public interface AnimalClient {
    @POST("animals")
    Call<PostAnimal> createAnimal(@Body final PostAnimal postAnimal);

}
