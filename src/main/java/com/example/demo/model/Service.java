package com.example.demo.model;

public abstract class Service {
    private String id;
    private String name;
    private String category;
    private double price;
    private int duration;
    private String description;

    public Service(){}

    public  Service(String id, String name, String category, double price, int duration, String description ){
        this.id=id;
        this.name=name;
        this.category=category;
        this.price=price;
        this.duration=duration;
        this.description=description;

    }

    public abstract String getServiceInfo();

    public abstract String getType();


    //Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        if (price >= 0) {
            this.price = price;
        }
    }

    public void setDuration(int duration) {
        if (duration > 0) {
            this.duration = duration;
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }


    //Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

}
