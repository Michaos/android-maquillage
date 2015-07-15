package brostore.maquillage.dao;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class Product implements Parcelable {

    private int id;
    private String quantityId;
    private String quantity;
    private double price;
    private double reducedPrice;
    private String name;
    private String description;
    private String imageId;
    private Bitmap bitmapImage;

    private String linkRewrite;

    public Product() {

    }

    public Product(JSONObject jsonProduct) {
        id = jsonProduct.optJSONObject("product").optInt("id");
        price = Double.parseDouble(jsonProduct.optJSONObject("product").optString("price")) * 1.2;
        name = jsonProduct.optJSONObject("product").optString("name");
        description = jsonProduct.optJSONObject("product").optString("description");
        quantityId = jsonProduct.optJSONObject("product").optJSONObject("associations").optJSONArray("stock_availables").optJSONObject(0).optString("id");
        linkRewrite = jsonProduct.optJSONObject("product").optString("link_rewrite");
        try {
            imageId = jsonProduct.optJSONObject("product").optJSONObject("associations").optJSONArray("images").optJSONObject(0).optString("id");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public Product(String id, String name, String imageId, String price, String reducedPrice, String description, String quantityId, String quantity, String linkRewrite) {
        this.id = Integer.parseInt(id);
        this.name = name;
        this.imageId = imageId;
        this.price = Double.parseDouble(price);
        this.reducedPrice = Double.parseDouble(reducedPrice);
        this.description = description;
        this.quantityId = quantityId;
        this.quantity = quantity;
        this.linkRewrite = linkRewrite;
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
        linkRewrite = in.readString();
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

    public Double getPrice() {
        return price;
    }

    public Double getReducedPrice() {
        return reducedPrice;
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

    public void calculReducedPrice(Double reduction) {
        this.reducedPrice = price - reduction;
    }

    public boolean noReduc() {
        return getPrice() == getReducedPrice();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getLinkRewrite() {
        return linkRewrite;
    }

    public void setLinkRewrite(String linkRewrite) {
        this.linkRewrite = linkRewrite;
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
        dest.writeString(linkRewrite);
    }
}