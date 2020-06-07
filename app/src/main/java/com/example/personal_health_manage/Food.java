package com.example.personal_health_manage;

public class Food {
    private String name;
    private int imageId;
    private String inclusion;
    private String inclusion_content;
    private String unit;
    public Food(String name,int imageId,String inclusion,String inclusion_content,String unit){
        this.name = name;
        this.imageId = imageId;
        this.inclusion = inclusion;
        this.inclusion_content = inclusion_content;
        this.unit = unit;
    }
    public String getName(){ return name;}
    public int getImageId(){return imageId;}
    public String getInclusion(){return inclusion;}
    public String getInclusion_content(){return inclusion_content;}
    public String getUnit(){return unit;}

}
