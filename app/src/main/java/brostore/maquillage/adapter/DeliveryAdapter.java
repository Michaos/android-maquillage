package brostore.maquillage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import brostore.maquillage.R;
import brostore.maquillage.dao.Address;
import brostore.maquillage.wrapper.DeliveryWrapper;

/**
 * Created by clairecoloma on 15/07/15.
 */
public class DeliveryAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private View row;
    private ArrayList<Address> userAddresses;
    private DeliveryWrapper wrapper;

    public DeliveryAdapter(Context context, ArrayList<Address> addresses) {
        inflater = LayoutInflater.from(context);
        userAddresses = addresses;

    }

    @Override
    public int getCount() {
        return userAddresses.size();
    }

    @Override
    public Address getItem(int position) { return userAddresses.get(position); }

    @Override
    public long getItemId(int position) {
        return userAddresses.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        row = convertView;
        if (row == null) {
            row = inflater.inflate(R.layout.item_address_delivery, null);
            wrapper = new DeliveryWrapper(row);
            row.setTag(wrapper);
        } else {
            wrapper = (DeliveryWrapper) row.getTag();
        }
        Address address = userAddresses.get(position);

        wrapper.getAlias().setText(address.getAlias());
        wrapper.getAddress().setText(address.getAddress1());
        wrapper.getPostalCode().setText(address.getPostcode());
        wrapper.getCity().setText(address.getCity());




        return row;
    }

}
