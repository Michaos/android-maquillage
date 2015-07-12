package brostore.maquillage.manager;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import brostore.maquillage.dao.Address;
import brostore.maquillage.utils.Utils;

/**
 * Created by Michaos on 12/07/2015.
 */
public class AdressManager {

    private static AdressManager instance;
    private static Context mContext;

    private ArrayList<Address> listAdresses;

    public static AdressManager getInstance(Context context) {
        if (instance == null) {
            instance = new AdressManager();
        }
        if (context != null) {
            mContext = context;
        }
        return instance;
    }

    public void getUserAdresses(){
        Utils.execute(new GetUserAddresses());
    }

    private class GetUserAddresses extends AsyncTask<Object, Object, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {

            Object o = ApiManager.callAPI(FluxManager.URL_GET_USER_ADDRESSES.replace("__ID__", UserManager.getInstance(mContext).getUser().getId()+""));

            if (o == null) {
                return false;
            }

            JSONArray jsonListAdresse = ((JSONObject) o).optJSONArray("addresses");

            for (int i = 0; i < jsonListAdresse.length(); i++) {
                JSONObject jsonObject = ApiManager.callAPI(FluxManager.URL_GET_ADRESS.replace("__ID__", jsonListAdresse.optJSONObject(i).optInt("id")+""));
                listAdresses.add(new Address(jsonObject));
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

    public ArrayList<Address> getListAdresses(){
        return listAdresses;
    }

}
