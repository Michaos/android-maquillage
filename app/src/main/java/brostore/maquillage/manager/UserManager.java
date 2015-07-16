package brostore.maquillage.manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import brostore.maquillage.dao.Product;
import brostore.maquillage.dao.User;
import brostore.maquillage.utils.Base64;
import brostore.maquillage.utils.Base64DecoderException;
import brostore.maquillage.utils.Utils;

/**
 * Created by Michaos on 11/07/2015.
 */
public class UserManager {

    private static final int CODE_PUT_SUCCESS = 200;
    private static final int CODE_POST_SUCCESS = 201;

    private static UserManager instance;
    private static Context mContext;

    public Boolean hasToOpenBasket = false;

    public Double totalBasket = 0.0;
    public Double totalSaving = 0.0;
    private User user;
    private User myUserTemp;
    private String userBlank;

    public static UserManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserManager();
        }
        if (context != null) {
            mContext = context;
        }
        return instance;
    }

    public void resetUser() {
        user = new User(user.getBasket(), user.getQuantities());
    }

    public void addInBasket(Product p, int quantity) {
        totalBasket += p.getReducedPrice() * quantity;
        getUser().setTotalBasket(totalBasket);

        totalSaving += (p.getPrice() - p.getReducedPrice()) * quantity;
        getUser().setTotalSaving(totalSaving);

        int has = getUser().hasAlreadyThatProduct(p);
        if (has != -1) {
            int qte = getUser().getQuantities().get(has);
            qte += quantity;
            getUser().getBasket().set(has, p);
            getUser().getQuantities().set(has, qte);
        } else {
            getUser().getBasket().add(p);
            getUser().getQuantities().add(quantity);
        }
        hasToOpenBasket = true;
    }

    public void minusInBasket(Product p, int quantity) {

        int has = getUser().hasAlreadyThatProduct(p);

        totalBasket -= p.getReducedPrice() * getUser().getQuantities().get(has);
        getUser().setTotalBasket(totalBasket);

        totalSaving -= (p.getPrice() - p.getReducedPrice()) * getUser().getQuantities().get(has);
        getUser().setTotalSaving(totalSaving);

        totalBasket += p.getReducedPrice() * quantity;
        getUser().setTotalBasket(totalBasket);

        totalSaving += (p.getPrice() - p.getReducedPrice()) * quantity;
        getUser().setTotalSaving(totalSaving);

        getUser().getQuantities().set(has, quantity);

        hasToOpenBasket = true;

    }

    public void removeFromBasket(Product p) {
        int has = getUser().hasAlreadyThatProduct(p);

        totalBasket -= p.getReducedPrice() * getUser().getQuantities().get(has);
        getUser().setTotalBasket(totalBasket);

        totalSaving -= (p.getPrice() - p.getReducedPrice()) * getUser().getQuantities().get(has);
        getUser().setTotalSaving(totalSaving);

        getUser().getBasket().remove(has);
        getUser().getQuantities().remove(has);

    }

    public User getUser() {
        if (user == null) {
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

            int id;

            Object o = ApiManager.callAPI(FluxManager.URL_CONNECT.replace("__EMAIL__", user.getEmail()).replace("__ENCRYPTED_MDP__", user.getEncryptedMdp()));

            if (o instanceof JSONArray || o == null) {
                return false;
            } else {
                id = ((JSONObject) o).optJSONArray("customers").optJSONObject(0).optInt("id");
            }

            JSONObject jsonObjectUser = (JSONObject) ApiManager.callAPI(FluxManager.URL_GET_USER.replace("__ID__", id + ""));

            user.setId(jsonObjectUser.optJSONObject("customer").optInt("id"));
            user.setLastName(jsonObjectUser.optJSONObject("customer").optString("lastname"));
            user.setFirstName(jsonObjectUser.optJSONObject("customer").optString("firstname"));
            user.setIdGender(jsonObjectUser.optJSONObject("customer").optString("id_gender"));
            user.setBirthday(jsonObjectUser.optJSONObject("customer").optString("birthday"));
            user.setNewsletter(jsonObjectUser.optJSONObject("customer").optString("newsletter"));
            user.setUserXML(ApiManager.callAPIXML(FluxManager.URL_GET_USER_XML.replace("__ID__", id + "")));


            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("OK CONNECT"));
            } else {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("KO CONNECT"));
            }
        }
    }

    public void editUser(User userModif) {
        myUserTemp = userModif;
        Utils.execute(new EditUser());
    }

    private class EditUser extends AsyncTask<Object, Object, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {

            myUserTemp.setUserXML(myUserTemp.getUserXML().replace("<id_gender><![CDATA[" + user.getIdGender(), "<id_gender><![CDATA[" + myUserTemp.getIdGender())
                            .replace("<birthday><![CDATA[" + user.getBirthday(), "<birthday><![CDATA[" + myUserTemp.getBirthday())
                            .replace("<lastname><![CDATA[" + user.getLastName(), "<lastname><![CDATA[" + myUserTemp.getLastName())
                            .replace("<firstname><![CDATA[" + user.getFirstName(), "<firstname><![CDATA[" + myUserTemp.getFirstName())
                            .replace("<email><![CDATA[" + user.getEmail(), "<email><![CDATA[" + myUserTemp.getEmail())
                            .replace("<newsletter><![CDATA[" + user.getNewsletter(), "<newsletter><![CDATA[" + myUserTemp.getNewsletter())
            );

            BufferedReader bufferedReader;

            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPut httpPut = new HttpPut();
                URI uri = new URI(FluxManager.URL_PUT_USER.replace("__ID__", myUserTemp.getId() + ""));

                httpPut.setURI(uri);
                httpPut.setHeader("Authorization", "Basic UDg2M1JVQzE3UlVTM1M5VDdOWk05VVAyREJJREhWNlM6");
                httpPut.setHeader("Content-Type", "raw");

                StringEntity se = new StringEntity(myUserTemp.getUserXML());

                httpPut.setEntity(se);

                HttpResponse httpResponse = httpClient.execute(httpPut);


                InputStream inputStream = httpResponse.getEntity().getContent();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder xmlReturn = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    xmlReturn.append(line);
                }

                if (httpResponse.getStatusLine().getStatusCode() == CODE_PUT_SUCCESS) {
                    return true;
                }

            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                mContext.sendBroadcast(new Intent("EditSuccess"));
                user = myUserTemp;
            } else {
                mContext.sendBroadcast(new Intent("EditFail"));
            }
            super.onPostExecute(result);
        }
    }

    public void checkMail() {
        Utils.execute(new CheckMail(), user.getEmail());
    }

    private class CheckMail extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            Object o = ApiManager.callAPI(FluxManager.URL_CHECK_MAIL.replace("__MAIL__", params[0]));

            if (o instanceof JSONArray) {
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("MAIL DISPONIBLE"));
            } else {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("MAIL INDISPONIBLE"));
            }
        }
    }

    public void getUserBlank() {
        Utils.execute(new UserBlank());
    }

    private class UserBlank extends AsyncTask<Object, Object, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {

            userBlank = ApiManager.callAPIXML(FluxManager.URL_GET_BLANK_USER);

            if (userBlank != null && !userBlank.equals("")) {
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("OK USER BLANK"));
            } else {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("KO USER BLANK"));
            }
        }
    }

    public void createUser() {
        Utils.execute(new CreateUser());
    }

    private class CreateUser extends AsyncTask<Object, Object, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {

            String userBlankFilled = userBlank.replace("<id_gender></id_gender>", "<id_gender>" + user.getIdGender() + "</id_gender>")
                    .replace("<birthday></birthday>", "<birthday>" + user.getBirthday() + "</birthday>")
                    .replace("<lastname></lastname>", "<lastname>" + user.getLastName() + "</lastname>")
                    .replace("<firstname></firstname>", "<firstname>" + user.getFirstName() + "</firstname>")
                    .replace("<email></email>", "<email>" + user.getEmail() + "</email>")
                    .replace("<newsletter></newsletter>", "<newsletter>" + user.getNewsletter() + "</newsletter>")
                    .replace("<passwd></passwd>", "<passwd>" + user.getMdp() + "</passwd>");

            BufferedReader bufferedReader;

            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost();
                URI uri = new URI(FluxManager.URL_POST_USER);

                httpPost.setURI(uri);
                httpPost.setHeader("Authorization", "Basic UDg2M1JVQzE3UlVTM1M5VDdOWk05VVAyREJJREhWNlM6");
                httpPost.setHeader("Content-Type", "raw");

                StringEntity se = new StringEntity(userBlankFilled);

                httpPost.setEntity(se);

                HttpResponse httpResponse = httpClient.execute(httpPost);


                InputStream inputStream = httpResponse.getEntity().getContent();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder xmlReturn = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    xmlReturn.append(line);
                }

                if (httpResponse.getStatusLine().getStatusCode() == CODE_POST_SUCCESS) {
                    return true;
                }

            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                mContext.sendBroadcast(new Intent("CreateSuccess"));
            } else {
                mContext.sendBroadcast(new Intent("CreateFail"));
            }
        }
    }

    public void saveLog() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String logs = user.getEmail() + "!" + user.getMdp();

                logs = Base64.encode(logs.getBytes());

                SharedPreferences preferences = mContext.getSharedPreferences("logs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editeur = preferences.edit();
                editeur.putString("logs", logs);
                editeur.commit();
            }
        }).start();
    }

    public List<String> getLog() {
        List<String> emailMdp = new ArrayList<>();
        SharedPreferences preferences = mContext.getSharedPreferences("logs", Context.MODE_PRIVATE);
        Map<String, ?> keys = preferences.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {

            String str = (String) entry.getValue();
            try {
                str = new String(Base64.decode(str.getBytes()), "UTF-8");
                String[] results = str.split("!");
                if (results.length > 0) {
                    emailMdp.add(results[0]);
                    emailMdp.add(results[1]);
                }
            } catch (Base64DecoderException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return emailMdp;
    }

}
