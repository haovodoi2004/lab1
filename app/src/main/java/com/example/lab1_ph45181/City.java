package com.example.lab1_ph45181;

public class City {
    public String id;
    public String img;
    public String city;
    public String country;
    public int people;

    public City(String id, String img, String city, String country, int people) {
        this.id = id;
        this.img = img;
        this.city = city;
        this.country = country;
        this.people = people;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public City() {
    }
}
