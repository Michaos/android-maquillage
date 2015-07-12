package brostore.maquillage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import brostore.maquillage.R;
import brostore.maquillage.dao.Product;
import brostore.maquillage.dao.User;
import brostore.maquillage.wrapper.BasketWrapper;

public class BasketAdapterRight extends BaseAdapter implements AdapterView.OnItemSelectedListener{

    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<Product> myProducts;
    private ArrayList<Integer> myQuantities;
    private BasketWrapper wrapper;

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

        if (p.getBitmapImage() == null) {
            wrapper.getArticleImage().setImageResource(R.drawable.maquillage);
        } else {
            wrapper.getArticleImage().setImageBitmap(p.getBitmapImage());
        }
        wrapper.getArticleName().setText(p.getName());

        setQuantitySpinner();
        wrapper.getArticleQuantitySpinner().setSelection(myQuantities.get(position));

        double totalPrice = p.getReducedPrice() * myQuantities.get(position);
        wrapper.getArticleTotalPrice().setText(String.format("%.2f", totalPrice)+"€");

        return row;
    }

    public void setQuantitySpinner() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i <= 10; i++) {
            list.add(i+"");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        wrapper.getArticleQuantitySpinner().setAdapter(dataAdapter);
        wrapper.getArticleQuantitySpinner().setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //TODO get position of the product !!
        int productPosition = 0;
        myQuantities.set(productPosition, position);
        System.out.println("AAAA B - all is ok right here");
        //this.notifyDataSetChanged();
        System.out.println("AAAA B - after notifyDataSetChanged...");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
