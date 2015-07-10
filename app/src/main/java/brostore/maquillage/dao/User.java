package brostore.maquillage.dao;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Michaos on 07/07/2015.
 */
public class User {

    private int id;
    private String lastName;
    private String firstName;
    private String email;
    private int idGender;
    private Date birthday;
    private ArrayList<Product> basket;
    private ArrayList<Integer> quantites;

    public User(){
        //TODO
        basket = new ArrayList<>();
        quantites = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdGender() {
        return idGender;
    }

    public void setIdGender(int idGender) {
        this.idGender = idGender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public ArrayList<Product> getBasket() {
        return basket;
    }

    public void setBasket(ArrayList<Product> basket) {
        this.basket = basket;
    }

    public ArrayList<Integer> getQuantites() {
        return quantites;
    }

    public void setQuantites(ArrayList<Integer> quantites) {
        this.quantites = quantites;
    }

    public int hasAlreadyThatProduct(Product p){
        for (int i = 0; i < basket.size(); i++) {
            if (basket.get(i).getId() == p.getId()){
                return i;
            }
        }
        return -1;
    }
}
