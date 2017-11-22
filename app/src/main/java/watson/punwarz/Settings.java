package watson.punwarz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookSdk;

/**
 * @author Daniel Tetzlaff
 * @version 1.0
 * Created: 2017-11-01
 *
 * Description: This class will handle user settings and provide linked access to privacy policy info
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
        Intent i = new Intent(Settings.this, ChangeName.class);
        startActivity(i);
    }

    public void goToChangeProfilePic(View v)
    {
        Intent i = new Intent(Settings.this, ChangePicture.class);
        startActivity(i);
    }

    public void deleteAccount(View v)
    {
        Toast.makeText(getApplicationContext(), "This will delete your account", Toast.LENGTH_SHORT).show();
    }

}
