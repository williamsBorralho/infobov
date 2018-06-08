package br.com.infobov.events;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import br.com.infobov.adapters.EstadoAdapterLv;
import br.com.infobov.sync.domain.Estado;

public class EstadoOnClickEventListView implements AdapterView.OnItemSelectedListener {

    private EstadoAdapterLv estadoAdapterLv ;
    private EstadoOnClickItem  activity ;


    public EstadoOnClickEventListView(Activity activity  , EstadoAdapterLv estadoAdapterLv) {
        this.activity = (EstadoOnClickItem) activity;
        this.estadoAdapterLv = estadoAdapterLv;




    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Estado e = (Estado) this.estadoAdapterLv.getItem(position);
        activity.getEstadoOnList(e);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface EstadoOnClickItem {
        public void getEstadoOnList(Estado e);
    }
}
