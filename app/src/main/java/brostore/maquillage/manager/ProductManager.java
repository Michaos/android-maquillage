package brostore.maquillage.manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import brostore.maquillage.dao.Product;
import brostore.maquillage.utils.Utils;

public class ProductManager {

    private static ProductManager instance;
    private static Context mContext;

    public static int MAX = 8;

    private HashMap<String, List<Product>> listRubrique = new HashMap<>();

    private ArrayList<Product> listProductsFavoris;
    private String favoris = "Favoris";

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

            JSONObject jsonObject = (JSONObject) ApiManager.callAPI(FluxManager.URL_CATEGORIES.replace("__ID__", categoryId + ""));

            if (jsonObject == null) {
                return false;
            }

            boolean etat = parseProductsForCategory(jsonObject, params[1]);

            return etat;
        }

        @Override
        public void onPostExecute(Boolean result) {
            if (result) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("OK PRODUCTS" + categoryId));
            } else {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("KO PRODUCTS" + categoryId));
            }
        }
    }

    public synchronized boolean parseProductsForCategory(JSONObject json, int base) {
        List<Product> productsListTemp = new ArrayList<>();
        try {
            JSONArray jsonArray = json.getJSONObject("category").getJSONObject("associations").getJSONArray("products");

            int count = base + MAX;

            if (count > jsonArray.length()) {
                count = jsonArray.length() - count;
            }

            for (int i = base; i < count; i++) {

                JSONObject jsonProduct = (JSONObject) ApiManager.callAPI(FluxManager.URL_PRODUCT.replace("__ID__", jsonArray.getJSONObject(i).optString("id", "")));
                if (jsonProduct == null) {
                    return false;
                }
                Product product = new Product(jsonProduct);

                JSONObject jsonPrice = (JSONObject) ApiManager.callAPI(FluxManager.URL_SPECIFIC_PRICE.replace("__ID_PRODUCT__", product.getId() + ""));

                if (jsonPrice != null) {
                    Double reduction = (Double.parseDouble(jsonPrice.optJSONArray("specific_prices").optJSONObject(0).optString("reduction")));
                    product.calculReducedPrice(reduction);
                } else {
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

    private void setList(String id, List<Product> list) {
        if (listRubrique == null) {
            listRubrique = new HashMap<>();
        }

        if (listRubrique.get(id) != null && listRubrique.get(id).size() != 0) {
            listRubrique.get(id).addAll(list);
        } else {
            listRubrique.put(id, list);
        }
    }

    public List<Product> getList(String id) {
        if (listRubrique != null && listRubrique.get(id) != null) {
            return listRubrique.get(id);
        }
        return new ArrayList<>();
    }

    public void getQuantityForProduct(Product p) {
        Utils.execute(new getQuantityForProductTask(), p);
    }

    public String quantity = "?";

    private class getQuantityForProductTask extends AsyncTask<Product, Object, Boolean> {

        @Override
        protected Boolean doInBackground(Product... params) {

            String quantityId = params[0].getQuantityId();
            JSONObject jsonObject = (JSONObject) ApiManager.callAPI(FluxManager.URL_STOCK.replace("__ID_QUANTITY__", quantityId));

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

    public void saveProductFavoris(final Product product) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String infoProduct = product.getName() + "!" + product.getImageId() + "!" + product.getPrice() + "!" + product.getReducedPrice() + "!" + product.getDescription() + "!" + product.getQuantityId() + "!" + product.getQuantity();
                SharedPreferences preferences = mContext.getSharedPreferences(favoris, Context.MODE_PRIVATE);
                SharedPreferences.Editor editeur = preferences.edit();
                editeur.putString(product.getId() + "", infoProduct);
                if (editeur.commit()) {
                    Bundle b = new Bundle();
                    b.putString("type", "ajout");
                    mContext.sendBroadcast(new Intent("FavorisSaveSuccess" + product.getId()));
                    listProductsFavoris.add(product);
                } else {
                    mContext.sendBroadcast(new Intent("FavorisSaveError" + product.getId()));
                }
            }
        }).start();
    }

    public void deleteProductFavoris(final Product productToDelete) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = mContext.getSharedPreferences(favoris, Context.MODE_PRIVATE);
                SharedPreferences.Editor editeur = preferences.edit();
                editeur.remove(Integer.toString(productToDelete.getId()));
                if (editeur.commit()) {
                    Bundle b = new Bundle();
                    b.putString("type", "suppression");
                    mContext.sendBroadcast(new Intent("FavorisDeleteSuccess"));
                    initListFavoris();
                } else {
                    mContext.sendBroadcast(new Intent("FavorisDeleteError"));
                }
            }
        }).start();
    }

    public void initListFavoris() {
        listProductsFavoris = new ArrayList<>();
        SharedPreferences preferences = mContext.getSharedPreferences(favoris, Context.MODE_PRIVATE);
        Map<String, ?> keys = preferences.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {

            String str = (String) entry.getValue();
            String[] results = str.split("!");

            String name = results[0];
            String imageId = results[1];
            String price = results[2];
            String reducedPrice = results[3];
            String description = results[4];
            String quantityId = results[5];
            String quantity = results[6];

            listProductsFavoris.add(new Product(entry.getKey(), name, imageId, price, reducedPrice, description, quantityId, quantity));
        }
    }

    public List<Product> getListProductsFavoris() {
        if (listProductsFavoris != null) {
            return listProductsFavoris;
        } else {
            return new ArrayList<>();
        }
    }

    public Boolean isProductFavoris(int idProduct) {
        for (int i = 0; i < listProductsFavoris.size(); i++) {
            if (idProduct == listProductsFavoris.get(i).getId()) {
                return true;
            }
        }
        return false;
    }

}