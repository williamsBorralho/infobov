package br.com.infobov.events;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

import br.com.infobov.adapters.MunicipioAdapterLv;
import br.com.infobov.sync.domain.Municipio;

public class MunicipioOnClickEventListView implements AdapterView.OnItemSelectedListener {

    private MunicipioAdapterLv adapter;
    private OnMunicipioSelected activity;

    public MunicipioOnClickEventListView(Activity activity, MunicipioAdapterLv adapter) {
        this.activity = (OnMunicipioSelected) activity;
        this.adapter = adapter;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Municipio e = (Municipio) this.adapter.getItem(position);
        activity.getMunicipio(e);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public interface OnMunicipioSelected {
        public void getMunicipio(Municipio e);
    }
}
