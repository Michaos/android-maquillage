package brostore.maquillage.manager;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ApiManager {

    public static final int TIMEOUT = 30000;

    public static JSONObject callAPI(String urlApi) {

        try {

            StringBuffer response = new StringBuffer();

            URL url = new URL(urlApi);

            String username = FluxManager.API_KEY;
            String authToBytes = username + ":";
            String authBytesString = Base64.encodeToString(authToBytes.getBytes(), 0);

            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(TIMEOUT);
            conn.setRequestProperty("Authorization", "Basic " + authBytesString);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return new JSONObject(response.toString());

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}