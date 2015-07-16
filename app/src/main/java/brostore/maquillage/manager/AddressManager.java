package brostore.maquillage.manager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
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

import brostore.maquillage.dao.Address;
import brostore.maquillage.utils.Utils;

/**
 * Created by Michaos on 12/07/2015.
 */
public class AddressManager {

    private static final int CODE_POST_SUCCESS = 201;

    private static AddressManager instance;
    private static Context mContext;

    private ArrayList<Address> listAddresses;

    private String addressBlank;

    public static AddressManager getInstance(Context context) {
        if (instance == null) {
            instance = new AddressManager();
        }
        if (context != null) {
            mContext = context;
        }
        return instance;
    }

    public void getUserAddresses() {
        Utils.execute(new GetUserAddresses());
    }

    private class GetUserAddresses extends AsyncTask<Object, Object, Integer> {

        @Override
        protected Integer doInBackground(Object... params) {

            listAddresses = new ArrayList<>();

            Object o = ApiManager.callAPI(FluxManager.URL_GET_USER_ADDRESSES.replace("__ID__", UserManager.getInstance(mContext).getUser().getId() + ""));

            if (o == null) {
                return 2;
            } else if (o instanceof JSONArray && ((JSONArray) o).length() == 0) {
                return 0;
            }

            JSONArray jsonListAddresse = ((JSONObject) o).optJSONArray("addresses");

            for (int i = 0; i < jsonListAddresse.length(); i++) {
                JSONObject jsonObject = (JSONObject) ApiManager.callAPI(FluxManager.URL_GET_ADDRESS.replace("__ID__", jsonListAddresse.optJSONObject(i).optInt("id") + ""));
                if (jsonObject != null) {

                    Address adress = new Address(jsonObject);

                    String idCountry = jsonObject.optJSONObject("address").optString("id_country");

                    JSONObject countryInfos = (JSONObject) ApiManager.callAPI(FluxManager.URL_GET_COUNTRY.replace("__ID__", idCountry));

                    if (countryInfos != null && countryInfos.optJSONObject("country") != null) {
                        adress.setCountry(countryInfos.optJSONObject("country").optString("name"));
                    }

                    listAddresses.add(adress);

                } else {
                    return 2;
                }
            }
            return 1;
        }

        @Override
        public void onPostExecute(Integer result) {
            if (result == 1) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("OK ADDRESSES"));
            } else if (result == 2) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("KO ADDRESSES"));
            } else if (result == 0) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("NO ADDRESSES"));
            }
        }
    }

    public ArrayList<Address> getListAddresses() {
        return listAddresses;
    }

    public void createAddress(Address newAddress) {
        Utils.execute(new CreateAddress(), newAddress);
    }

    private class CreateAddress extends AsyncTask<Address, Object, Boolean> {

        @Override
        protected Boolean doInBackground(Address... params) {

            addressBlank = ApiManager.callAPIXML(FluxManager.URL_GET_BLANK_ADDRESS);

            if (addressBlank == null || addressBlank.equals("")) {
                return false;
            }

            String addressBlankFilled = addressBlank.replace("<id_customer></id_customer>", "<id_customer>" + UserManager.getInstance(mContext).getUser().getId() + "</id_customer>")
                    .replace("<id_country></id_country>", "<id_country>" + params[0].getIdCountry() + "</id_country>")
                    .replace("<alias></alias>", "<alias>" + params[0].getAlias() + "</alias>")
                    .replace("<company></company>", "<company>" + params[0].getCompany() + "</company>")
                    .replace("<lastname></lastname>", "<lastname>" + params[0].getLastName() + "</lastname>")
                    .replace("<firstname></firstname>", "<firstname>" + params[0].getFirstName() + "</firstname>")
                    .replace("<vat_number></vat_number>", "<vat_number>" + params[0].getVatNumber() + "</vat_number>")
                    .replace("<address1></address1>", "<address1>" + params[0].getAddress1() + "</address1>")
                    .replace("<address2></address2>", "<address2>" + params[0].getAddress2() + "</address2>")
                    .replace("<postcode></postcode>", "<postcode>" + params[0].getPostcode() + "</postcode>")
                    .replace("<city></city>", "<city>" + params[0].getCity() + "</city>")
                    .replace("<other></other>", "<other>" + params[0].getOther() + "</other>")
                    .replace("<phone></phone>", "<phone>" + params[0].getPhone() + "</phone>")
                    .replace("<phone_mobile></phone_mobile>", "<phone_mobile>" + params[0].getPhoneMobile() + "</phone_mobile>");

            BufferedReader bufferedReader;

            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost();
                URI uri = new URI(FluxManager.URL_POST_ADDRESS);

                httpPost.setURI(uri);
                httpPost.setHeader("Authorization", "Basic UDg2M1JVQzE3UlVTM1M5VDdOWk05VVAyREJJREhWNlM6");
                httpPost.setHeader("Content-Type", "raw");

                StringEntity se = new StringEntity(addressBlankFilled);

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
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("CREATE ADDRESS SUCCESS"));
            } else {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("CREATE ADDRESS FAIL"));
            }
        }
    }

}
