package com.okbiometry.okbiometry.interfaces;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MyApiService {


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })

    @POST("Login")
    Call<Object> Login(@Body JsonObject body);

    @POST("User")
    Call<Object> User(@Body JsonObject body);

}