package br.com.infobov.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.infobov.activities.ibovmobile.R;
import br.com.infobov.adapters.EstadoAdapterLv;
import br.com.infobov.adapters.MunicipioAdapterLv;
import br.com.infobov.events.EstadoOnClickEventListView;
import br.com.infobov.events.MunicipioOnClickEventListView;
import br.com.infobov.helpers.CustomInfoWindowGmaps;
import br.com.infobov.network.NetworkHelper;
import br.com.infobov.network.ProcessGSONRespUtils;
import br.com.infobov.network.RetrofitFactory;
import br.com.infobov.pojo.InfoWindowData;
import br.com.infobov.sync.api.FazendaDeserializer;
import br.com.infobov.sync.api.FiltroFazenda;
import br.com.infobov.sync.api.MunicipioDeserializer;
import br.com.infobov.sync.api.RestAPI;
import br.com.infobov.sync.api.TipoFiltro;
import br.com.infobov.sync.domain.Estado;
import br.com.infobov.sync.domain.Fazenda;
import br.com.infobov.sync.domain.Municipio;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        DialogFragmentFiltroMapa.OnFragmentInteractionListener,
        EstadoOnClickEventListView.EstadoOnClickItem,
        ProcessGSONRespUtils.ProcessGSONEvents,
        MunicipioOnClickEventListView.OnMunicipioSelected {

    public static final String FAZENDA = "FAZENDA";
    public static final String ESTADO = "ESTADO";
    public static final String MUNICIPIO = "MUNICIPIO";

    private View progressBar;
    private Context context = MainActivity.this;
    private GoogleMap mMap;
    private LatLngBounds.Builder builder;
    private List<Fazenda> fazendas;
    private List<Estado> estados;
    private List<Municipio> municipios;
    private BottomNavigationView navBottom;
    private AlertDialog.Builder dialogFiltro;
    private BottomSheetBehavior mBottomSheetBehavior;
    private View bottomFiltro;
    private EditText mEdtxtPalavraChave;
    private Button btnFiltrar;
    private Spinner spinnerEstados, spinnerMunicipios, spinnerTipoFiltro;
    private EstadoAdapterLv estadoAdapterLv;
    private MunicipioAdapterLv municipioAdapterLv;
    private ImageView imvSatelite;
    private ImageView imvPadrao;

    //Filtros
    private Estado estadoSelected;
    private Municipio municipioSelected;
    private String tipoFiltro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fazendas = (List<Fazenda>) getIntent().getSerializableExtra(SplashActivity.FAZENDAS);
        estados = new ArrayList<>();
        estados.add(new Estado("Estado", ""));
        estados.addAll((Collection<? extends Estado>) getIntent().getSerializableExtra(SplashActivity.ESTADOS));

        municipios = new ArrayList<>();
        municipios.add(new Municipio("", "Municipio"));

        validaConexoes();
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
        mBottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback);

        navBottom = findViewById(R.id.navigation);
        navBottom.setOnNavigationItemSelectedListener(onClickMenuBottomBar);

        mEdtxtPalavraChave = findViewById(R.id.palavra_chave);
        btnFiltrar = findViewById(R.id.filtrar);
        btnFiltrar.setOnClickListener(filtrarPropriedadeOnClickListener);

        imvPadrao = findViewById(R.id.padrao);
        imvSatelite = findViewById(R.id.satelite);

        imvPadrao.setOnClickListener(alterarTipoMapa);
        imvSatelite.setOnClickListener(alterarTipoMapa);

        initSpinners();
        initTipoFiltro();

    }
