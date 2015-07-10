package brostore.maquillage.dao;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Michaos on 31/05/2015.
 */
public class Product implements Serializable {

    int id;
    int quantity;
    String price;
    String name;
    String description;
    String imageId;


    public Product(JSONObject jsonProduct) {
        id = jsonProduct.optJSONObject("product").optInt("id");
        quantity = jsonProduct.optJSONObject("product").optInt("quantity");
        price = jsonProduct.optJSONObject("product").optString("price");
        name = jsonProduct.optJSONObject("product").optString("name");
        description = jsonProduct.optJSONObject("product").optString("description");
        //imageId = jsonProduct.optJSONObject("product").optJSONObject("associations").optJSONArray("images").optJSONObject(0).optString("id");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
