package br.com.infobov.activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import br.com.infobov.activities.ibovmobile.R;
import br.com.infobov.sync.api.RestAPI;
import br.com.infobov.sync.api.FazendaDeserializer;
import br.com.infobov.sync.domain.Fazenda;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AllPropMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static String API = "http://192.168.1.6/infobov/" ;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_prop_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Gson gson = new GsonBuilder().registerTypeAdapter(Fazenda.class, new FazendaDeserializer()).create() ;
        Retrofit retrofit =  new Retrofit
                .Builder()
                .baseUrl(API)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build() ;
        RestAPI fazendaAPI =  retrofit.create(RestAPI.class);
        Call<Fazenda> call = fazendaAPI.getFazenda(3);
        call.enqueue(new Callback<Fazenda>() {
            @Override
            public void onResponse(Call<Fazenda> call, Response<Fazenda> response) {
                Fazenda fazenda = response.body();
                Log.i("TAG" , fazenda.getNome());
            }

            @Override
            public void onFailure(Call<Fazenda> call, Throwable t) {
                Log.i("ERROR: " , t.getMessage());
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .build();





}
