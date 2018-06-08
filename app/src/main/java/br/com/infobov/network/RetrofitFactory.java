package br.com.infobov.network;

import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import br.com.infobov.sync.domain.Fazenda;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    private Gson gson ;
    private Retrofit retrofit ;


    /**
     * Prepara objeto retrofi e Gson
     * @param clazz
     * @param typeAdapter
     * @return
     */
    public RetrofitFactory builCall(Class clazz, Object typeAdapter) {
        gson = new GsonBuilder().registerTypeAdapter(clazz, typeAdapter).create();
        retrofit = new Retrofit
                .Builder()
                .baseUrl(NetworkHelper.getDomain())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return this ;
    }



    public Retrofit getRetrofit() {
        return retrofit;
    }

    public Gson getGson() {
        return gson;
    }
}
