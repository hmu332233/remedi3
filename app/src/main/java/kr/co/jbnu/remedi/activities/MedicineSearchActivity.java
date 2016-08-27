package kr.co.jbnu.remedi.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.co.jbnu.remedi.R;
import kr.co.jbnu.remedi.Utils.ProgressBarDialog;
import kr.co.jbnu.remedi.adapters.MedicineAdapter;
import kr.co.jbnu.remedi.models.Medicine;
import kr.co.jbnu.remedi.parser.Medicine_Parser;

/**
 * Created by mu on 2016-08-21.
 */
public class MedicineSearchActivity extends AppCompatActivity {

    Medicine_Parser medicine_Parser;
    ListView medicineListView;
    MedicineAdapter medicineAdapter;
    MedicineListTask m;

    Medicine medicine;

    TextView tv_medicine_name;
    TextView tv_medicine_element;
    TextView tv_medicine_company ;
    TextView tv_medicine_serialnumber;
    TextView tv_medicine_category;
    TextView tv_medicine_effect;

    ProgressBarDialog progressBarDialog;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_search);

        medicineListView = (ListView)findViewById(R.id.lv_medicine);

        progressBarDialog = new ProgressBarDialog(this);

        final EditText editText = (EditText)findViewById(R.id.et_medicine_name);
        Button btn = (Button)findViewById(R.id.btn_search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( editText.getText().toString().equals("") )
                    Toast.makeText(getBaseContext(),"검색어를 입력해주세요",Toast.LENGTH_SHORT).show();
                else
                {
                    m = new MedicineListTask();
                    m.execute(editText.getText()+"");
                }

                InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        });

        Button btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("medicine",medicine);
                setResult(RESULT_OK,intent);

                finish();
            }
        });

        tv_medicine_name = (TextView) findViewById(R.id.medicine_name);
        tv_medicine_element = (TextView) findViewById(R.id.medicine_element);
        tv_medicine_company = (TextView) findViewById(R.id.medicine_enterprise);
        tv_medicine_serialnumber = (TextView) findViewById(R.id.medicine_standardCode);
        tv_medicine_category = (TextView) findViewById(R.id.medicine_category);
        tv_medicine_effect = (TextView) findViewById(R.id.medicine_effect);



    }

    public class MedicineListTask extends AsyncTask<String,String,ArrayList<Medicine>> {

        @Override
        protected void onPreExecute() {
            progressBarDialog.show();
        }

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
                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                    MedicineDetailTask medicineDetailTask = new MedicineDetailTask();
                    medicineDetailTask.execute(medicineAdapter.getItem(position));

                }
            });

            progressBarDialog.dismiss();
        }
    }

    public class MedicineDetailTask extends AsyncTask<Medicine,Medicine,Medicine> {

        @Override
        protected Medicine doInBackground(Medicine... params) {

            Medicine_Parser medicine_Parser = new Medicine_Parser();

            Medicine medicine = params[0];
            medicine = medicine_Parser.requestMedicine_detail(medicine);
            medicine.print();

            Log.d("알림","테스트입니다");

            return medicine;
        }

        @Override
        protected void onPostExecute(final Medicine _medicine) {
            super.onPostExecute(medicine);

            Log.d("알림","테스트입니다2");

            medicine = _medicine;

            tv_medicine_name.setText(medicine.getName());
            tv_medicine_element.setText(medicine.getIngredient());
            tv_medicine_company.setText(medicine.getEnterprise());
            tv_medicine_serialnumber.setText(medicine.getStandardCode());
            tv_medicine_category.setText(medicine.getCategory());
            tv_medicine_effect.setText(medicine.getEffect());

        }
    }
}
