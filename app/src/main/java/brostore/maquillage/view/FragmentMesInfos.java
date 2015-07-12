package brostore.maquillage.view;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class FragmentMesInfos extends Fragment {

    private View rootView;

    public FragmentMesInfos(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_mes_infos, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init(){

        if(UserManager.getInstance(getActivity()).getUser().getIdGender() != null && UserManager.getInstance(getActivity()).getUser().getIdGender().equals("2")){
            ((RadioButton) getView().findViewById(R.id.femme)).setChecked(true);
        }else if(UserManager.getInstance(getActivity()).getUser().getIdGender() != null && UserManager.getInstance(getActivity()).getUser().getIdGender().equals("1")){
            ((RadioButton) getView().findViewById(R.id.homme)).setChecked(true);
        }else{
            ((RadioButton) getView().findViewById(R.id.femme)).setChecked(false);
            ((RadioButton) getView().findViewById(R.id.homme)).setChecked(false);
        }

        ((EditText) rootView.findViewById(R.id.firstName)).setText(UserManager.getInstance(getActivity()).getUser().getFirstName());
        ((EditText) rootView.findViewById(R.id.lastName)).setText(UserManager.getInstance(getActivity()).getUser().getLastName());
        ((EditText) rootView.findViewById(R.id.email)).setText(UserManager.getInstance(getActivity()).getUser().getEmail());

        ((EditText) rootView.findViewById(R.id.jj)).setText(UserManager.getInstance(getActivity()).getUser().getDdnj());
        ((EditText) rootView.findViewById(R.id.mm)).setText(UserManager.getInstance(getActivity()).getUser().getDdnm());
        ((EditText) rootView.findViewById(R.id.aa)).setText(UserManager.getInstance(getActivity()).getUser().getDdna());

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

        ((TextView) rootView.findViewById(R.id.champs)).setText(Html.fromHtml(getString(R.string.champs)));
        ((TextView) rootView.findViewById(R.id.conf)).setText(Html.fromHtml(getString(R.string.conf)));

    }

    public void dateClick(){

        int mYear, mMonth, mDay;
        final Calendar today = Calendar.getInstance();

        if(!((EditText) rootView.findViewById(R.id.jj)).getText().toString().equals("")){
            mDay = Integer.parseInt(((EditText) rootView.findViewById(R.id.jj)).getText().toString());
            mMonth = Integer.parseInt(((EditText) rootView.findViewById(R.id.mm)).getText().toString());
            mYear = Integer.parseInt(((EditText) rootView.findViewById(R.id.aa)).getText().toString());
        }else{
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
}
