package brostore.maquillage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import brostore.maquillage.R;
import brostore.maquillage.dao.Order;
import brostore.maquillage.wrapper.OrderWrapper;

/**
 * Created by Michaos on 13/07/2015.
 */
public class OrdersAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<Order> myOrders;
    private OrderWrapper wrapper;

    public OrdersAdapter(Context context, ArrayList<Order> listOrders) {

        System.out.println("ADDRESS :: " + listOrders.size());

        inflater = LayoutInflater.from(context);
        mContext = context;
        myOrders = listOrders;
    }

    @Override
    public int getCount() {
        return myOrders.size();
    }

    @Override
    public Order getItem(int position) {
        return myOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return myOrders.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = inflater.inflate(R.layout.item_order, null);
            wrapper = new OrderWrapper(row);
            row.setTag(wrapper);
        } else {
            wrapper = (OrderWrapper) row.getTag();
        }

        Order myOrder = myOrders.get(position);

        // fill with wrapper...

        return row;
    }
}
