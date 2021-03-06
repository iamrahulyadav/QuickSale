package marmu.com.quicksale.modules;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import marmu.com.quicksale.R;
import marmu.com.quicksale.activity.LandingActivity;
import marmu.com.quicksale.activity.SetTakenActivity;
import marmu.com.quicksale.adapter.TakenAdapter;
import marmu.com.quicksale.api.FireBaseAPI;
import marmu.com.quicksale.model.TakenModel;

/**
 * Created by azharuddin on 25/7/17.
 */

@SuppressWarnings("unchecked")
@SuppressLint("SimpleDateFormat")
public class Taken {

    private Activity activity;
    private View itemView;
    private List<TakenModel> takenList;

    private TextView datePicker;

    public void evaluate(final LandingActivity activity, View itemView) {

        try {

            this.activity = activity;
            this.itemView = itemView;

            initViews();
            currentDate();

            datePicker();
            createTaken();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void initViews() {
        datePicker = itemView.findViewById(R.id.et_date_picker);
    }

    private void currentDate() throws ParseException {
        Date currentDate = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(currentDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (month <= 9) {
            datePicker.setText("");
            datePicker.append(day + "/" + "0" + (month) + "/" + year);
        } else {
            datePicker.setText("");
            datePicker.append(day + "/" + (month) + "/" + year);
        }
        changeMapToList(datePicker.getText().toString());
    }


    private void changeMapToList(String datePicker) {
        HashMap<String, Object> taken = FireBaseAPI.taken;
        takenList = new ArrayList<>();
        if (taken != null) {
            for (String key : taken.keySet()) {
                HashMap<String, Object> takenOrder = (HashMap<String, Object>) taken.get(key);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date pickedDate = formatter.parse(datePicker);
                    Date salesDate = formatter.parse(takenOrder.get("sales_date").toString());
                    if (pickedDate.compareTo(salesDate) == 0) {
                        takenList.add(new TakenModel(key, takenOrder));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        populateTaken();
    }

    @SuppressLint("SimpleDateFormat")
    private void datePicker() {
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                final int mYear = c.get(Calendar.YEAR);
                final int mMonth = c.get(Calendar.MONTH);
                final int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String pYear = String.valueOf(year);
                                String pMonth = String.valueOf(monthOfYear + 1);
                                String pDay = String.valueOf(dayOfMonth);

                                String cYear = String.valueOf(mYear);
                                String cMonth = String.valueOf(mMonth + 1);
                                String cDay = String.valueOf(mDay);

                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    Date pickedDate = formatter.parse((pDay + "-" + pMonth + "-" + pYear));
                                    Date currentDate = formatter.parse((cDay + "-" + cMonth + "-" + cYear));
                                    if (pickedDate.compareTo(currentDate) <= 0) {
                                        if ((monthOfYear + 1) <= 9) {
                                            datePicker.setText("");
                                            datePicker.append(dayOfMonth + "/0" + (monthOfYear + 1) + "/" + year);
                                        } else {
                                            datePicker.setText("");
                                            datePicker.append(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                        }
                                        datePicker.clearFocus();
                                        changeMapToList(datePicker.getText().toString());
                                    } else {
                                        datePicker.setError("Choose Valid date");
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    private void populateTaken() {
        TakenAdapter adapter = new TakenAdapter(activity, takenList);
        RecyclerView takenView = itemView.findViewById(R.id.rv_taken);
        takenView.removeAllViews();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        takenView.setLayoutManager(layoutManager);
        takenView.setItemAnimator(new DefaultItemAnimator());
        takenView.setAdapter(adapter);
    }

    private void createTaken() {
        TextView createOrder = itemView.findViewById(R.id.btn_create_taken);
        createOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SetTakenActivity.class);
                activity.startActivity(intent);
            }
        });
    }

}
