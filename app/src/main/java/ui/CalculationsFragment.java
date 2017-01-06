package ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sails.bordandsimpsonmethods.R;

import java.util.ArrayList;

import Helpers.DBHelperExperts;
import Logics.MethodsCounter;
import Models.Expert;
import Models.ResultInfo;

public class CalculationsFragment extends android.app.Fragment {

    TextView textViewCalculations, textViewWinner, textViewErrorMessage;
    Button buttonBoardMethod, buttonSimpsonMethod;
    DBHelperExperts dbHelperExperts;

    public CalculationsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calculations, container, false);

        textViewCalculations = (TextView) view.findViewById(R.id.textViewCalculation);
        textViewWinner = (TextView) view.findViewById(R.id.textViewWinner);
        textViewErrorMessage = (TextView) view.findViewById(R.id.textViewErrorMessage);

        buttonBoardMethod = (Button) view.findViewById(R.id.buttonBoardMethod);
        buttonSimpsonMethod = (Button) view.findViewById(R.id.buttonSimpsonMethod);

        buttonBoardMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonBoardMethodClick();
            }
        });


        buttonSimpsonMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonSimpsonMethodClick();
            }
        });

        dbHelperExperts = new DBHelperExperts(getActivity().getApplicationContext());

        calculate();

        return view;
    }


    private void onButtonBoardMethodClick() {

        MethodsCounter methodsCounter = new MethodsCounter();
        ResultInfo resultInfo = methodsCounter.boardMethod(dbHelperExperts.getExpertsFromDB());
        fillTextViewWithResultInfo(resultInfo);

    }


    private void onButtonSimpsonMethodClick() {
        MethodsCounter methodsCounter = new MethodsCounter();

        ResultInfo resultInfo = new ResultInfo();
        ArrayList<Expert> experts = dbHelperExperts.getExpertsFromDB();

        if ((experts.size() - 1) == experts.get(0).getResultsOrder().size() - 1)
            resultInfo = methodsCounter.simpsonsMethod(dbHelperExperts.getExpertsFromDB());

        else resultInfo.setErrorMessage("Works only for NxN matrixs.");
        fillTextViewWithResultInfo(resultInfo);
    }

    private void fillTextViewWithResultInfo(ResultInfo resultInfo) {
        if (resultInfo.isSuccess()) {
            textViewCalculations.setText(resultInfo.getCountingMessage());
            textViewWinner.setText(resultInfo.getWinner());
            textViewErrorMessage.setText("");
        } else {
            textViewCalculations.setText("");
            textViewWinner.setText("");
            textViewErrorMessage.setText(resultInfo.getErrorMessage());
        }
    }

    private void calculate() {


    }


}
