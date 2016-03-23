package watson.punwarz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Author: Carille Mendoza
 * Created: March 2, 2016
 * Description: This class will handle adding new titles
 * Modified: March 16, 2016
 * Desc: Can now add themes to the lobby page
 */
public class AddTitle extends Page
{
    private int lobbyID;

    private EditText titleEdit;
    private EditText descEdit;
    private TextView titleCount;
    private TextView descCount;
    private final TextWatcher titleTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //set TextView to current count
            titleCount.setText(String.valueOf(s.length()) + "/" + getResources().getString(R.string.TITLE_LIMIT));
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    private final TextWatcher descTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //set TextView to current count
            descCount.setText(String.valueOf(s.length()) + "/" + getResources().getString(R.string.DESC_LIMIT));
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtitle);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Spinner staticSpanner = (Spinner)findViewById(R.id.spin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        staticSpanner.setAdapter(adapter);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        titleEdit = (EditText)findViewById(R.id.promptField);
        descEdit = (EditText)findViewById(R.id.promptDescField);
        titleCount = (TextView)findViewById(R.id.titleCount);
        descCount = (TextView)findViewById(R.id.descCount);

        titleEdit.addTextChangedListener(titleTextWatcher);
        descEdit.addTextChangedListener(descTextWatcher);

        titleCount.setText("0/" + getResources().getString(R.string.TITLE_LIMIT));
        descCount.setText("0/" + getResources().getString(R.string.DESC_LIMIT));
        lobbyID = this.getIntent().getIntExtra("LOBBY_ID", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.lobby_settings)
        {
            goToLobby(item);
            return true;
        }
        else if(id == R.id.logout_settings)
        {
            logOut(item);
            return true;
        }
        else if(id == R.id.profile_settings)
        {
            goToProfile(item);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToProfile(MenuItem item)
    {
        Intent i = new Intent(AddTitle.this, Profile.class);
        startActivity(i);
    }

    //collects data for prompt to be created
    public void submitPrompt(View v)
    {
        final EditText promptField = (EditText) findViewById(R.id.promptField);
        String prompt = promptField.getText().toString().toLowerCase();
        final EditText promptDescField = (EditText) findViewById(R.id.promptDescField);
        String promptDesc = promptDescField.getText().toString();
        final Spinner dateSpinner = (Spinner) findViewById(R.id.spin);

        int length = Integer.valueOf((String) dateSpinner.getSelectedItem());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, length);
        Date expDate = cal.getTime();
        ParseApplication parse = new ParseApplication();
        com.facebook.Profile prof = com.facebook.Profile.getCurrentProfile();

        if(!(parse.doesThemeExist(prompt)))
        {
            parse.createNewLobby(prof.getId(), prompt, promptDesc, expDate);

            Toast.makeText(getApplicationContext(), "Theme added Successfully!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(AddTitle.this, Lobby.class);
            i.putExtra("LOBBY_ID", lobbyID);

            destroyKeyboard();
            startActivity(i);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Theme already exists!", Toast.LENGTH_SHORT).show();
            destroyKeyboard();
        }
    }

    //returns to lobby page
    public void cancelEvent(View v)
    {
        Intent i = new Intent(AddTitle.this, Lobby.class);
        startActivity(i);
    }

    public void destroyKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
