package br.com.infobov.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.infobov.activities.ibovmobile.R;
import br.com.infobov.network.RetrofitFactory;
import br.com.infobov.sync.api.EstadoDeserializer;
import br.com.infobov.sync.api.RestAPI;
import br.com.infobov.sync.api.FazendaDeserializer;
import br.com.infobov.sync.domain.Estado;
import br.com.infobov.sync.domain.Fazenda;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private RetrofitFactory retrofitFactory =  new RetrofitFactory();
    private List<Fazenda> fazendas ;
    private List<Estado> estados ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_splash);
        retrofitFactory.builCall(Estado.class , EstadoDeserializer.class) ;
        retrofitFactory.getRetrofit().create(RestAPI.class).getAllEstados();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginFinal();
                //                MTools.vibra(Splash.this);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void preparaEstados ( ) {
        retrofitFactory = new RetrofitFactory();
        retrofitFactory.builCall(Estado.class , EstadoDeserializer.class) ;
        retrofitFactory.getRetrofit().create(RestAPI.class).getAllEstados();
    }

    private void preparaFazendas ( ) {
        retrofitFactory = new RetrofitFactory();
        retrofitFactory.builCall(Fazenda.class , FazendaDeserializer.class) ;
        retrofitFactory.getRetrofit().create(RestAPI.class).getAll();
    }

    private void processRestFazendas(Call<List<Fazenda>> call) {
        call.enqueue(new Callback<List<Fazenda>>() {
            @Override
            public void onResponse(Call<List<Fazenda>> call, Response<List<Fazenda>> response) {
                if (response.body() != null) {
                    List<Fazenda> resp = response.body();
                    if (resp != null && resp.size() > 0) {
                        fazendas = new ArrayList<>();
                        fazendas.addAll(resp);
                    } else {
                        Toast.makeText(getBaseContext(), "Nem um resultado encontrado para sua pesquisa.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Erro >>> ", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<Fazenda>> call, Throwable t) {
                Log.i("IB_ERROR", t.getMessage());
            }
        });
    }



    private void loginFinal() {
        Intent maindItent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(maindItent);
        SplashActivity.this.finish();
    }
}
