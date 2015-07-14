package brostore.maquillage.manager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import brostore.maquillage.R;
import brostore.maquillage.dao.HideMenuItem;
import brostore.maquillage.utils.Utils;

/**
 * Created by Michaos on 18/05/2015.
 */
public class MenuManager {

    private static MenuManager instance;
    private static Context mContext;
    private static List<HideMenuItem> itemsMenuLeft;

    public static final String OK_MENU_CACHE_OR_RAW = "OK_MENU_CACHE_OR_RAW";
    public static final String OK_MENU = "OK_MENU";


    private MenuManager() {

    }

    public static MenuManager getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        if (instance == null) {
            instance = new MenuManager();
            itemsMenuLeft = new ArrayList<>();
        }
        return instance;
    }

    public void initMenuTask() {
        Utils.execute(new getItemMenuTask());
    }

    private class getItemMenuTask extends AsyncTask<Object, Object, Boolean> {

        @Override
        protected void onPreExecute() {

            initMenuCacheOrRaw();

            if (itemsMenuLeft != null && itemsMenuLeft.size() != 0) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(OK_MENU_CACHE_OR_RAW));
            }

            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Object... theme) {
            JSONObject jsonObject = (JSONObject) ApiManager.callAPI(FluxManager.URL_MENU);
            if (jsonObject == null) {
                return false;
            }
            return parseMenu(jsonObject);
        }

        @Override
        public void onPostExecute(Boolean result) {
            if (result) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(OK_MENU));
            }
        }
    }

    public synchronized boolean parseMenu(JSONObject json) {
        if (json == null) {
            return false;
        }

        CacheManager.createCache(json, FluxManager.DIR_DATA, "Menu");

        List<HideMenuItem> itemsMenuLeftTemp = new ArrayList<>();
        try {
            JSONArray jsonMenuLeft = json.getJSONObject("category").getJSONObject("associations").getJSONArray("categories");
            for (int i = 0; i < jsonMenuLeft.length(); i++) {
                JSONObject cat = (JSONObject) ApiManager.callAPI(FluxManager.URL_CATEGORIES.replace("__ID__", jsonMenuLeft.getJSONObject(i).optString("id", "")));
                if (cat != null) {
                    CacheManager.createCache(json, FluxManager.DIR_DATA, "Categorie::" + jsonMenuLeft.getJSONObject(i).optString("id", ""));
                }
                HideMenuItem hmi = new HideMenuItem(mContext, cat, 3);
                itemsMenuLeftTemp.add(hmi);
            }

            itemsMenuLeft = itemsMenuLeftTemp;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public synchronized HideMenuItem parseMenuBis(JSONObject json) {
        JSONObject cat = (JSONObject) ApiManager.callAPI(FluxManager.URL_CATEGORIES.replace("__ID__", json.optString("id", "")));
        if (cat != null) {
            CacheManager.createCache(json, FluxManager.DIR_DATA, "Categorie::" + json.optString("id", ""));
        }
        HideMenuItem hmi = new HideMenuItem(mContext, cat, 3);
        return hmi;
    }

    public synchronized boolean parseMenuFromCache(JSONObject json) {
        List<HideMenuItem> itemsMenuLeftTemp = new ArrayList<>();
        try {
            JSONArray jsonMenuLeft = json.getJSONObject("category").getJSONObject("associations").getJSONArray("categories");
            for (int i = 0; i < jsonMenuLeft.length(); i++) {
                JSONObject cat = CacheManager.loadCache(FluxManager.DIR_DATA, "Categorie::" + jsonMenuLeft.getJSONObject(i).optString("id", ""));
                HideMenuItem hmi = new HideMenuItem(mContext, cat, 2);
                itemsMenuLeftTemp.add(hmi);
            }
            itemsMenuLeft = itemsMenuLeftTemp;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public synchronized HideMenuItem parseMenuBisFromCache(JSONObject json) {
        JSONObject cat = CacheManager.loadCache(FluxManager.DIR_DATA, "Categorie::" + json.optString("id", ""));
        HideMenuItem hmi = new HideMenuItem(mContext, cat, 2);
        return hmi;
    }

    public synchronized void initMenuCacheOrRaw() {

        if (itemsMenuLeft != null && itemsMenuLeft.size() != 0) {
            return;
        }

        //todo + check if same version with url : yes = no DL.
        JSONObject jsonMenu = null; //CacheManager.loadCache(FluxManager.DIR_DATA, "Menu");


        if (jsonMenu != null) {
            parseMenuFromCache(jsonMenu);
        } else {
            InputStream is = mContext.getResources().openRawResource(R.raw.menu);
            try {
                byte[] buffer = new byte[is.available()];
                int bytesRead;
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                while ((bytesRead = is.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                byte[] bytes = output.toByteArray();
                JSONObject jsonMenuBis = new JSONObject(new String(bytes));
                parseMenuFromRaw(jsonMenuBis);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeStream(is);
            }
        }
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                Log.e("DATAMANAGER", "Message : IOException " + e.getMessage());
            }
        }
    }

    public synchronized boolean parseMenuFromRaw(JSONObject json) {

        if (json == null) {
            return false;
        }
        List<HideMenuItem> itemsMenuLeftTemp = new ArrayList<>();
        try {
            JSONArray jsonMenuLeft = json.getJSONObject("category").getJSONObject("associations").getJSONArray("categories");
            for (int i = 0; i < jsonMenuLeft.length(); i++) {

                int resId = mContext.getResources().getIdentifier("raw/cat" + jsonMenuLeft.optJSONObject(i).optString("id", ""), null, mContext.getPackageName());
                InputStream is = mContext.getResources().openRawResource(resId);

                try {
                    byte[] buffer = new byte[is.available()];
                    int bytesRead;
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    while ((bytesRead = is.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                    byte[] bytes = output.toByteArray();
                    JSONObject cat = new JSONObject(new String(bytes));
                    HideMenuItem hmi = new HideMenuItem(mContext, cat, 1);
                    itemsMenuLeftTemp.add(hmi);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    closeStream(is);
                }
            }
            itemsMenuLeft = itemsMenuLeftTemp;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public synchronized HideMenuItem parseMenuBisFromRaw(JSONObject json) {

        int resId = mContext.getResources().getIdentifier("raw/cat" + json.optString("id", ""), null, mContext.getPackageName());
        InputStream is = mContext.getResources().openRawResource(resId);

        try {
            byte[] buffer = new byte[is.available()];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = is.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            byte[] bytes = output.toByteArray();
            JSONObject cat = new JSONObject(new String(bytes));
            HideMenuItem hmi = new HideMenuItem(mContext, cat, 1);
            return hmi;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(is);
        }
        return null;
    }

    public List<HideMenuItem> getItemsMenuLeft() {
        if (itemsMenuLeft == null) {
            return new ArrayList<>();
        }
        return itemsMenuLeft;
    }
}