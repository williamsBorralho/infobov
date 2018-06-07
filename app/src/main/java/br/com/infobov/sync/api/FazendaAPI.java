package br.com.infobov.sync.api;

import java.util.List;

import br.com.infobov.sync.domain.Estado;
import br.com.infobov.sync.domain.Fazenda;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FazendaAPI {

    @GET("rest/{id}")
    Call<Fazenda> getFazenda(@Path("id") Integer id);

    @GET("rest/all")
    Call<List<Fazenda>> getAll();

    @GET("rest/AllEst")
    Call<List<Estado>> getAllEstados();

    @POST("rest/FltProp")
    Call<List<Fazenda>> getPropByFiltro(@Body FiltroFazenda filtro);

}
