package brostore.maquillage.manager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import brostore.maquillage.dao.HideMenuItem;
import brostore.maquillage.utils.Utils;

/**
 * Created by Michaos on 18/05/2015.
 */
public class MenuManager {

    private static MenuManager instance;
    private static Context mContext;
    private static List<HideMenuItem> itemsMenuLeft;

    private MenuManager() {

    }

    public static MenuManager getInstance(Context context){
        if(context != null){
            mContext = context;
        }
        if (instance == null){
            instance = new MenuManager();
            itemsMenuLeft = new ArrayList<>();
        }
        return instance;
    }

    public void initMenuTask(){
        Utils.execute(new getItemMenuTask());
    }

    private class getItemMenuTask extends AsyncTask<Object, Object, Boolean> {

        /*@Override
        protected void onPreExecute() {
            System.out.println("AAAA preexecute");
            initMenu();
            if(itemsMenuLeft != null && itemsMenuLeft.size() != 0){
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("OK MENU"));
                System.out.println("AAAA ok ok ok FOM CACHE");
            }
            System.out.println("AAAA ok preexecute");
            super.onPreExecute();
        }*/

        @Override
        protected Boolean doInBackground(Object... theme) {

            JSONObject jsonObject = ApiManager.callAPI(FluxManager.URL_MENU);

            if( jsonObject == null ) {
                return false;
            }
            boolean etat = parseMenu(jsonObject);
            if(etat){
                CacheManager.createCache(jsonObject, FluxManager.DIR_DATA, "Menu");
            }
            return etat;
        }

        @Override
        public void onPostExecute(Boolean result) {
            if(result){
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("OK MENU"));
                System.out.println("AAAA ok ok ok");
            }else{
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("KO MENU"));
            }
        }
    }

    public synchronized boolean parseMenu(JSONObject json){
        if( json == null ) {
            return false;
        }

        System.out.println("AAAA in parsemenu");

        List<HideMenuItem> itemsMenuLeftTemp = new ArrayList<>();
        try{
            JSONArray jsonMenuLeft = json.getJSONObject("category").getJSONObject("associations").getJSONArray("categories");
            for (int i = 0; i < jsonMenuLeft.length(); i++) {
                JSONObject cat = ApiManager.callAPI(FluxManager.URL_CATEGORIES.replace("__ID__", jsonMenuLeft.getJSONObject(i).optString("id", "")));
                HideMenuItem hmi = new HideMenuItem(mContext, cat);
                itemsMenuLeftTemp.add(hmi);
            }

            itemsMenuLeft = itemsMenuLeftTemp;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public synchronized HideMenuItem parseMenuBis(JSONObject json){
        JSONObject cat = ApiManager.callAPI(FluxManager.URL_CATEGORIES.replace("__ID__", json.optString("id", "")));
        HideMenuItem hmi = new HideMenuItem(mContext, cat);
        return hmi;
    }

    public List<HideMenuItem> getItemsMenuLeft(){
        if(itemsMenuLeft == null) {
            return new ArrayList<>();
        }
        return itemsMenuLeft;
    }

    public void initMenu(){
        System.out.println("AAAA initMenu preexecute");
        if(itemsMenuLeft != null && itemsMenuLeft.size() != 0){
            return;
        }
        JSONObject jsonMenu = CacheManager.loadCache( FluxManager.DIR_DATA, "Menu" );
        if( jsonMenu != null ){
            System.out.println("AAAA initMenu preexecute go for parsemenu");
            parseMenu(jsonMenu);
        }else{
            //TODO menu en dur dans l'appli
        }
    }
}
