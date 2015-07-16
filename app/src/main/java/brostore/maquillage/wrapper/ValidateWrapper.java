package brostore.maquillage.wrapper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import brostore.maquillage.R;

/**
 * Created by clairecoloma on 16/07/15.
 */
public class ValidateWrapper {

    private View baseView;
    private ImageView imageView;
    private TextView name;
    private TextView quantity;
    private TextView totalPrice;

    public ValidateWrapper(View base) { this.baseView = base; }

    public View getBaseView() { return baseView; }

    public ImageView getImageView() {
        if (imageView == null) {
            imageView = (ImageView) baseView.findViewById(R.id.order_article_image);
        }
        return (imageView);
    }

    public TextView getName() {
        if(name == null) {
            name = (TextView) baseView.findViewById(R.id.order_article_name);
        }
        return(name);
    }

    public TextView getQuantity() {
        if( quantity == null) {
            quantity = (TextView) baseView.findViewById(R.id.order_article_qty);
        }
        return quantity;
    }

    public TextView getTotalPrice() {
        if (totalPrice == null) {
            totalPrice = (TextView) baseView.findViewById(R.id.order_total_price);
        }
        return totalPrice;
    }
}
