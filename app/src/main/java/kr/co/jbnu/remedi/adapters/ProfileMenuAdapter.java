package kr.co.jbnu.remedi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.jbnu.remedi.R;

/**
 * Created by mu on 2016-08-22.
 */
public class ProfileMenuAdapter extends ArrayAdapter<String> {


    public ProfileMenuAdapter(Context context,int layout, ArrayList<String> data) {
        super(context, layout,  data);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String param= getItem(position);

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View item = inflater.inflate(R.layout.item_profile_menu,null);

        TextView menutext = (TextView) item.findViewById(R.id.profile_menu_item);

        menutext.setText(param);

        return item;
    }
}
