package watson.punwarz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;


/**
 * Author: Carille
 * Created: 2016-03-06
 * Desc: Test inheritance
 */
public class Page extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("CLOSE_ALL");
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // close activity
                Intent i = new Intent(Page.this, Login.class);
                startActivity(i);
            }
        };
        registerReceiver(broadcastReceiver, intentFilter); //Why is this here? causing leaked intentreceiver
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        new Instabug.Builder(getApplication(), getResources().getString(R.string.instabug_app_id))
                .setInvocationEvent(InstabugInvocationEvent.SHAKE)
                .build();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.logout_settings)
        {
            logOut(item);
            return true;
        }
        else if (id == R.id.setting_settings)
        {
            goToSettings(item);
            return true;
        }
        else if(id == R.id.lobby_settings)
        {
            goToLobby(item);
            return true;
        }
        else if(id == R.id.profile_settings)
        {
            goToProfile(item);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToLobby(MenuItem item)
    {
        Intent i = new Intent(Page.this, Lobby.class);
        startActivity(i);
    }

    public void goToSettings(MenuItem item)
    {
        Intent i = new Intent(Page.this, Settings.class);
        startActivity(i);
    }

    public void goToProfile(MenuItem item)
    {
        Intent i = new Intent(Page.this, Profile.class);
        startActivity(i);
    }

    public void goToLeaderboard(MenuItem item)
    {
        Intent i = new Intent(Page.this, Leaderboard.class);
        startActivity(i);
    }

    public void sendFriendRequest(MenuItem item)
    {
        Toast.makeText(getApplicationContext(), "sent friend request", Toast.LENGTH_SHORT).show();
    }

    public void logOut(MenuItem item)
    {
        SharedPreferences sharedPref = getSharedPreferences("Toke_Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user_toke", "empty");
        editor.commit();

        sharedPref = getSharedPreferences("Prof_ID", MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putString("user_id", "empty");
        editor.commit();

        LoginManager.getInstance().logOut();

        Intent intent = new Intent("CLOSE_ALL");
        this.sendBroadcast(intent);

        Intent i = new Intent(this, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(i);
    }
}