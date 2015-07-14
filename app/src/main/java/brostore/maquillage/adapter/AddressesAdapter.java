package brostore.maquillage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import brostore.maquillage.R;
import brostore.maquillage.dao.Address;
import brostore.maquillage.wrapper.AddressWrapper;

/**
 * Created by Michaos on 13/07/2015.
 */
public class AddressesAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<Address> myAddresses;
    private AddressWrapper wrapper;

    public AddressesAdapter(Context context, ArrayList<Address> listAddresses) {

        System.out.println("ADDRESS :: " + listAddresses.size());

        inflater = LayoutInflater.from(context);
        mContext = context;
        myAddresses = listAddresses;
    }

    @Override
    public int getCount() {
        return myAddresses.size();
    }

    @Override
    public Address getItem(int position) {
        return myAddresses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return myAddresses.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = inflater.inflate(R.layout.item_address, null);
            wrapper = new AddressWrapper(row);
            row.setTag(wrapper);
        } else {
            wrapper = (AddressWrapper) row.getTag();
        }

        Address myAddress = myAddresses.get(position);

        System.out.println(myAddress.toString());

        wrapper.getAlias().setText(myAddress.getAlias());
        wrapper.getSociete().setText(myAddress.getCompany());
        wrapper.getTva().setText(myAddress.getVatNumber());
        wrapper.getAdresse().setText(myAddress.getAddress1());
        wrapper.getAdresse2().setText(myAddress.getAddress2());
        wrapper.getCp().setText(myAddress.getPostcode());
        wrapper.getVille().setText(myAddress.getCity());
        wrapper.getPays().setText(myAddress.getCountry());
        wrapper.getTelfixe().setText(myAddress.getPhone());
        wrapper.getTelport().setText(myAddress.getPhoneMobile());
        wrapper.getInfos().setText(myAddress.getOther());

        return row;
    }
}
