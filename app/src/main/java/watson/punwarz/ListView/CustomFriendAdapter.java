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
import android.widget.TextView;

import java.util.ArrayList;

import watson.punwarz.Friends;
import watson.punwarz.ImageView.RoundedImageView;
import watson.punwarz.R;

/**
 * @author Daniel Tetzlaff
 * @version 1.0
 * Created: 2017-12-01
 *
 * Description:
 */

public class CustomFriendAdapter extends BaseAdapter implements View.OnClickListener
{
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    FriendModel tempValues=null;
    int i=0;

    public CustomFriendAdapter(Activity a, ArrayList d, Resources resLocal){
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

        public TextView friendName;
        public TextView friendScore;
        public RoundedImageView friendImg;

    }

    public View getView(int position, View convertView, ViewGroup parent){

        View vi = convertView;
        CustomFriendAdapter.ViewHolder holder;

        if(convertView==null){
            vi = inflater.inflate(R.layout.frienditem, null);

            holder = new CustomFriendAdapter.ViewHolder();
            holder.friendName = (TextView) vi.findViewById(R.id.friendName);
            holder.friendScore = (TextView) vi.findViewById(R.id.friendScore);
            holder.friendImg = (RoundedImageView) vi.findViewById(R.id.friendImage);

            vi.setTag( holder );
        }
        else {
            holder=(CustomFriendAdapter.ViewHolder)vi.getTag();
        }

        if (data.size()<=0)
        {
            holder.friendName.setText("Add Some Friends, See Them Here");
        }
        else
        {
            tempValues=null;
            tempValues = ( FriendModel ) data.get( position );

            holder.friendName.setText( tempValues.getFriendName() );
            holder.friendScore.setText( tempValues.getFriendScore() );
            holder.friendImg.setImageBitmap( tempValues.getFriendImg() );


            vi.setOnClickListener(new CustomFriendAdapter.OnItemClickListener( position ));
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
        Log.v("CustomFriendAdapter", "=-=-=Row button clicked=-=-=");
    }

    private class OnItemClickListener implements View.OnClickListener{
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0){

            Friends sct = (Friends) activity;

            sct.onFriendItemClick(mPosition);
        }
    }
}
