package com.example.mask_app_java;

import com.example.mask_app_java.model.StoreInfor;
import com.example.mask_app_java.repository.MaskService;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void retrofitTest() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MaskService.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                // 모시 형식으로 변형
                .build();

        MaskService service = retrofit.create(MaskService.class);

        Call<StoreInfor> storeInforCall = service.fetchStoreInfo();
        // 데이터를 받아올 준비를 함

        StoreInfor storeInfor = storeInforCall.execute().body();
        //동기방식으로 값 가져옴(response를 반환받고 그 안에 body가 있음)

        assertEquals(222, storeInfor.getCount());
        assertEquals(222, storeInfor.getStores().size());
    }
}