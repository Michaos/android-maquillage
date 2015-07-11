package brostore.maquillage.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import brostore.maquillage.R;

/**
 * Created by Michaos on 11/07/2015.
 */
public class FragmentMonCompte extends Fragment {

    private View rootView;

    public FragmentMonCompte(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_mon_compte, container, false);
        return rootView;
    }

}
