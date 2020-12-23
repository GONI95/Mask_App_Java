# Mask_App_Java

### 1. Retrofit과 Moshi를 이용해 서버에 저장된 sample.json에 요청, 응답을 받아 Mask 재고를 Recycler View로 출력시켜주는 마스크 재고 앱

### 2. 라이브러리 추가(Module 수준의 Gradle)
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



### 3. Retrofit 사용을 위한 가이드 
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  [Retrofit](https://square.github.io/retrofit/)

### 4. Json 형식의 데이터를 Java 객체(Model)로 변환해주는 사이트
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  [Jsonschema2pojo](http://www.jsonschema2pojo.org/)
<pre>
  <b>1. 해당 사이트에 응답 시 필요로 하는 json 데이터 형식을 입력합니다.</b>
  <b>2. Target language : java</b>
  <b>3. Source type : JSON</b>
  <b>4. Annotation style : Moshi</b>
        Moshi를 사용하므로 Moshi 선택
  <b>5. Use primitive types (Check)</b>
        체크하지 않으면 래퍼 클래스를 사용하게 됩니다.
        primitive type(기본 자료형)
        wrapper class(기본 자료형을 객체로 다루는 클래스)
  <b>6. Use double numbers (Check)</b>
        위도, 경도가 있으니 double이 체크되어야 합니다.
  <b>7. Include getters and setters (Check)</b>
  <b>8. Zip 버튼을 클릭해서 프로젝트 패키지로 복붙</b>
</pre>


  
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
  <b>1. com 패키지를 사용하여 Test </b>
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
  <b>5. Adapter 클래스로 ArrayList 정보를 전달합니다. </b>
        전형적인 코드와 다른 부분이라면 ArrayList의 값이 변경되는 경우 Adapter 객체를 통해 ArrayList 전달하는 것이 비효율적이기 때문에 UpdateItems() 메서드로 ArrayList 만 넘겨서 동적으로 데이터를 변경합니다.
        <code>
        public void UpdateItems(List<Store> items){
          mItems = items;
          notifyDataSetChanged(); //UI 갱신
        }
        </code>
   <b>6. 출력 확인하기 </b>
        <code>
         List<Store> items = new ArrayList<>();
         Store store = new Store();
         store.setAddr("약국 주소");
         store.setName("약국 명");
         
         items.add(store);
         items.add(store);
         items.add(store);
         </code>
</pre>

### 8. ViewModel



