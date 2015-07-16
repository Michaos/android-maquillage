package brostore.maquillage.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import brostore.maquillage.R;
import brostore.maquillage.adapter.DeliveryAdapter;
import brostore.maquillage.dao.Address;
import brostore.maquillage.manager.AddressManager;
import brostore.maquillage.manager.ProductManager;

/**
 * Created by clairecoloma on 13/07/15.
 */
public class DeliveryActivity extends Activity {

    private DeliveryActivity mContext;
    private ListView addressList;
    private DeliveryAdapter deliveryAdapter;

    private BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("OK ADDRESSES")) {
                initAddresses();
            } else if (intent.getAction().equals("KO ADDRESSES")) {

            } else if (intent.getAction().equals("NO ADDRESSES")) {
                findViewById(R.id.progress).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.chargement)).setText(R.string.no_addresses);
                findViewById(R.id.add_addresse).setVisibility(View.VISIBLE);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        mContext = this;

        IntentFilter filter = new IntentFilter();
        filter.addAction("OK ADDRESSES");
        filter.addAction("KO ADDRESSES");
        filter.addAction("NO ADDRESSES");
        LocalBroadcastManager.getInstance(mContext).registerReceiver(broadCastReceiver, filter);
        AddressManager.getInstance(mContext).getUserAddresses();
    }

    public void initAddresses() {
        findViewById(R.id.loadinglayout).setVisibility(View.GONE);
        addressList = (ListView) findViewById(R.id.delivery_addresses);
        ArrayList<Address> listAddresses =  AddressManager.getInstance(this).getListAddresses();
        deliveryAdapter = new DeliveryAdapter(this, listAddresses);
        addressList.setAdapter(deliveryAdapter);

        addressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ValidateActivity.class);
                intent.putExtra("address", AddressManager.getInstance(mContext).getListAddresses().get(position));
                startActivity(intent);
            }
        });

    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(broadCastReceiver);
        super.onDestroy();
    }
}
