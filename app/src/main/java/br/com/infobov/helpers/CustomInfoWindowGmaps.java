package br.com.infobov.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import br.com.infobov.activities.ibovmobile.R;
import br.com.infobov.network.NetworkHelper;
import br.com.infobov.pojo.InfoWindowData;

public class CustomInfoWindowGmaps implements GoogleMap.InfoWindowAdapter {

    private Context context;


    public CustomInfoWindowGmaps(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null ;
    }

    @Override
    public View getInfoContents(Marker marker) {

        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.info_window_custom, null);
        TextView txtNomeFazenda = view.findViewById(R.id.nome_fazenda);
        ImageView imvFerro = view.findViewById(R.id.ferro_fazenda);

        InfoWindowData idata = (InfoWindowData) marker.getTag();

        txtNomeFazenda.setText(idata.getFazenda());
//        imvFerro.setImageResource(R.drawable.home_i);
        String uri = String.format("%simagem/final/%s", NetworkHelper.getDomain(), idata.getImagem());

        String TAG = "GLIDE";
        Glide.with(context)
                .load(uri)
                .apply(new RequestOptions().placeholder(R.drawable.loading).override(500,500).fitCenter())
//                .listener(new RequestListener() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
//                        android.util.Log.d("GLIDE", String.format(Locale.ROOT,
//                                "onException(%s, %s, %s, %s)", e, model, target, isFirstResource), e);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
//                        android.util.Log.d("GLIDE", String.format(Locale.ROOT,
//                                "onResourceReady(%s, %s, %s,  %s)", resource, model, target, isFirstResource));
//                        return false;
//                    }
//
//
//                })
                .into(imvFerro);


        return view;
    }


}
