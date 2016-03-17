package com.example.watson.punwarz.ListView;

/**
 * Created by Dan on 2016-02-28.
 */
public class ListModel {

    private String lobbyTitle="";
    private String lobbyAuthor="";
    private String lobbyDes="";
    private String expireDate="";
    private String topPun="";
    private String lobbyID="";

    /** Setter Methods **/

    public void setLobbyTitle(String lobbyTitle){
        this.lobbyTitle = lobbyTitle;
    }

    public void setLobbyAuthor(String lobbyAuthor){
        this.lobbyAuthor = lobbyAuthor;
    }

    public void setLobbyDes(String lobbyDes){ this.lobbyDes = lobbyDes; }

    public void setExpireDate(String expireDate){
        this.expireDate = expireDate;
    }

    public void setTopPun(String topPun){
        this.topPun = topPun;
    }

    public void setLobbyID(String lobbyID) { this.lobbyID = lobbyID; }

    /** Getter Methods **/

    public String getLobbyTitle(){
        return this.lobbyTitle;
    }

    public String getLobbyAuthor(){
        return this.lobbyAuthor;
    }

    public String getLobbyDes(){ return this.lobbyDes; }

    public String getExpireDate(){
        return this.expireDate;
    }

    public String getTopPun(){
        return this.topPun;
    }

    public String getLobbyID() { return this.lobbyID; }

}
