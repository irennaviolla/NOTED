package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.myapplication.Home.notesList;
import static com.example.myapplication.MyAdapter.mode;
import static com.example.myapplication.MyAdapter.notePos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class Note extends AppCompatActivity {

    String currentTitle;
    Data currentNote;
    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        EditText titleInput = findViewById(R.id.titleinput);
        EditText descriptionInput = findViewById(R.id.descriptioninput);
        ImageView save = findViewById(R.id.save);
        ImageView back = findViewById(R.id.back);

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);

        Log.d("test", "TES PISANNNN");
        if(mode == 1){
            currentNote = notesList.get(notePos);
            currentTitle = notesList.get(notePos).getTitle();
            time = notesList.get(notePos).getCreatedTime();
            titleInput.setText(notesList.get(notePos).getTitle());
            descriptionInput.setText(notesList.get(notePos).getDescription());
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();
                long createdTime = System.currentTimeMillis();


                if(mode == 0){
                    realm.beginTransaction();
                    Data data = realm.createObject(Data.class);
                    data.setTitle(title);
                    data.setDescription(description);
                    data.setCreatedTime(createdTime);
                    realm.commitTransaction();
                    Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
                }else{
                    mode = 0;
                    Log.d("testText", title);
                    Log.d("testText", description);
                    for(Data data : notesList){
                        Log.d("test", "masuk, title: " + data.getTitle() + "currentTitle : " + currentTitle);
                        if(data.getTitle().equals(currentTitle)){
                            realm.beginTransaction();
                            currentNote.deleteFromRealm();
                            realm.commitTransaction();

                            realm.beginTransaction();
                            Data newData = realm.createObject(Data.class);
                            newData.setTitle(title);
                            newData.setDescription(description);
                            newData.setCreatedTime(time);
                            realm.commitTransaction();
                        }
                    }
                }
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = 0;
                finish();
            }
        });
    }

}