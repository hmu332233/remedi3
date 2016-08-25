package kr.co.jbnu.remedi.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kr.co.jbnu.remedi.R;
import kr.co.jbnu.remedi.Utils.GlobalValue;
import kr.co.jbnu.remedi.Utils.ImageDialog;
import kr.co.jbnu.remedi.activities.MedicineSearchActivity;
import kr.co.jbnu.remedi.models.Answer;
import kr.co.jbnu.remedi.models.Board;
import kr.co.jbnu.remedi.models.Reply;
import kr.co.jbnu.remedi.models.User;
import kr.co.jbnu.remedi.serverIDO.ServerConnectionManager;
import kr.co.jbnu.remedi.serverIDO.ServerConnectionService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardAdapter extends ArrayAdapter<Board> {

    String user_type;
    Context context;
    User user;

    int index;


    public BoardAdapter(Context _context, User _user, ArrayList<Board> boards) {
        super(_context, 0, boards);
        user = _user;
        user_type = user.getUser_type();
        context = _context;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        index = position;
        System.out.println("getview 호출");
        final Board board = getItem(position);

        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = null;


        if( user_type.equals(User.NORMAL) || board.getAnswer() != null )
        {
            item = inflater.inflate(R.layout.item_board,null);

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

            TextView tv_question = (TextView)item.findViewById(R.id.tv_question);
            tv_question.setText(board.getContent());

            ImageView imageView = (ImageView)item.findViewById(R.id.iv_warning);
            imageView.setAlpha(50);


            if(board.getAnswer() != null)
            {

                extra_btn.setVisibility(View.VISIBLE);

                TextView tv_answer = (TextView) item.findViewById(R.id.tv_answer);
                tv_answer.setVisibility(View.VISIBLE);
                tv_answer.setText(board.getAnswer().getAnswer_content());

                LinearLayout layout_answer = (LinearLayout) item.findViewById(R.id.layout_answer);
                layout_answer.setVisibility(View.VISIBLE);

                LinearLayout layout_no_answer = (LinearLayout) item.findViewById(R.id.layout_no_answer);
                layout_no_answer.setVisibility(View.GONE);

                TextView tv_pharm_name = (TextView) item.findViewById(R.id.tv_pharm_name);
                //tv_pharm_name.setText();

                Answer answer = board.getAnswer();

                TextView tv_medicine_name;
                TextView tv_medicine_element;
                TextView tv_medicine_company ;
                TextView tv_medicine_serialnumber;
                TextView tv_medicine_category;
                TextView tv_medicine_effect;

                tv_medicine_name = (TextView) item.findViewById(R.id.medicine_name);
                tv_medicine_element = (TextView) item.findViewById(R.id.medicine_element);
                tv_medicine_company = (TextView) item.findViewById(R.id.medicine_enterprise);
                tv_medicine_serialnumber = (TextView) item.findViewById(R.id.medicine_standardCode);
                tv_medicine_category = (TextView) item.findViewById(R.id.medicine_category);
                tv_medicine_effect = (TextView) item.findViewById(R.id.medicine_effect);

                tv_medicine_name.setText(answer.getMedi_name());
                tv_medicine_element.setText(answer.getMedi_element());
                tv_medicine_company.setText(answer.getMedi_company());
                tv_medicine_serialnumber.setText(answer.getMedi_serialnumber());
                tv_medicine_category.setText(answer.getMedi_category());
                tv_medicine_effect.setText(answer.getMedi_effect());


                //------댓글
                ArrayList<Reply> replies = User.getInstance().getUserBoardList().get(position).getAnswer().getRepliesList();

                if(replies == null)
                {
                    replies = new ArrayList<Reply>();
                    User.getInstance().getUserBoardList().get(position).getAnswer().setRepliesList(replies);
                }

                ListView lv_reply = (ListView) item.findViewById(R.id.lv_reply);
                final ReplyAdapter replyAdapter = new ReplyAdapter(context,replies);
                lv_reply.setAdapter(replyAdapter);

                final LinearLayout layout_reply = (LinearLayout) item.findViewById(R.id.layout_reply);

                CardView cv_reply = (CardView) item.findViewById(R.id.cv_reply);
                cv_reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(layout_reply.getVisibility() == View.GONE) {
                            layout_reply.setVisibility(View.VISIBLE);

                            /*
                            * 여기다가 클릭하면 로딩ㅇ할수있게
                            */

                            replyAdapter.notifyDataSetChanged();
                        }
                        else
                            layout_reply.setVisibility(View.GONE);
                    }
                });

                cv_reply.setVisibility(View.VISIBLE);

                final EditText et_reply = (EditText) item.findViewById(R.id.et_reply);

                et_reply.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER)
                        {
                            Log.d("현재 쓰는 위치의 컨텐츠 입니다.",getItem(position).getContent());

                            String content = et_reply.getText().toString();

                            ArrayList<Reply> arr = User.getInstance().getUserBoardList().get(position).getAnswer().getRepliesList();
                            if(arr==null){
                                arr = new ArrayList<Reply>();
                                System.out.println("답글 리스트 널입니다");
                            }

                            arr.add(0,new Reply(content,User.getInstance().getName()));
                            register_reply(content,User.getInstance().getName(),User.getInstance().getUserBoardList().get(position).getAnswer().getId());
                            et_reply.setText("");

                            replyAdapter.notifyDataSetChanged();


                            return true;
                        }

                        return false;
                    }
                });



            }
        }
        else
        {
            item = inflater.inflate(R.layout.item_board_pharm,null);

            LinearLayout layout_btn_input = (LinearLayout) item.findViewById(R.id.layout_btn_input);
            final LinearLayout layout_btn = (LinearLayout) item.findViewById(R.id.layout_btn);
            final LinearLayout layout_input = (LinearLayout) item.findViewById(R.id.layout_input);

            final LinearLayout top = (LinearLayout)item.findViewById(R.id.answer_btn_top);
            final LinearLayout bottom = (LinearLayout)item.findViewById(R.id.answer_btn_bottom);

            layout_btn_input.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if( layout_btn.getVisibility() == View.VISIBLE)
                    {
                        top.setVisibility(View.GONE);
                        bottom.setVisibility(View.GONE);
                        layout_btn.setVisibility(View.GONE);
                        layout_input.setVisibility(View.VISIBLE);
                    }

/*
                    Intent intent = new Intent(context, AnswerActivity.class);

                    intent.putExtra("board", board);
                    intent.putExtra("index",position);

                    ((Activity) context).startActivityForResult(intent,123);
*/

                }
            });

            TextView tv_question = (TextView)item.findViewById(R.id.tv_question);
            tv_question.setText(board.getContent());

            TextView tv_medicine_name = (TextView) item.findViewById(R.id.tv_medicine_name);
            tv_medicine_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MedicineSearchActivity.class);

                    intent.putExtra("board", board);
                    intent.putExtra("index",position);

                    ((Activity) context).startActivityForResult(intent, GlobalValue.GET_MEDICINE);
                }
            });
