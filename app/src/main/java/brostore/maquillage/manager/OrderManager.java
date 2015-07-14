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

    private class GetUserOrders extends AsyncTask<Object, Object, Integer> {

        @Override
        protected Integer doInBackground(Object... params) {

            listOrders = new ArrayList<>();

            Object o = ApiManager.callAPI(FluxManager.URL_GET_USER_ORDERS.replace("__ID__", UserManager.getInstance(mContext).getUser().getId() + ""));

            if (o == null) {
                return 2;
            } else if (((JSONArray) o).length() == 0) {
                return 0;
            }

            JSONArray jsonListOrder = ((JSONObject) o).optJSONArray("orders");

            for (int i = 0; i < jsonListOrder.length(); i++) {
                JSONObject jsonObject = (JSONObject) ApiManager.callAPI(FluxManager.URL_GET_ORDER.replace("__ID__", jsonListOrder.optJSONObject(i).optInt("id") + ""));
                if (jsonObject != null) {
                    listOrders.add(new Order(jsonObject));
                } else {
                    return 2;
                }
            }
            return 1;
        }

        @Override
        public void onPostExecute(Integer result) {
            if (result == 1) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("OK ORDERS"));
            } else if (result == 2) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("KO ORDERS"));
            } else if (result == 0) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("NO ORDERS"));
            }
        }
    }

    public ArrayList<Order> getListOrders() {
        return listOrders;
    }

}
