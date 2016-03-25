package watson.punwarz.ListView;

/**
 * Created by Dan on 2016-03-01.
 */
public class PunModel {

    private String punAuth="";
    private String punAuthID="";
    private String pun="";
    private String punID="";
    private String punVotes;
    private String themeID="";

    /** Setter Methods **/

    public void setPunAuth(String punAuth) { this.punAuth = punAuth; }

    public void setPun(String pun) { this.pun = pun; }

    public void setPunVotes(String punVotes) { this.punVotes = punVotes; }

    public void setPunID(String punID) { this.punID = punID; }

    public void setPunAuthID(String punAuthID) { this.punAuthID = punAuthID; }

    public void setThemeID(String themeID) { this.themeID = themeID; }

    /** Getter Methods **/

    public String getPunAuth() { return this.punAuth; }

    public String getPun() { return this.pun; }

    public String getPunVotes() { return this.punVotes; }

    public String getPunID() { return this.punID; }

    public String getPunAuthID() { return this.punAuthID; }

    public String getThemeID() { return this.themeID; }
}
