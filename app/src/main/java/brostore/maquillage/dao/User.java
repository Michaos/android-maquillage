package brostore.maquillage.dao;

import java.util.ArrayList;

import brostore.maquillage.manager.FluxManager;
import brostore.maquillage.utils.Utils;

/**
 * Created by Michaos on 07/07/2015.
 */
public class User {

    private int id;
    private String lastName;
    private String firstName;
    private String email;
    private String mdp;
    private String idGender;
    private String birthday;
    private String[] birthdayCutted;
    private String newsletter;

    private String userXML;

    private ArrayList<Address> listAdress;

    private ArrayList<Product> basket;
    private ArrayList<Integer> quantities;
    private Double totalBasket;
    private Double totalSaving;

    public User(){
        basket = new ArrayList<>();
        quantities = new ArrayList<>();
    }

    public User(User u){
        id = u.getId();
        lastName = u.getLastName();
        firstName = u.getFirstName();
        email = u.getEmail();
        mdp = u.getMdp();
        idGender = u.getIdGender();
        birthday = u.getBirthday();
        userXML = u.getUserXML();
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdGender() {
        return idGender;
    }

    public void setIdGender(String idGender) {
        this.idGender = idGender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
        birthdayCutted = birthday.split("-");
    }

    public String getDdnj(){
        return birthdayCutted[2];
    }

    public String getDdnm(){
        return birthdayCutted[1];
    }

    public String getDdna(){
        return birthdayCutted[0];
    }

    public String getEncryptedMdp() {
        return Utils.md5(FluxManager.COOKIE_KEY + mdp);
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getUserXML() {
        return userXML;
    }

    public void setUserXML(String userXML) {
        this.userXML = userXML;
    }

    public String getNewsletter() {
        return newsletter;
    }

    public void setNewsletter(String newsletter) {
        this.newsletter = newsletter;
    }

    // BASKET //

    public ArrayList<Product> getBasket() {
        return basket;
    }
    public void setBasket(ArrayList<Product> basket) {
        this.basket = basket;
    }

    public ArrayList<Integer> getQuantities() {
        return quantities;
    }
    public void setQuantities(ArrayList<Integer> quantities) {
        this.quantities = quantities;
    }

    public Double getTotalBasket() { return totalBasket; }
    public void setTotalBasket(Double totalBasket) { this.totalBasket = totalBasket; }

    public Double getTotalSaving() { return totalSaving; }
    public void setTotalSaving(Double totalSaving) { this.totalSaving = totalSaving; }

    public int hasAlreadyThatProduct(Product p){
        for (int i = 0; i < basket.size(); i++) {
            if (basket.get(i).getId() == p.getId()){
                return i;
            }
        }
        return -1;
    }
}