package com.example.kwave.servernodejs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
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

    }

    interface myServer{
        public static final String SERVER = "http://192.168.10.68/";

        @GET("bbs")
        public Observable<Bbs> read();

        @POST("bbs")
        public void write(Bbs bbs);

        @PUT("bbs")
        public void update(Bbs bbs);

        @DELETE("bbs")
        public void delete(Bbs bbs);

    }
}