//
//    private void requestStoragePermission() {
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
//
//        }
//    }

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
        progressBar.setVisibility(View.GONE);
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                criarMarkersOnMap();
            }
        });
    }

    private void validaConexoes() {
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
        RetrofitFactory retrofitFactory = new RetrofitFactory();
        retrofitFactory.builCall(Fazenda.class, new FazendaDeserializer());
        new ProcessGSONRespUtils<Fazenda>("FAZENDA", getBaseContext(), this, this.fazendas = new ArrayList<>())
                .processData(retrofitFactory.getRetrofit().create(RestAPI.class).getAll());
    }

    private void criarMarkersOnMap() {
        List<MarkerOptions> markerOptions = prepataMarkers();
        List<Marker> mks = new ArrayList<>();

        for (int i = 0; i < fazendas.size(); i++) {
            Fazenda fazenda = fazendas.get(i);
            MarkerOptions mo = markerOptions.get(i);

            InfoWindowData tag = new InfoWindowData(fazenda.getNome(), fazenda.getFerro(), "F");

            CustomInfoWindowGmaps customInfoWindowGmaps = new CustomInfoWindowGmaps(context);
            mMap.setInfoWindowAdapter(customInfoWindowGmaps);

            Marker marker = mMap.addMarker(mo);
            marker.setTag(tag);
            marker.showInfoWindow();
            mks.add(marker);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(preparaLimites(mks), 300));
    }

    public void filtrarPropriedades() {
        FiltroFazenda filtro = new FiltroFazenda();
        filtro.setPalavraChave(mEdtxtPalavraChave.getText().toString());
        TipoFiltro tipofiltro = TipoFiltro.byDescricao(this.tipoFiltro);
        filtro.setTipoFiltro(tipofiltro == null ? 0 : tipofiltro.getCodigo());
        filtro.setUf(estadoSelected != null ? estadoSelected.getUf().equals("Estado") ? null : estadoSelected.getUf() : null);
        filtro.setMunicipio(municipioSelected != null ? municipioSelected.getUf().equals("Munnicipio") ? null : municipioSelected.getNome() : null);

        RetrofitFactory retrofitFactory = new RetrofitFactory();
        retrofitFactory.builCall(Fazenda.class, new FazendaDeserializer());
        new ProcessGSONRespUtils<Fazenda>("FAZENDA", getBaseContext(), this, this.fazendas = new ArrayList<>())
                .processData(retrofitFactory.getRetrofit().create(RestAPI.class).getPropByFiltro(filtro));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void getEstadoOnList(Estado e) {
        this.estadoSelected = e;
        if (!this.estadoSelected.getUf().startsWith("Est")) {
            RetrofitFactory retrofitFactory = new RetrofitFactory();
            retrofitFactory.builCall(Municipio.class, new MunicipioDeserializer());
            new ProcessGSONRespUtils<Municipio>(MUNICIPIO, getBaseContext(), this, this.municipios = new ArrayList<>())
                    .processData(retrofitFactory.getRetrofit().create(RestAPI.class).getAllMunicipiosPorUf(this.estadoSelected.getUf()));
        }
    }

    public void initSpinners() {
        spinnerEstados = findViewById(R.id.spinner_estados);
        estadoAdapterLv = new EstadoAdapterLv(context, estados);
        spinnerEstados.setAdapter(estadoAdapterLv);
        spinnerEstados.setOnItemSelectedListener(new EstadoOnClickEventListView((Activity) context, estadoAdapterLv));

        spinnerMunicipios = findViewById(R.id.spinner_municipios);
        spinnerMunicipios.setEnabled(false);
    }

    public void initTipoFiltro() {
        spinnerTipoFiltro = findViewById(R.id.spinner_tipo_filtro);
        spinnerTipoFiltro.setFocusable(true);
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
                    mEdtxtPalavraChave.setText("");
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

    private View.OnClickListener filtrarPropriedadeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            filtrarPropriedades();
        }
    };

    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
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
    };

    @Override
    public void after(String tag, ProcessGSONRespUtils process) {
        boolean estado = tag.equals(FAZENDA) || tag.equals(ESTADO);
        if (estado) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        List data = process.getData();
        if (data != null && data.size() > 0) {
            if (tag.equals(FAZENDA)) {
                mMap.clear();
                this.fazendas = new ArrayList<>();
                this.fazendas.addAll(data);
                criarMarkersOnMap();
            } else if (tag.equals(ESTADO)) {

            } else if (tag.equals(MUNICIPIO)) {
                spinnerMunicipios.setEnabled(true);
                this.municipios = new ArrayList<>();
                this.municipios.add(new Municipio("", "Municipio"));
                this.municipios.addAll(data);
                municipioAdapterLv = new MunicipioAdapterLv(context, municipios);
                spinnerMunicipios.setAdapter(municipioAdapterLv);
                spinnerMunicipios.setOnItemSelectedListener(new MunicipioOnClickEventListView((Activity) context, municipioAdapterLv));
            }
        } else {
            Toast.makeText(context, "Não foram encontradas propriedades para sua busca", Toast.LENGTH_LONG).show();
        }

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void before(String tag, ProcessGSONRespUtils processGSONRespUtils) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void getMunicipio(Municipio e) {
        this.municipioSelected = e;
    }


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
