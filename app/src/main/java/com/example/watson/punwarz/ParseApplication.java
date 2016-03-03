package com.example.watson.punwarz;

import android.app.Application;

import com.parse.*;

import java.util.Arrays;


public class ParseApplication extends Application {
    public static final String YOUR_APPLICATION_ID = "5UQqqOAeFhEDsGhrMMka0a1vKWNxpu4IlNonVn4z";
    public static final String YOUR_CLIENT_KEY = "STbqcRcr7FcJxkmjEiz8Qs2qgq8SjsPVOtqnMDgG";

    private String userObjectID = null; //the active user
    private String tempLobbyObjectID = null; //since Parse queries can't actually return???
    private String tempPostObjectID = null;
    private boolean exists = false;
    private ParseObject activeObject = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
    }

    public void runTests(){ //Runs a series of tests with pre-determined values to test Parse functionality

    }


    public void createNewUser(String facebookID, String displayName) {
        ParseObject newUser = new ParseObject("Users");
        newUser.put("UserID", facebookID);
        newUser.put("DisplayName", displayName);
        userObjectID = newUser.getObjectId(); //This may have to be after the save??
        newUser.saveInBackground();
    }

    //creates a new lobby associated with the active user
    public void createNewLobby(String lobbyPrompt, int daysActive){
        ParseObject newLobby = new ParseObject("Lobby");
        newLobby.put("Prompt", lobbyPrompt);
        newLobby.put("CreatorID", userObjectID);
        newLobby.saveInBackground();

    }

    public void createNewPost(String creatorID, String lobbyID, String postText){
        ParseObject newPost = new ParseObject("Posts");
        newPost.put("UserID", userObjectID);
        newPost.put("LobbyID", lobbyID);
        newPost.put("Post", postText);
        newPost.saveInBackground();
        String postObjectID = newPost.getObjectId(); //may have to move this before the save??
    }


    private void setUserObjectID(final String FacebookID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("UserID", FacebookID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    //The getFirst request failed.
                } else {
                    //Retrieved the object.
                    userObjectID = object.getObjectId();
                }
            }
        });

    }

    //Checks if an object from the selected tableName, in the selected tableRow, and the searchedValue already exists
    //This is the general use function.
    private void checkIfObjectExists(String tableName, String tableRow, String searchedValue){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
        query.whereEqualTo(tableRow, searchedValue);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    //Object does not exist.
                    exists = false;
                } else {
                    //Object does exist already.
                    exists = true;
                }
            }
        });
    }

    private ParseObject getLobbyObject(String lobbyID){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Lobby");
        query.whereEqualTo("ObjectID", lobbyID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    //Object does not exist.

                } else {
                    //Object does exist.
                    activeObject = object;
                }
            }
        });
        return activeObject;
    }


    //Checks if a given Facebook user ID is already in the Parse database
    public boolean doesUserExist(String facebookID) {
        clearTempVars();
        checkIfObjectExists("Users", "UserID", facebookID);
        if(exists == true){setUserObjectID(facebookID);} //if the user exists, they are automatically set to active.
        return exists;
    }

    public boolean doesLobbyExist(String description){
        clearTempVars();
        checkIfObjectExists("Lobby", "Prompt", description);
        return exists;
    }

    public void doesPostExist(String post) {
        // Posts are specific to a lobby, so a check would need the associated lobby ID
    }

    public String getLobbyObjectID(){
        return tempLobbyObjectID;
    }

    //resets the Global variables to ensure data from previous calls does not persist
    private void clearTempVars(){
        tempLobbyObjectID = null;
        tempPostObjectID = null;
        exists = false;
    }

    //Call this when logging out to clear the userObjectID
    private void deactivateUser(){
        userObjectID = null;
        activeObject = null;
        clearTempVars();
    }
}
