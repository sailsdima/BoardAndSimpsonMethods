package ui;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sails.bordandsimpsonmethods.R;

import java.util.ArrayList;
import java.util.List;

import Adapters.ExpertsAdapter;
import Helpers.DBHelperExperts;
import Models.Expert;


public class ExpertsFragment extends android.app.Fragment {

    ListView listViewExperts;
    ExpertsAdapter expertsAdapter;
    ArrayList<Expert> experts;
    DBHelperExperts dbHelperExperts;
    SQLiteDatabase database;

    public ExpertsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("myLogs", "onCreateView");

        experts = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_experts, container, false);

        listViewExperts = (ListView) view.findViewById(R.id.listViewExperts);
        listViewExperts.addHeaderView(inflater.inflate(R.layout.header_list_view, null));

        dbHelperExperts = new DBHelperExperts(getActivity().getApplicationContext());
        database = dbHelperExperts.getWritableDatabase();

        updateListView();
        return view;
    }

    public void updateListView() {

        ArrayList<Expert> experts = dbHelperExperts.getExpertsFromDB();

        listViewExperts.setAdapter(new ExpertsAdapter(getActivity().getApplicationContext(), experts));
    }
}
