package br.com.infobov.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.infobov.activities.ibovmobile.R;
import br.com.infobov.network.ProcessGSONRespUtils;
import br.com.infobov.network.RetrofitFactory;
import br.com.infobov.sync.api.EstadoDeserializer;
import br.com.infobov.sync.api.FazendaDeserializer;
import br.com.infobov.sync.api.RestAPI;
import br.com.infobov.sync.domain.Estado;
import br.com.infobov.sync.domain.Fazenda;

public class SplashActivity extends Activity implements ProcessGSONRespUtils.ProcessGSONEvents {

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
        new ProcessGSONRespUtils<Estado>("ESTADO", getBaseContext(), this, this.estados = new ArrayList<>())
                .processData(retrofitFactory.getRetrofit().create(RestAPI.class).getAllEstados());
    }

    private void preparaFazendas() {
        retrofitFactory = new RetrofitFactory();
        retrofitFactory.builCall(Fazenda.class, new FazendaDeserializer());
        new ProcessGSONRespUtils<Fazenda>("FAZENDA", getBaseContext(), this, this.fazendas = new ArrayList<>())
                .processData(retrofitFactory.getRetrofit().create(RestAPI.class).getAll());

    }

    private void goPrincipal() {
        Intent maindItent = new Intent(SplashActivity.this, MainActivity.class);
        maindItent.putExtra(FAZENDAS, (Serializable) this.fazendas);
        maindItent.putExtra(ESTADOS, (Serializable) this.estados);
        SplashActivity.this.startActivity(maindItent);
        SplashActivity.this.finish();
    }

    @Override
    public void after(String tag, ProcessGSONRespUtils process) {
        List data = process.getData();
        if (tag.equals("FAZENDA")) {
            preparaEstados();
            this.fazendas.addAll(data) ;
        } else if (tag.equals("ESTADO")) {
            this.estados.addAll(data) ;
            goPrincipal();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                }
//            }, SPLASH_DISPLAY_LENGTH);
        }

    }

    @Override
    public void before(String tag, ProcessGSONRespUtils process) {

    }
}
