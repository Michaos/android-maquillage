package brostore.maquillage.dao;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Michaos on 11/07/2015.
 */
public class Address implements Serializable {

    private int id;
    private String alias;
    private String lastName;
    private String firstName;
    private String company;
    private String vatNumber;
    private String address1;
    private String address2;
    private String postcode;
    private String city;
    private String idCountry;
    private String country;
    private String other;
    private String phone;
    private String phoneMobile;

    public Address(){
    }

    public Address(JSONObject jsonObject) {
        id = jsonObject.optJSONObject("address").optInt("id");
        alias = jsonObject.optJSONObject("address").optString("alias");
        lastName = jsonObject.optJSONObject("address").optString("lastName");
        firstName = jsonObject.optJSONObject("address").optString("firstName");
        company = jsonObject.optJSONObject("address").optString("company");
        vatNumber = jsonObject.optJSONObject("address").optString("vatNumber");
        address1 = jsonObject.optJSONObject("address").optString("address1");
        address2 = jsonObject.optJSONObject("address").optString("address2");
        postcode = jsonObject.optJSONObject("address").optString("postcode");
        city = jsonObject.optJSONObject("address").optString("city");
        idCountry = jsonObject.optJSONObject("address").optString("id_country");
        other = jsonObject.optJSONObject("address").optString("other");
        phone = jsonObject.optJSONObject("address").optString("phone");
        phoneMobile = jsonObject.optJSONObject("address").optString("phoneMobile");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneMobile() {
        return phoneMobile;
    }

    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    public String getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(String idCountry) {
        this.idCountry = idCountry;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", alias='" + alias + '\'' +
                ", company='" + company + '\'' +
                ", vatNumber='" + vatNumber + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", postcode='" + postcode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", other='" + other + '\'' +
                ", phone='" + phone + '\'' +
                ", phoneMobile='" + phoneMobile + '\'' +
                '}';
    }
}
