package com.example.mask_app_java.repository;

import com.example.mask_app_java.model.StoreInfor;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MaskService {
    //String BASE_URL = "https://gist.githubusercontent.com/junsuk5/bb7485d5f70974deee920b8f0cd1e2f0/raw/063f64d9b343120c2cb01a6555cf9b38761b1d94/";
    String BASE_URL = "https://raw.githubusercontent.com/GONI95/sample/main/https%3A/sjs4209.cafe24.com/";
    // cafe24 서버에 있는 json 파일을 Github에 연결되어 있는 파일로 가져오도록 했음

    @GET("sample.json")
    // www -> sample.json 파일
    Call<StoreInfor> fetchStoreInfo();  // 약국 위치정보 요청

    /*
     @GET("sample.json/?m=5000")
    // www -> sample.json 파일
    Call<StoreInfor> fetchStoreInfo(@Query("lat") double lat,
                                    @Query("lng") double lng);  // 약국 위치정보 요청
     */
}

/** 요청
 @GET("sample.json")
 suspend fun fetchStoreInfo(
 @Query("lat") lat: Double,
 @Query("lng") lng: Double
 ): StoreInfo
 */