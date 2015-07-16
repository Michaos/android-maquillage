package brostore.maquillage.wrapper;

import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import brostore.maquillage.R;

/**
 * Created by clairecoloma on 15/07/15.
 */
public class DeliveryWrapper {
    private View baseView;
    private TextView alias;
    private TextView address;
    private TextView postalCode;
    private TextView city;

    public DeliveryWrapper(View base) {
        this.baseView = base;
    }

    public View getBaseView() {
        return baseView;
    }

    public TextView getAlias() {
        if (alias == null) {
            alias = (TextView) baseView.findViewById(R.id.address_alias);
        }
        return alias;
    }

    public TextView getAddress() {
        if (address == null) {
            address = (TextView) baseView.findViewById(R.id.address_complete);
        }
        return address;
    }

    public TextView getPostalCode() {
        if (postalCode == null) {
            postalCode = (TextView) baseView.findViewById(R.id.address_postal_code);
        }
        return postalCode;
    }

    public TextView getCity() {
        if (city == null) {
            city = (TextView) baseView.findViewById(R.id.address_city);
        }
        return city;
    }

}
