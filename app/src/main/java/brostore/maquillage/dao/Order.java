package brostore.maquillage.dao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Michaos on 12/07/2015.
 */
public class Order {

    private int id;
    private String shipping_number;
    private String payment;
    private String total_discounts;
    private String total_paid;
    private ArrayList<Product> listProduct;

    public Order(JSONObject jsonObject) {
        id = jsonObject.optInt("id");
        shipping_number = jsonObject.optString("shipping_number");
        payment = jsonObject.optString("payment");
        total_discounts = jsonObject.optString("total_discounts");
        total_paid = jsonObject.optString("total_paid");
        JSONArray jsonArray = jsonObject.optJSONObject("associations").optJSONArray("order_rows");
        for (int i = 0; i < jsonArray.length(); i++) {
            Product p = new Product();
            // todo product, quantité etc >> ajouté champ quantit" comandé ?
            listProduct.add(p);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShipping_number() {
        return shipping_number;
    }

    public void setShipping_number(String shipping_number) {
        this.shipping_number = shipping_number;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getTotal_discounts() {
        return total_discounts;
    }

    public void setTotal_discounts(String total_discounts) {
        this.total_discounts = total_discounts;
    }

    public String getTotal_paid() {
        return total_paid;
    }

    public void setTotal_paid(String total_paid) {
        this.total_paid = total_paid;
    }

    public ArrayList<Product> getListProduct() {
        return listProduct;
    }

    public void setListProduct(ArrayList<Product> listProduct) {
        this.listProduct = listProduct;
    }
}
