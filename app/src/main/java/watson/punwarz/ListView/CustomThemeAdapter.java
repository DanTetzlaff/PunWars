package watson.punwarz.ListView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.app.*;
import android.widget.TextView;
import watson.punwarz.Profile;
import watson.punwarz.R;

import java.util.*;

/**
 * Created by Dan on 2016-03-25.
 */
public class CustomThemeAdapter extends BaseAdapter implements View.OnClickListener{

    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    ListModel tempValues=null;
    int i=0;

    public CustomThemeAdapter(Activity a, ArrayList d, Resources resLocal){
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

        public TextView titleText;
        public TextView desText;
        public TextView expireDate;
        public TextView topPun;

    }

    public View getView(int position, View convertView, ViewGroup parent){

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){
            vi = inflater.inflate(R.layout.userthemeitem, null);

            holder = new ViewHolder();
            holder.titleText = (TextView) vi.findViewById(R.id.lobbyTitle);
            holder.desText = (TextView) vi.findViewById(R.id.lobbyDes);
            holder.expireDate = (TextView) vi.findViewById(R.id.expireDate);
            holder.topPun = (TextView) vi.findViewById(R.id.topPun);

            vi.setTag( holder );
        }
        else {
            holder=(ViewHolder)vi.getTag();
        }

        if (data.size()<=0)
        {
            holder.titleText.setText("No Themes to Show");
        }
        else
        {
            tempValues=null;
            tempValues = ( ListModel ) data.get( position );

            holder.titleText.setText( tempValues.getLobbyTitle() );
            holder.desText.setText( tempValues.getLobbyDes() );
            holder.expireDate.setText( tempValues.getExpireDate() );
            holder.topPun.setText( tempValues.getTopPun() );

            vi.setOnClickListener(new OnItemClickListener( position ));
        }

        if (position % 2 == 1) {
            vi.setBackgroundColor(Color.parseColor("#ffa147"));
        } else {
            vi.setBackgroundColor(Color.parseColor("#d5d3d3"));
        }

        return vi;
    }

    @Override
    public void onClick(View v){
        Log.v("CustomThemeAdapter", "=-=-=Row button clicked=-=-=");
    }

    private class OnItemClickListener implements View.OnClickListener{
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0){

            Profile sct = (Profile)activity;

            //sct.onItemClick(mPosition);
        }
    }
}
