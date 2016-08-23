package kr.co.jbnu.remedi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.jbnu.remedi.R;
import kr.co.jbnu.remedi.models.Medicine;

/**
 * Created by mu on 2016-08-17.
 */
public class MedicineAdapter extends ArrayAdapter<Medicine> {

    public MedicineAdapter(Context context, ArrayList<Medicine> medicines) {
        super(context, 0, medicines);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Medicine medicine = getItem(position);

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View item = inflater.inflate(R.layout.item_medicine,null);

        TextView tv_name = (TextView) item.findViewById(R.id.medicine_name);
        TextView tv_ingredient = (TextView) item.findViewById(R.id.medicine_ingredient);
        TextView tv_ingredientCount = (TextView) item.findViewById(R.id.medicine_ingredientCount);
        TextView tv_enterprise = (TextView) item.findViewById(R.id.medicine_enterprise);
        TextView tv_category = (TextView) item.findViewById(R.id.medicine_category);

        tv_name.setText(medicine.getName());
        tv_ingredient.setText(medicine.getIngredient());
        tv_ingredientCount.setText(medicine.getIngredientCount());
        tv_enterprise.setText(medicine.getEnterprise());
        tv_category.setText(medicine.getCategory());

        return item;
    }
}
