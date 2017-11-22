package watson.punwarz;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.webkit.WebView;


import com.facebook.FacebookSdk;

/**
 * @author Daniel Tetzlaff
 * @version 1.0
 * Created: 2017-11-01
 *
 * Description: displays privacy policy info on pages based on html file containing current policy
 */

//TODO add hyperlink (possibly in html file) to the policy webpage
public class PrivacyPolicy extends Page
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacypolicy);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        WebView wv;
        wv = (WebView) findViewById(R.id.privacypolicywebview);
        wv.loadUrl("file:///android_asset/punwarspp.html");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

}
