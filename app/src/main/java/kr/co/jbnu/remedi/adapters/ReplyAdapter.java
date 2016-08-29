package kr.co.jbnu.remedi.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kr.co.jbnu.remedi.R;
import kr.co.jbnu.remedi.models.Reply;

/**
 * Created by mu on 2016-08-22.
 */
public class ReplyAdapter extends ArrayAdapter<Reply> {

    Context context;
    public ReplyAdapter(Context context, ArrayList<Reply> replies) {
        super(context, 0, replies);
        this.context=context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Reply reply = getItem(position);

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View item = inflater.inflate(R.layout.item_reply,null);

        de.hdodenhof.circleimageview.CircleImageView profile_image = (de.hdodenhof.circleimageview.CircleImageView) item.findViewById(R.id.iv_profile_image);
        TextView tv_user_name = (TextView) item.findViewById(R.id.tv_user_name);
        TextView tv_content = (TextView) item.findViewById(R.id.tv_content);

        tv_user_name.setText(reply.getUser_name());
        tv_content.setText(reply.getContent());
        Picasso.with(context).load(Uri.parse(reply.getProfileUril())).error(R.drawable.ic_nocover).into(profile_image);
        return item;
    }
}
