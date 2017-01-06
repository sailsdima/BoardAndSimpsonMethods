package ui;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sails.bordandsimpsonmethods.R;

import Helpers.DBHelperExperts;

public class MainActivity extends AppCompatActivity {

    String LOG_TAG = "myLogs";
    private FragmentTransaction fragmentTransaction;
    CalculationsFragment calculationsFragment;

    private Button buttonEditExperts;
    DBHelperExperts dbHelperExperts;
    //resultsFrame
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        calculationsFragment = new CalculationsFragment();

        buttonEditExperts = (Button) findViewById(R.id.buttonEditExperts);

        dbHelperExperts = new DBHelperExperts(this);

        buttonEditExperts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ManagementExpertsActivity.class);
                startActivity(intent);
            }
        });

    }

}