package brostore.maquillage.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import brostore.maquillage.R;
import brostore.maquillage.manager.MenuManager;

/**
 * Created by Michaos on 31/05/2015.
 */
public class Splashscreen extends Activity {


    private BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("OK MENU")){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        IntentFilter filter = new IntentFilter();
        filter.addAction("OK MENU");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadCastReceiver, filter);

        MenuManager.getInstance(this).initMenuTask();

    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadCastReceiver);
        super.onDestroy();
    }
}
