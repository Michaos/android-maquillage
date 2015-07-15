package brostore.maquillage.wrapper;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import brostore.maquillage.R;

/**
 * Created by Michaos on 07/07/2015.
 */
public class BasketWrapper {

    private View baseView;
    private ImageView image;
    private TextView name;
    //private Spinner quantitySpinner;
    private TextView totalPrice;
    private Button btnDelete;
    private EditText qty;
    //private ImageView refresh;

    public BasketWrapper(View base) {
        this.baseView = base;
    }

    public View getBaseView() {
        return baseView;
    }

    public ImageView getArticleImage() {
        if (image == null) {
            image = (ImageView) baseView.findViewById(R.id.article_image);
        }
        return (image);
    }

    public TextView getArticleName() {
        if (name == null) {
            name = (TextView) baseView.findViewById(R.id.article_name);
        }
        return (name);
    }

    /*public Spinner getArticleQuantitySpinner() {
        if (quantitySpinner == null) {
            quantitySpinner = (Spinner) baseView.findViewById(R.id.article_quantity_spinner);
        }
        return(quantitySpinner);
    }*/

    public Button getBtnDelete() {
        if (btnDelete == null) {
            btnDelete = (Button) baseView.findViewById(R.id.article_delete);
        }

        return(btnDelete);
    }

    public TextView getArticleTotalPrice() {
        if (totalPrice == null) {
            totalPrice = (TextView) baseView.findViewById(R.id.article_total_price);
        }
        return (totalPrice);
    }

    public EditText getArticleQty() {
        if (qty == null) {
            qty = (EditText) baseView.findViewById(R.id.article_qty);
        }
        return (qty);
    }

    /*public ImageView getRefresh() {
        if (refresh == null) {
            refresh = (ImageView) baseView.findViewById(R.id.refresh);
        }
        return (refresh);
    }*/

}
