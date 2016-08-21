package kr.co.jbnu.remedi.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import kr.co.jbnu.remedi.R;
import kr.co.jbnu.remedi.models.Board;

/**
 * Created by sec on 2016-08-21.
 */
public class WriteBoardActivity extends AppCompatActivity {

    EditText et_question;
    Button btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_board);


        Uri imageUri = getIntent().getData();

        ImageView iv_medicine_image = (ImageView)findViewById(R.id.iv_medicine_image);
        iv_medicine_image.setImageURI(imageUri);


        et_question = (EditText)findViewById(R.id.et_question_content);
        btn_ok = (Button)findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Board board = new Board("",et_question.getText().toString(),null);

                Intent intent = new Intent();
                intent.putExtra("board",board);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
