package com.example.watson.punwarz.ListView;

/**
 * Created by Dan on 2016-02-28.
 */
public class ListModel {

    private String lobbyTitle="";
    private String lobbyAuthor="";
    private String lobbyTheme="";
    private String expireDate="";
    private String topPun="";

    /** Setter Methods **/

    public void setLobbyTitle(String lobbyTitle){
        this.lobbyTitle = lobbyTitle;
    }

    public void setLobbyAuthor(String lobbyAuthor){
        this.lobbyAuthor = lobbyAuthor;
    }

    public void setLobbyTheme(String lobbyTheme){
        this.lobbyTheme = lobbyTheme;
    }

    public void setExpireDate(String expireDate){
        this.expireDate = expireDate;
    }

    public void setTopPun(String topPun){
        this.topPun = topPun;
    }

    /** Getter Methods **/

    public String getLobbyTitle(){
        return this.lobbyTitle;
    }

    public String getLobbyAuthor(){
        return this.lobbyAuthor;
    }

    public String getLobbyTheme(){
        return this.lobbyTheme;
    }

    public String getExpireDate(){
        return this.expireDate;
    }

    public String getTopPun(){
        return this.topPun;
    }

}
