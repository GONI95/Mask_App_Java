package com.example.mask_app_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import com.example.mask_app_java.model.Store;
import com.example.mask_app_java.model.StoreInfor;
import com.example.mask_app_java.repository.MaskService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this, RecyclerView.VERTICAL, false));

        final StoreAdapter adapter = new StoreAdapter();
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MaskService.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                // 모시 형식으로 변형
                .build();

        MaskService service = retrofit.create(MaskService.class);

        Call<StoreInfor> storeInforCall = service.fetchStoreInfo();
        // 데이터를 받아올 준비를 함

            // 안드로이드에선 네트워크 처리를 할 때 비동기로 작업하도록 강제가 되어있음
            storeInforCall.enqueue(new Callback<StoreInfor>() {
                @Override
                public void onResponse(Call<StoreInfor> call, Response<StoreInfor> response) {
                    // 요청을 성공할 경우

                    List<Store> items = response.body().getStores();
                    adapter.UpdateItems(items);
                    //동기방식으로 값 가져옴(response를 반환받고 그 안에 body가 있음)
                }

                @Override
                public void onFailure(Call<StoreInfor> call, Throwable t) {
                    // 요청을 실패할 경우
                    Log.e(TAG, "onFailure : "+t);

                }
            });
            // enqueue는 비동기로 동작

    }
}