package brostore.maquillage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import brostore.maquillage.R;
import brostore.maquillage.dao.Product;
import brostore.maquillage.dao.User;
import brostore.maquillage.wrapper.BasketWrapper;

public class BasketAdapterRight extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<Product> myProducts;
    private ArrayList<Integer> myQuantities;
    private BasketWrapper wrapper;

    public BasketAdapterRight(Context context, User myUser){
        inflater = LayoutInflater.from(context);
        mContext = context;
        myProducts = myUser.getBasket();
        myQuantities = myUser.getQuantites();
    }

    @Override
    public int getCount() {
        return myProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return myProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return myProducts.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = inflater.inflate(R.layout.item_basket, null);
            wrapper = new BasketWrapper(row);
            row.setTag(wrapper);
        } else {
            wrapper = (BasketWrapper) row.getTag();
        }

        Product p = myProducts.get(position);

        if(p.getBitmapImage() == null){
            wrapper.getArticleImage().setImageResource(R.drawable.maquillage);
        }else{
            wrapper.getArticleImage().setImageBitmap(p.getBitmapImage());
        }
        wrapper.getArticleNom().setText(p.getName());
        wrapper.getArticlePrix().setText(p.getPriceReduced()+"€");
        wrapper.getArticleQuantite().setText("Qté : " + myQuantities.get(position));
        double prixTotal = Double.parseDouble(p.getPriceReduced().replace(",", ".")) * myQuantities.get(position);
        wrapper.getArticlePrixTotal().setText("Total : " + String.format("%.2f", prixTotal)+"€");

        return row;
    }
}
