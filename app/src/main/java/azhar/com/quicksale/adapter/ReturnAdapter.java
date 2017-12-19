package azhar.com.quicksale.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import azhar.com.quicksale.R;
import azhar.com.quicksale.activity.ReturnActivity;
import azhar.com.quicksale.model.TakenModel;
import azhar.com.quicksale.utils.Constants;

/**
 * Created by azharuddin on 24/7/17.
 */

public class ReturnAdapter extends RecyclerView.Adapter<ReturnAdapter.MyViewHolder> {

    private Context context;
    private List<TakenModel> returnList;
    private static List<String> selectedSalesMan = new ArrayList<>();

    public ReturnAdapter(Context context, List<TakenModel> returnList) {
        this.context = context;
        this.returnList = returnList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_common,
                        parent, false);
        return new ReturnAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final TakenModel taken = returnList.get(position);
        final HashMap<String, Object> returnMap = taken.getTakenMap();
        holder.returnName.setText("");
        holder.returnName.append(
                "Sales Man : " + returnMap.get(Constants.TAKEN_SALES_MAN_NAME).toString() +
                        "\n" +
                        "Route : " + returnMap.get(Constants.TAKEN_ROUTE).toString()
        );

        holder.returnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (returnMap.get(Constants.TAKEN_PROCESS).toString().
                        equalsIgnoreCase(Constants.CLOSED)) {
                    Intent returnIntent = new Intent(context, ReturnActivity.class);
                    returnIntent.putExtra(Constants.KEY, taken.getKey());
                    returnIntent.putExtra(Constants.TAKEN, returnMap);
                    returnIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(returnIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return returnList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView returnName, returnView, returnDelete;

        MyViewHolder(View itemView) {
            super(itemView);
            returnName = itemView.findViewById(R.id.name);
            returnView = itemView.findViewById(R.id.view_edit);
            returnDelete = itemView.findViewById(R.id.delete);
        }
    }

}
