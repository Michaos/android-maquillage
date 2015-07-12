package brostore.maquillage.wrapper;

import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import brostore.maquillage.R;

/**
 * Created by Michaos on 07/07/2015.
 */
public class BasketWrapper {

    private View baseView;
    private ImageView image;
    private TextView name;
    private Spinner quantitySpinner;
    private TextView totalPrice;

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

    public Spinner getArticleQuantitySpinner() {
        if (quantitySpinner == null) {
            quantitySpinner = (Spinner) baseView.findViewById(R.id.article_quantity_spinner);
        }
        return(quantitySpinner);
    }

    public TextView getArticleTotalPrice() {
        if (totalPrice == null) {
            totalPrice = (TextView) baseView.findViewById(R.id.article_total_price);
        }
        return (totalPrice);
    }

}
