package watson.punwarz.ListView;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import watson.punwarz.ImageView.RoundedImageView;
import watson.punwarz.FriendRequests;
import watson.punwarz.R;

//TODO COMPLETE DOC

/**
 * @author Daniel Tetzlaff
 * @version 1.0
 * Created: 2017-12-05
 *
 * Description:
 */
public class CustomRequestAdapter extends BaseAdapter implements View.OnClickListener
{
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    RequestModel tempValues=null;
    int i=0;

    public CustomRequestAdapter(Activity a, ArrayList d, Resources resLocal){
        activity = a;
        data = d;
        res = resLocal;

        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount(){
        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position){
        return position;
    }

    public long getItemId(int position){
        return position;
    }

    public static class ViewHolder {
        public TextView requestName;
        public RoundedImageView requestImg;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){
            vi = inflater.inflate(R.layout.requestitem, null);

            holder = new ViewHolder();
            holder.requestName = (TextView) vi.findViewById(R.id.requestName);
            holder.requestImg = (RoundedImageView) vi.findViewById(R.id.requestImage);

            vi.setTag( holder );
        }
        else {
            holder=(ViewHolder)vi.getTag();
        }

        if (data.size()<=0)
        {
            holder.requestName.setText("No Requests");
        }
        else
        {
            tempValues=null;
            tempValues = ( RequestModel ) data.get( position );

            holder.requestName.setText( tempValues.getRequestName() );
            holder.requestImg.setImageBitmap( tempValues.getRequestImg() );


            vi.setOnClickListener(new OnItemClickListener( position ));
        }

        if (position % 2 == 1) {
            vi.setBackgroundColor(Color.parseColor("#d5d3d3"));
        } else {
            vi.setBackgroundColor(Color.parseColor("#ffa147"));
        }

        return vi;
    }

    @Override
    public void onClick(View v){
        Log.v("CustomRequestAdapter", "=-=-=Row button clicked=-=-=");
    }

    private class OnItemClickListener implements View.OnClickListener{
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0){

            FriendRequests sct = (FriendRequests) activity;

            sct.onRequestItemClick(mPosition);
        }
    }
}
