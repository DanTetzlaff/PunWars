package com.example.watson.punwarz;

/**
 * Author: Carille Mendoza
 * Date created: 2016-02-20
 * Description: Splash screen of the PunWarz app. Main activity page.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.FacebookSdk;
import com.parse.Parse;


public class SplashScreen extends Activity
{
    private static int SPLASH_TIME_OUT = 1500;
    public static final String PREFS_NAME = "Toke_Settings";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Parse.initialize(this, "5UQqqOAeFhEDsGhrMMka0a1vKWNxpu4IlNonVn4z", "STbqcRcr7FcJxkmjEiz8Qs2qgq8SjsPVOtqnMDgG");


        new Handler().postDelayed(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                Intent i = new Intent(SplashScreen.this, Login.class);
                                                Intent b = new Intent(SplashScreen.this, Lobby.class);

                                                if (checkLogin()) {
                                                    startActivity(b);
                                                }
                                                else {
                                                    startActivity(i);
                                                }
                                                finish();
                                            }
                                        }, SPLASH_TIME_OUT);
    }

    private boolean checkLogin(){
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME , MODE_PRIVATE);
        Boolean result;

        String toke = sharedPref.getString("user_toke", "empty");
        if(toke.equals("empty")){
            result = false;
        }
        else
        {
            result = true;
        }
        return result;
    }
}
