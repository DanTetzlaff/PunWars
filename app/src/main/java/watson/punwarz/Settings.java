package watson.punwarz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookSdk;

/**
 * Author: Daniel Tetzlaff
 * Created: 2017-11-01
 * Description: This class will handle user settings and privacy policy info
 */
public class Settings extends Page
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settingspage, menu);
        return true;
    }

    public void goToPrivacyPolicy(View v)
    {
        Intent i = new Intent(Settings.this, PrivacyPolicy.class);
        startActivity(i);
    }

    public void goToChangeName(View v)
    {
        Toast.makeText(getApplicationContext(), "This will take you to a page to chnage your name", Toast.LENGTH_SHORT).show();
    }

    public void goToChangeProfilePic(View v)
    {
        Toast.makeText(getApplicationContext(), "This will take you to a page to change your picture", Toast.LENGTH_SHORT).show();
    }

    public void deleteAccount(View v)
    {
        Toast.makeText(getApplicationContext(), "This will delete your account", Toast.LENGTH_SHORT).show();
    }

}
