package ui;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import Helpers.DBHelperExperts;
import Listeners.OnTableExpertUpdatedListener;
import Models.Expert;

import com.example.sails.bordandsimpsonmethods.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EditExpertFragment extends android.app.Fragment {
    OnTableExpertUpdatedListener mCallback;

    public static final String LOG_TAG = "myLogs";

    EditText editTextExpertsRatings, editTextExpertsImportance;
    Button buttonEditExpert, buttonRemoveExpert;
    Spinner spinnerExpertsID;
    ArrayList<Expert> experts;
    DBHelperExperts dbHelperExperts;
    SQLiteDatabase database;
    ArrayAdapter<String> adapter;
    String[] expertsIDs;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;

        if (context instanceof Activity) {
            activity = (Activity) context;

            try {
                mCallback = (OnTableExpertUpdatedListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnHeadlineSelectedListener");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_edit_experts, null);

        editTextExpertsRatings = (EditText) view.findViewById(R.id.editTextExpertsRatings);
        editTextExpertsImportance = (EditText) view.findViewById(R.id.editTextExpertsImportance);
        buttonEditExpert = (Button) view.findViewById(R.id.buttonEditExpert);
        buttonRemoveExpert = (Button) view.findViewById(R.id.buttonRemoveExpert);
        spinnerExpertsID = (Spinner) view.findViewById(R.id.spinnerIDs);


        buttonEditExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButtonAdd();
            }
        });

        buttonRemoveExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButtonRemove();
            }
        });

        dbHelperExperts = new DBHelperExperts(getActivity().getApplicationContext());
        database = dbHelperExperts.getWritableDatabase();

        setSpinnerAdapter();

        spinnerExpertsID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int id = Integer.parseInt((String) spinnerExpertsID.getSelectedItem());
                setExpertInfoByIdSelecter(id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }


    private void setExpertInfoByIdSelecter(int id) {

        for (Expert expert : experts) {
            if (expert.getId() == id) {

                editTextExpertsRatings.setText(expert.getStringResultsOrder());
                editTextExpertsImportance.setText(String.valueOf(expert.getImportance()));
                break;
            }
        }

    }


    private void onClickButtonAdd() {
        int id = Integer.parseInt((String) spinnerExpertsID.getSelectedItem());

        String ratings = editTextExpertsRatings.getText().toString();
        int importance = Integer.parseInt(editTextExpertsImportance.getText().toString());

        database.execSQL("UPDATE " + DBHelperExperts.TABLE_EXPERT + " " +
                "SET " + DBHelperExperts.TABLE_EXPERT_RATINGS + " = '" + ratings + "', " +
                DBHelperExperts.TABLE_EXPERT_IMPORTANCE + " = " + importance + " " +
                "WHERE " + DBHelperExperts.TABLE_EXPERT_ID + " = " + id + ";");


        mCallback.onTableExpertUpdated();
    }


    private void onClickButtonRemove() {
        if(spinnerExpertsID.getSelectedItem() != null) {

            int id = Integer.parseInt((String) spinnerExpertsID.getSelectedItem());
            String ratings = editTextExpertsRatings.getText().toString();
            int importance = Integer.parseInt(editTextExpertsImportance.getText().toString());

            database.execSQL("DELETE FROM " + DBHelperExperts.TABLE_EXPERT + " " +
                    "WHERE " + DBHelperExperts.TABLE_EXPERT_ID + " = " + id + ";");

            expertsIDs = getExistingExpertsIDs();

            setSpinnerAdapter();

            mCallback.onTableExpertUpdated();
        }
    }

    private void setSpinnerAdapter(){
        expertsIDs = getExistingExpertsIDs();

        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext()
                , R.layout.spinner_sel_item, expertsIDs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerExpertsID.setAdapter(adapter);
    }

    private String[] getExistingExpertsIDs(){
        experts = dbHelperExperts.getExpertsFromDB();
        String[] expertsIDs = new String[experts.size()];
        int i = 0;
        for (Expert expert : experts) {
            expertsIDs[i++] = String.valueOf(expert.getId());
        }
        return expertsIDs;
    }

}
