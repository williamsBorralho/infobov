package br.com.infobov.network;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessGSONRespUtils<T> {


    private String tag;
    private Context context;
    private List<T> data;
    private Activity activity;
    private ProcessGSONRespUtils<T> process = this;

    public ProcessGSONRespUtils(String tag, Context context, Activity activity, List<T> data) {
        this.tag = tag;
        this.context = context;
        this.data = data;
        this.activity = activity;
    }

    public void processData(Call<List<T>> call) {
        ((ProcessGSONEvents) activity).before(tag, process);
        call.enqueue(new Callback<List<T>>() {
            @Override
            public void onResponse(Call<List<T>> call, Response<List<T>> response) {
                if (response.body() != null) {
                    List<T> resp = response.body();
                    if (resp != null && resp.size() > 0) {
                        data = new ArrayList<>();
                        data.addAll(resp);
                    } else {
                        Toast.makeText(context, "Nem um registro foi encontrado !", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.i("IB_ERRO ", getClass().getName());
                }
                ((ProcessGSONEvents) activity).after(tag, process);
            }

            @Override
            public void onFailure(Call<List<T>> call, Throwable t) {
                Log.i("IB_ERROR", t.getMessage());
                ((ProcessGSONEvents) activity).after(tag, process);
            }
        });
    }

    public List<T> getData() {
        return data;
    }

    public interface ProcessGSONEvents {
        public void after(String tag, ProcessGSONRespUtils processGSONRespUtils);

        public void before(String tag, ProcessGSONRespUtils processGSONRespUtils);
    }
}
