package ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import Adapters.ExpertsAdapter;
import Listeners.OnTableExpertUpdatedListener;
import Models.Expert;

import com.example.sails.bordandsimpsonmethods.R;

import java.util.ArrayList;

public class ManagementExpertsActivity extends AppCompatActivity implements OnTableExpertUpdatedListener {

    public static final String LOG_TAG = "myLogs";
    private Button buttonAddExpert, buttonEditExpert;
    private ArrayList<Expert> experts;

    private ListView listViewExperts;
    private ExpertsAdapter expertsAdapter;

    private FragmentTransaction fragmentTransaction;
    private AddExpertFragment addExpertFragment;
    private EditExpertFragment editExpertFragment;
    private ExpertsFragment expertsFragment;

    private boolean isFragmentAddShown = false;
    private boolean isFragmentEditShown = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_experts);

        init();

        addExpertsListViewFragment();


    }

    private void init() {
        experts = new ArrayList<>();


        buttonAddExpert = (Button) findViewById(R.id.buttonAddExpert);
        buttonEditExpert = (Button) findViewById(R.id.buttonEditExpert);
        addExpertFragment = new AddExpertFragment();
        editExpertFragment = new EditExpertFragment();
        expertsFragment = new ExpertsFragment();


        buttonAddExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonAddExpertClick();
            }
        });

        buttonEditExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonEditExpertClick();
            }
        });



    }

    private void addExpertsListViewFragment() {
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.expertsListViewFrame, expertsFragment);
        fragmentTransaction.commit();

    }

    private void onButtonAddExpertClick() {

        fragmentTransaction = getFragmentManager().beginTransaction();

        if (isFragmentEditShown) {
            fragmentTransaction.remove(editExpertFragment);
            isFragmentEditShown = false;
        }

        if (!isFragmentAddShown) {
            fragmentTransaction.add(R.id.addExpertFrame, addExpertFragment);
            isFragmentAddShown = true;
        } else {
            fragmentTransaction.remove(addExpertFragment);
            isFragmentAddShown = false;
        }

        fragmentTransaction.commit();
    }

    private void onButtonEditExpertClick() {

        fragmentTransaction = getFragmentManager().beginTransaction();


        if (isFragmentAddShown) {
            fragmentTransaction.remove(addExpertFragment);
            isFragmentAddShown = false;
        }

        if (!isFragmentEditShown) {
            fragmentTransaction.add(R.id.editExpertFrame, editExpertFragment);
            isFragmentEditShown = true;
        } else {
            fragmentTransaction.remove(editExpertFragment);
            isFragmentEditShown = false;
        }

        fragmentTransaction.commit();
    }


    public void addExpert(Expert expert) {
        expertsFragment.updateListView();
    }

    @Override
    public void onTableExpertUpdated() {
        expertsFragment.updateListView();
    }


}
