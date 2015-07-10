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

public class ProductManager {

    private static ProductManager instance;
    private static Context mContext;

    public static int MAX = 8;

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

    public void getProductsForCategory(Integer id, Integer base) {
        Utils.execute(new getProductsForCategoryTask(), id, base);
    }

    private class getProductsForCategoryTask extends AsyncTask<Integer, Object, Boolean> {

        private int categoryId;

        @Override
        protected Boolean doInBackground(Integer... params) {

            categoryId = params[0];

            JSONObject jsonObject = ApiManager.callAPI(FluxManager.URL_CATEGORIES.replace("__ID__", categoryId + ""));

            if (jsonObject == null) {
                return false;
            }

            boolean etat = parseProductsForCategory(jsonObject, params[1]);

            if (etat) {
                //CacheManager.createCache(jsonObject, DIR_DATA, "Menu");
            }
            return etat;
        }

        @Override
        public void onPostExecute(Boolean result) {
            if (result) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("OK PRODUCTS" + categoryId));
            } else {
                // LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("KO MENU"));
            }
        }
    }

    public synchronized boolean parseProductsForCategory(JSONObject json, int base) {
        if (json == null) {
            return false;
        }
        List<Product> productsListTemp = new ArrayList<>();
        try {
            JSONArray jsonArray = json.getJSONObject("category").getJSONObject("associations").getJSONArray("products");

            int count = base + MAX;

            if (count > jsonArray.length()){
                count = jsonArray.length();
            }

            for (int i = base; i < count ; i++) {

                JSONObject jsonProduct = ApiManager.callAPI(FluxManager.URL_PRODUCT.replace("__ID__", jsonArray.getJSONObject(i).optString("id", "")));
                Product product = new Product(jsonProduct);

                JSONObject jsonPrice = ApiManager.callAPI(FluxManager.URL_SPECIFIC_PRICE.replace("__ID_PRODUCT__", product.getId()+""));

                if(jsonPrice != null){
                    Double reduction = (Double.parseDouble(jsonPrice.optJSONArray("specific_prices").optJSONObject(0).optString("reduction")));
                    product.calculReducedPrice(reduction);
                }else{
                    product.calculReducedPrice(0.0);
                }
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

        if(listRubrique.get(id) != null && listRubrique.get(id).size() != 0){
            listRubrique.get(id).addAll(list);
        }else{
            listRubrique.put(id, list);
        }
    }

    public List<Product> getList(String id) {
        if (listRubrique != null && listRubrique.get(id) != null) {
            return listRubrique.get(id);
        }
        return new ArrayList<>();
    }

    public void getQuantityForProduct(Product p){
        Utils.execute(new getQuantityForProductTask(), p);
    }

    public String quantity = "?";

    private class getQuantityForProductTask extends AsyncTask<Product, Object, Boolean> {

        @Override
        protected Boolean doInBackground(Product... params) {

            String quantityId = params[0].getQuantityId();
            JSONObject jsonObject = ApiManager.callAPI(FluxManager.URL_STOCK.replace("__ID_QUANTITY__", quantityId));

            if (jsonObject == null) {
                return false;
            }

            quantity = jsonObject.optJSONObject("stock_available").optString("quantity");

            return true;
        }

        @Override
        public void onPostExecute(Boolean result) {
            if (result) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("OK QUANTITY"));
            }
        }
    }
}