/*
            Button btn_add_answer = (Button) item.findViewById(R.id.btn_add_answer);

            btn_add_answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("알림","테스트입니다");
                }
            });*/


        }

        System.out.println(board.getImg_url());
        ImageView iv_medicine_image = (ImageView) item.findViewById(R.id.iv_medicine_image);
        Picasso.with(context).load(Uri.parse(board.getImg_url())).error(R.drawable.ic_nocover).into(iv_medicine_image);
        iv_medicine_image.setScaleType(ImageView.ScaleType.FIT_XY);
        iv_medicine_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialog imgdial = new ImageDialog(context,board.getImg_url());
                imgdial.show();
            }
        });

        //Picasso.with(context).load(Uri.parse("http://kossi.iptime.org:2000/viate/getimg?imgname=1164417882042743Screenshot_2016-08-24-04-42-19.png")).error(R.drawable.ic_nocover).into(iv_medicine_image);

        return item;
    }

    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("알림", "onActivityResult");
    }

    private void register_reply(String content,String email,int answer_id){
        System.out.println("댓글 등록 요청");

        ServerConnectionManager serverConnectionManager = ServerConnectionManager.getInstance();
        ServerConnectionService serverConnectionService = serverConnectionManager.getServerConnection();


        final Call<Boolean> reply = serverConnectionService.register_reply(content,email,answer_id);
        reply.enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                System.out.println("댓글 등록 완료");
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                //final TextView textView = (TextView) findViewById(R.id.textView);
                //textView.setText("Something went wrong: " + t.getMessage());
                //progressBarDialog.dismiss();
                Log.w("서버 통신 실패",t.getMessage());
            }
        });
    }
}
