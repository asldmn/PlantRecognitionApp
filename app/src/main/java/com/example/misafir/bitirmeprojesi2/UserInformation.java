package com.example.misafir.bitirmeprojesi2;

/**
 * Created by MISAFIR on 16.11.2017.
 */


public class UserInformation {

    private String name;
    private String age;
    private String country;
    private String phone;
    private String birthday;


    public UserInformation(){
    }

    public UserInformation(String name, String age, String country, String phone, String birthday) {
        this.name = name;
        this.age = age;
        this.country = country;
        this.phone = phone;
        this.birthday = birthday;
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



    public void setAge(String email) {
        this.age = email;
    }





    public String getCountry() {
        return country;
    }



    public void setCountry(String country) {
        this.country = country;
    }



    public String getPhone() {
        return phone;
    }



    public void setPhone(String phone) {
        this.phone = phone;
    }



    public String getBirthday() {
        return birthday;
    }



    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }




}

