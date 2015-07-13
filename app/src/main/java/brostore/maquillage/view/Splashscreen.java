package brostore.maquillage.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;

import brostore.maquillage.R;
import brostore.maquillage.manager.MenuManager;
import brostore.maquillage.manager.ProductManager;

public class Splashscreen extends Activity {


    private BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MenuManager.OK_MENU_CACHE_OR_RAW) || intent.getAction().equals(MenuManager.OK_MENU)) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        IntentFilter filter = new IntentFilter();
        filter.addAction(MenuManager.OK_MENU_CACHE_OR_RAW);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadCastReceiver, filter);

        popupInternet();

        ProductManager.getInstance(this).initListFavoris();

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void popupInternet() {

        if (isOnline()) {
            MenuManager.getInstance(this).initMenuTask();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Maquillage.fr");
            builder.setMessage("Une connection à internet est recquise. Veuillez vérifier votre connection puis réessayez.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            popupInternet();
                        }
                    })
                    .setNegativeButton("FERMER", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            builder.create();
            builder.show();
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadCastReceiver);
        super.onDestroy();
    }
}