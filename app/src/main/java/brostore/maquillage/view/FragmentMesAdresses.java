package brostore.maquillage.view;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import brostore.maquillage.R;
import brostore.maquillage.adapter.AddressesAdapter;
import brostore.maquillage.manager.AddressManager;

/**
 * Created by Michaos on 11/07/2015.
 */
public class FragmentMesAdresses extends Fragment {

    private View rootView;

    private BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("OK ADDRESSES")) {
                init();
            } else if (intent.getAction().equals("KO ADDRESSES")) {

            } else if (intent.getAction().equals("NO ADDRESSES")) {
                rootView.findViewById(R.id.progress).setVisibility(View.GONE);
                ((TextView) rootView.findViewById(R.id.chargement)).setText(R.string.no_addresses);
                rootView.findViewById(R.id.add_addresse).setVisibility(View.VISIBLE);
            }
        }
    };

    public FragmentMesAdresses() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_mes_adresses, container, false);

        IntentFilter filter = new IntentFilter();
        filter.addAction("OK ADDRESSES");
        filter.addAction("KO ADDRESSES");
        filter.addAction("NO ADDRESSES");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadCastReceiver, filter);

        AddressManager.getInstance(getActivity()).getUserAddresses();

        rootView.findViewById(R.id.add_addresse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, new FragmentFormAdresse()).commit();
            }
        });

        return rootView;
    }

    private void init() {

        rootView.findViewById(R.id.loadinglayout).setVisibility(View.GONE);

        rootView.findViewById(R.id.add_addresse).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.listView).setVisibility(View.VISIBLE);

        ListView myListView = (ListView) rootView.findViewById(R.id.listView);
        myListView.setAdapter(new AddressesAdapter(getActivity(), AddressManager.getInstance(getActivity()).getListAddresses()));
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadCastReceiver);
        super.onDestroy();
    }
}
