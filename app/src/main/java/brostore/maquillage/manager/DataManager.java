package brostore.maquillage.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import brostore.maquillage.dao.Product;
import brostore.maquillage.dao.User;
import brostore.maquillage.utils.Utils;

/**
 * Created by Michaos on 19/06/2015.
 */
public class DataManager {

    public static final long TIMING_SOFT_TTL = 86400000L; // 1 jours
    public static final long TIMING_TTL = 8640000000L; // 100 jours

    public static final String CUST_PARAM_BLUR = "?custParam=blur";

    private static DataManager instance;
    private static Context mContext;

    private HashMap<String, Typeface> listFont = new HashMap<String, Typeface>();

    public Boolean hasToOpenBasket = false;
    private User userAnon;

    public static DataManager getInstance(Context context) {
        if (instance == null) {
            instance = new DataManager();
        }
        if (context != null) {
            mContext = context;
        }
        return instance;
    }

    public Typeface getFont(String ttfName) {
        if (listFont.get(ttfName) != null) {
            return listFont.get(ttfName);
        } else {
            listFont.put(ttfName, Typeface.createFromAsset(mContext.getAssets(), "fonts/" + ttfName));
            return listFont.get(ttfName);
        }
    }

    public void callImgAPI(ImageView iv, String url, Product p){
        Utils.execute(new DownloadTask(iv, p), url);
    }

    private class DownloadTask extends AsyncTask<String, Integer, Boolean> {

        private Bitmap bitmap;
        private ImageView imageView;
        private Product product;

        public DownloadTask(ImageView iv, Product p){
            imageView = iv;
            product = p;
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            try{
                URL url = new URL(strings[0]);
                String username = FluxManager.API_KEY;
                String authToBytes = username + ":";
                String authBytesString = Base64.encodeToString(authToBytes.getBytes(), 0);

                HttpURLConnection conn;
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(ApiManager.TIMEOUT);
                conn.setRequestProperty("Authorization", "Basic " + authBytesString);
                conn.connect();

                InputStream iStream = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(iStream);

                return true;

            }catch(Exception e){
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                imageView.setImageBitmap(bitmap);
                product.setBitmapImage(bitmap);
            }
        }
    }

    public void addInBasket(Product p, int quantite){
        int has = getUserAnon().hasAlreadyThatProduct(p);
        if(has != -1){
            int qte = getUserAnon().getQuantites().get(has);
            qte += quantite;
            getUserAnon().getBasket().set(has, p);
            getUserAnon().getQuantites().set(has, qte);
        }else{
            getUserAnon().getBasket().add(p);
            getUserAnon().getQuantites().add(quantite);
        }
        hasToOpenBasket = true;
    }

    public User getUserAnon(){
        if(userAnon == null){
            userAnon = new User();
        }
        return userAnon;
    }
}