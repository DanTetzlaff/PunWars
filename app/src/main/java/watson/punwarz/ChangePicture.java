package watson.punwarz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.Profile;

/**
 * @author Daniel Tetzlaff
 * @version 1.0
 *
 * Created: 2017-11-02
 *
 * Description: Handles changing a user's picture
 */
public class ChangePicture extends Page
{
    private String userID;
    private Profile profile;
    private ParseApplication parse;
    int selectedImage = 0;
    int selectedColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepicture);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        profile = Profile.getCurrentProfile();
        userID = profile.getId();
        parse = new ParseApplication();

        Button select = (Button)findViewById(R.id.subPicButt);
        select.setText(R.string.submit_defaultpicture);
    }

    public void imageSelect(View v)
    {
        if (selectedImage != v.getId()) {
            if (selectedImage != 0) {
                View oldView = (View) findViewById(selectedImage);
                oldView.setBackgroundColor(selectedColor);
            }
            selectedImage = v.getId();
            Drawable background = v.getBackground();
            selectedColor = ((ColorDrawable) background).getColor();

            v.setBackgroundColor(Color.RED);
            Button select = (Button)findViewById(R.id.subPicButt);
            select.setText(R.string.submit_newpicture);
        }
        else
        {
            if (selectedImage != 0) {
                View oldView = (View) findViewById(selectedImage);
                oldView.setBackgroundColor(selectedColor);
            }
            selectedImage = 0;
            selectedColor = 0;
            Button select = (Button)findViewById(R.id.subPicButt);
            select.setText(R.string.submit_defaultpicture);
        }
    }

    public void submitNewPicture(View v)
    {
        if (selectedImage != 0)
        {
            String temp = getResources().getResourceName(selectedImage);
            temp = temp.substring(temp.length() - 1);
            parse.changeUserPic(userID, Integer.parseInt(temp));
        }
        else
        {
            parse.changeUserPic(userID, 0);
        }

        Toast.makeText(getApplicationContext(), "Picture set Successfully!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(ChangePicture.this, Settings.class);

        destroyKeyboard();
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_changepicture, menu);
        return true;
    }

    public void cancelEvent(View v)
    {
        Intent i = new Intent(ChangePicture.this, Settings.class);
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
