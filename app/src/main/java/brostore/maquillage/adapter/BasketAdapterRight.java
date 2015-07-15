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
import brostore.maquillage.manager.UserManager;
import brostore.maquillage.view.MainActivity;
import brostore.maquillage.wrapper.BasketWrapper;

public class BasketAdapterRight extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<Product> myProducts;
    private ArrayList<Integer> myQuantities;
    private BasketWrapper wrapper;
    private View row;

    public BasketAdapterRight(Context context, User myUser) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        myProducts = myUser.getBasket();
        myQuantities = myUser.getQuantities();
    }

    @Override
    public int getCount() {
        return myProducts.size();
    }

    @Override
    public Product getItem(int position) {
        return myProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return myProducts.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        row = convertView;
        if (row == null) {
            row = inflater.inflate(R.layout.item_basket, null);
            wrapper = new BasketWrapper(row);
            row.setTag(wrapper);
        } else {
            wrapper = (BasketWrapper) row.getTag();
        }

        final Product p = myProducts.get(position);

        if (p.getBitmapImage() == null) {
            wrapper.getArticleImage().setImageResource(R.drawable.maquillage);
        } else {
            wrapper.getArticleImage().setImageBitmap(p.getBitmapImage());
        }

        wrapper.getArticleName().setText(p.getName());

        double totalPrice = p.getReducedPrice() * myQuantities.get(position);
        wrapper.getArticleTotalPrice().setText(String.format("%.2f", totalPrice) + "€");

        // delete button
        wrapper.getBtnDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance(mContext).removeFromBasket(myProducts.get(position));
                ((MainActivity) mContext).refreshBasket();
            }
        });

        wrapper.getArticleQty().setText(myQuantities.get(position) + "");
        wrapper.getArticleQty().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mContext).popupQty(myQuantities.get(position), position);
            }
        });

        return row;
    }
}