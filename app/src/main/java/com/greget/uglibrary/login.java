package com.greget.uglibrary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.greget.uglibrary.Common.Common;
import com.greget.uglibrary.Model.Users;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class login extends AppCompatActivity {

    EditText npm,pw;
    ImageButton back;
    CheckBox ckbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        npm = (EditText)findViewById(R.id.npm_id);
        pw = (EditText)findViewById(R.id.password);
        back = (ImageButton)findViewById(R.id.back_button);
        final Button login = findViewById(R.id.login);
        ckbRemember = (CheckBox)findViewById(R.id.ckbRemember);
        final ProgressBar prog_login = findViewById(R.id.progress_lgn);
        prog_login.setVisibility(View.INVISIBLE);

//        init firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("Users");


//      init paper
        Paper.init(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prog_login.setVisibility(View.VISIBLE);
                login.setVisibility(View.INVISIBLE);


                if (ckbRemember.isChecked()){
                    Paper.book().write(Common.USER_KEY,npm.getText().toString());
                    Paper.book().write(Common.PW_KEY,pw.getText().toString());
                }

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

//                        check exist user
                        if (dataSnapshot.child(npm.getText().toString()).exists()) {

//                        Get user information
                            prog_login.setVisibility(View.INVISIBLE);
                            Users users = dataSnapshot.child(npm.getText().toString()).getValue(Users.class);
                            if (users.getPass().equals(pw.getText().toString())) {
                                Intent home = new Intent(login.this,
                                        MainActivity.class);
                                startActivity(home);
                                users.setNpm(npm.getText().toString());
                                Common.currentUsers = users;
                                login.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(login.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                login.setVisibility(View.VISIBLE);
                            }
                        }

                        else{
                            Toast.makeText(login.this, "User not exists", Toast.LENGTH_SHORT).show();
                            login.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
