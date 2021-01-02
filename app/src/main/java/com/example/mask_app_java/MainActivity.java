package com.example.mask_app_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.AtomicFile;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mask_app_java.model.Store;
import com.example.mask_app_java.model.StoreInfor;
import com.example.mask_app_java.repository.MaskService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

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

    private MainViewModel viewModel;
    // viewModel 객체를 생성

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "OnCreate");

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // 초기화

        // TedPermission (Make PermissionListener)
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                perfromAction();
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        // TedPermission (Start TedPermission)
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    @SuppressLint("MissingPermission")
    private void perfromAction() {
        
        // 실질적인 위치정보를 받아오는 리스너와 메서드
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.d(TAG, "getLatitude : "+location.getLatitude());
                            Log.d(TAG, "getLongitude : "+location.getLongitude());
                            
                            viewModel.setLocation(location);
                            Log.d(TAG, "fetchStoreInfor() 호출");
                            viewModel.fetchStoreInfor();    //약국 정보 출력
                        }
                    }
                });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this, RecyclerView.VERTICAL, false));

        final StoreAdapter adapter = new StoreAdapter(this);
        recyclerView.setAdapter(adapter);

        // UI 변경을 감지하여 업데이트
        // viewModel에서 변경된 stores 정보를 받음
        viewModel.getItemLiveData().observe(this, stores -> {
            Log.d(TAG, "LiveData 관찰 중");
            adapter.UpdateItems(stores);
            getSupportActionBar().setTitle("마스크 보유 약국 : " + stores.size() + "곳");
            // 상단의 액션바를 얻고. 타이틀을 설정
        });

        ProgressBar progressBar = findViewById(R.id.progressBar);
        viewModel.getProgressLiveData().observe(this, loading -> {
            if (loading){
                progressBar.setVisibility(View.VISIBLE);
            }else if (!loading){
                progressBar.setVisibility(View.GONE);
            }
        });

        //viewModel.fetchStoreInfor();
        // 실질적으로 데이터를 요청
        // 라이브러리 특성상 화면 전환될 때 마다 새로 만들어지는 Activity로 인해 계속 요청을
        // 하기에 ViewModel에서 생성자로 ViewModel 최초 생성 시 한 번의 요청으로 사용하도록 수정
        // 화면을 전환해도 refresh Log가 출력되지 않는 것을 확인했음
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
                viewModel.fetchStoreInfor();
                // 리프래쉬 버튼으로 재요청을 시킴
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "OnDestroy");
        super.onDestroy();
    }
}


/**

 // 안드로이드에선 네트워크 처리를 할 때 비동기로 작업하도록 강제가 되어있음
 storeInforCall.enqueue(new Callback<StoreInfor>() {
@Override
public void onResponse(Call<StoreInfor> call, Response<StoreInfor> response) {
// 요청을 성공할 경우
Log.d(TAG, "REFRESH");
List<Store> items = response.body().getStores();


    //List<Store> result = new ArrayList<>();
    //for(int i = 0; i < items.size(); i++) {
        //Store store = items.get(i);

        //if (store.getRemainStat() != null){
            //result.add(store);
        //}
    //}   // 아래의 코드와 같은 의미임


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

 */