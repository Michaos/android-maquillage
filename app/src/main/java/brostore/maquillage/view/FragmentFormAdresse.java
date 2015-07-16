package brostore.maquillage.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import brostore.maquillage.R;
import brostore.maquillage.dao.Address;
import brostore.maquillage.manager.AddressManager;
import brostore.maquillage.manager.UserManager;

/**
 * Created by Michaos on 16/07/2015.
 */
public class FragmentFormAdresse extends Fragment {

    private View rootView;

    private BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("CREATE ADDRESS SUCCESS")) {
                getActivity().getSupportFragmentManager().popBackStack();
            } else if (intent.getAction().equals("CREATE ADDRESS FAIL")) {
                Toast.makeText(getActivity(), "Une erreur est survenue, réessayez plus tard.", Toast.LENGTH_LONG).show();
                rootView.findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.loadinglayout).setVisibility(View.GONE);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_new_address, container, false);

        IntentFilter filter = new IntentFilter("CREATE ADDRESS SUCCESS");
        filter.addAction("CREATE ADDRESS FAIL");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadCastReceiver, filter);

        init();

        return rootView;
    }

    private void init() {

        ((EditText) rootView.findViewById(R.id.firstName)).setText(UserManager.getInstance(getActivity()).getUser().getFirstName());
        ((EditText) rootView.findViewById(R.id.lastName)).setText(UserManager.getInstance(getActivity()).getUser().getLastName());

        rootView.findViewById(R.id.valider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow((rootView.findViewById(R.id.company)).getWindowToken(), 0);

                if (checkForm()) {

                    rootView.findViewById(R.id.scrollView).setVisibility(View.GONE);
                    rootView.findViewById(R.id.loadinglayout).setVisibility(View.VISIBLE);

                    Address newAddress = new Address();

                    if (((RadioButton) rootView.findViewById(R.id.france)).isChecked()) {
                        newAddress.setIdCountry("8");
                    }
                    if (((RadioButton) rootView.findViewById(R.id.belgium)).isChecked()) {
                        newAddress.setIdCountry("3");
                    }

                    newAddress.setAlias(((EditText) rootView.findViewById(R.id.alias)).getText().toString());
                    newAddress.setLastName(((EditText) rootView.findViewById(R.id.lastName)).getText().toString());
                    newAddress.setFirstName(((EditText) rootView.findViewById(R.id.firstName)).getText().toString());

                    newAddress.setCompany(((EditText) rootView.findViewById(R.id.company)).getText().toString());
                    newAddress.setVatNumber(((EditText) rootView.findViewById(R.id.vat)).getText().toString());

                    newAddress.setAddress1(((EditText) rootView.findViewById(R.id.address)).getText().toString());
                    newAddress.setAddress2(((EditText) rootView.findViewById(R.id.address2)).getText().toString());

                    newAddress.setPostcode(((EditText) rootView.findViewById(R.id.cp)).getText().toString());
                    newAddress.setCity(((EditText) rootView.findViewById(R.id.city)).getText().toString());

                    newAddress.setPhone(((EditText) rootView.findViewById(R.id.tel_fixe)).getText().toString());
                    newAddress.setPhoneMobile(((EditText) rootView.findViewById(R.id.tel_portable)).getText().toString());

                    newAddress.setPostcode(((EditText) rootView.findViewById(R.id.cp)).getText().toString());
                    newAddress.setOther(((EditText) rootView.findViewById(R.id.infos)).getText().toString());

                    AddressManager.getInstance(getActivity()).createAddress(newAddress);

                }
            }
        });

        ((TextView) rootView.findViewById(R.id.champs)).setText(Html.fromHtml(getString(R.string.champs)));
        ((TextView) rootView.findViewById(R.id.champs2)).setText(Html.fromHtml(getString(R.string.champs2)));
        ((TextView) rootView.findViewById(R.id.conf)).setText(Html.fromHtml(getString(R.string.conf)));

    }

    private boolean checkForm() {
        if (((EditText) rootView.findViewById(R.id.alias)).getText().toString().trim().equalsIgnoreCase("")) {
            ((EditText) rootView.findViewById(R.id.alias)).setError("Veuillez renseignez ce champ");
            return false;
        }
        if (((EditText) rootView.findViewById(R.id.firstName)).getText().toString().trim().equalsIgnoreCase("")) {
            ((EditText) rootView.findViewById(R.id.firstName)).setError("Veuillez renseignez ce champ");
            return false;
        }
        if (((EditText) rootView.findViewById(R.id.lastName)).getText().toString().trim().equalsIgnoreCase("")) {
            ((EditText) rootView.findViewById(R.id.lastName)).setError("Veuillez renseignez ce champ");
            return false;
        }
        if (((EditText) rootView.findViewById(R.id.address)).getText().toString().trim().equalsIgnoreCase("")) {
            ((EditText) rootView.findViewById(R.id.address)).setError("Veuillez renseignez ce champ");
            return false;
        }
        if (((EditText) rootView.findViewById(R.id.cp)).getText().toString().trim().equalsIgnoreCase("")) {
            ((EditText) rootView.findViewById(R.id.cp)).setError("Veuillez renseignez ce champ");
            return false;
        }
        if (((EditText) rootView.findViewById(R.id.city)).getText().toString().trim().equalsIgnoreCase("")) {
            ((EditText) rootView.findViewById(R.id.city)).setError("Veuillez renseignez ce champ");
            return false;
        }
        if (((EditText) rootView.findViewById(R.id.tel_fixe)).getText().toString().trim().equalsIgnoreCase("") && ((EditText) rootView.findViewById(R.id.tel_portable)).getText().toString().trim().equalsIgnoreCase("")) {
            ((EditText) rootView.findViewById(R.id.tel_fixe)).setError("Veuillez renseignez au moins un numéro de téléphone");
            ((EditText) rootView.findViewById(R.id.tel_portable)).setError("Veuillez renseignez au moins un numéro de téléphone");
            return false;
        }

        return true;
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadCastReceiver);
        super.onDestroy();
    }
}