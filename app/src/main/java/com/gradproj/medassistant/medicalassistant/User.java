package com.gradproj.medassistant.medicalassistant;

import java.util.ArrayList;

public class User {
    String name;


    String userName;
    String age;
    String password = "smart123";
    String sex;
    String code;
    String refDr = "Dr. Mahmoud Abbas";

    public User(String name, String age, String sex,String code,String userName) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.userName = userName;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRefDr() {
        return refDr;
    }

    public void setRefDr(String refDr) {
        this.refDr = refDr;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static ArrayList<User> getUsersList()
    {
        ArrayList<User> myUsers = new ArrayList<>(4);
        myUsers.add(new User("Eslam Ahmed","23","male","9477","eslam.ahmed"));
        myUsers.add(new User("Dina Al Sayed","23","female","9594","dina.elsayed"));
        myUsers.add(new User("Ahmed Farid","23","male","151060054","ahmed.farid"));
        myUsers.add(new User("Mahmoud Nasser","23","male","6481","mahmoud.abd"));

        return myUsers;
    }
}
