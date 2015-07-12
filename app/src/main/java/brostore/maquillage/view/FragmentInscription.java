package brostore.maquillage.view;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import brostore.maquillage.R;
import brostore.maquillage.manager.UserManager;

/**
 * Created by Michaos on 11/07/2015.
 */
public class FragmentInscription extends Fragment {

    private View rootView;

    private BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("CreateSuccess")) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentMonCompte()).commit();
                Toast.makeText(getActivity(), "Votre compte a été créé !", Toast.LENGTH_LONG).show();
            } else if (intent.getAction().equals("CreateFail")) {
                Toast.makeText(getActivity(), "Une erreur est survenue, réessayez plus tard.", Toast.LENGTH_LONG).show();
                rootView.findViewById(R.id.loadinglayout).setVisibility(View.GONE);
            }
        }
    };


    public FragmentInscription() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_mes_infos, container, false);

        IntentFilter filter = new IntentFilter();
        filter.addAction("CreateSuccess");
        filter.addAction("CreateFail");
        getActivity().registerReceiver(broadCastReceiver, filter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {

        ((EditText) rootView.findViewById(R.id.email)).setText(UserManager.getInstance(getActivity()).getUser().getEmail());
        rootView.findViewById(R.id.email).setFocusable(false);
        ((EditText) rootView.findViewById(R.id.email)).setTextColor(getResources().getColor(R.color.grey_new_new));


        rootView.findViewById(R.id.jj).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateClick();
            }
        });

        rootView.findViewById(R.id.mm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateClick();
            }
        });

        rootView.findViewById(R.id.aa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateClick();
            }
        });

        rootView.findViewById(R.id.valider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow((rootView.findViewById(R.id.email)).getWindowToken(), 0);

                if (checkForm()) {

                    rootView.findViewById(R.id.loadinglayout).setVisibility(View.VISIBLE);
                    ((TextView) rootView.findViewById(R.id.chargement)).setText(R.string.creation);

                    if (((RadioButton) getView().findViewById(R.id.femme)).isChecked()) {
                        UserManager.getInstance(getActivity()).getUser().setIdGender("2");
                    }
                    if (((RadioButton) getView().findViewById(R.id.homme)).isChecked()) {
                        UserManager.getInstance(getActivity()).getUser().setIdGender("1");
                    }
                    UserManager.getInstance(getActivity()).getUser().setFirstName(((EditText) getView().findViewById(R.id.firstName)).getText().toString());
                    UserManager.getInstance(getActivity()).getUser().setLastName(((EditText) getView().findViewById(R.id.lastName)).getText().toString());
                    UserManager.getInstance(getActivity()).getUser().setEmail(((EditText) getView().findViewById(R.id.email)).getText().toString());

                    if (((EditText) getView().findViewById(R.id.jj)).getText() != null && ((EditText) getView().findViewById(R.id.mm)).getText() != null && ((EditText) getView().findViewById(R.id.aa)).getText() != null) {
                        UserManager.getInstance(getActivity()).getUser().setBirthday(((EditText) getView().findViewById(R.id.aa)).getText().toString() + "-" + ((EditText) getView().findViewById(R.id.mm)).getText().toString() + "-" + ((EditText) getView().findViewById(R.id.jj)).getText().toString());
                    }

                    if (((CheckBox) getView().findViewById(R.id.newsletter)).isChecked()) {
                        UserManager.getInstance(getActivity()).getUser().setNewsletter("1");
                    } else {
                        UserManager.getInstance(getActivity()).getUser().setNewsletter("0");
                    }

                    UserManager.getInstance(getActivity()).createUser();

                }
            }
        });

        ((TextView) rootView.findViewById(R.id.champs)).setText(Html.fromHtml(getString(R.string.champs)));
        ((TextView) rootView.findViewById(R.id.conf)).setText(Html.fromHtml(getString(R.string.conf)));

    }

    private boolean checkForm() {
        if (((EditText) getView().findViewById(R.id.firstName)).getText().toString().trim().equalsIgnoreCase("")) {
            ((EditText) rootView.findViewById(R.id.firstName)).setError("Veuillez renseignez ce champ");
            return false;
        }
        if (((EditText) getView().findViewById(R.id.lastName)).getText().toString().trim().equalsIgnoreCase("")) {
            ((EditText) rootView.findViewById(R.id.lastName)).setError("Veuillez renseignez ce champ");
            return false;
        }
        if (((EditText) getView().findViewById(R.id.email)).getText().toString().trim().equalsIgnoreCase("")) {
            ((EditText) rootView.findViewById(R.id.email)).setError("Veuillez renseignez ce champ");
            return false;
        }
        return true;
    }

    public void dateClick() {

        int mYear, mMonth, mDay;
        final Calendar today = Calendar.getInstance();

        if (!((EditText) rootView.findViewById(R.id.jj)).getText().toString().equals("")) {
            mDay = Integer.parseInt(((EditText) rootView.findViewById(R.id.jj)).getText().toString());
            mMonth = Integer.parseInt(((EditText) rootView.findViewById(R.id.mm)).getText().toString());
            mYear = Integer.parseInt(((EditText) rootView.findViewById(R.id.aa)).getText().toString());
        } else {
            mDay = today.get(Calendar.DAY_OF_MONTH);
            mMonth = today.get(Calendar.MONTH);
            mYear = today.get(Calendar.YEAR);
        }

        final DatePickerDialog dpdFromDate = new DatePickerDialog(getActivity(), null, mYear, mMonth, mDay);
        dpdFromDate.show();

        dpdFromDate.setButton(DialogInterface.BUTTON_POSITIVE, "Okk", new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {

                    Calendar picked = Calendar.getInstance();
                    picked.set(dpdFromDate.getDatePicker().getYear(), dpdFromDate.getDatePicker().getMonth(), dpdFromDate.getDatePicker().getDayOfMonth());

                    if (picked.get(Calendar.YEAR) < today.get(Calendar.YEAR) - 101 || picked.compareTo(today) == 1) {
                        Toast.makeText(getActivity(), "Valeur incorrecte", Toast.LENGTH_LONG).show();
                    } else {
                        ((EditText) rootView.findViewById(R.id.jj)).setText(Integer.toString(dpdFromDate.getDatePicker().getDayOfMonth()));
                        ((EditText) rootView.findViewById(R.id.mm)).setText(Integer.toString(dpdFromDate.getDatePicker().getMonth() + 1));
                        ((EditText) rootView.findViewById(R.id.aa)).setText(Integer.toString(dpdFromDate.getDatePicker().getYear()));
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadCastReceiver);
    }
}
