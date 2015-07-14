package brostore.maquillage.wrapper;

import android.view.View;
import android.widget.TextView;

import brostore.maquillage.R;

/**
 * Created by Michaos on 13/07/2015.
 */
public class AddressWrapper {

    private View baseView;
    private TextView alias;
    private TextView societe;
    private TextView tva;
    private TextView adresse;
    private TextView adresse2;
    private TextView cp;
    private TextView ville;
    private TextView pays;
    private TextView telfixe;
    private TextView telport;
    private TextView infos;

    public AddressWrapper(View base) {
        this.baseView = base;
    }

    public View getBaseView() {
        return baseView;
    }

    public TextView getAlias() {
        if (alias == null) {
            alias = (TextView) baseView.findViewById(R.id.alias);
        }
        return alias;
    }

    public TextView getSociete() {
        if (societe == null) {
            societe = (TextView) baseView.findViewById(R.id.societe);
        }
        return societe;
    }

    public TextView getTva() {
        if (tva == null) {
            tva = (TextView) baseView.findViewById(R.id.tva);
        }
        return tva;
    }

    public TextView getAdresse() {
        if (adresse == null) {
            adresse = (TextView) baseView.findViewById(R.id.adresse);
        }
        return adresse;
    }

    public TextView getAdresse2() {
        if (adresse2 == null) {
            adresse2 = (TextView) baseView.findViewById(R.id.adresse2);
        }
        return adresse2;
    }

    public TextView getCp() {
        if (cp == null) {
            cp = (TextView) baseView.findViewById(R.id.cp);
        }
        return cp;
    }

    public TextView getVille() {
        if (ville == null) {
            ville = (TextView) baseView.findViewById(R.id.ville);
        }
        return ville;
    }

    public TextView getPays() {
        if (pays == null) {
            pays = (TextView) baseView.findViewById(R.id.pays);
        }
        return pays;
    }

    public TextView getTelfixe() {
        if (telfixe == null) {
            telfixe = (TextView) baseView.findViewById(R.id.tel_fixe);
        }
        return telfixe;
    }

    public TextView getTelport() {
        if (telport == null) {
            telport = (TextView) baseView.findViewById(R.id.tel_portable);
        }
        return telport;
    }

    public TextView getInfos() {
        if (infos == null) {
            infos = (TextView) baseView.findViewById(R.id.infos);
        }
        return infos;
    }
}
