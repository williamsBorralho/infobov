package br.com.infobov.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import br.com.infobov.activities.ibovmobile.R;
import br.com.infobov.adapters.EstadoAdapterLv;
import br.com.infobov.events.EstadoOnClickEventListView;
import br.com.infobov.network.NetworkHelper;
import br.com.infobov.sync.api.EstadoDeserializer;
import br.com.infobov.sync.api.RestAPI;
import br.com.infobov.sync.api.FazendaDeserializer;
import br.com.infobov.sync.api.FiltroFazenda;
import br.com.infobov.sync.api.TipoFiltro;
import br.com.infobov.sync.domain.Estado;
import br.com.infobov.sync.domain.Fazenda;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        DialogFragmentFiltroMapa.OnFragmentInteractionListener, EstadoOnClickEventListView.EstadoOnClickItem {
    private View progressBar;
    private Gson gson;
    private Retrofit retrofit;
    private Context context = this;
    private GoogleMap mMap;
    private LatLngBounds.Builder builder;
    private List<Fazenda> fazendas;
    private List<Estado> estados;
    private BottomNavigationView navBottom;
    private AlertDialog.Builder dialogFiltro;
    private BottomSheetBehavior mBottomSheetBehavior;
    private View bottomFiltro;
    private EditText mEdtxtPalavraChave;
    private Button btnFiltrar;
    private EstadoAdapterLv mEstadoAdapterLv;
    private Spinner spinnerEstados, spinnerMunicipios, spinnerTipoFiltro;
    private EstadoAdapterLv estadoAdapterLv;
    private ImageView imvSatelite;
    private ImageView imvPadrao;

    //Filtros
    private Estado estadoSelected;
    private String tipoFiltro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validaConecoes();
        progressBar = findViewById(R.id.progressbar_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bottomFiltro = findViewById(R.id.bottom_filtro);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomFiltro);
        mBottomSheetBehavior.setPeekHeight(120);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEdtxtPalavraChave.getWindowToken(), 0);
                }
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                }

                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        navBottom = findViewById(R.id.navigation);
        navBottom.setOnNavigationItemSelectedListener(onClickMenuBottomBar);

        mEdtxtPalavraChave = findViewById(R.id.palavra_chave);
        btnFiltrar = findViewById(R.id.filtrar);
        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrarPropriedades();
            }
        });

        imvPadrao = findViewById(R.id.padrao);
        imvSatelite = findViewById(R.id.satelite);

        imvPadrao.setOnClickListener(alterarTipoMapa);
        imvSatelite.setOnClickListener(alterarTipoMapa);

        initListViewEstados();
        initTipoFiltro();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.nav_manage) {
//        } else
        if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        restAllProp();
    }

    private void validaConecoes() {
        if (!NetworkHelper.isConnect(this)) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Você precisa ativar sua conexação com a internet!");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ativar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(i);
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            alertDialog.show();

            return;
        }
    }

    private LatLngBounds preparaLimites(List<Marker> mks) {
        builder = new LatLngBounds.Builder();
        for (Marker marker : mks) {
            builder.include(marker.getPosition());
        }
        return builder.build();
    }

    private List<MarkerOptions> prepataMarkers() {
        List<MarkerOptions> mop = new ArrayList<>();
        for (Fazenda fa : fazendas) {
            LatLng propGeo = new LatLng(fa.getLatitude().doubleValue(), fa.getLongitude().doubleValue());
            mop.add(new MarkerOptions().position(propGeo).title(fa.getNome()));
        }
        return mop;
    }

    private void restAllProp() {
        preparaChamada(Fazenda.class, new FazendaDeserializer());
        processaChamadaResposa(retrofit.create(RestAPI.class).getAll());
    }

    private void criarMarkersOnMap() {
        List<MarkerOptions> markerOptions = prepataMarkers();
        List<Marker> mks = new ArrayList<>();
        for (MarkerOptions mop : markerOptions) {
            mks.add(mMap.addMarker(mop));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(preparaLimites(mks), 300));
    }

    public void filtrarPropriedades() {
        String pChave = mEdtxtPalavraChave.getText().toString();
        TipoFiltro filtro = TipoFiltro.byDescricao(tipoFiltro);
        String uf = estadoSelected != null ? estadoSelected.getUf().equals("Estado") ? null : estadoSelected.getUf() : null;
        processaChamadaResposa(retrofit.create(RestAPI.class).getPropByFiltro(new FiltroFazenda((filtro == null ? 0 : filtro.getCodigo()), pChave, uf)));
    }

    private void preparaChamada(Class clazz, Object typeAdapter) {
        gson = new GsonBuilder().registerTypeAdapter(clazz, typeAdapter).create();
        retrofit = new Retrofit
                .Builder()
                .baseUrl(NetworkHelper.getDomain())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    private void processaChamadaResposa(Call<List<Fazenda>> call) {

        call.enqueue(new Callback<List<Fazenda>>() {
            @Override
            public void onResponse(Call<List<Fazenda>> call, Response<List<Fazenda>> response) {
                if (response.body() != null) {
                    List<Fazenda> resp = response.body();
                    if (resp != null && resp.size() > 0) {
                        mMap.clear();
                        fazendas = new ArrayList<>();
                        fazendas.addAll(resp);
                        criarMarkersOnMap();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(context, "Nem um resultado encontrado para sua pesquisa.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Erro >>> ", Toast.LENGTH_LONG).show();
                }
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

            @Override
            public void onFailure(Call<List<Fazenda>> call, Throwable t) {
                Log.i("ERRO", t.getMessage());

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void getEstadoOnList(Estado e) {
        this.estadoSelected = e;
    }

    public void initListViewEstados() {
        estados = new ArrayList<>();
        estados.add(new Estado("Estado", ""));

        preparaChamada(Estado.class, new EstadoDeserializer());
        getEstadoRest(retrofit.create(RestAPI.class).getAllEstados());
    }


    public void initTipoFiltro() {
        spinnerTipoFiltro = findViewById(R.id.spinner_tipo_filtro);
        spinnerTipoFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoFiltro = (String) parent.getItemAtPosition(position);
                mEdtxtPalavraChave.setHint(tipoFiltro.startsWith("Filtrar") ? "Todas" : tipoFiltro);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getEstadoRest(Call<List<Estado>> call) {
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<List<Estado>>() {
            @Override
            public void onResponse(Call<List<Estado>> call, Response<List<Estado>> response) {
                if (response.body() != null) {
                    List<Estado> resp = response.body();
                    if (resp != null && resp.size() > 0) {
                        estados.addAll(resp);
                        spinnerEstados = findViewById(R.id.spinner_estados);
                        spinnerMunicipios = findViewById(R.id.spinner_municipios);
                        estadoAdapterLv = new EstadoAdapterLv(context, estados);
                        spinnerEstados.setAdapter(estadoAdapterLv);
                        spinnerEstados.setOnItemSelectedListener(new EstadoOnClickEventListView((Activity) context, estadoAdapterLv));
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(context, "Nem um resultado encontrado para sua pesquisa.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Estado>> call, Throwable t) {
                Log.i("ERRO", t.getMessage());
            }
        });
    }

    private void processaOnClickBottomBar(int id) {
        switch (id) {
            case R.id.action_item1:
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case R.id.action_item3:
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    restAllProp();
                } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    spinnerTipoFiltro.setSelection(0);
                    spinnerEstados.setSelection(0);
                    spinnerMunicipios.setSelection(0);
                    mEdtxtPalavraChave.setHint("Todas");
                }
                break;
        }
    }

    private View.OnClickListener alterarTipoMapa = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.satelite) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            } else if (v.getId() == R.id.padrao) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener onClickMenuBottomBar = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            processaOnClickBottomBar(item.getItemId());
            return false;
        }
    };


    class TaskAllPropriedades extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
//            listView.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressBar.setVisibility(View.GONE);
//            listView.setVisibility(View.VISIBLE);
//            adapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("MAP", "Carregando Mapa ");

            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
