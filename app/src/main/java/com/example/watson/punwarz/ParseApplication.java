package com.example.watson.punwarz;

import android.app.Application;
import com.parse.*;

public class ParseApplication extends Application {
    public static final String YOUR_APPLICATION_ID = "5UQqqOAeFhEDsGhrMMka0a1vKWNxpu4IlNonVn4z";
    public static final String YOUR_CLIENT_KEY = "STbqcRcr7FcJxkmjEiz8Qs2qgq8SjsPVOtqnMDgG";

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
    }
}
