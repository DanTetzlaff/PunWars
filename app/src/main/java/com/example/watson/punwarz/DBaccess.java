package com.example.watson.punwarz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.*;
import com.facebook.*;
import com.parse.*;

import java.util.List;

//copy-pasta from login until I know which ones we need

public class DBaccess {

    private ParseObject user; //holds the parseobject for the user
    private String userID;
    private String userObjectID; //Object retrieval is based on this.

    public void onCreate(String userID) {
    //TODO: checks the Parse database for userID, returns the relevant ParseObject if found, otherwise creates and returns new object

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("UserID", userID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    //User not found, must be new user, creature user object.
                } else {
                    //User found, retrieve object somehow?
                }
            }
        });


    }

    public void createLobby(){}

    public void createPost(String lobbyID){}

    public void incrementPostScore(String postID){}

    public void getPost(String postID){}

    public void getLobby(String lobbyID){}




}

