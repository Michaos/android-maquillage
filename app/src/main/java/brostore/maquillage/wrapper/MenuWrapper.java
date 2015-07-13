package brostore.maquillage.wrapper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import brostore.maquillage.R;

public class MenuWrapper {

    private View baseView;
    private ImageView menuView = null;
    private ImageView fullImgView = null;
    private TextView menuText = null;
    private TextView messageIndicator = null;
    private View selView = null;
    private View sepView = null;
    private View colorView = null;

    public MenuWrapper(View base) {
        this.baseView = base;
    }

    public View getBaseView() {
        return baseView;
    }

    public View getSelView() {
        if (selView == null) {
            selView = (View) baseView.findViewById(R.id.selView);
        }
        return (selView);
    }

    public ImageView getMenuView() {
        if (menuView == null) {
            menuView = (ImageView) baseView.findViewById(R.id.menuView);
        }
        return (menuView);
    }

    public ImageView getFullImgView() {
        if (fullImgView == null) {
            fullImgView = (ImageView) baseView.findViewById(R.id.fullImgView);
        }
        return (fullImgView);
    }

    public TextView getMenuText() {
        if (menuText == null) {
            menuText = (TextView) baseView.findViewById(R.id.menuText);
        }
        return (menuText);
    }

    public View getSepView() {
        if (sepView == null) {
            sepView = (View) baseView.findViewById(R.id.sepView);
        }
        return (sepView);
    }

    public View getColorView() {
        if (colorView == null) {
            colorView = (View) baseView.findViewById(R.id.colorView);
        }
        return (colorView);
    }

}
