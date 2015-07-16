package brostore.maquillage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import brostore.maquillage.R;
import brostore.maquillage.dao.Product;
import brostore.maquillage.dao.User;
import brostore.maquillage.view.ValidateActivity;
import brostore.maquillage.wrapper.ValidateWrapper;

/**
 * Created by clairecoloma on 16/07/15.
 */
public class ValidateAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private View row;
    private ArrayList<Product> myProducts;
    private ArrayList<Integer> myQuantities;
    private ValidateWrapper wrapper;

    public ValidateAdapter(Context context, User myUser) {
        inflater = LayoutInflater.from(context);
        myProducts = myUser.getBasket();
        myQuantities = myUser.getQuantities();
    }

    @Override
    public int getCount() { return myProducts.size(); }

    @Override
    public Product getItem(int position) { return myProducts.get(position); }

    @Override
    public long getItemId(int position) { return myProducts.get(position).getId(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        row = convertView;
        if (row == null) {
            row = inflater.inflate(R.layout.item_basket_order, null);
            wrapper = new ValidateWrapper(row);
            row.setTag(wrapper);
        } else {
            wrapper = (ValidateWrapper) row.getTag();
        }
        Product product = myProducts.get(position);

        if (product.getBitmapImage() == null) {
            System.out.println("AAAA VA null - wrapper"+ wrapper);
            System.out.println("AAAA VA null - wrapper.getImageView()"+ wrapper.getImageView());
            wrapper.getImageView().setImageResource(R.drawable.maquillage);
        } else {
            System.out.println("AAAA VA - wrapper"+ wrapper);
            System.out.println("AAAA VA - wrapper.getImageView()"+ wrapper.getImageView());
            wrapper.getImageView().setImageBitmap(product.getBitmapImage());
        }

        wrapper.getName().setText(product.getName());

        double totalPrice = product.getReducedPrice() * myQuantities.get(position);
        wrapper.getTotalPrice().setText(String.format("%.2f", totalPrice) + "€");
        wrapper.getQuantity().setText("Qté: " + myQuantities.get(position));

        return row;
    }
}
