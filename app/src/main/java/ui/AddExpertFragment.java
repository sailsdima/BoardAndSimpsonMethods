package ui;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import Helpers.DBHelperExperts;
import Listeners.OnTableExpertUpdatedListener;
import Models.Expert;

import com.example.sails.bordandsimpsonmethods.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class AddExpertFragment extends android.app.Fragment {

    OnTableExpertUpdatedListener mCallback;

    public static final String LOG_TAG = "myLogs";

    private ArrayList<Expert> experts;
    EditText editTextAddExpertsID;
    EditText editTextExpertsRatings, editTextExpertsImportance;
    Button buttonAddExpert, buttonRefresh;
    DBHelperExperts dbHelperExperts;
    SQLiteDatabase database;

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
        View view = inflater.inflate(R.layout.fragment_add_expert, null);

        editTextAddExpertsID = (EditText) view.findViewById(R.id.editTextAddExpertsID);
        editTextExpertsRatings = (EditText) view.findViewById(R.id.editTextExpertsRatings);
        editTextExpertsImportance = (EditText) view.findViewById(R.id.editTextExpertsImportance);
        buttonAddExpert = (Button) view.findViewById(R.id.buttonAddExpert);
        buttonRefresh = (Button) view.findViewById(R.id.buttonRefresh);

        buttonAddExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButtonAdd();
            }
        });

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButtonRefresh();
            }
        });

        dbHelperExperts = new DBHelperExperts(getActivity().getApplicationContext());
        database = dbHelperExperts.getWritableDatabase();

        ArrayList<Expert> experts = dbHelperExperts.getExpertsFromDB();

        try {
            String valueToSet = String.valueOf(experts.get(experts.size() - 1).getId() + 1);
            editTextAddExpertsID.setText(valueToSet);
        } catch (IndexOutOfBoundsException e) {

            editTextAddExpertsID.setText(0 + "");
        }

        return view;
    }

    private void onClickButtonRefresh() {

        ArrayList<Expert> experts = dbHelperExperts.getExpertsFromDB();

        List<String> possibleMarks;
        if (experts.size() == 0) {
            possibleMarks = new ArrayList<>();
            possibleMarks.add("A");
            possibleMarks.add("B");
            possibleMarks.add("C");
            possibleMarks.add("D");
        } else
            possibleMarks = experts.get(0).getResultsOrder();

        int[] arrOfRandoms = new int[possibleMarks.size()];
        Random random = new Random();
        for (int i = 0; i < arrOfRandoms.length; i++) {
            boolean flag = true;

            while (flag) {
                int newInt = random.nextInt(possibleMarks.size()) + 1;
                flag = false;
                System.out.println(Arrays.toString(arrOfRandoms) + "    newInt: " + newInt);
                for (int j = 0; j < arrOfRandoms.length; j++) {
                    if (arrOfRandoms[j] == newInt) {
                        flag = true;
                        break;
                    }
                }
                if (!flag)
                    arrOfRandoms[i] = newInt;
            }

        }

        StringBuilder ratings = new StringBuilder();
        for (int i : arrOfRandoms) {
            ratings.append(possibleMarks.get(i - 1) + ", ");
        }
        ratings.delete(ratings.lastIndexOf(", "), ratings.length());
        int importance = random.nextInt(1000) + 1;

        editTextExpertsRatings.setText(ratings.toString());
        editTextExpertsImportance.setText(String.valueOf(importance));

    }


    private void onClickButtonAdd() {
        int id = Integer.parseInt(editTextAddExpertsID.getText().toString());
        String ratings = editTextExpertsRatings.getText().toString();
        int importance = Integer.parseInt(editTextExpertsImportance.getText().toString());

        ArrayList<Expert> experts = dbHelperExperts.getExpertsFromDB();
        for (Expert expert : experts) {
            if (expert.getId() == id)
                return;
        }

        addExpertInDB(new Expert(id, ratings, importance));
        setLastIDInEditText();
    }

    private void setLastIDInEditText() {
        ArrayList<Expert> experts = dbHelperExperts.getExpertsFromDB();
        editTextAddExpertsID.setText(String.valueOf(experts.get(experts.size() - 1).getId() + 1));

    }

    private void addExpertInDB(Expert expert) {

        database.execSQL("INSERT INTO " + DBHelperExperts.TABLE_EXPERT + " VALUES (" +
                expert.getId() + ", '" + expert.getStringResultsOrder() + "', " + expert.getImportance() + ");");

        mCallback.onTableExpertUpdated();
    }
}