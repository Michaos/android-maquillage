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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import brostore.maquillage.R;
import brostore.maquillage.manager.UserManager;

public class FragmentCompte extends Fragment {

    private View rootView;

    private BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("OK CONNECT")) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentMonCompte()).commit();
            }else if (intent.getAction().equals("KO CONNECT")){
                Toast.makeText(getActivity(), "E-mail ou Mot de passe incorrecte.", Toast.LENGTH_LONG).show();
                rootView.findViewById(R.id.loadinglayout).setVisibility(View.GONE);
            }else if(intent.getAction().equals("MAIL DISPONIBLE")){
                UserManager.getInstance(getActivity()).getUserBlank();
            }else if(intent.getAction().equals("MAIL INDISPONIBLE")){
                ((EditText) rootView.findViewById(R.id.email)).setError("Cet E-mail est déja utilisé.");
                rootView.findViewById(R.id.loadinglayout).setVisibility(View.GONE);
            }else if(intent.getAction().equals("OK USER BLANK")){
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentInscription()).commit();
            }else if(intent.getAction().equals("KO USER BLANK")){
                Toast.makeText(getActivity(), "Erreur lors de la récupération du formulaire. Veuillez réessayer.", Toast.LENGTH_LONG).show();
                rootView.findViewById(R.id.loadinglayout).setVisibility(View.GONE);
            }
        }
    };

    public FragmentCompte() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_compte, container, false);

        UserManager.getInstance(getActivity()).resetUser();

        IntentFilter filter = new IntentFilter();
        filter.addAction("OK CONNECT");
        filter.addAction("KO CONNECT");
        filter.addAction("MAIL DISPONIBLE");
        filter.addAction("MAIL INDISPONIBLE");
        filter.addAction("OK USER BLANK");
        filter.addAction("KO USER BLANK");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadCastReceiver, filter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TextView) rootView.findViewById(R.id.champs)).setText(Html.fromHtml(getString(R.string.champs)));
        ((TextView) rootView.findViewById(R.id.conf)).setText(Html.fromHtml(getString(R.string.conf)));

        rootView.findViewById(R.id.connexion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo remetre le checkform et enlevé le test
                if (true /*checkForm()*/) {
                    //UserManager.getInstance(getActivity()).getUser().setEmail(((EditText) rootView.findViewById(R.id.email)).getText().toString());
                    //UserManager.getInstance(getActivity()).getUser().setMdp(((EditText) rootView.findViewById(R.id.mdp)).getText().toString());

                    //test
                    UserManager.getInstance(getActivity()).getUser().setEmail("finaltest@mail.com");
                    UserManager.getInstance(getActivity()).getUser().setMdp("finaltest");
                    //test

                    UserManager.getInstance(getActivity()).goConnect();

                    rootView.findViewById(R.id.loadinglayout).setVisibility(View.VISIBLE);
                    ((TextView)rootView.findViewById(R.id.chargement)).setText(R.string.connexion);

                }
            }
        });

        rootView.findViewById(R.id.inscription).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo remetre le checkform et enlevé le test
                if (true /*checkForm()*/) {
                    //UserManager.getInstance(getActivity()).getUser().setEmail(((EditText) rootView.findViewById(R.id.email)).getText().toString());
                    //UserManager.getInstance(getActivity()).getUser().setMdp(((EditText) rootView.findViewById(R.id.mdp)).getText().toString());

                    //test
                    UserManager.getInstance(getActivity()).getUser().setEmail("finaltest@mail.com");
                    UserManager.getInstance(getActivity()).getUser().setMdp("finaltest");
                    //test

                    UserManager.getInstance(getActivity()).checkMail();

                    rootView.findViewById(R.id.loadinglayout).setVisibility(View.VISIBLE);
                    ((TextView)rootView.findViewById(R.id.chargement)).setText(R.string.verifmail);

                }
            }
        });


    }

    private boolean checkForm(){
        if (((EditText) rootView.findViewById(R.id.email)).getText().toString().trim().equalsIgnoreCase("")) {
            ((EditText) rootView.findViewById(R.id.email)).setError("Veuillez renseignez ce champ");
            return false;
        } else {
            ((EditText) rootView.findViewById(R.id.email)).setError(null);
        }

        if (((EditText) rootView.findViewById(R.id.mdp)).getText().toString().trim().equalsIgnoreCase("")) {
            ((EditText) rootView.findViewById(R.id.mdp)).setError("Veuillez renseignez ce champ");
            return false;
        } else {
            ((EditText) rootView.findViewById(R.id.mdp)).setError(null);
        }
        return true;
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadCastReceiver);
        super.onDestroy();
    }
}
