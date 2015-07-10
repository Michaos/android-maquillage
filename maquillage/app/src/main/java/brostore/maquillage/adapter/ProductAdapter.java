package brostore.maquillage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import brostore.maquillage.R;
import brostore.maquillage.dao.Product;
import brostore.maquillage.wrapper.ProductWrapper;

/**
 * Created by Michaos on 07/06/2015.
 */
public class ProductAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<Product> productsList;
    private ProductWrapper wrapper;

    public ProductAdapter(Context context, List<Product> list) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        productsList = list;
    }

    @Override
    public int getCount() {
        return productsList.size();
    }

    @Override
    public Object getItem(int i) {
        return productsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return productsList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        Product product = productsList.get(i);

        if (row == null) {
            row = inflater.inflate(R.layout.item_article, null);
            wrapper = new ProductWrapper(row);
            row.setTag(wrapper);
        } else {
            wrapper = (ProductWrapper) row.getTag();
        }

        wrapper.getArticleNom().setText(product.getName());

        return (row);
    }
}
