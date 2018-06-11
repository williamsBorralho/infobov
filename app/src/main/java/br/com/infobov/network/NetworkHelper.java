package br.com.infobov.network;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.URL;
import java.net.URLConnection;

import br.com.infobov.exceptions.ConexaoDesativaException;


/**
 * Created by will_ on 07/03/2016.
 */
public class NetworkHelper {

    //Sincronização de dados no sentido servidor para aplicação
    private static final int index = 1;
    public static final String DOMAIN[] = {
            "http://infobov.com.br",
            "http://192.168.0.12/infobov/",
            "http://192.168.1.6:8080",
            "http://192.168.0.7:8080"};

    public static final String ALL_FAZENDAS = DOMAIN[index] + "/vaqueiro/wsvaqueiro/SyncSal/request_login";
    public static final String POR_ID = DOMAIN[index] + "/vaqueiro/wsvaqueiro/SyncSal/request_fazendas";


    public static boolean isConnect(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    public static boolean isNetWork(Activity activity) {
        return ((LocationManager) activity.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static String getDomain() {
        return DOMAIN[index];
    }

    public static boolean isURLReachable(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        try {
            if (netInfo == null && !netInfo.isConnected()) {
                throw new ConexaoDesativaException();
            }

            URL url = new URL(DOMAIN[index]);   // Change to "http://google.com" for www  test.
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.connect();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
