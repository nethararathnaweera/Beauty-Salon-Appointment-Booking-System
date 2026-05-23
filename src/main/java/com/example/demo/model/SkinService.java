package com.example.demo.model;

public class SkinService extends Service {
    private String skinType;

    public  SkinService(){}

    public SkinService(String id ,String name, String category, double price, int duration,String description, String skinType){
        super(id,name,category,price,duration,description);
        this.skinType=skinType;
    }

    @Override
    public String getServiceInfo(){
        return "Skin Service: "+getName()+" _ Rs. "+getPrice();
    }

    @Override
    public String getType(){
        return "SKIN";
    }

    public void setSkinType(String skinType) {
        this.skinType = skinType;
    }

    public String getSkinType(){
        return skinType;
    }
}
