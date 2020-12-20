package com.example.mask_app_java.repository;

import com.example.mask_app_java.model.StoreInfor;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MaskService {
    String BASE_URL = "https://gist.githubusercontent.com/junsuk5/bb7485d5f70974deee920b8f0cd1e2f0/raw/063f64d9b343120c2cb01a6555cf9b38761b1d94/";

    @GET("sample.json")
    Call<StoreInfor> fetchStoreInfo();
}
