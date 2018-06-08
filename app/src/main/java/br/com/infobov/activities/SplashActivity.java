package br.com.infobov.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.infobov.activities.ibovmobile.R;
import br.com.infobov.network.FazendaNetworkUtils;
import br.com.infobov.network.ProcessGSONRespUtils;
import br.com.infobov.network.RetrofitFactory;
import br.com.infobov.sync.api.EstadoDeserializer;
import br.com.infobov.sync.api.FazendaDeserializer;
import br.com.infobov.sync.api.RestAPI;
import br.com.infobov.sync.domain.Estado;
import br.com.infobov.sync.domain.Fazenda;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends Activity implements FazendaNetworkUtils.OnBeforeResult  {

    public static final String FAZENDAS = "FAZENDAS";
    public static final String ESTADOS = "ESTADOS";

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private RetrofitFactory retrofitFactory;
    private List<Fazenda> fazendas;
    private List<Estado> estados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_splash);
        preparaFazendas();

    }

    private void preparaEstados() {
        retrofitFactory = new RetrofitFactory();
        retrofitFactory.builCall(Estado.class, new EstadoDeserializer());
        new ProcessGSONRespUtils<Estado>("ESTADO" , getBaseContext() , this , this.estados)
        .processData(retrofitFactory.getRetrofit().create(RestAPI.class).getAllEstados());
    }

    private void preparaFazendas() {
        retrofitFactory = new RetrofitFactory();
        retrofitFactory.builCall(Fazenda.class, new FazendaDeserializer());
        new FazendaNetworkUtils(getBaseContext(), this, this.fazendas)
                .processRestFazendas(retrofitFactory.getRetrofit().create(RestAPI.class).getAll());
    }

    private void processRestEstados(Call<List<Estado>> call) {
        call.enqueue(new Callback<List<Estado>>() {
            @Override
            public void onResponse(Call<List<Estado>> call, Response<List<Estado>> response) {
                if (response.body() != null) {
                    List<Estado> resp = response.body();
                    if (resp != null && resp.size() > 0) {
                        estados = new ArrayList<>();
                        estados.addAll(resp);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                goPrincipal();
                            }
                        }, SPLASH_DISPLAY_LENGTH);
                    } else {
                        Toast.makeText(getBaseContext(), "Nem estado foi encontrado na base de dadsos!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Erro >>> ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Estado>> call, Throwable t) {
                Log.i("IB_ERROR", t.getMessage());
            }
        });
    }

    private void goPrincipal() {
        Intent maindItent = new Intent(SplashActivity.this, MainActivity.class);
        maindItent.putExtra(FAZENDAS, (Serializable) this.fazendas);
        maindItent.putExtra(ESTADOS, (Serializable) this.estados);
        SplashActivity.this.startActivity(maindItent);
        SplashActivity.this.finish();
    }

    @Override
    public void before() {
        preparaEstados();

    }



}
