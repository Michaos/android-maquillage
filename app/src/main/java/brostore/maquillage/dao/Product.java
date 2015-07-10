package brostore.maquillage.dao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.io.Serializable;

import brostore.maquillage.R;

public class Product implements Parcelable {

    int id;
    String quantityId;
    String quantity;
    double price;
    double reducedPrice;
    String name;
    String description;
    String imageId;
    Bitmap bitmapImage;


    public Product(JSONObject jsonProduct) {
        id = jsonProduct.optJSONObject("product").optInt("id");
        price = Double.parseDouble(jsonProduct.optJSONObject("product").optString("price")) * 1.2;
        name = jsonProduct.optJSONObject("product").optString("name");
        description = jsonProduct.optJSONObject("product").optString("description");
        try {
            imageId = jsonProduct.optJSONObject("product").optJSONObject("associations").optJSONArray("images").optJSONObject(0).optString("id");
            quantityId = jsonProduct.optJSONObject("product").optJSONObject("associations").optJSONArray("stock_availables").optJSONObject(0).optString("id");
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    protected Product(Parcel in) {
        id = in.readInt();
        quantityId = in.readString();
        quantity = in.readString();
        price = in.readDouble();
        reducedPrice = in.readDouble();
        name = in.readString();
        description = in.readString();
        imageId = in.readString();
        bitmapImage = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuantityId() {
        return quantityId;
    }

    public void setQuantityId(String quantityId) {
        this.quantityId = quantityId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return String.format("%.2f", price);
    }

    public String getPriceReduced() {
        return String.format("%.2f", reducedPrice);
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

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    public void setBitmapImage(Bitmap bitmap) {
        this.bitmapImage = bitmap;
    }

    public void calculReducedPrice(String reduction) {
        this.reducedPrice = price - Double.parseDouble(reduction);
    }

    public boolean noReduc() {
        return getPrice().equals(getPriceReduced());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(quantityId);
        dest.writeString(quantity);
        dest.writeDouble(price);
        dest.writeDouble(reducedPrice);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(imageId);
        dest.writeParcelable(bitmapImage, flags);
    }
}