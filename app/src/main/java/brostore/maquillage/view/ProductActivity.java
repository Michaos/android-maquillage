package brostore.maquillage.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import brostore.maquillage.R;
import brostore.maquillage.dao.Product;
import brostore.maquillage.manager.FluxManager;
import brostore.maquillage.manager.ProductManager;
import brostore.maquillage.manager.UserManager;

public class ProductActivity extends Activity {

    private Product myProduct;
    private int quantityAvailable;

    private boolean isFavorite;

    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    private BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("OK QUANTITY")) {
                quantityAvailable = Integer.parseInt(ProductManager.getInstance(getApplicationContext()).quantity);
                if (quantityAvailable > 10) {
                    inStock(false);
                } else if (quantityAvailable == 0) {
                    outOfStock();
                } else {
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

        //TODO
        FacebookSdk.sdkInitialize(getApplicationContext());

        IntentFilter filter = new IntentFilter();
        filter.addAction("OK QUANTITY");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadCastReceiver, filter);

        myProduct = getIntent().getParcelableExtra("product");

        ProductManager.getInstance(this).getQuantityForProduct(myProduct);

        init();

    }

    private void shareFacebook(){
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        String url = FluxManager.URL_SHARE.replace("__ID__", myProduct.getId()+"").replace("__LINK_REWRITE__", myProduct.getLinkRewrite());

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Maquillage.fr")
                    .setContentDescription("")
                    .setContentUrl(Uri.parse(url))
                    .build();
            shareDialog.show(linkContent);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {

        findViewById(R.id.article_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFacebook();
            }
        });

        if (myProduct.getBitmapImage() == null) {
            ((ImageView) findViewById(R.id.article_image)).setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.maquillage));
        } else {
            ((ImageView) findViewById(R.id.article_image)).setImageBitmap(myProduct.getBitmapImage());
        }

        ((TextView) findViewById(R.id.article_name)).setText(myProduct.getName());
        ((TextView) findViewById(R.id.article_infos)).setText(Html.fromHtml(myProduct.getDescription()));
        ((TextView) findViewById(R.id.article_prix1)).setText(" " + String.format("%.2f", myProduct.getPrice()) + "€ ");
        ((TextView) findViewById(R.id.article_prix1)).getPaint().setStrikeThruText(true);
        ((TextView) findViewById(R.id.article_prix2)).setText(String.format("%.2f", myProduct.getReducedPrice()) + "€");

        isFavorite = ProductManager.getInstance(this).isProductFavoris(myProduct.getId());

        if (isFavorite) {
            ((ImageView) findViewById(R.id.like)).setImageResource(R.drawable.ic_favorite_black_48dp);
        }

        findViewById(R.id.like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFavoriteStatus();
            }
        });
    }

    private void switchFavoriteStatus() {
        isFavorite = !isFavorite;
        if (isFavorite) {
            ((ImageView) findViewById(R.id.like)).setImageResource(R.drawable.ic_favorite_black_48dp);
            ProductManager.getInstance(this).saveProductFavoris(myProduct);
        } else {
            ((ImageView) findViewById(R.id.like)).setImageResource(R.drawable.ic_favorite_border_black_48dp);
            ProductManager.getInstance(this).deleteProductFavoris(myProduct);
        }
    }

    private void inStock(boolean rare) {

        findViewById(R.id.stock_loading).setVisibility(View.GONE);

        if (rare) {
            ((TextView) findViewById(R.id.stock)).setText("o Derniers");
            ((TextView) findViewById(R.id.stock)).setTextColor(getResources().getColor(R.color.orange_rare_stock));
        } else {
            ((TextView) findViewById(R.id.stock)).setText("o Disponible");
            ((TextView) findViewById(R.id.stock)).setTextColor(getResources().getColor(R.color.green_en_stock));
        }

        findViewById(R.id.ajout_panier).setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
        findViewById(R.id.ajout_panier).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(((EditText) findViewById(R.id.quantity)).getText().toString());

                if (quantity > quantityAvailable) {
                    Toast.makeText(getApplicationContext(), "Le nombre de produits disponible pour cette référence est limitée à " + quantityAvailable, Toast.LENGTH_LONG).show();
                } else {
                    UserManager.getInstance(getApplicationContext()).addInBasket(myProduct, quantity);
                    finish();
                }
                finish();
            }
        });
    }

    private void outOfStock() {

        findViewById(R.id.stock_loading).setVisibility(View.GONE);

        ((TextView) findViewById(R.id.stock)).setText("o Indisponible");
        ((TextView) findViewById(R.id.stock)).setTextColor(getResources().getColor(R.color.rouge_rupture_stock));

        findViewById(R.id.ajout_panier).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadCastReceiver);
        super.onDestroy();
    }
}