package com.jaypee.dheerain.blogin.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Dheerain on 14-07-2017.
 */

public class SavedPost extends RealmObject {

    @PrimaryKey
    private String blogImage;

   private String title,date,discription,userDp,userName;

    public SavedPost() {
    }

    public SavedPost(String title, String date, String discription, String userDp, String userName, String blogImage) {

        this.title = title;
        this.date = date;
        this.discription = discription;
        this.userDp = userDp;
        this.userName = userName;
        this.blogImage = blogImage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public void setUserDp(String userDp) {
        this.userDp = userDp;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBlogImage(String blogImage) {
        this.blogImage = blogImage;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDiscription() {
        return discription;
    }

    public String getUserDp() {
        return userDp;
    }

    public String getUserName() {
        return userName;
    }

    public String getBlogImage() {
        return blogImage;
    }


}
