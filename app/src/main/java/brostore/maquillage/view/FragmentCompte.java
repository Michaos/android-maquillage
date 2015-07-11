package brostore.maquillage.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import brostore.maquillage.R;
import brostore.maquillage.manager.UserManager;

public class FragmentCompte extends Fragment {

    private View rootView;

    public FragmentCompte() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_compte, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView.findViewById(R.id.connexion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean go = true;
                /*if (((EditText) rootView.findViewById(R.id.email)).getText().toString().trim().equalsIgnoreCase("")) {
                    ((EditText) rootView.findViewById(R.id.email)).setError("Veuillez renseignez ce champ");
                    go = false;
                } else {
                    ((EditText) rootView.findViewById(R.id.email)).setError(null);
                }

                if (((EditText) rootView.findViewById(R.id.mdp)).getText().toString().trim().equalsIgnoreCase("")) {
                    ((EditText) rootView.findViewById(R.id.mdp)).setError("Veuillez renseignez ce champ");
                    go = false;
                } else {
                    ((EditText) rootView.findViewById(R.id.mdp)).setError(null);
                }*/

                if (go) {
                    //UserManager.getInstance(getActivity()).getUser().setEmail(((EditText) rootView.findViewById(R.id.email)).getText().toString());
                    //UserManager.getInstance(getActivity()).getUser().setMdp(((EditText) rootView.findViewById(R.id.mdp)).getText().toString());

                    UserManager.getInstance(getActivity()).getUser().setEmail("test2@yopmail.com");
                    UserManager.getInstance(getActivity()).getUser().setMdp("test2");

                    UserManager.getInstance(getActivity()).goConnect();
                }
            }
        });


    }
}
