package com.jaypee.dheerain.blogin.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jaypee.dheerain.blogin.Adapter.BookmarkAdapter;
import com.jaypee.dheerain.blogin.Model.SavedPost;
import com.jaypee.dheerain.blogin.R;
import com.jaypee.dheerain.blogin.Realm.RealmController;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class BookmarkActivity extends AppCompatActivity {

    RecyclerView bookMarkView;
    RealmResults<SavedPost> savedPost;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        setRealmConfigration();
        savedPost=null;
        this.realm = RealmController.with(getApplication()).getRealm();

        bookMarkView= (RecyclerView) findViewById(R.id.bookmarkRecyclerView);
        bookMarkView.setLayoutManager(new LinearLayoutManager(this));
        savedPost=RealmController.with(this).getAllPosts();

        BookmarkAdapter bookmarkAdapter=new BookmarkAdapter(savedPost,this);

        bookMarkView.setAdapter(bookmarkAdapter);
    }
    void setRealmConfigration()
    {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
