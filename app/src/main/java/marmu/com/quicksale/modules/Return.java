package marmu.com.quicksale.modules;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import marmu.com.quicksale.R;
import marmu.com.quicksale.adapter.ReturnAdapter;
import marmu.com.quicksale.api.FireBaseAPI;
import marmu.com.quicksale.utils.Constants;
import marmu.com.quicksale.model.TakenModel;

/**
 * Created by azharuddin on 25/7/17.
 */

@SuppressWarnings("unchecked")
@SuppressLint("SimpleDateFormat")
public class Return {

    private static List<TakenModel> returnList;

    public static void evaluate(final Context context, View itemView) {

        try {
            final EditText datePicker = itemView.findViewById(R.id.et_date_picker);

            Date currentDate = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(currentDate);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            if (month <= 9) {
                datePicker.setText(day + "/" + "0" + (month) + "/" + year);
            } else {
                datePicker.setText(day + "/" + (month) + "/" + year);
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date pickedDate = formatter.parse(datePicker.getText().toString());

            changeMapToList(context, itemView, pickedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        datePicker(context, itemView);
    }

    private static void changeMapToList(Context context, View itemView, Date pickedDate) {
        HashMap<String, Object> taken = FireBaseAPI.taken;
        returnList = new ArrayList<>();
        if (taken != null) {
            for (String key : taken.keySet()) {
                HashMap<String, Object> takenOrder = (HashMap<String, Object>) taken.get(key);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date salesDate = formatter.parse(takenOrder.get("sales_date").toString());
                    String process = takenOrder.get("process").toString();
                    if (pickedDate.compareTo(salesDate) == 0 && process.equalsIgnoreCase(Constants.CLOSED)) {
                        returnList.add(new TakenModel(key, takenOrder));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        populateReturn(context, itemView);
    }

    @SuppressLint("SimpleDateFormat")
    private static void datePicker(final Context context, final View itemView) {
        final EditText datePicker = itemView.findViewById(R.id.et_date_picker);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                final int mYear = c.get(Calendar.YEAR);
                final int mMonth = c.get(Calendar.MONTH);
                final int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
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
                                            datePicker.setText(dayOfMonth + "/0" + (monthOfYear + 1) + "/" + year);
                                        } else {
                                            datePicker.setText(dayOfMonth + "/" +(monthOfYear + 1) + "/" + year);
                                        }
                                        datePicker.clearFocus();
                                        changeMapToList(context, itemView, pickedDate);
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

    private static void populateReturn(Context context, View itemView) {

        ReturnAdapter adapter = new ReturnAdapter(context, returnList);
        RecyclerView returnView = itemView.findViewById(R.id.rv_return);
        returnView.removeAllViews();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        returnView.setLayoutManager(layoutManager);
        returnView.setItemAnimator(new DefaultItemAnimator());
        returnView.setAdapter(adapter);
    }
}
