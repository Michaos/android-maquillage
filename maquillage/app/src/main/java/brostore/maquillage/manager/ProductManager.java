package brostore.maquillage.manager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import brostore.maquillage.dao.Product;
import brostore.maquillage.utils.Utils;

/**
 * Created by Michaos on 18/05/2015.
 */
public class ProductManager {

    private static ProductManager instance;
    private static Context mContext;

    private HashMap<String, List<Product>> listRubrique = new HashMap<>();

    private ProductManager() {

    }

    public static ProductManager getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    public void getProductsForCategory(Integer id) {
        Utils.execute(new getProductsForCategoryTask(), id);
    }

    private class getProductsForCategoryTask extends AsyncTask<Integer, Object, Boolean> {

        private int categoryId;

        @Override
        protected Boolean doInBackground(Integer... id) {

            categoryId = id[0];

            JSONObject jsonObject = ApiManager.callAPI(FluxManager.URL_CATEGORIES.replace("__ID__", categoryId + ""));

            if (jsonObject == null) {
                return false;
            }

            boolean etat = parseProductsForCategory(jsonObject);

            if (etat) {
                //CacheManager.createCache(jsonObject, DIR_DATA, "Menu");
            }
            return etat;
        }

        @Override
        public void onPostExecute(Boolean result) {
            if (result) {
                System.out.println("AAAA OK get all products");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("OK PRODUCTS" + categoryId));
            } else {
                // LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("KO MENU"));
            }
        }
    }

    public synchronized boolean parseProductsForCategory(JSONObject json) {
        if (json == null) {
            return false;
        }
        List<Product> productsListTemp = new ArrayList<>();
        try {
            JSONArray jsonArray = json.getJSONObject("category").getJSONObject("associations").getJSONArray("products");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonProduct = ApiManager.callAPI(FluxManager.URL_PRODUCT.replace("__ID__", jsonArray.getJSONObject(i).optString("id", "")));
                Product product = new Product(jsonProduct);
                productsListTemp.add(product);
            }
            setList(json.optJSONObject("category").optString("id"), productsListTemp);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void setList(String id, List<Product> list){
        if (listRubrique == null) {
            listRubrique = new HashMap<>();
        }
        listRubrique.put(id, list);
    }

    public List<Product> getList(String id) {
        if (listRubrique != null && listRubrique.get(id) != null) {
            return listRubrique.get(id);
        }
        return new ArrayList<>();
    }
}
