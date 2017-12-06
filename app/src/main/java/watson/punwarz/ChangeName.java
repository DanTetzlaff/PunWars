package watson.punwarz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;

/**
 * Author: Daniel
 * Created: 2017-11-01
 * Desc: Handles changing a user's name on the db
 */
public class ChangeName extends Page
{
    private String curName;
    private String userID;
    private com.facebook.Profile profile;
    private ParseApplication parse;

    private EditText editText;
    private TextView countText;
    private final TextWatcher editTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //set TextView to current count
            countText.setText(String.valueOf(s.length()) + "/" + getResources().getString(R.string.U_LIMIT));
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changename);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        profile = com.facebook.Profile.getCurrentProfile();
        userID = profile.getId();
        parse = new ParseApplication();
        curName = parse.getUserName(userID);

        TextView titleView = (TextView)findViewById(R.id.CurUserName);
        editText = (EditText)findViewById(R.id.editText);
        countText = (TextView)findViewById(R.id.countText);

        countText.setText("0/" + getResources().getString(R.string.U_LIMIT));

        editText.addTextChangedListener(editTextWatcher);
        titleView.setText(curName);


    }

    public void submitNewName(View v)
    {
        EditText nameText = (EditText)findViewById(R.id.editText);
        String newName = nameText.getText().toString();
        parse.changeUserName(userID, newName);

        Toast.makeText(getApplicationContext(), "Name Changed Successfully!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(ChangeName.this, Settings.class);
        
        destroyKeyboard();
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_changename, menu);
        return true;
    }

    public void cancelEvent(View v)
    {
        Intent i = new Intent(ChangeName.this, Settings.class);
        destroyKeyboard();
        startActivity(i);
    }

    public void destroyKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
