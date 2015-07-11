package brostore.maquillage.manager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONObject;

import brostore.maquillage.dao.Product;
import brostore.maquillage.dao.User;
import brostore.maquillage.utils.Utils;

/**
 * Created by Michaos on 11/07/2015.
 */
public class UserManager {

    private static UserManager instance;
    private static Context mContext;

    public static UserManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserManager();
        }
        if (context != null) {
            mContext = context;
        }
        return instance;
    }

    public Boolean hasToOpenBasket = false;

    public Double totalBasket = 0.0;
    public Double totalSaving = 0.0;
    private User user;

    public void addInBasket(Product p, int quantity){
        totalBasket +=  p.getReducedPrice() * quantity;
        getUser().setTotalBasket(totalBasket);

        totalSaving += (p.getPrice() - p.getReducedPrice()) * quantity;
        getUser().setTotalSaving(totalSaving);

        int has = getUser().hasAlreadyThatProduct(p);
        if(has != -1) {
            int qte = getUser().getQuantities().get(has);
            qte += quantity;
            getUser().getBasket().set(has, p);
            getUser().getQuantities().set(has, qte);
        }else{
            getUser().getBasket().add(p);
            getUser().getQuantities().add(quantity);
        }
        hasToOpenBasket = true;
    }

    public User getUser(){
        if(user == null){
            user = new User();
        }
        return user;
    }

    public void goConnect() {
        Utils.execute(new Connect());
    }

    private class Connect extends AsyncTask<Object, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {

            String url = FluxManager.URL_CONNECT.replace("__EMAIL__", user.getEmail()).replace("__ENCRYPTED_MDP__", user.getEncryitedMdp());

            System.out.println("URL :: " + url);

            Object o = ApiManager.callAPI(url);
            int id;


            if(o == null){
                return false;
            }else{
                id = ((JSONObject) o).optJSONArray("customers").optJSONObject(0).optInt("id");
            }

            JSONObject jsonObjectUser =  ApiManager.callAPI(FluxManager.URL_GET_USER.replace("__ID__", id+""));

            user.setId(jsonObjectUser.optJSONObject("customer").optInt("id"));
            user.setLastName(jsonObjectUser.optJSONObject("customer").optString("lastname"));
            user.setFirstName(jsonObjectUser.optJSONObject("customer").optString("firstname"));
            user.setIdGender(jsonObjectUser.optJSONObject("customer").optString("id_gender"));
            user.setBirthday(jsonObjectUser.optJSONObject("customer").optString("birthday"));

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("OK CONNECT"));
            }else{
                System.out.println("ERROR IDENTIFICATION");
            }
        }
    }
}
