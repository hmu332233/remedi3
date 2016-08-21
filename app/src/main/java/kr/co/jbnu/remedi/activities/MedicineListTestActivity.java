package kr.co.jbnu.remedi.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.jbnu.remedi.R;
import kr.co.jbnu.remedi.adapters.MedicineAdapter;
import kr.co.jbnu.remedi.models.Medicine;
import kr.co.jbnu.remedi.parser.Medicine_Parser;

/**
 * Created by mu on 2016-08-17.
 */
public class MedicineListTestActivity extends Activity{

    Medicine_Parser medicine_Parser;
    ListView medicineListView;
    MedicineAdapter medicineAdapter;
    MedicineListTask m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list_test);

        medicineListView = (ListView)findViewById(R.id.lv_medicine);


        final EditText editText = (EditText)findViewById(R.id.et_medicine_name);
        Button btn = (Button)findViewById(R.id.btn_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m = new MedicineListTask();
                m.execute(editText.getText()+"");

            }
        });


    }

    public class MedicineListTask extends AsyncTask<String,String,ArrayList<Medicine>> {

        @Override
        protected ArrayList<Medicine> doInBackground(String... params) {

            medicine_Parser = new Medicine_Parser();

            ArrayList<Medicine> medicineList = medicine_Parser.requestMedicines(params[0]);

            Log.d("알림","테스트입니다");

            return medicineList;
        }

        @Override
        protected void onPostExecute(final ArrayList<Medicine> medicines) {
            super.onPostExecute(medicines);

            Log.d("알림","테스트입니다2");

            medicineAdapter = new MedicineAdapter(getBaseContext(),medicines);
            medicineListView.setAdapter(medicineAdapter);
            medicineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Medicine medicine = medicineAdapter.getItem(position);
                            medicine = medicine_Parser.requestMedicine_detail(medicine);
                            medicine.print();
                        }
                    });

                    t.start();

                }
            });

        }
    }

}
