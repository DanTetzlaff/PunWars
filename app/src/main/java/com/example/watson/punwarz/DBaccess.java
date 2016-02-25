package com.example.watson.punwarz;

import android.app.Activity;
import android.os.Bundle;
import com.parse.*;

//copy-pasta from login until I know which ones we need

public class DBaccess extends Activity{

    private ParseObject user; //holds the parseobject for the user
    private String userID;
    private String userObjectID; //Object retrieval is based on this.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "5UQqqOAeFhEDsGhrMMka0a1vKWNxpu4IlNonVn4z", "STbqcRcr7FcJxkmjEiz8Qs2qgq8SjsPVOtqnMDgG");
    }

    public void add(String name){
        
    }




}

