package com.example.watson.punwarz;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.facebook.FacebookSdk;

/**
 * Author: Carille
 * Created: 2016-03-08
 * Desc: Using inheritance, takes after page and will display user, the user's points, rank, themes created, and puns submitted.
 */
public class Profile extends Page
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }
}
