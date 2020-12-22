package com.example.mask_app_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.AtomicFile;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.mask_app_java.model.Store;
import com.example.mask_app_java.model.StoreInfor;
import com.example.mask_app_java.repository.MaskService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        final StoreAdapter adapter = new StoreAdapter(this);
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
                    Log.d(TAG, "REFRESH");
                    List<Store> items = response.body().getStores();

                    /*
                        List<Store> result = new ArrayList<>();
                    for(int i = 0; i < items.size(); i++) {
                        Store store = items.get(i);
                        if (store.getRemainStat() != null){
                            result.add(store);
                        }
                    }   // 아래의 코드와 같은 의미임
                     */
                    
                    items = items.stream()
                            .filter(item -> item.getRemainStat() != null)
                            .collect(Collectors.toList());

                    adapter.UpdateItems(items);
                    //동기방식으로 값 가져옴(response를 반환받고 그 안에 body가 있음)
                    //스트림 기능으로 item 중에 remainstar이 null과 값이 없는 것이 아닌 데이터를 필터링하여 다시 (리스트 형태로 만듬 : collect)
                    getSupportActionBar().setTitle("마스크 보유 약국 : " + items.size() + "곳");
                    // 상단의 액션바를 얻고. 타이틀을 설정
                }

                @Override
                public void onFailure(Call<StoreInfor> call, Throwable t) {
                    // 요청을 실패할 경우
                    Log.e(TAG, "onFailure : "+t);

                }
            });
            // enqueue는 비동기로 동작
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}