package brostore.maquillage.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import brostore.maquillage.R;
import brostore.maquillage.adapter.ValidateAdapter;
import brostore.maquillage.dao.Address;
import brostore.maquillage.manager.UserManager;
import brostore.maquillage.wrapper.ValidateWrapper;

/**
 * Created by clairecoloma on 14/07/15.
 */
public class ValidateActivity extends Activity {
    private ValidateActivity mContext;
    private ListView productsAdded;
    private ValidateAdapter validateAdapter;

    private Address myAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate);

        mContext = this;

        myAddress = (Address) getIntent().getSerializableExtra("address");

        initPayment();
    }

    public void initPayment() {
        productsAdded = (ListView) findViewById(R.id.validate_products);
        validateAdapter = new ValidateAdapter(this, UserManager.getInstance(this).getUser());
        productsAdded.setAdapter(validateAdapter);

        ((TextView)findViewById(R.id.validate_total)).append(String.format("%.2f", UserManager.getInstance(this).getUser().getTotalBasket()) + " €");
        ((TextView)findViewById(R.id.validate_address_alias)).setText(myAddress.getAlias());
        ((TextView)findViewById(R.id.validate_address_delivery)).setText(myAddress.getAddress1());
        ((TextView)findViewById(R.id.validate_postal_code)).setText(myAddress.getPostcode());
        ((TextView)findViewById(R.id.validate_city)).setText(myAddress.getCity());
    }

}
