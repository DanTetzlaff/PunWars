package com.example.watson.punwarz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.*;
import com.facebook.*;


public class Login extends AppCompatActivity {

    private CallbackManager callbackManager;
    private TextView info;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                    Profile.fetchProfileForCurrentAccessToken();

                info.setText("User id: " + loginResult.getAccessToken().getUserId() + "\n" +
                        "Auth token: " + loginResult.getAccessToken().getToken() + "\n" +
                        "Name: " + Profile.getCurrentProfile().getName());
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt cancelled.");
            }

            @Override
            public void onError(FacebookException error) {
                info.setText("Login failed.");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}