package br.com.infobov.network;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.infobov.sync.domain.Fazenda;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FazendaNetworkUtils {

    private Context context  ;
    private List<Fazenda> fazendas ;
    private Activity activity ;

    public FazendaNetworkUtils(Context context, Activity activity , List<Fazenda> fazendas) {
        this.context = context;
        this.fazendas = fazendas;
        this.activity = activity;
    }

    public FazendaNetworkUtils processRestFazendas(Call<List<Fazenda>> call) {
        call.enqueue(new Callback<List<Fazenda>>() {
            @Override
            public void onResponse(Call<List<Fazenda>> call, Response<List<Fazenda>> response) {
                if (response.body() != null) {
                    List<Fazenda> resp = response.body();
                    if (resp != null && resp.size() > 0) {
                        fazendas = new ArrayList<>();
                        fazendas.addAll(resp);
                        ((OnBeforeResult) activity ).before();
                    } else {
                        Toast.makeText(context , "Nem um resultado encontrado para sua pesquisa.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "IB_FAZENDA_ERR", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Fazenda>> call, Throwable t) {
                Log.i("IB_ERROR", t.getMessage());
            }
        });
        return this ;
    }

    public interface OnBeforeResult {
        public void before() ;
    }
}
