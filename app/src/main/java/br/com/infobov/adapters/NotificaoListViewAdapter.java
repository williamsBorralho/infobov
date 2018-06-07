package br.com.infobov.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import br.com.infobov.sync.domain.Notifcacao;

public class NotificaoListViewAdapter extends BaseAdapter {

    Context  context ;
    List<Notifcacao> notificacoes ;

    public NotificaoListViewAdapter() {
    }

    public NotificaoListViewAdapter(Context context, List<Notifcacao> notificacoes) {
        this.context = context;
        this.notificacoes = notificacoes;
    }

    @Override
    public int getCount() {
        return this.notificacoes.size();
    }

    @Override
    public Object getItem(int position) {
        return this.notificacoes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
