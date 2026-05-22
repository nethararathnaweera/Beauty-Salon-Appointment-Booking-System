package com.beautysalon.beautysalonbooking.model;

public class HairService extends Service{
    private String hairType;

    public HairService(){}


    public HairService(String id, String name, String category,double price, int duration, String description, String hairType){
        super(id, name, category, price, duration, description);
        this.hairType=hairType;
    }

    @Override
    public String getServiceInfo(){
        return "Heir Service: "+getName()+" - "+getDuration()+"mintes";
    }

    @Override
    public String getType(){
        return "HAIR";
    }

    public void setHairType(String hairType) {
        this.hairType = hairType;
    }

    public String getHairType(){
        return hairType;
    }
}

