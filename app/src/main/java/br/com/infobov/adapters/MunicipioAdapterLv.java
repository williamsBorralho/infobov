package br.com.infobov.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.infobov.activities.ibovmobile.R;
import br.com.infobov.sync.domain.Estado;
import br.com.infobov.sync.domain.Municipio;

public class MunicipioAdapterLv extends BaseAdapter {

    Context context;
    List<Municipio> municipios;

    public MunicipioAdapterLv() {
    }

    public MunicipioAdapterLv(Context context, List<Municipio> estados) {
        this.context = context;
        this.municipios = estados;
    }

    @Override
    public int getCount() {
        return this.municipios.size();
    }

    @Override
    public Object getItem(int position) {
        return this.municipios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Municipio municipio = (Municipio) getItem(position);
        View linha  = LayoutInflater.from(this.context).inflate(R.layout.activity_estado_lv, null) ;
        TextView tvUf = linha.findViewById(R.id.uf) ;
        TextView tvNome  = linha.findViewById(R.id.nome) ;

        tvUf.setText(municipio.getUf());
        tvNome.setText(municipio.getNome());
        return linha;
    }


}
