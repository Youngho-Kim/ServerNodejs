package com.example.kwave.servernodejs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public class MainActivity extends AppCompatActivity {

    private Button btnWrite ;
    private RecyclerView recyclerView;
    private List<Bbs> data;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lambdaTest();
        initView();
        data = new ArrayList<>();
        adapter = new RecyclerAdapter(this,data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loader();
    }

    private void lambdaTest() {
        new Thread(
                () -> Log.i("Lambda", "======================================== running OK")
        ).start();
    }

    private void initView() {
        btnWrite  = (Button) findViewById(R.id.button);
        recyclerView = (RecyclerView) findViewById(R.id.list);
    }

    private void loader(){
        // 1. 레트로핏 생성
        Retrofit client = new Retrofit.Builder()
                .baseUrl(MyServer.SERVER)
//                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        // 2. 서비스 연결
        MyServer myServer = client.create(MyServer.class);

        // 3. 서비스의 특정 함수 호출  -> observable 생성
        Observable<ResponseBody> observable = myServer.read();

        // 4. subscribe 등록
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        responseBody -> {
                            // 1. 데이터를 꺼내고
                            String jsonString = responseBody.string();
//                           Log.e("Retrofit","data"+jsonString);
                            Gson gson = new Gson();
//                          Type type = new TypeToken<List<Bbs>>(){}.getType(); // 컨버팅 하기 위한 타입지정
//                          List<Bbs> data = gson.fromJson(jsonString, type);
//                          ==>
//                            Log.e("jsonString","jsonString"+jsonString);
                            Bbs data[] = gson.fromJson(jsonString,Bbs[].class);
                            Log.e("data[]","data[]"+data);

                            // 2. 데이터를 아답터에 세팅하고
                            for(Bbs bbs : data){
                            this.data.add(bbs);
                            }
                            // 3. 아답터 갱신
                            adapter.notifyDataSetChanged();
                        }
                );
    }

    interface MyServer{
        public static final String SERVER = "http://192.168.10.68/";

        @GET("bbs")
        public Observable<ResponseBody> read();

        @POST("bbs")
        public void write(Bbs bbs);

        @PUT("bbs")
        public void update(Bbs bbs);

        @DELETE("bbs")
        public void delete(Bbs bbs);

    }
}
