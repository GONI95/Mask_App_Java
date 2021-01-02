package com.example.mask_app_java;

import android.location.Location;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mask_app_java.model.Store;
import com.example.mask_app_java.model.StoreInfor;
import com.example.mask_app_java.repository.MaskService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private Location location;
    private String distance_unit = "k";

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    private MutableLiveData<List<Store>> itemLiveData = new MutableLiveData<>();
    // null 값으로 초기화

    public MutableLiveData<List<Store>> getItemLiveData() {
        Log.d(TAG, "getItemLiveData()" + itemLiveData);
        return itemLiveData;
    }

    public void setItemLiveData(MutableLiveData<List<Store>> itemLiveData) { this.itemLiveData = itemLiveData; }

    private MutableLiveData<Boolean> progressLiveData = new MutableLiveData<>();

    public MutableLiveData<Boolean> getProgressLiveData() { return progressLiveData; }

    public void setProgressLiveData(MutableLiveData<Boolean> progressLiveData) { this.progressLiveData = progressLiveData; }

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(MaskService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            // 모시 형식으로 변형
            .build();

    private MaskService service = retrofit.create(MaskService.class);

    //private Call<StoreInfor> storeInforCall = service.fetchStoreInfo();

    public MainViewModel() {
        Log.d(TAG, "Create");
        //fetchStoreInfor();
    }

    // 안드로이드에선 네트워크 처리를 할 때 비동기로 작업하도록 강제가 되어있음
    public void fetchStoreInfor() {
        Log.d(TAG, "fetchStoreInfor");
        // 로딩을 시작하는 시작점
        progressLiveData.setValue(true);
        
        service     //위치정보 받아와 전달하며 호출하기위해 코드 변경!!!
                .fetchStoreInfo()   // location.getLatitude(), location.getLongitude()
        //storeInforCall
                .clone()
                .enqueue(new Callback<StoreInfor>() {
                    // enqueue는 비동기로 동작
                    // LiveData 사용 후 화면 전환 시 앱이 꺼지는 현상이 발생. 이유는 아래에 있다.
                    // Call은 한 번만 사용할 수 있는데, 동일한 웹 서버에 동일한 매개 변수를 사용하여
                    // 여러번 호출을 하려면 clone()을 사용해야함, 따라서 clone() 메서드를 사용해
                    // 각각의 요청에 대해 사용 중인 Call 객체가 있는지 확인한다. 즉 객체를 복사해서 사용하는 것
                    // Retrofit을 만든 Jake Wharton이 직접 이렇게 해야한다고 stackoverflow에 댓글로 달았음음            @Override
                    public void onResponse(Call<StoreInfor> call, Response<StoreInfor> response) {
                        // 요청 성공
                        Log.d(TAG, "REFRESH");

                        List<Store> items = response.body().getStores().stream()
                                .filter(item -> item.getRemainStat() != null)
                                // null 값 빼는 코드
                                .collect(Collectors.toList());


                        // items의 크키만큼 반복하며 각 위치 값에 따른 items의 항목을
                        // Store 클래스의 인스턴스인 store에 대입
                        // 전달받은 위도 경도를 items 각 항목에 할당
                        for (Store store : items) {
                            double distance = LocationDistance.distance(
                                    location.getAltitude(), location.getLongitude(),
                                    store.getLat(), store.getLng(), distance_unit
                            );
                            Log.d(TAG, "items : " + items.toString());
                            store.setDistance_unit(distance);
                        }

                        Collections.sort(items);
                        // 두 지점간의 저리에 따라 정보를 정렬 하게된다.
                        itemLiveData.postValue(items);
                        // 비동기(background)에 대한 코드를 사용 시 setValue()가 아닌 postValue() 사용

                        // 로딩을 끝을 내는 끝점
                        progressLiveData.postValue(false);
                        // 비동기(background)에 대한 코드를 사용 시 setValue()가 아닌 postValue() 사용
                    }

                    @Override
                    public void onFailure(Call<StoreInfor> call, Throwable t) {
                        // 요청 실패
                        Log.e(TAG, "onFailure : " + t);
                        itemLiveData.postValue(Collections.emptyList());
                        // 빈 값의 데이터를 셋팅 : 강제 종료를 막기위해

                        progressLiveData.postValue(false);
                        // 로딩을 끝을 내는 끝점
                    }
                });
    }

}

/*
List<Store> result = new ArrayList<>();
                    for(int i = 0; i < items.size(); i++) {
                        Store store = items.get(i);
                        if (store.getRemainStat() != null){
                            result.add(store);
                        }
                    }   // 아래의 코드와 같은 의미임
//========================================================================
items = items.stream()
                        .filter(item -> item.getRemainStat() != null)
                        .collect(Collectors.toList());

 */