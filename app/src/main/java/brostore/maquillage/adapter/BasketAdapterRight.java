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
import brostore.maquillage.manager.UserManager;
import brostore.maquillage.wrapper.BasketWrapper;

public class BasketAdapterRight extends BaseAdapter {

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
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

        // Spinner Quantity
        List<String> list = new ArrayList<String>();
        for (int i = 0; i <= 10; i++) {
            list.add(i+"");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        wrapper.getArticleQuantitySpinner().setAdapter(dataAdapter);
        wrapper.getArticleQuantitySpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spinnerPosition, long id) {
                if (myQuantities.get(position) != spinnerPosition) {
                    myQuantities.set(position, spinnerPosition);
                    updateListView();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        wrapper.getArticleQuantitySpinner().setSelection(myQuantities.get(position));

        double totalPrice = p.getReducedPrice() * myQuantities.get(position);
        wrapper.getArticleTotalPrice().setText(String.format("%.2f", totalPrice) + "€");

        // delete button
        wrapper.getBtnDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myProducts.remove(position);
                User myUser = UserManager.getInstance(mContext).getUser();
                Double totalBasket = myUser.getTotalBasket();
                System.out.println("AAAA B - totalBasket " + String.format("%.2f", totalBasket) + " / user: " + String.format("%.2f", myUser.getTotalBasket()));

                totalBasket -= p.getReducedPrice() * myQuantities.get(position);
                System.out.println("AAAA B - new totalBasket " + String.format("%.2f", totalBasket) + " / user: " + String.format("%.2f", myUser.getTotalBasket()));
                myUser.setTotalBasket(totalBasket);

                Double totalSaving = myUser.getTotalSaving();
                System.out.println("AAAA B - totalSaving " + String.format("%.2f", totalSaving) + " / user: " + String.format("%.2f", myUser.getTotalSaving()));
                totalSaving -= (p.getPrice() - p.getReducedPrice()) * myQuantities.get(position);
                System.out.println("AAAA B - new totalSaving " + String.format("%.2f", totalSaving) + " / user: " + String.format("%.2f", myUser.getTotalSaving()));
                myUser.setTotalSaving(totalSaving);

                updateListView();
            }
        });

        return row;
    }

    private void updateListView() {
        this.notifyDataSetChanged();
    }
}
