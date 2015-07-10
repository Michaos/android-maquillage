package brostore.maquillage.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import brostore.maquillage.R;
import brostore.maquillage.dao.Product;
import brostore.maquillage.manager.DataManager;
import brostore.maquillage.manager.ProductManager;

public class ProductActivity extends Activity {

    private Product myProduct;

    private BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("OK QUANTITY")) {
                //TODO
                String quant = ProductManager.getInstance(getApplicationContext()).quantity;
                ((TextView)findViewById(R.id.article_nom)).setText("quantité :: " + quant);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_product);

        IntentFilter filter = new IntentFilter();
        filter.addAction("OK QUANTITY");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadCastReceiver, filter);

        myProduct = getIntent().getParcelableExtra("product");

        ProductManager.getInstance(this).getQuantityForProduct(myProduct);

        if(myProduct.getBitmapImage() == null){
            ((ImageView)findViewById(R.id.article_image)).setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.maquillage));
        }else{
            ((ImageView)findViewById(R.id.article_image)).setImageBitmap(myProduct.getBitmapImage());
        }

        ((TextView)findViewById(R.id.article_nom)).setText(myProduct.getName());
        ((TextView)findViewById(R.id.article_infos)).setText(Html.fromHtml(myProduct.getDescription()));
        ((TextView)findViewById(R.id.article_prix1)).setText(" " + myProduct.getPrice() + "€ ");
        ((TextView)findViewById(R.id.article_prix1)).getPaint().setStrikeThruText(true);
        ((TextView)findViewById(R.id.article_prix2)).setText(myProduct.getPriceReduced() + "€");

        findViewById(R.id.ajout_panier).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantite = Integer.parseInt(((EditText) findViewById(R.id.quantite)).getText().toString());
                DataManager.getInstance(getApplicationContext()).addInBasket(myProduct, quantite);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadCastReceiver);
        super.onDestroy();
    }
}