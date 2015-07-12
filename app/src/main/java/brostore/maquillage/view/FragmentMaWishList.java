package brostore.maquillage.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import brostore.maquillage.R;
import brostore.maquillage.adapter.ProductAdapter;
import brostore.maquillage.custom.GridViewWithHeaderAndFooter;
import brostore.maquillage.manager.ProductManager;

/**
 * Created by Michaos on 11/07/2015.
 */
public class FragmentMaWishList extends Fragment {

    private View rootView;

    public FragmentMaWishList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_ma_wishlist, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {

        GridViewWithHeaderAndFooter gridView = (GridViewWithHeaderAndFooter) getView().findViewById(R.id.listView);
        gridView.setVerticalSpacing(1);

        ProductAdapter adapter = new ProductAdapter(getActivity(), ProductManager.getInstance(getActivity()).getListProductsFavoris());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("product", ProductManager.getInstance(getActivity()).getListProductsFavoris().get(i));
                startActivity(intent);

            }
        });
    }
}
