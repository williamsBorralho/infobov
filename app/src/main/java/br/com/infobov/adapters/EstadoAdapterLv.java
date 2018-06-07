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

public class EstadoAdapterLv extends BaseAdapter {

    Context context;
    List<Estado> estados;

    public EstadoAdapterLv() {
    }

    public EstadoAdapterLv(Context context, List<Estado> estados) {
        this.context = context;
        this.estados = estados;
    }

    @Override
    public int getCount() {
        return this.estados.size();
    }

    @Override
    public Object getItem(int position) {
        return this.estados.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Estado estado = (Estado) getItem(position);
        View linha  = LayoutInflater.from(this.context).inflate(R.layout.activity_estado_lv, null) ;
        TextView tvUf = linha.findViewById(R.id.uf) ;
        TextView tvNome  = linha.findViewById(R.id.nome) ;

        tvUf.setText(estado.getUf().toUpperCase());
        tvNome.setText(estado.getNome().toUpperCase());
        return linha;
    }


}
