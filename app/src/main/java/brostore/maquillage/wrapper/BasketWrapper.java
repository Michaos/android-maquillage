package brostore.maquillage.wrapper;

import android.view.View;
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
    private TextView prix;
    private TextView quantite;
    private TextView prixTotal;

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

    public TextView getArticleNom() {
        if (name == null) {
            name = (TextView) baseView.findViewById(R.id.article_nom);
        }
        return (name);
    }

    public TextView getArticlePrix() {
        if (prix == null) {
            prix = (TextView) baseView.findViewById(R.id.article_prix);
        }
        return (prix);
    }

    public TextView getArticleQuantite() {
        if (quantite == null) {
            quantite = (TextView) baseView.findViewById(R.id.article_quantite);
        }
        return (quantite);
    }

    public TextView getArticlePrixTotal() {
        if (prixTotal == null) {
            prixTotal = (TextView) baseView.findViewById(R.id.article_prix_total);
        }
        return (prixTotal);
    }

}
