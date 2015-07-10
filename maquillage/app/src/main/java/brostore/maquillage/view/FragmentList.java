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
import android.widget.ListView;

import brostore.maquillage.R;
import brostore.maquillage.adapter.ProductAdapter;
import brostore.maquillage.manager.ProductManager;

/**
 * Created by Michaos on 31/05/2015.
 */
public class FragmentList extends Fragment {

    private int id;
    private String title;

    private BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("OK PRODUCTS" + id)){
                System.out.println("AAAA OK GOT IT LETS ROLL :: " + id + " :: " + title);
                init();
            }
        }
    };

    public FragmentList() {
    }

    public static FragmentList newInstance(int id, String title) {
        FragmentList frag = new FragmentList();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putString("title", title);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_list, container, false);

        id = this.getArguments().getInt("id");
        title = this.getArguments().getString("title");

        IntentFilter filter = new IntentFilter();
        filter.addAction("OK PRODUCTS" + id);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadCastReceiver, filter);

        ProductManager.getInstance(getActivity()).getProductsForCategory(id);

        return rootView;
    }

    private void init(){
        ListView listView = (ListView) getActivity().findViewById(R.id.listView);
        listView.setAdapter(new ProductAdapter(getActivity(), ProductManager.getInstance(getActivity()).getList(id+"")));
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadCastReceiver);
        super.onDestroy();
    }
}
