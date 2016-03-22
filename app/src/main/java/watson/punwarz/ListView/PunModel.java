package watson.punwarz.ListView;

/**
 * Created by Dan on 2016-03-01.
 */
public class PunModel {

    private String punAuth="";
    private String pun="";
    private String punVotes;

    /** Setter Methods **/

    public void setPunAuth(String punAuth) { this.punAuth = punAuth; }

    public void setPun(String pun) { this.pun = pun; }

    public void setPunVotes(String punVotes) { this.punVotes = punVotes; }

    /** Getter Methods **/

    public String getPunAuth() { return this.punAuth; }

    public String getPun() { return this.pun; }

    public String getPunVotes() { return this.punVotes; }
}
