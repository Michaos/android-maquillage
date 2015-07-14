package brostore.maquillage.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import brostore.maquillage.R;
import brostore.maquillage.manager.UserManager;

/**
 * Created by Michaos on 11/07/2015.
 */
public class FragmentMonCompte extends Fragment {

    private View rootView;

    public FragmentMonCompte() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_mon_compte, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String welcome = "Bienvenue ";

        if (UserManager.getInstance(getActivity()).getUser().getIdGender().equals("1")) {
            welcome = "Bienvenu ";
        }

        ((TextView) rootView.findViewById(R.id.welcome)).setText(welcome + UserManager.getInstance(getActivity()).getUser().getFirstName());

        rootView.findViewById(R.id.adresses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, new FragmentMesAdresses()).commit();
            }
        });

        rootView.findViewById(R.id.commandes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, new FragmentMesCommandes()).commit();
            }
        });

        rootView.findViewById(R.id.infos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, new FragmentMesInfos()).commit();
            }
        });

        rootView.findViewById(R.id.wishlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, new FragmentMaWishList()).commit();
            }
        });

    }
}
