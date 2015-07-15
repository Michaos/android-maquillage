package brostore.maquillage.wrapper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import brostore.maquillage.R;

public class ProductWrapper {

    private View baseView;
    private ImageView image;
    private TextView name;
    private TextView infos;
    private TextView prix1;
    private TextView prix2;


    public ProductWrapper(View base) {
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
            name = (TextView) baseView.findViewById(R.id.article_name);
        }
        return (name);
    }

    /*public TextView getArticleInfos() {
        if (infos == null) {
            infos = (TextView) baseView.findViewById(R.id.article_infos);
        }
        return (infos);
    }*/

    public TextView getArticlePrix1() {
        if (prix1 == null) {
            prix1 = (TextView) baseView.findViewById(R.id.article_prix1);
        }
        return (prix1);
    }

    public TextView getArticlePrix2() {
        if (prix2 == null) {
            prix2 = (TextView) baseView.findViewById(R.id.article_prix2);
        }
        return (prix2);
    }


}
