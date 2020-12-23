# Mask_App_Java

### 1. Retrofit과 Moshi를 이용해 서버에 저장된 sample.json에 요청, 응답을 받아 Mask 재고를 Recycler View로 출력시켜주는 마스크 재고 앱

### 2. 라이브러리 추가(Module 수준의 Gradle)와 AndroidManifest.xml에 Internet 권한 허가
* Retrofit
<pre>
  implementation 'com.squareup.retrofit2:retrofit:2.9.0'
</pre>

* Mosshi
<pre>
  implementation 'com.squareup.retrofit2:converter-moshi:2.9.0'
</pre>

* ViewModel
<pre>
  def lifecycle_version = "2.2.0"
  implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
</pre>

* LiveData
<pre>
  implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
</pre>

* Stream API
<pre>
   coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:1.1.1'

   compileOptions {
        // 자바 8의 기능을 사용할 수 있습니다.
        coreLibraryDesugaringEnabled true
    }
</pre>

* Internet 권한 허가
<pre>
   <uses-permission android:name="android.permission.INTERNET"/>
</pre>



### 3. Retrofit 사용을 위한 가이드 
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  [Retrofit](https://square.github.io/retrofit/)

### 4. Json 형식의 데이터를 Java 객체(Model)로 변환해주는 사이트
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  [Jsonschema2pojo](http://www.jsonschema2pojo.org/)
><b>1. 해당 사이트에 응답 시 필요로 하는 json 데이터 형식을 입력합니다.</b>
><b>2. Target language : java</b>
><b>3. Source type : JSON</b>
><b>4. Annotation style : Moshi</b>
<pre>
       Moshi를 사용하므로 Moshi 선택
</pre>
  ><b>5. Use primitive types (Check)</b>
<pre>
        체크하지 않으면 래퍼 클래스를 사용하게 됩니다.
        primitive type(기본 자료형)
        wrapper class(기본 자료형을 객체로 다루는 클래스)
</pre>
><b>6. Use double numbers (Check)</b>
<pre>
        위도, 경도가 있으니 double이 체크되어야 합니다.
</pre>
  ><b>7. Include getters and setters (Check)</b>
  
  ><b>8. Zip 버튼을 클릭해서 프로젝트 패키지로 복사 붙여넣기</b>

  
### 5. Retrofit은 HTTP API를 JAVA Interface로 전환이 가능합니다. Service를 작성

><b>1. StoreInfor는 목차. 4에 의해 생성된 Model </b>
<pre>
<code>
public interface MaskService {
    String BASE_URL = "https://raw.githubusercontent.com/GONI95/sample/main/https%3A/sjs4209.cafe24.com/";
    // Retrofit 객체를 생성하여 baseUrl을 설정해줄 때 하드코딩 하지않기 위해서 변수 선언

    @GET("sample.json")
    // GET 방식(가져오기)
    Call<StoreInfor> fetchStoreInfo();
    // Interface를 호출하는 return type이 StoreInfor인 Call 객체를 받아오는 메서드
}
</code>
</pre>

### 6. ViewModel, RecyclerView를 사용하기 전 응답이 제대로 도착하는지 Test

<pre>
  ><b>1. com 패키지를 사용하여 Test </b>
        com 패키지는 2가지 있는데, 일반적인 로직을 Test 할 수 있는 com (test) 패키지에서 Test를 합니다.
        com (androidTest) 패키지에선 안드로이드에서만 구현되는, 즉 안드로이드에 의존성을 지닌 UI 등을 Test하는 패키지 입니다.
  <b>2. Test Code </b>
  
  ______________________________________________________________________________________________________________________
  <code>
  @Test
    public void retrofitTest() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MaskService.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                // 모시 형식으로 변형하는 메서드
                .build();

        MaskService service = retrofit.create(MaskService.class);

        Call<StoreInfor> storeInforCall = service.fetchStoreInfo();
        // 실질적인 데이터를 받아올 준비를 마쳤습니다. 

        StoreInfor storeInfor = storeInforCall.execute().body();
        //동기방식으로 값 가져옵니다.(response를 반환받고 그 안에 body가 있음)

        assertEquals(222, storeInfor.getCount());
        assertEquals(222, storeInfor.getStores().size());
    }
  </code>
</pre>

### 7. RecyclerView

<pre>
  <b>1. RecyclerView에서 사용할 Item Layout을 생성합니다. </b>
  <b>2. RecyclerView를 띄우려는 xml에 RecyclerView를 추가합니다. </b>
  <b>3. RecyclerView Adapter 클래스를 생성하고 ViewHolder 클래스를 생성하여 코드를 작성합니다.(대부분 전형적인 코드로 기준이 잡혀있습니다.)</b>
  <b>4. RecyclerView를 Adapter와 연결시켜 줍니다.  </b>
  <code>
        final StoreAdapter adapter = new StoreAdapter(this);
            recyclerView.setAdapter(adapter);
  </code>
  <b>5. Adapter 클래스로 ArrayList 정보를 전달합니다 : MainViewModel </b>
        전형적인 코드와 다른 부분이라면 ArrayList의 값이 변경되는 경우 Adapter 객체를 통해 ArrayList 전달하는 것이 비효율적이기 때문에 UpdateItems() 메서드로 ArrayList 만 넘겨서 동적으로 데이터를 변경합니다.
        <code>
        public void UpdateItems(List<Store> items){
          mItems = items;
          notifyDataSetChanged(); //UI 갱신
        }
        </code>
   <b>6. 출력을 확인하기위해 ArrayList 전달 : MainAcitivty </b>
         <code>
         List<Store> items = new ArrayList<>();
         Store store = new Store();
         store.setAddr("약국 주소");
         store.setName("약국 명");
         
         items.add(store);
         items.add(store);
         items.add(store);
         
         adapter.UpdataItems(items);
         </code>
</pre>

### 8. ViewModel

  <b>1. MainViewModel 클래스를 생성합니다.  </b>
  
  <b>2. 변경이 가능한 LiveData 객체 생성, 즉 MutableLiveData 객체를 생성하고 getter, setter 구현 </b>
  
  <pre>
        <code> 
        private MutableLiveData<List<Store>> itemLiveData = new MutableLiveData<>();  // null 값으로 초기화
          public MutableLiveData<List<Store>> getItemLiveData() {
          return itemLiveData;
        }
        public void setItemLiveData(MutableLiveData<List<Store>> itemLiveData) {
         this.itemLiveData = itemLiveData;
        }
        </code>
  </pre>   
  
  <b>3. Retrofit 클래스를 이용해 MaskService 인터페이스의 구현을 생성합니다. </b>
  
  <pre>
  <code> 
        private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(MaskService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            // 모시 형식으로 변형
            .build();

        private MaskService service = retrofit.create(MaskService.class);

        private Call<StoreInfor> storeInforCall = service.fetchStoreInfo();
        // 데이터를 받아올 준비를 함
        </code>
  </pre>      
  
  <b>4. 실질적인 데이터를 받아오는 코드를 정의할 fetchStoreInfor를 정의 </b>
  <pre>
        안드로이드에선 네트워크 처리를 할 때 비동기로 작업하도록 강제되어 있습니다. execute()는 동기 방식이며, 비동기 방식인 enqueue()를 사용합니다.
        fetchStoreInfor()를 외부에서 호출하게되면 처리한 후 다시 호출한 곳으로 돌려줘야합니다. 그렇게 되면 Interface를 가지고 또 Callback 구현해야합니다.
        LiveData를 사용하니 그런 작업 필요없이 Activity에서 데이터를 관찰하다 변경점을 캐치하여 데이터를 변경해주면 됩니다. 단 문제가 하나 발생하게 되는데
        화면 전환 시 Call은 한 번만 사용할 수 있는데, 요청에 대한 Call 객체가 이미 있다며 앱이 꺼지는 문제가 발생합니다.
        해당 문제는 아래 링크를 통해 해결할 수 있습니다.
  </pre>
        
        [Already_executed](https://stackoverflow.com/questions/35093884/retrofit-illegalstateexception-already-executed/) 
        
  <pre>     
        <code>
        public void fetchStoreInfor() {
        // 안드로이드에선 네트워크 처리를 할 때 비동기로 작업하도록 강제가 되어있음
        storeInforCall.clone().enqueue(new Callback<StoreInfor>() {
            // 각각의 요청에 대해 사용 중인 Call 객체가 있는지 확인한다. 즉 객체를 복사해서 사용하는 것
            // Retrofit을 만든 Jake Wharton이 직접 이렇게 해야한다고 stackoverflow에 댓글로 달았음음            @Override
            public void onResponse(Call<StoreInfor> call, Response<StoreInfor> response) {
                // 요청 성공
                Log.d(TAG, "REFRESH");
                List<Store> items = response.body().getStores().stream()
                        .filter(item -> item.getRemainStat() != null)
                        .collect(Collectors.toList());

                itemLiveData.postValue(items);
                // 비동기(background)에 대한 코드를 사용 시 setValue()가 아닌 postValue() 사용
            }

            @Override
            public void onFailure(Call<StoreInfor> call, Throwable t) {
                // 요청 실패
                Log.e(TAG, "onFailure : "+t);
                itemLiveData.postValue(Collections.emptyList());
                // 빈 값의 데이터를  : 강제 종료를 막기위해

            }
        });
        // enqueue는 비동기로 동작
        } 

        </code>   
         
   <b>5. MainViewModel 객체 생성 : MainActivity </b>
   <code> 
         private MainViewModel viewModel;
            ...
         Oncreate(){
           viewModel = new ViewModelProvider(this).get(MainViewModel.class);
         }
   </code>
        
   <b>6. LiveData 읽어오기위한 Observe </b>
   
   <code>
        // UI 변경을 감지하여 업데이트
        viewModel.getItemLiveData().observe(this, stores -> {
            adapter.UpdateItems(stores);
            getSupportActionBar().setTitle("마스크 보유 약국 : " + stores.size() + "곳");
            // 상단의 액션바를 얻고. 타이틀을 설정
        });
  
        viewModel.fetchStoreInfor();
   </code>
   
   <b>7. 리팩토링 (6에서 정의한 MainActivity Lifecycle 로 인해 화면전환 시 viewModel.fetchStoreInfor();가 실행되면서 새로 요청하고 데이터를 다시 쓰는 문제점) </b>
         화면전환 시 MainActivity Lifecycle이 새로 돌면서 viewModel.fetchStoreInfor(); 코드로 인해 fetchStoreInfor()를 다시 호출하고 데이터를 화면에 띄우게 됩니다. 
         MainViewModel에서 생성자를 통해 fetchStoreInfor()를 실행되도록 변경하여 화면 전환 시 발생하는 위의 문제를 해결할 수 있습니다.
         
         ViewModel 객체는 생성자를 통해 SavedStateHandle 객체를 받는데, SavedStateHandle는 저장된 상태에 객체를 작성, 검색 등을 할 수 있게 하는 Key-Value Map 이며,
         시스템에 의해 종료된 후에도 유지되기 때문에 데이터를 새로 쓰지않고 저장된 데이터를 불러오게 됩니다.
         
         <code>
         public MainViewModel() {
           fetchStoreInfor();
         }
         </code>
</pre>
