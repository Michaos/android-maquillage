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

    private class GetUserAddresses extends AsyncTask<Object, Object, Integer> {

        @Override
        protected Integer doInBackground(Object... params) {

            listAddresses = new ArrayList<>();

            Object o = ApiManager.callAPI(FluxManager.URL_GET_USER_ADDRESSES.replace("__ID__", UserManager.getInstance(mContext).getUser().getId() + ""));

            if (o == null){
                return 2;
            } else if (o instanceof JSONArray && ((JSONArray) o).length() == 0){
                return 0;
            }

            JSONArray jsonListAddresse = ((JSONObject) o).optJSONArray("addresses");

            for (int i = 0; i < jsonListAddresse.length(); i++) {
                JSONObject jsonObject = (JSONObject) ApiManager.callAPI(FluxManager.URL_GET_ADDRESS.replace("__ID__", jsonListAddresse.optJSONObject(i).optInt("id") + ""));
                if(jsonObject != null){

                    Address adress = new  Address(jsonObject);

                    String idCountry = jsonObject.optJSONObject("address").optString("id_country");

                    JSONObject countryInfos = (JSONObject) ApiManager.callAPI(FluxManager.URL_GET_COUNTRY.replace("__ID__", idCountry));

                    if(countryInfos != null && countryInfos.optJSONObject("country") != null){
                        adress.setCountry(countryInfos.optJSONObject("country").optString("name"));
                    }

                    listAddresses.add(adress);

                }else{
                    return 2;
                }
            }
            return 1;
        }

        @Override
        public void onPostExecute(Integer result) {
            if (result == 1) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("OK ADDRESSES"));
            } else  if (result == 2) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("KO ADDRESSES"));
            } else if (result == 0) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("NO ADDRESSES"));
            }
        }
    }

    public ArrayList<Address> getListAddresses() {
        return listAddresses;
    }

}
