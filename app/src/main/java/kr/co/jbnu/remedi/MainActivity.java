package kr.co.jbnu.remedi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import kr.co.jbnu.remedi.adapters.BoardAdapter;
import kr.co.jbnu.remedi.models.Board;
import kr.co.jbnu.remedi.models.Medicine;
import kr.co.jbnu.remedi.parser.Medicine_Parser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //asdfasd
        //asdfasdf
        ArrayList<Board> boards = new ArrayList<>();
        Board b = new Board("","",null);

        boards.add(b);
        boards.add(b);
        boards.add(b);
        boards.add(b);
        boards.add(b);
        boards.add(b);
        boards.add(b);
        boards.add(b);
        boards.add(b);

        ListView boardListView = (ListView) findViewById(R.id.lv_board);
        boardListView.setAdapter(new BoardAdapter(this,boards));


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Medicine_Parser medicine_Parser = new Medicine_Parser();

                ArrayList<Medicine> medicineList = medicine_Parser.requestMedicines("타이레놀");

                Medicine medicine = medicineList.get(0);

                medicine = medicine_Parser.requestMedicine_detail(medicine,medicine.getCodeNumber());

                medicine.print();
            }
        });

       // t.start();
    }


}
