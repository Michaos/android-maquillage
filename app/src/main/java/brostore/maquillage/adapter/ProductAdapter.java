package brostore.maquillage.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import brostore.maquillage.R;
import brostore.maquillage.dao.Product;
import brostore.maquillage.manager.DataManager;
import brostore.maquillage.manager.FluxManager;
import brostore.maquillage.wrapper.ProductWrapper;

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

        wrapper.getArticleImage().setImageResource(R.drawable.maquillage);

        if(product.getBitmapImage() != null){
            wrapper.getArticleImage().setImageBitmap(product.getBitmapImage());
        } else if(product.getImageId() != null){
            String myUrl = FluxManager.URL_IMAGES.replace("__ID_PRODUCT__", product.getId()+"").replace("__ID_IMAGE__" , product.getImageId());
            DataManager.getInstance(mContext).callImgAPI(wrapper.getArticleImage(), myUrl, product);
        }

        wrapper.getArticleNom().setText(product.getName());
        //wrapper.getArticleInfos().setText(Html.fromHtml(product.getDescription()));

        if(product.noReduc()){
            wrapper.getArticlePrix2().setVisibility(View.GONE);
        }

        wrapper.getArticlePrix1().setText(product.getPrice() + "€ ");
        wrapper.getArticlePrix1().getPaint().setStrikeThruText(true);
        wrapper.getArticlePrix2().setText(product.getPriceReduced() + "€");

        return (row);
    }
}