package com.jaypee.dheerain.blogin;

/**
 * Created by Dheerain on 18-03-2017.
 */

public class blog {


    String title,discription,image,user_name,date,u_image,blog_id;

    public blog() {
    }

    public blog(String title, String discription, String image, String name, String date, String u_id,String blog_id) {
        this.title = title;
        this.discription = discription;
        this.image = image;
        this.user_name = name;
        this.date = date;
        this.u_image = u_id;
        this.blog_id=blog_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getName() {
        return user_name;
    }

    public String getDate() {
        return date;
    }

    public String getDp() {
        return u_image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDiscription() {
        return discription;
    }

    public String getImage() {
        return image;
    }
    public String getBlog_id() {
        return blog_id;
    }

}
