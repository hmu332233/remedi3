package kr.co.jbnu.remedi.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;

import kr.co.jbnu.remedi.R;
import kr.co.jbnu.remedi.models.Board;

public class BoardAdapter extends ArrayAdapter<Board> {

    public BoardAdapter(Context context, ArrayList<Board> boards) {
        super(context, 0, boards);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Board board = getItem(position);

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View item = inflater.inflate(R.layout.item_board,null);


        final LinearLayout layout_extra = (LinearLayout) item.findViewById(R.id.layout_extra);

        CardView extra_btn = (CardView) item.findViewById(R.id.cv_btn_extra);
        extra_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(layout_extra.getVisibility() == View.GONE )
                    layout_extra.setVisibility(View.VISIBLE);
                else
                    layout_extra.setVisibility(View.GONE);
            }
        });

        return item;
    }
}
