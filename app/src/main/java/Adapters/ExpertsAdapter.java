package Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sails.bordandsimpsonmethods.R;

import java.util.ArrayList;

import Models.Expert;

/**
 * Created by sails on 11.12.2016.
 */

public class ExpertsAdapter extends BaseAdapter {

    String LOG_TAG = "myLogs";
    private ArrayList<Expert> experts;
    private Context context;
    private LayoutInflater lInflater;

    public ExpertsAdapter(Context context, ArrayList<Expert> experts) {
        this.context = context;
        this.experts = experts;

        this.lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return experts.size();
    }

    @Override
    public Object getItem(int i) {
        return experts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        if (view == null) {
            Log.d(LOG_TAG, "view = null!");
            view = lInflater.inflate(R.layout.lv_expert_item, viewGroup, false);
        }

        Expert expert = getExpert(position);


        ((TextView) view.findViewById(R.id.textViewExpertsID)).setText(String.valueOf(expert.getId()));
        ((TextView) view.findViewById(R.id.textViewRatings)).setText(expert.getStringResultsOrder());
        ((TextView) view.findViewById(R.id.textViewImportance)).setText(String.valueOf(expert.getImportance()));

        return view;
    }

    private Expert getExpert(int position) {
        return ((Expert) getItem(position));
    }
}
