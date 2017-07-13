package com.jaypee.dheerain.blogin.Realm;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.jaypee.dheerain.blogin.Model.SavedPost;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Dheerain on 14-07-2017.
 */

public class RealmController {
    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Book.class
    public void clearAll() {

        realm.beginTransaction();
        realm.clear(SavedPost.class);
        realm.commitTransaction();
    }

    //find all objects in the SasvedPost.class
    public RealmResults<SavedPost> getAllPosts() {

        return realm.where(SavedPost.class).findAll();
    }

    //query a single item with the given id
    public SavedPost getSinglePost(String id) {

        return realm.where(SavedPost.class).equalTo("id", id).findFirst();
    }

    //check if SavedPost.class is empty
    public boolean hasBooks() {

        return !realm.allObjects(SavedPost.class).isEmpty();
    }


}
