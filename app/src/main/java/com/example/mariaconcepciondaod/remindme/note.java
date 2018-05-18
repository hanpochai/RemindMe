package com.example.mariaconcepciondaod.remindme;

public class note {
    private int id;
    private String title;
    private String desc;
    private byte[] image;
    private String type;

    public note(int id, String title, String desc, byte[] image,String type){
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.type=type;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
