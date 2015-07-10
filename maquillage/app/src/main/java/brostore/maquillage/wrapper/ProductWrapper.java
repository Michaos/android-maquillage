package brostore.maquillage.wrapper;

import android.view.View;
import android.widget.TextView;

import brostore.maquillage.R;

public class ProductWrapper {

	private View baseView;
	private TextView name;

	public ProductWrapper(View base) {
		this.baseView = base;
	}
	
	public View getBaseView() {
		return baseView;
	}

	public TextView getArticleNom() {
		if (name == null) {
			name = (TextView) baseView.findViewById(R.id.article_nom);
		}
		return (name);
	}

}
