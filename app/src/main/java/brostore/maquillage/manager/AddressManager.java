package brostore.maquillage.manager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import brostore.maquillage.dao.Address;
import brostore.maquillage.utils.Utils;

/**
 * Created by Michaos on 12/07/2015.
 */
public class AddressManager {

    private static AddressManager instance;
    private static Context mContext;

    private ArrayList<Address> listAddresses;

    public static AddressManager getInstance(Context context) {
        if (instance == null) {
            instance = new AddressManager();
        }
        if (context != null) {
            mContext = context;
        }
        return instance;
    }

    public void getUserAddresses() {
        Utils.execute(new GetUserAddresses());
    }

    private class GetUserAddresses extends AsyncTask<Object, Object, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {

            Object o = ApiManager.callAPI(FluxManager.URL_GET_USER_ADDRESSES.replace("__ID__", UserManager.getInstance(mContext).getUser().getId() + ""));

            if (o == null) {
                return false;
            }

            JSONArray jsonListAddresse = ((JSONObject) o).optJSONArray("addresses");

            for (int i = 0; i < jsonListAddresse.length(); i++) {
                JSONObject jsonObject = ApiManager.callAPI(FluxManager.URL_GET_ADRESS.replace("__ID__", jsonListAddresse.optJSONObject(i).optInt("id") + ""));
                listAddresses.add(new Address(jsonObject));
            }
            return true;
        }

        @Override
        public void onPostExecute(Boolean result) {
            if (result) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("OK ADDRESSES"));
            } else {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("KO ADDRESSES"));
            }
        }
    }

    public ArrayList<Address> getListAddresses() {
        return listAddresses;
    }

}
