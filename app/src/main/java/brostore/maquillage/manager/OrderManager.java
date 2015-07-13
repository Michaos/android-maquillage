package brostore.maquillage.manager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import brostore.maquillage.dao.Order;
import brostore.maquillage.utils.Utils;

/**
 * Created by Michaos on 12/07/2015.
 */
public class OrderManager {

    private static OrderManager instance;
    private static Context mContext;

    private ArrayList<Order> listOrders;

    public static OrderManager getInstance(Context context) {
        if (instance == null) {
            instance = new OrderManager();
        }
        if (context != null) {
            mContext = context;
        }
        return instance;
    }

    public void getUserOrders() {
        Utils.execute(new GetUserOrders());
    }

    private class GetUserOrders extends AsyncTask<Object, Object, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {

            Object o = ApiManager.callAPI(FluxManager.URL_GET_USER_ORDERS.replace("__ID__", UserManager.getInstance(mContext).getUser().getId() + ""));

            if (o == null) {
                return false;
            }

            JSONArray jsonListOrder = ((JSONObject) o).optJSONArray("addresses");

            for (int i = 0; i < jsonListOrder.length(); i++) {
                JSONObject jsonObject = ApiManager.callAPI(FluxManager.URL_GET_ADRESS.replace("__ID__", jsonListOrder.optJSONObject(i).optInt("id") + ""));
                listOrders.add(new Order(jsonObject));
            }
            return true;
        }

        @Override
        public void onPostExecute(Boolean result) {
            if (result) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("OK ORDERS"));
            } else {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("KO ORDERS"));
            }
        }
    }

    public ArrayList<Order> getListOrders() {
        return listOrders;
    }

}
