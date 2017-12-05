package watson.punwarz.ListView;

import android.graphics.Bitmap;

/**
 * @author Daniel Tetzlaff
 * @version 1.0
 * Created: 2017-12-05
 */

public class RequestModel
{
    private Bitmap requestImg=Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
    private String requestName="";
    private String requestID="";

    /** Setter Methods **/

    public void setRequestImg(Bitmap requestImg) { this.requestImg = requestImg; }

    public void setRequestName(String requestName) { this.requestName = requestName; }

    public void setRequestID(String requestID) { this.requestID = requestID; }

    /** Getter Methods **/

    public Bitmap getRequestImg() { return this.requestImg; }

    public String getRequestName() { return this.requestName; }

    public String getRequestID() { return this.requestID; }
}
