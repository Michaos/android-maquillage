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
import android.widget.LinearLayout;
import android.widget.Toast;

import brostore.maquillage.R;
import brostore.maquillage.adapter.ProductAdapter;
import brostore.maquillage.custom.GridViewWithHeaderAndFooter;
import brostore.maquillage.manager.ProductManager;

public class FragmentListProducts extends Fragment {

    private int id;
    private ProductAdapter adapter;
    private int base;
    private boolean needInit = true;
    private View footerView;

    private BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("OK PRODUCTS" + id)) {
                if (needInit) {
                    init();
                } else {
                    base += ProductManager.MAX;
                    footerView.findViewById(R.id.loadinglayoutfooter).setVisibility(View.INVISIBLE);
                    footerView.findViewById(R.id.txt).setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
            } else if (intent.getAction().equals("KO PRODUCTS" + id)) {
                ((LinearLayout) getView().findViewById(R.id.loadinglayout)).removeAllViews();
                getView().findViewById(R.id.loadinglayout).setBackgroundResource(R.drawable.maquillage);
                Toast.makeText(getActivity(), "Une erreur s'est produite lors de la connexion avec le site maquillage.fr Veuillez réessayer ultèrieurement.", Toast.LENGTH_LONG).show();
            }
        }
    };

    public FragmentListProducts() {
    }

    public static FragmentListProducts newInstance(int id, String title) {
        FragmentListProducts frag = new FragmentListProducts();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putString("title", title);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_products, container, false);

        id = this.getArguments().getInt("id");

        IntentFilter filter = new IntentFilter();
        filter.addAction("OK PRODUCTS" + id);
        filter.addAction("KO PRODUCTS" + id);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadCastReceiver, filter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        base = ProductManager.getInstance(getActivity()).getList(id + "").size();

        if (base > 0) {
            init();
        } else {
            ProductManager.getInstance(getActivity()).getProductsForCategory(id, base);
        }

    }

    private void init() {

        needInit = false;
        base += ProductManager.MAX;

        getView().findViewById(R.id.loadinglayout).setVisibility(View.INVISIBLE);

        GridViewWithHeaderAndFooter gridView = (GridViewWithHeaderAndFooter) getView().findViewById(R.id.listView);
        gridView.findViewById(R.id.listView).setVisibility(View.VISIBLE);
        gridView.setVerticalSpacing(1);

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        footerView = layoutInflater.inflate(R.layout.item_footer, null);
        gridView.addFooterView(footerView, null, false);

        adapter = new ProductAdapter(getActivity(), ProductManager.getInstance(getActivity()).getList(id + ""));
        gridView.setAdapter(adapter);

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductManager.getInstance(getActivity()).getProductsForCategory(id, base);
                footerView.findViewById(R.id.loadinglayoutfooter).setVisibility(View.VISIBLE);
                footerView.findViewById(R.id.txt).setVisibility(View.GONE);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("product", ProductManager.getInstance(getActivity()).getList(id + "").get(i));
                startActivity(intent);

            }
        });
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadCastReceiver);
        super.onDestroy();
    }
}