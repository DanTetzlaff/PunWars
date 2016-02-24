package com.example.watson.punwarz;

/**
 * Author: Carille Mendoza
 * Date created: 2016-02-20
 * Description: Splash screen of the PunWarz app. Main activity page.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity
{
    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                Intent i = new Intent(SplashScreen.this, Login.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        }, SPLASH_TIME_OUT);
    }
}
