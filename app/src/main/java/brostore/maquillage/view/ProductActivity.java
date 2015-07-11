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

import com.facebook.FacebookSdk;

import brostore.maquillage.R;
import brostore.maquillage.dao.Product;
import brostore.maquillage.manager.ProductManager;
import brostore.maquillage.manager.UserManager;

public class ProductActivity extends Activity {

    private Product myProduct;
    int quantityAvailable;

    private BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("OK QUANTITY")) {
                quantityAvailable = Integer.parseInt(ProductManager.getInstance(getApplicationContext()).quantity);
                if(quantityAvailable > 10){
                    inStock(false);
                }else if(quantityAvailable == 0) {
                    outOfStock();
                }else{
                    inStock(true);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_product);

        FacebookSdk.sdkInitialize(getApplicationContext());

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
        ((TextView)findViewById(R.id.article_prix1)).setText(" " + String.format("%.2f", myProduct.getPrice()) + "€ ");
        ((TextView)findViewById(R.id.article_prix1)).getPaint().setStrikeThruText(true);
        ((TextView)findViewById(R.id.article_prix2)).setText(String.format("%.2f" ,myProduct.getReducedPrice()) + "€");

    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadCastReceiver);
        super.onDestroy();
    }

    private void inStock(boolean rare){

        findViewById(R.id.stock_loading).setVisibility(View.GONE);


        if(rare){
            ((TextView)findViewById(R.id.stock)).setText("o Derniers");
            ((TextView)findViewById(R.id.stock)).setTextColor(getResources().getColor(R.color.orange_rare_stock));
        }else{
            ((TextView)findViewById(R.id.stock)).setText("o Disponible");
            ((TextView)findViewById(R.id.stock)).setTextColor(getResources().getColor(R.color.green_en_stock));
        }

        findViewById(R.id.ajout_panier).setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
        findViewById(R.id.ajout_panier).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(((EditText) findViewById(R.id.quantity)).getText().toString());

                if(quantity > quantityAvailable){
                    Toast.makeText(getApplicationContext(), "Le nombre de produits disponible pour cette référence est limitée à " + quantityAvailable, Toast.LENGTH_LONG).show();
                }else{
                    UserManager.getInstance(getApplicationContext()).addInBasket(myProduct, quantity);
                    finish();
                }
                finish();
            }
        });
    }

    private void outOfStock(){

        findViewById(R.id.stock_loading).setVisibility(View.GONE);

        ((TextView)findViewById(R.id.stock)).setText("o Indisponible");
        ((TextView)findViewById(R.id.stock)).setTextColor(getResources().getColor(R.color.rouge_rupture_stock));

        findViewById(R.id.ajout_panier).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}